# -*- coding: utf-8 -*-
from __future__ import print_function
import unittest
import uuid

import semantria

# the consumer key and secret
consumerKey = ""
consumerSecret = ""

col_id = str(uuid.uuid1()).replace("-", "")
doc_id = str(uuid.uuid1()).replace("-", "")
message = "Amazon Web Services has announced a new feature called VMWare Import, " \
          "which allows IT departments to move virtual machine images from their " \
          "internal data centers to the cloud."


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


class SemantriaSessionTest(unittest.TestCase):
    def setUp(self):
        #self.serializer = semantria.XmlSerializer()
        self.serializer = semantria.JsonSerializer()
        self.session = semantria.Session(consumerKey, consumerSecret, self.serializer, use_compression=True)

        self.session.Request += onRequest
        self.session.Response += onResponse
        self.session.Error += onError
        self.session.DocsAutoResponse += onDocsAutoResponse
        self.session.CollsAutoResponse += onCollsAutoResponse

    def test_AddBlacklist(self):
        status = self.session.addBlacklist("27123")
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_GetBlacklist(self):
        response = self.session.getBlacklist()
        self.failIf(response is None)
        print("%(count)d item(s) in blacklist" % {'count': len(response)})

    def test_RemoveBlacklist(self):
        status = self.session.removeBlacklist(["http*"])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_GetCategories(self):
        status = self.session.getCategories()
        self.failIf(status is None)

    def test_AddCategories(self):
        status = self.session.addCategories([{
            'name': "Service",
            'samples': ["Amazon", "VMWare"],
            'weight': 0.1,
        }, {
            'name': "Web",
            'samples': ["Google", "Yahoo"],
            'weight': 0.2
        }])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_RemoveCategories(self):
        status = self.session.removeCategories(["Web"])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_GetQueries(self):
        status = self.session.getQueries()
        self.failIf(status is None)

    def test_AddQueries(self):
        status = self.session.addQueries([{"name": "Web", "query": "Amazon"}])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_RemoveQueries(self):
        status = self.session.removeQueries(["any"])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_GetSentimentPhrases(self):
        status = self.session.getPhrases()
        self.failIf(status is None)

    def test_AddSentimentPhrases(self):
        status = self.session.addPhrases([
            {"name": "Web", "weight": 0.1},
            {"name": "any", "weight": 0.2}
        ])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_RemoveSentimentPhrases(self):
        status = self.session.removePhrases(["any"])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_GetEntities(self):
        status = self.session.getEntities()
        self.failIf(status is None)

    def test_AddEntities(self):
        status = self.session.addEntities([
            {"name": "Amazon", "type": "Web"},
            {"name": "any", "type": "any"}
        ])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_RemoveEntities(self):
        status = self.session.removeEntities(["any"])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

    def test_CreateDocument(self):
        print("\n", "*** Create document: ", doc_id, "\n")
        status = self.session.queueDocument({"id": doc_id, "text": message})
        print("RESULT:", status, "\n")
        self.failIf(status != 200 and status != 202)

    def test_GetDocument(self):
        print("\n", "*** Get document: ", doc_id, "\n")
        status = self.session.getDocument(doc_id)
        print("RESULT:", status, "\n")
        self.failIf(status is None)

    # def test_CancelDocument(self):
    #     print "\n", "*** Cancel document: ", doc_id, "\n"
    #     status = self.session.cancelDocument(doc_id)
    #     print "RESULT:", status, "\n"
    #     self.failIf(status != 200 and status != 202)

    def test_CreateBatchDocuments(self):
        batch = []
        var = 10
        while var > 0:
            batch.append({"id": str(uuid.uuid1()).replace("-", ""), "text": message})
            var -= 1
        status = self.session.queueBatch(batch)
        print("RESULT:", status, "\n")
        self.failIf(status != 200 and status != 202)

    def test_GetProcessedDocuments(self):
        status = self.session.getProcessedDocuments()
        print("RESULT:", len(status), status, "\n")
        self.failIf(status is None)

    def test_CreateCollection(self):
        print("\n", "*** Create collection: ", col_id, "\n")
        status = self.session.queueCollection({"id": col_id, "documents": [message, message]})
        print("RESULT:", status, "\n")
        self.failIf(status != 200 and status != 202)

    def test_GetCollection(self):
        print("\n", "*** Get collection: ", col_id, "\n")
        status = self.session.getCollection(col_id)
        print("RESULT:", status, "\n")
        self.failIf(status is None)

    # def test_CancelCollection(self):
    #     print "\n", "*** Cancel collection: ", col_id, "\n"
    #     status = self.session.cancelCollection(col_id)
    #     print "RESULT:", status, "\n"
    #     self.failIf(status != 200 and status != 202)

    def test_GetProcessedCollections(self):
        status = self.session.getProcessedCollections()
        print("RESULT:", len(status), status, "\n")
        self.failIf(status is None)

    def test_Status(self):
        status = self.session.getStatus()
        print("RESULT:", status, "\n")
        self.failIf(status is None)

    def test_VerifySubscription(self):
        status = self.session.getSubscription()
        print("RESULT:", status, "\n")
        self.failIf(status is None)

    def test_GetConfigurations(self):
        status = self.session.getConfigurations()
        print("RESULT:", status, "\n")
        self.failIf(status is None)

    def test_UpdateConfiguration(self):
        response = self.session.getConfigurations()
        print("RESULT:", response)
        self.failIf(response is None)

        default = None
        for item in response:
            if item["name"] == "default":
                item["auto_response"] = False
                default = item

        self.failIf(default is None)
        status = self.session.updateConfigurations([default])
        print("RESULT:", status, "\n")
        self.failIf(status != 202)

