#!/usr/bin/env ruby
require 'semantria'

print 'Semantria service demo ...', "\r\n"

# the consumer key and secret
$consumer_key = ''
$consumer_secret = ''

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

class SessionCallbackHandler < CallbackHandler
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
session = Session.new($consumer_key, $consumer_secret, 'TestApp', true)
# Initialize session callback handlers
callback = SessionCallbackHandler.new()
session.setCallbackHandler(callback)

$initial_texts.each do |text|
  # Creates a sample document which need to be processed on Semantria
  # Unique document ID
  # Source text which need to be processed
  doc = {'id' => rand(10 ** 10).to_s.rjust(10, '0'), 'text' => text}
  # Queues document for processing on Semantria service
  status = session.queueDocument(doc)
  # Check status from Semantria service
  if status == 202
    print 'Document ', doc['id'], ' queued successfully.', "\r\n"
  end
end

# Count of the sample documents which need to be processed on Semantria
length = $initial_texts.length
results = []

while results.length < length
  print 'Please wait 10 sec for documents ...', "\r\n"
  # As Semantria isn't real-time solution you need to wait some time before getting of the processed results
  # In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
  # Wait ten seconds while Semantria process queued document
  sleep(10)
  # Requests processed results from Semantria service
  status = session.getProcessedDocuments()
  # Check status from Semantria service
  status.is_a? Array and status.each do |object|
    results.push(object)
  end
  print status.length, ' documents received successfully.', "\r\n"
end

results.each do |data|
  # Printing of document sentiment score
  print 'Document ', data['id'], ' Sentiment score: ', data['sentiment_score'], "\r\n"

  # Printing of document themes
  print 'Document themes:', "\r\n"
  data['themes'].nil? or data['themes'].each do |theme|
    print '  ', theme['title'], ' (sentiment: ', theme['sentiment_score'], ")", "\r\n"
  end

  # Printing of document entities
  print 'Entities:', "\r\n"
  data['entities'].nil? or data['entities'].each do |entity|
    print '  ', entity['title'], ' : ', entity['entity_type'], ' (sentiment: ', entity['sentiment_score'], ')', "\r\n"
  end

  print "\r\n"
end