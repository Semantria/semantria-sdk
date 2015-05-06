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

initialTexts = [
    "Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol",
    "In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.",
    "On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time."
]

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

results = []
tracker = {}
documents = []

for text in initialTexts:
    # Creates a sample document which need to be processed on Semantria
    # Unique document ID
    # Source text which need to be processed
    doc_id = str(uuid.uuid4())
    documents.append({'id': doc_id, 'text': text})
    tracker[doc_id] = TASK_STATUS_QUEUED

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
