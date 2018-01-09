# -*- coding: utf-8 -*-

from __future__ import print_function

import sys, os, uuid, time
import semantria

# API Key/Secret
# Set the environment vars before calling this program
# or edit this file and put your key and secret here.
consumerKey = os.getenv('SEMANTRIA_KEY')
consumerSecret = os.getenv('SEMANTRIA_SECRET')

# List for results
all_results = []

# config to use or create
CONFIG_NAME = 'AutoResponseTest'

def onError(sender, result):
    print("\n", "ERROR: ", result)


def autoresponse_handler(sender, result):
    """Auto-response callback handler"""
    for data in result:
        all_results.append(data)


print("Semantria Auto-response feature demo.")

docs = []
print("Reading documents from file...")
with open('source.txt', encoding='utf-8') as f:
    for line in f:
        docs.append(line)

if len(docs) < 1:
    print("Source file isn't available or empty.")
    sys.exit(1)

# Create some batches
batches = []
batch_size = 10
for i in range(0, len(docs), batch_size):
    batches.append([{'id': str(uuid.uuid4()), 'text': doc} for doc in docs[i : i+batch_size]])

print(len(batches))
print([len(b) for b in batches])
print(sum([len(b) for b in batches]))

# Initialize Semantria Session
session = semantria.Session(consumerKey, consumerSecret)
session.Error += onError
session.DocsAutoResponse += autoresponse_handler

# Update or create configuration for this test.
# The config must have auto_response set to true.
configurations = session.getConfigurations()
autoresponse_config = None

for c in configurations:
    if c['name'] == CONFIG_NAME:
        autoresponse_config = c

if autoresponse_config:
    print("Using config '{}' - id = {}".format(
        autoresponse_config['name'], autoresponse_config['id']))
else:
    print("Creating new config named '{}'...".format(CONFIG_NAME))
    new_configs = session.addConfigurations({'name': CONFIG_NAME,
                                             'language': 'English',
                                             'auto_response': True,})
    autoresponse_config = new_configs[0]
    print("Created new config - id = {0}".format(autoresponse_config['id']))

if not autoresponse_config['auto_response']:
    print("config named '{0}' does not have auto_response set to true".format(
        autoresponse_config['name']))
    sys.exit(1)


# Queue documents for analysis, sleeping for 100 msec between each batch.
# This allows enough time for some docs to be processed and thus be available to be
# returned by auto response.
counter = 0
for batch in batches:
    session.queueBatch(batch, autoresponse_config['id'])
    counter += len(batch)
    #time.sleep(0.01)
    print("Documents queued/received: {0}/{1}".format(counter, len(all_results)))

# Finally, poll for any docs that weren't returned via auto response.
# This is needed in this demo because it is only processing a fixed number of docs.
# A continuous process that runs forever would not need to do this step, because
# it would never get to the end of it's docs to be processed.
while len(all_results) < len(docs):
    time.sleep(0.2)
    results = session.getProcessedDocuments(autoresponse_config['id'])
    for data in results:
        all_results.append(data)
    print("Polling: Documents queued/received: {0}/{1}".format(len(docs), len(all_results)))
    sys.stdout.flush()
    sys.stderr.flush()

print("")
print("Done: Documents queued/received: {0}/{1}".format(len(docs), len(all_results)))

