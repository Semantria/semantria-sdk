#!/usr/bin/env ruby

require File.expand_path('lib/semantria')

# API Key/Secret
# Set the environment vars before calling this program
# or edit this file and put your key and secret here.
$consumer_key = ENV['SEMANTRIA_KEY']
$consumer_secret = ENV['SEMANTRIA_SECRET']

$results = []

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
    args.each { |data| $results.push(data) }
  end

  def onCollsAutoResponse(sender, args)
    #print "CollsAutoResponse: ", args.length, args, "\n"
  end
end

print("Semantria Auto-response feature demo.\n")

documents = []

print("Reading collection from file...\n")
f = File.open('source.txt').read
f.gsub!(/\r\n?/, "\n")
f.each_line do |line|
  documents.push(line)
end

if documents.size < 1
  print("Source file isn't available or blank.\n")
  exit(1)
end

# Initializes new session with the keys and app name.
# We also will use compression.
session = Semantria::Session.new($consumer_key, $consumer_secret,
                                 application_name:'TestApp',
                                 use_compression:true)

# Initialize session callback handlers
callback = SessionCallbackHandler.new()
session.setCallbackHandler(callback)

# Get or create auto response config
configurations = session.getConfigurations()
config_id = nil

configurations.each do |c|
  if c['name'] == 'AutoResponseTest'
    config_id = c['id']
  end
end

if config_id.nil?
  response = session.addConfigurations({
    'name' => 'AutoResponseTest',
    'language' => 'English',
    'auto_response' => True,
  })
  config_id = response[0]['id']
end

# Queues documents for analysis one by one
counter = 0
documents.each do |document|
  session.queueDocument({'id' => rand(10 ** 10).to_s.rjust(10, '0'), 'text' => document},
                        config_id)
  counter += 1
  sleep(0.1)
  print("Documents queued/received rate: #{counter}/#{$results.size}\n")
end

# Finally, poll for any docs that weren't returned via auto response.
# This is needed in this demo because it is only processing a fixed number of docs.
# A continuous process that runs forever would not need to do this step, because
# it would never get to the end of it's docs to be processed.
sleep(1)
while $results.size < documents.size
  result = session.getProcessedDocuments(config_id)
  result.each { |data| $results.push(data) }

  sleep(0.5)
end

print("\nDocuments queued/received rate: #{documents.size}/#{$results.size}\n")