if __name__ == '__main__':
    #unittest.main()

    suite = unittest.TestSuite()
    suite.addTest(SemantriaSessionTest('test_Status'))
    suite.addTest(SemantriaSessionTest('test_VerifySubscription'))
    suite.addTest(SemantriaSessionTest('test_GetConfigurations'))
    suite.addTest(SemantriaSessionTest('test_UpdateConfiguration'))
    suite.addTest(SemantriaSessionTest('test_GetBlacklist'))
    suite.addTest(SemantriaSessionTest('test_AddBlacklist'))
    suite.addTest(SemantriaSessionTest('test_RemoveBlacklist'))
    suite.addTest(SemantriaSessionTest('test_GetCategories'))
    suite.addTest(SemantriaSessionTest('test_AddCategories'))
    suite.addTest(SemantriaSessionTest('test_RemoveCategories'))
    suite.addTest(SemantriaSessionTest('test_GetQueries'))
    suite.addTest(SemantriaSessionTest('test_AddQueries'))
    suite.addTest(SemantriaSessionTest('test_RemoveQueries'))
    suite.addTest(SemantriaSessionTest('test_GetSentimentPhrases'))
    suite.addTest(SemantriaSessionTest('test_AddSentimentPhrases'))
    suite.addTest(SemantriaSessionTest('test_RemoveSentimentPhrases'))
    suite.addTest(SemantriaSessionTest('test_GetEntities'))
    suite.addTest(SemantriaSessionTest('test_AddEntities'))
    suite.addTest(SemantriaSessionTest('test_RemoveEntities'))

    suite.addTest(SemantriaSessionTest('test_CreateDocument'))
    suite.addTest(SemantriaSessionTest('test_GetDocument'))
    #suite.addTest(SemantriaSessionTest('test_CancelDocument'))
    suite.addTest(SemantriaSessionTest('test_CreateBatchDocuments'))
    suite.addTest(SemantriaSessionTest('test_GetProcessedDocuments'))

    suite.addTest(SemantriaSessionTest('test_CreateCollection'))
    suite.addTest(SemantriaSessionTest('test_GetCollection'))
    #suite.addTest(SemantriaSessionTest('test_CancelCollection'))
    suite.addTest(SemantriaSessionTest('test_GetProcessedCollections'))

    unittest.TextTestRunner(verbosity=2).run(suite)
