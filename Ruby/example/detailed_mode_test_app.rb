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

$initial_texts = [
    'Lisa - there\'s 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes
   and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2
   (1 from my account and 1 from dh\'s). I couldn\'t find them instore and i\'m not going to walmart
   before the 19th. Oh well sounds like i\'m not missing much ...lol',

    "In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe
   on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise
   and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff
   a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls
   and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit
   to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.",

    'On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time.'
]

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

documents = []
tracker = Hash.new

$initial_texts.each do |text|
  # Creates a sample document which need to be processed on Semantria
  # Unique document ID
  # Source text which need to be processed
  doc_id = SecureRandom.uuid

  documents.push({'id' => doc_id, 'text' => text})
  tracker[doc_id] = $TASK_STATUS_QUEUED
end

result = session.queueBatch(documents)
if result != 200 and result != 202
  print("Unexpected error!")
  exit(1)
end

print("#{documents.size} documents queued successfully.\n")
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
