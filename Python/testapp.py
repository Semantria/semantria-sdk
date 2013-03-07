# -*- coding: utf-8 -*-
from __future__ import print_function
import semantria
import uuid
import time

print("Semantria service demo ...", "\r\n")

# the consumer key and secret
consumerKey = ""
consumerSecret = ""

initialTexts = [
    "Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol",
    "In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.",
    "On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time."
]


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
#session.Request += onRequest
#session.Response += onResponse
session.Error += onError
#session.DocsAutoResponse += onDocsAutoResponse
#session.CollsAutoResponse += onCollsAutoResponse

for text in initialTexts:
    # Creates a sample document which need to be processed on Semantria
    # Unique document ID
    # Source text which need to be processed
    doc = {"id": str(uuid.uuid1()).replace("-", ""), "text": text}
    # Queues document for processing on Semantria service
    status = session.queueDocument(doc)
    # Check status from Semantria service
    if status == 202:
        print("\"", doc["id"], "\" document queued successfully.", "\r\n")

# Count of the sample documents which need to be processed on Semantria
length = len(initialTexts)
results = []

while len(results) < length:
    print("Retrieving your processed results...", "\r\n")
    # As Semantria isn't real-time solution you need to wait some time before getting of the processed results
    # In real application here can be implemented two separate jobs, one for queuing of source data
    # another one for retreiving
    # Wait ten seconds while Semantria process queued document
    time.sleep(2)	
    # Requests processed results from Semantria service
    status = session.getProcessedDocuments()
    # Check status from Semantria service
    if isinstance(status, list):
        for object_ in status:
            results.append(object_)

for data in results:
    # Printing of document sentiment score
    print("Document ", data["id"], " Sentiment score: ", data["sentiment_score"], "\r\n")

    # Printing of document themes
    if "themes" in data:
        print("Document themes:", "\r\n")
        for theme in data["themes"]:
            print("	", theme["title"], " (sentiment: ", theme["sentiment_score"], ")", "\r\n")

    # Printing of document entities
    if "entities" in data:
        print("Entities:", "\r\n")
        for entity in data["entities"]:
            print("\t",
                  entity["title"], " : ", entity["entity_type"],
                  " (sentiment: ", entity["sentiment_score"], ")", "\r\n"
            )

#i = raw_input("Enter to close app ...")
