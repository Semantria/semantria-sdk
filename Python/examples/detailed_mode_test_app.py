# -*- coding: utf-8 -*-

from __future__ import print_function, unicode_literals

import sys, os, uuid, time
import semantria


# API Key/Secret
# Set the environment vars before calling this program
# or edit this file and put your key and secret here.
consumerKey = os.getenv('SEMANTRIA_KEY')
consumerSecret = os.getenv('SEMANTRIA_SECRET')

# Task statuses
TASK_STATUS_UNDEFINED = 'UNDEFINED'
TASK_STATUS_FAILED = 'FAILED'
TASK_STATUS_QUEUED = 'QUEUED'
TASK_STATUS_PROCESSED = 'PROCESSED'


def onRequest(sender, result):
    print("\n", "REQUEST: ", result)

def onResponse(sender, result):
    print("\n", "RESPONSE: ", result)

def onError(sender, result):
    print("\n", "ERROR: ", result)

def onDocsAutoResponse(sender, result):
    print("\n", "AUTORESPONSE: ", len(result), result)

def onCollectionsAutoResponse(sender, result):
    print("\n", "AUTORESPONSE: ", len(result), result)


print("Semantria Detailed mode demo ...")
print("")

# Initializes new session with the serializer object and the keys.
session = semantria.Session(consumerKey, consumerSecret)

# Initialize session callback handlers
# session.Request += onRequest
# session.Response += onResponse
session.Error += onError
# session.DocsAutoResponse += onDocsAutoResponse
# session.CollectionsAutoResponse += onCollectionsAutoResponse

subscription = session.getSubscription()

initialTexts = []

print("Reading collection from file...")
with open('source.txt', encoding='utf-8') as f:
    for line in f:
        initialTexts.append(line)

results = []
tracker = {}
batch = []
for text in initialTexts:
    # Creates a sample document which need to be processed on Semantria
    # Unique document ID
    # Source text which need to be processed
    doc_id = str(uuid.uuid4())
    batch.append({'id': doc_id, 'text': text})
    tracker[doc_id] = TASK_STATUS_QUEUED
    
    if len(batch) == subscription['basic_settings']['incoming_batch_limit']:
        res = session.queueBatch(batch)
        if res in [200, 202]:
            print("{0} documents queued successfully.".format(len(batch)))
            batch = []

if len(batch):
    res = session.queueBatch(batch)
    if res not in [200, 202]:
        print("Unexpected error!")
        sys.exit(1)
    print("{0} documents queued successfully.".format(len(batch)))

print("")

while len(list(filter(lambda x: x == TASK_STATUS_QUEUED, tracker.values()))):
    time.sleep(0.5)
    print("Retrieving your processed results...")

    response = session.getProcessedDocuments()
    for item in response:
        if item['id'] in tracker:
            tracker[item['id']] = item['status']
            results.append(item)

print("")

# Print sample of analysis results. (There's lots more in there!)
for data in results:
    # Print document sentiment score
    print("Document {0} / Sentiment score: {1}".format(
        data['id'], data['sentiment_score']))

    # Print document themes
    if "themes" in data:
        print("Document themes:")
        for theme in data["themes"]:
            print("\t {0} (sentiment: {1})".format(
                theme['title'].encode('ascii', 'replace'), theme['sentiment_score']))

    # Print document entities
    if "entities" in data:
        print("Entities:")
        for entity in data["entities"]:
            print("\t {0}: {1} (sentiment: {2})".format(
                entity['title'].encode('ascii', 'replace'), entity['entity_type'],
                entity['sentiment_score']
            ))

    print("")

print("Done!")
