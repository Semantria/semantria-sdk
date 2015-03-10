# -*- coding: utf-8 -*-
from __future__ import print_function, unicode_literals

import sys
import time
import uuid
import semantria

# API Key/Secret
SEMANTRIA_KEY = ''
SEMANTRIA_SECRET = ''


def onError(sender, result):
    print("\n", "ERROR: ", result)

if __name__ == "__main__":
	print("Semantria Discovery mode demo.")

	docs = []
	print("Reading collection from file...")
	with open('source.txt') as f:
		for line in f:
			docs.append(line)

	if len(docs) < 1:
		print("Source file isn't available or blank.")
		sys.exit(1)

	# Initializes Semantria Session
	session = semantria.Session(SEMANTRIA_KEY, SEMANTRIA_SECRET, use_compression=True)
	session.Error += onError

	# Queues collection for analysis using default configuration
	collection_id = str(uuid.uuid4())
	status = session.queueCollection({"id": collection_id, "documents": docs})
	if status != 200 and status != 202:
		print("Error.")
		sys.exit(1)

	print("%s collection queued successfully." % collection_id)

	# Retreives analysis results for queued collection
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

	# Prints analysis results
	print("")
	for facet in result['facets']:
		print("%s : %s" % (facet['label'], facet['count']))
		try:
			for attr in facet['attributes']:
				print("\t%s : %s" % (attr['label'], attr['count']))
		except KeyError:
			pass
