#!/usr/bin/env ruby

require 'securerandom'
require File.expand_path('lib/semantria')

# API Key/Secret
# Set the environment vars before calling this program
# or edit this file and put your key and secret here.
$consumer_key = ENV['SEMANTRIA_KEY']
$consumer_secret = ENV['SEMANTRIA_SECRET']

# 0 - send every single document separately
# 1 - send uniqueJobIdCount batches
# 2 - send all documents in single batch
data_sending_mode = 2
unique_jobid_count = 4

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

print("Semantria JobId feature demo.\n")

job_documents = Hash.new
jobs = Hash.new
job_ids = []

Array(0..unique_jobid_count-1).each do |i|
  job_id = SecureRandom.uuid
  job_ids.push(job_id)
  jobs[job_id] = 0
  job_documents[job_id] = []
end

print("Reading collection from file...\n")
f = File.open('source.txt').read
f.gsub!(/\r\n?/, "\n")
f.each_line do |line|
  job_id = job_ids[SecureRandom.random_number(unique_jobid_count)]
  jobs[job_id] += 1
  job_documents[job_id].push({'id' => SecureRandom.uuid, 'text' => line, 'job_id' => job_id})
end

# Initializes new session with the keys and app name.
# We also will use compression.
session = Semantria::Session.new($consumer_key, $consumer_secret,
                                 application_name:'TestApp',
                                 use_compression:true)

# Initialize session callback handlers
callback = SessionCallbackHandler.new()
session.setCallbackHandler(callback)

if data_sending_mode == 0
  job_documents.each do |job_id, documents|
    documents.each do |document|
      session.queueDocument(document)
    end

    print("#{documents.size} documents queued for #{job_id} job ID\n")
  end
elsif data_sending_mode == 1
  job_documents.each do |job_id, documents|
    res = session.queueBatch(documents)
    if not res.nil?
      print("#{documents.size} documents queued for #{job_id} job ID\n")
    end
  end
else
  full_batch = []

  job_documents.each do |job_id, documents|
    full_batch += documents
  end

  res = session.queueBatch(full_batch)
  if not res.nil?
    print("#{full_batch.size} documents queued in single batch\n")
  end
end

print("\n")

jobs.each do |job_id, documents_count|
  counter = 0

  while jobs[job_id] > 0
    sleep(0.5)

    res = session.getProcessedDocumentsByJobId(job_id)
    jobs[job_id] -= res.size
    counter += res.size
  end

  print("#{counter} documents received for #{job_id} job ID\n")
end

print("\n")
print("Done!\n")
    
