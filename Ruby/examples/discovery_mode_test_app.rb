#!/usr/bin/env ruby

require File.expand_path('lib/semantria')

# API Key/Secret
# Set the environment vars before calling this program
# or edit this file and put your key and secret here.
$consumer_key = ENV['SEMANTRIA_KEY']
$consumer_secret = ENV['SEMANTRIA_SECRET']

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

print("Semantria Discovery mode demo.\n")

documents = []

print("Reading collection from file...\n")
f = File.open('bellagio-data-100.utf8').read
f.gsub!(/\r\n?/, "\n")
f.each_line do |line|
  documents.push(line)
end

if documents.size < 1
  print("Source file isn't available or blank.\n")
  exit(1)
end

# Initializes new session with the keys and app name.
session = Semantria::Session.new($consumer_key, $consumer_secret,
                                 application_name:'TestApp',
                                 use_compression:true)

# Initialize session callback handlers
callback = SessionCallbackHandler.new()
session.setCallbackHandler(callback)

collection_id = rand(10 ** 10).to_s.rjust(10, '0')

# Queues collection for analysis using default configuration
status = session.queueCollection({"id" => collection_id, "documents" => documents})

if status != 200 and status != 202
  print("Error.\n")
  exit(1)
end

print("Collection queued successfully (collection id: #{collection_id}\n")

# Retreives analysis results for queued collection
status = nil
while true
	sleep(5)
	print("Retrieving your processed results...\n")
	result = session.getCollection(collection_id)
	if result['status'] != 'QUEUED'
		break
	end
end

if result['status'] != 'PROCESSED'
	print("Error\n")
	exit(1)
end

# Prints a sample of analysis results 
print("\n")
result['facets'].nil? or result['facets'].each do |facet|
	print(facet['label'], " : ", facet['count'], "\n")
	facet['attributes'].nil? or facet['attributes'].each do |attrib|
		print("\t", attrib['label'], " : ", attrib['count'], "\n")
	end
end
