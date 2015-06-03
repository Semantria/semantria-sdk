# -*- coding: utf-8 -*-
from __future__ import print_function, unicode_literals

import sys
import semantria
import uuid
import time

print("Semantria Detailed mode demo ...")
print("")

# the consumer key and secret
consumerKey = ""
consumerSecret = ""

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


def onCollsAutoResponse(sender, result):
    print("\n", "AUTORESPONSE: ", len(result), result)

# Creates JSON serializer instance
serializer = semantria.JsonSerializer()
# Initializes new session with the serializer object and the keys.
session = semantria.Session(consumerKey, consumerSecret, serializer, use_compression=True)

# Initialize session callback handlers
# session.Request += onRequest
# session.Response += onResponse
session.Error += onError
# session.DocsAutoResponse += onDocsAutoResponse
# session.CollsAutoResponse += onCollsAutoResponse

subscription = session.getSubscription()

initialTexts = []
results = []
tracker = {}
documents = []

print("Reading collection from file...")
with open('source.txt') as f:
	for line in f:
		initialTexts.append(line)

for text in initialTexts:
    # Creates a sample document which need to be processed on Semantria
    # Unique document ID
    # Source text which need to be processed
    doc_id = str(uuid.uuid4())
    documents.append({'id': doc_id, 'text': text})
    tracker[doc_id] = TASK_STATUS_QUEUED
    
    if len(documents) == subscription['basic_settings']['batch_limit']:
        res = session.queueBatch(documents)
        if res in [200, 202]:
            print("{0} documents queued successfully.".format(len(documents)))
            documents = []

if len(documents):
    res = session.queueBatch(documents)
    if res not in [200, 202]:
        print("Unexpected error!")
        sys.exit(1)
    print("{0} documents queued successfully.".format(len(documents)))

print("")

while len(filter(lambda x: x == TASK_STATUS_QUEUED, tracker.values())):
    time.sleep(0.5)
    print("Retrieving your processed results...")

    response = session.getProcessedDocuments()
    for item in response:
        if item['id'] in tracker:
            tracker[item['id']] = item['status']
            results.append(item)

print("")

for data in results:
    # Printing of document sentiment score
    print("Document {0} / Sentiment score: {1}".format(data['id'], data['sentiment_score']))

    # Printing of document themes
    if "themes" in data:
        print("Document themes:")
        for theme in data["themes"]:
            print("\t {0} (sentiment: {1})".format(theme['title'], theme['sentiment_score']))

    # Printing of document entities
    if "entities" in data:
        print("Entities:")
        for entity in data["entities"]:
            print("\t {0}: {1} (sentiment: {2})".format(
                entity['title'], entity['entity_type'], entity['sentiment_score']
            ))

    print("")

print("Done!")
