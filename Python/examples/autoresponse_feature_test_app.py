# -*- coding: utf-8 -*-
from __future__ import print_function, unicode_literals

import sys
import time
import uuid
import semantria

# API Key/Secret
SEMANTRIA_KEY = ''
SEMANTRIA_SECRET = ''

# List for results
results = []


def onError(sender, result):
    print("\n", "ERROR: ", result)


def autoresponse_handler(sender, result):
    """ Auto-response callback handler """
    for data in result:
        results.append(data)

if __name__ == "__main__":
    print("Semantria Auto-response feature demo.")

    docs = []
    print("Reading documents from file...")
    with open('source.txt') as f:
        for line in f:
            docs.append(line)

    if len(docs) < 1:
        print("Source file isn't available or blank.")
        sys.exit(1)

    # Initializes Semantria Session
    session = semantria.Session(SEMANTRIA_KEY, SEMANTRIA_SECRET, use_compression=True)
    session.Error += onError
    session.DocsAutoResponse += autoresponse_handler

    # Remembers primary configuration to set it back after the test.
    configurations = session.getConfigurations()
    primary_configuration = None
    autoresponse_configuration = None

    for c in configurations:
        if c['is_primary']:
            primary_configuration = c

        if c['name'] == 'AutoResponseTest':
            autoresponse_configuration = c

    if autoresponse_configuration is None:
        session.addConfigurations({
            'name': 'AutoResponseTest',
            'language': 'English',
            'is_primary': True,
            'auto_response': True,
        })
    else:
        autoresponse_configuration['is_primary'] = True
        session.updateConfigurations(autoresponse_configuration)

    # Queues documents for analysis one by one
    counter = 0
    for document in docs:
        session.queueDocument({'id': str(uuid.uuid4()), 'text': document})
        counter += 1
        time.sleep(0.1)
        print("Documents queued/received rate: {0}/{1}".format(counter, len(results)))

    # The final call to get remained data from server, Just for demo purposes.
    time.sleep(1)
    while len(results) < len(docs):
        result = session.getProcessedDocuments()
        for data in results:
            results.append(data)

        time.sleep(0.5)

    print("Documents queued/received rate: {0}/{1}".format(len(docs), len(results)))

    # Sets original primary configuration back after the test.
    session.updateConfigurations(primary_configuration)
