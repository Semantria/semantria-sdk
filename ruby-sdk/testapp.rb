#!/usr/bin/env ruby

$LOAD_PATH << File.dirname(__FILE__) unless $LOAD_PATH.include?(File.dirname(__FILE__)) 

require 'semantria/session'
require 'semantria/jsonserializer'
require 'semantria/xmlserializer'

print "Semantria service demo ...", "\r\n"

# the consumer key and secret
$consumerKey = ""
$consumerSecret = ""

$initialTexts = [
	"Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol",
	"In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.",
	"On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time."
]
	
class SessionCallbackHandler < CallbackHandler
  def onRequest(sender, args)
    #print "Request: ", args, "\n"
  end
  
  def onResponse(sender, args)
    #print "Response: ", args, "\n"
  end
    
  def onError(sender, args)
    print "Error: ", args, "\n"
  end
  
  def onDocsAutoResponse(sender, args)
    #print "DocsAutoResponse: ", args.length, args, "\n"
  end
    
  def onCollsAutoResponse(sender, args)
    #print "CollsAutoResponse: ", args.length, args, "\n"
  end
end

print "Semantria service demo.", "\r\n"

# Creates JSON serializer instance
serializer = XmlSerializer.new()
# Initializes new session with the serializer object and the keys.
session = Session.new($consumerKey, $consumerSecret, serializer)
# Initialize session callback handlers
callback = SessionCallbackHandler.new()
session.setCallbackHandler(callback)

$initialTexts.each do |text|
    # Creates a sample document which need to be processed on Semantria
    # Unique document ID
    # Source text which need to be processed
    doc = { "id"=>rand(10 ** 10).to_s.rjust(10,'0'), "text"=>text }
    # Queues document for processing on Semantria service
    status = session.queueDocument(doc)
    # Check status from Semantria service
    if (status == 202)
        print "\"", doc["id"], "\" document queued successfully.", "\r\n"
	end
end

# Count of the sample documents which need to be processed on Semantria
length = $initialTexts.length
results = []

while (results.length < length)
    # As Semantria isn't real-time solution you need to wait some time before getting of the processed results
    # In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
    # Wait ten seconds while Semantria process queued document
    print "Retrieving your processed results...", "\r\n"
	sleep(2)	
    # Requests processed results from Semantria service
    status = session.getProcessedDocuments()
    # Check status from Semantria service
    if (status.is_a?(Array))
        status.each do |object|
            results.push(object)
		end
	end
end

results.each do |data|
    # Printing of document sentiment score
	print "Document ", data["id"], " Sentiment score: ", data["sentiment_score"], "\r\n"
    
	# Printing of document themes
	print "Document themes:", "\r\n"
	if data["themes"]
		data["themes"].each do |theme|
			print "	", theme["title"], " (sentiment: ", theme["sentiment_score"], ")", "\r\n"
		end
	end
	
	# Printing of document entities
	print "Entities:", "\r\n"
	if data["entities"]
		data["entities"].each do |entity|
			print "	", entity["title"], " : ", entity["entity_type"], " (sentiment: ", entity["sentiment_score"], ")", "\r\n"
		end
	end
	
	print "\r\n"
end