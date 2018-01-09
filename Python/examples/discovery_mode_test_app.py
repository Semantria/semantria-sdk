# -*- coding: utf-8 -*-

from __future__ import print_function, unicode_literals

import sys, os, uuid, time
import semantria

# API Key/Secret
# Set the environment vars before calling this program
# or edit this file and put your key and secret here.
consumerKey = os.getenv('SEMANTRIA_KEY')
consumerSecret = os.getenv('SEMANTRIA_SECRET')


def onError(sender, result):
    print("\n", "ERROR: ", result)


print("Semantria Discovery mode demo.")

docs = []
print("Reading collection from file...")
with open('source.txt', encoding='utf-8') as f:
    for line in f:
        docs.append(line)

if len(docs) < 1:
    print("Source file isn't available or blank.")
    sys.exit(1)

# Initialize Semantria Session
session = semantria.Session(consumerKey, consumerSecret)
session.Error += onError

# Queue collection for analysis using default configuration
collection_id = str(uuid.uuid4())
status = session.queueCollection({"id": collection_id, "documents": docs})
if status != 200 and status != 202:
    print("Error.")
    sys.exit(1)

print("%s collection queued successfully." % collection_id)

# Retreive analysis results for queued collection
result = None
while True:
    time.sleep(1)
    print("Retrieving your processed results...")
    result = session.getCollection(collection_id)
    if result['status'] != 'QUEUED':
        break

if result['status'] != 'PROCESSED':
    print("Error.")
    sys.exit(1)

# Print sample of analysis results. (There's lots more in there!)
print("")
for facet in result['facets']:
    print("%s : %s" % (facet['label'], facet['count']))
    try:
        for attr in facet['attributes']:
            print("\t%s : %s" % (attr['label'], attr['count']))
    except KeyError:
        pass

print("")
print("Done!")
