# -*- coding: utf-8 -*-
from __future__ import print_function, unicode_literals

import time
import uuid
import random
import semantria

# API Key/Secret
SEMANTRIA_KEY = ''
SEMANTRIA_SECRET = ''

# 0 - send every single document separately
# 1 - send uniqueJobIdCount batches
# 2 - send all documents in single batch
data_sending_mode = 2
unique_jobid_count = 4
job_ids = []
jobs = {}
documents = {}


def onError(sender, response):
    print("\n", "ERROR: ", response)


if __name__ == "__main__":
    print("Semantria JobId feature demo.")

    # Generates N unique jobId values
    for i in range(unique_jobid_count):
        job_id = str(uuid.uuid4())
        job_ids.append(job_id)
        jobs[job_id] = 0
        documents[job_id] = []

    print("Reading documents from file...")
    with open('source.txt') as f:
        for line in f:
            if len(line) < 3:
                continue

            job_id = job_ids[random.randint(0, unique_jobid_count-1)]

            jobs[job_id] += 1
            documents[job_id].append({
                'id': str(uuid.uuid4()),
                'text': line,
                'job_id': job_id
            })

    # Initializes Semantria Session
    session = semantria.Session(SEMANTRIA_KEY, SEMANTRIA_SECRET, use_compression=True)
    session.Error += onError

    if data_sending_mode == 0:
        for job_id, docs in documents.items():
            for document in docs:
                session.queueDocument(document)
            print("{0} documents queued for {1} job ID".format(len(docs), job_id))
    elif data_sending_mode == 1:
        for job_id, docs in documents.items():
            if session.queueBatch(docs) is not None:
                print("{0} documents queued for {1} job ID".format(len(docs), job_id))
    else:
        full_batch = []

        for job_id, docs in documents.items():
            full_batch.extend(docs)

        if session.queueBatch(full_batch) is not None:
            print("{0} documents queued in single batch".format(len(full_batch)))

    print("")

    for job_id, doc_count in jobs.items():
        counter = 0
        while jobs[job_id] > 0:
            time.sleep(0.5)

            results = session.getProcessedDocumentsByJobId(job_id)
            jobs[job_id] -= len(results)
            counter += len(results)

        print("{0} documents received for {1} Job ID.".format(counter, job_id))

    print("")
    print("Done!")
