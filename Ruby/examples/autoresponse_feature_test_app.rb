#!/usr/bin/env ruby
require 'semantria'

# the consumer key and secret
consumer_key = ''
consumer_secret = ''

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
session = Semantria::Session.new(consumer_key, consumer_secret, 'TestApp', true)
# Initialize session callback handlers
callback = SessionCallbackHandler.new()
session.setCallbackHandler(callback)

# Remembers primary configuration to set it back after the test.
configurations = session.getConfigurations()
primary_configuration = nil
autoresponse_configuration = nil

configurations.each do |c|
  if c['is_primary']
    primary_configuration = c
  end

  if c['name'] == 'AutoResponseTest'
    autoresponse_configuration = c
  end
end

if autoresponse_configuration.nil?
  session.addConfigurations({
    'name' => 'AutoResponseTest',
    'language' => 'English',
    'is_primary' => True,
    'auto_response' => True,
  })
else
  autoresponse_configuration['is_primary'] = true
  session.updateConfigurations([autoresponse_configuration])
end

# Queues documents for analysis one by one
counter = 0
documents.each do |document|
  session.queueDocument({'id' => rand(10 ** 10).to_s.rjust(10, '0'), 'text' => document})
  counter += 1
  sleep(0.1)
  print("Documents queued/received rate: #{counter}/#{$results.size}\n")
end

# The final call to get remained data from server, Just for demo purposes.
sleep(1)
while $results.size < documents.size
  result = session.getProcessedDocuments()
  result.each { |data| $results.push(data) }

  sleep(0.5)
end

print("\nDocuments queued/received rate: #{documents.size}/#{$results.size}\n")

# Sets original primary configuration back after the test.
session.updateConfigurations([primary_configuration])
