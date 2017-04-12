#!/usr/bin/env ruby
require 'securerandom'
require 'semantria'

print 'Semantria Detailed mode demo ...', "\r\n"

# the consumer key and secret
$consumer_key = ''
$consumer_secret = ''

# Task statuses
$TASK_STATUS_UNDEFINED = 'UNDEFINED'
$TASK_STATUS_FAILED = 'FAILED'
$TASK_STATUS_QUEUED = 'QUEUED'
$TASK_STATUS_PROCESSED = 'PROCESSED'

$initial_texts = []

class SessionCallbackHandler < Semantria::CallbackHandler
  def onRequest(sender, args)
    #print "Request: ", args, "\n"
  end

  def onResponse(sender, args)
    #print "Response: ", args, "\n"
  end

  def onError(sender, args)
    print 'Error: ', args, "\n"
  end

  def onDocsAutoResponse(sender, args)
    #print "DocsAutoResponse: ", args.length, args, "\n"
  end

  def onCollsAutoResponse(sender, args)
    #print "CollsAutoResponse: ", args.length, args, "\n"
  end
end

# Initializes new session with the keys and app name.
# We also will use compression.
session = Semantria::Session.new($consumer_key, $consumer_secret, 'TestApp', true)

# Initialize session callback handlers
callback = SessionCallbackHandler.new()
session.setCallbackHandler(callback)

subscription = session.getSubscription()

documents = []
tracker = Hash.new

print("Reading collection from file...\n")
f = File.open('source.txt').read
f.gsub!(/\r\n?/, "\n")
f.each_line do |line|
  if not line.empty?
    $initial_texts.push(line)
  end
end

if $initial_texts.size < 1
  print("Source file isn't available or blank.\n")
  exit(1)
end

$initial_texts.each do |text|
  # Creates a sample document which need to be processed on Semantria
  # Unique document ID
  # Source text which need to be processed
  doc_id = SecureRandom.uuid

  documents.push({'id' => doc_id, 'text' => text})
  tracker[doc_id] = $TASK_STATUS_QUEUED
  
  if documents.size == subscription['basic_settings']['incoming_batch_limit']
    result = session.queueBatch(documents)
    if result == 200 or result == 202
      print("#{documents.size} documents queued successfully.\n")
      documents = []
    end
  end
end

if documents.size > 0
  result = session.queueBatch(documents)
  if result != 200 and result != 202
    print("Unexpected error!")
    exit(1)
  end
  
  print("#{documents.size} documents queued successfully.\n")
end

print("\n")

results = []

while not tracker.keep_if{|k, v| v == $TASK_STATUS_QUEUED}.empty?
  sleep(0.5)
  print("Retrieving your processed results...\n")

  response = session.getProcessedDocuments
  response.each do |item|
    if tracker.has_key?(item['id'])
      tracker[item['id']] = item['status']
      results.push(item)
    end
  end
end

print("\n")

results.each do |data|
  # Printing of document sentiment score
  print 'Document ', data['id'], ' Sentiment score: ', data['sentiment_score'], "\r\n"

  # Printing of document themes
  print 'Document themes:', "\r\n"
  data['themes'].nil? or data['themes'].each do |theme|
    print "\t", theme['title'], ' (sentiment: ', theme['sentiment_score'], ")", "\r\n"
  end

  # Printing of document entities
  print 'Entities:', "\r\n"
  data['entities'].nil? or data['entities'].each do |entity|
    print "\t", entity['title'], ' : ', entity['entity_type'], ' (sentiment: ', entity['sentiment_score'], ')', "\r\n"
  end

  print "\r\n"
end
