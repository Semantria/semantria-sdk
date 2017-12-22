# -*- coding: utf-8 -*-

from __future__ import print_function
import unittest, uuid, os

import semantria

# the consumer key and secret
consumerKey = os.getenv('SEMANTRIA_KEY')
consumerSecret = os.getenv('SEMANTRIA_SECRET')

col_id = str(uuid.uuid1()).replace("-", "")
doc_id = str(uuid.uuid1()).replace("-", "")
message = "Amazon Web Services has announced a new feature called VMWare Import, " \
          "which allows IT departments to move virtual machine images from their " \
          "internal data centers to the cloud."


def onRequest(sender, result):
    # print("\n", "REQUEST: ", result)
    pass


def onResponse(sender, result):
    # print("\n", "RESPONSE: ", result)
    pass


def onError(sender, result):
    print("\n", "ERROR: ", result)


def onDocsAutoResponse(sender, result):
    # print("\n", "AUTORESPONSE: ", len(result), result)
    pass


def onCollsAutoResponse(sender, result):
    # print("\n", "AUTORESPONSE: ", len(result), result)
    pass


class SemantriaSessionTest(unittest.TestCase):
    def setUp(self):
        # self.serializer = semantria.XmlSerializer()
        self.serializer = semantria.JsonSerializer()
        self.session = semantria.Session(consumerKey, consumerSecret, self.serializer, use_compression=True)

        self.session.Request += onRequest
        self.session.Response += onResponse
        self.session.Error += onError
        self.session.DocsAutoResponse += onDocsAutoResponse
        self.session.CollsAutoResponse += onCollsAutoResponse

    def test_CRUDBlacklist(self):
        response = self.session.getBlacklist()
        self.failIf(response is None)

        response = self.session.addBlacklist({"name": "python3*"})
        self.failIf(len(response) == 0)

        blacklist = None
        for item in response:
            if item['name'] == 'python3*':
                blacklist = item
                break
        self.failIf(blacklist is None)

        status = self.session.removeBlacklist([blacklist['id']])
        self.failIf(status != 202)

    def test_CRUDCategories(self):
        response = self.session.getCategories()
        self.failIf(response is None)
        response = self.session.addCategories([{
            'name': "TEST_CATEGORY_PYTHON3",
            'samples': ["Amazon", "VMWare"],
            'weight': 0.1,
        }])
        self.failIf(len(response) == 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_CATEGORY_PYTHON3':
                obj = item
                break
        self.failIf(obj is None)

        response = self.session.removeCategories([obj['id']])
        self.failIf(response != 202)

    def test_CRUDQueries(self):
        response = self.session.getQueries()
        self.failIf(response is None)

        response = self.session.addQueries([{"name": "TEST_QUERY_PYTHON3", "query": "Amazon"}])
        self.failIf(len(response) == 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_QUERY_PYTHON3':
                obj = item
                break
        self.failIf(obj is None)

        response = self.session.removeQueries([obj['id']])
        self.failIf(response != 202)

    def test_CRUDPhrases(self):
        response = self.session.getPhrases()
        self.failIf(response is None)

        response = self.session.addPhrases([
            {"name": "TEST_PHRASE_PYTHON3", "weight": 0.1},
        ])
        self.failIf(len(response) == 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_PHRASE_PYTHON3':
                obj = item
                break
        self.failIf(obj is None)

        response = self.session.removePhrases([obj['id']])
        self.failIf(response != 202)

    def test_CRUDEntities(self):
        response = self.session.getEntities()
        self.failIf(response is None)

        response = self.session.addEntities([
            {"name": "TEST_ENTITY_PYTHON3", "type": "Web"},
        ])
        self.failIf(len(response) == 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_ENTITY_PYTHON3':
                obj = item
                break
        self.failIf(obj is None)

        response = self.session.removeEntities([obj['id']])
        self.failIf(response != 202)

    def test_CRUDTaxonomy(self):
        response = self.session.getTaxonomy()
        self.failIf(response is None)

        response = self.session.addTaxonomy([
            {"name": "TEST_TAXONOMY_PYTHON3"},
        ])
        self.failIf(len(response) == 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_TAXONOMY_PYTHON3':
                obj = item
                break
        self.failIf(obj is None)

        response = self.session.removeTaxonomy([obj['id']])
        self.failIf(response != 202)

    def test_CreateDocument(self):
        status = self.session.queueDocument({"id": doc_id, "text": message})
        self.failIf(status != 200 and status != 202)

    def test_GetDocument(self):
        status = self.session.getDocument(doc_id)
        self.failIf(status is None)

    def test_CreateBatchDocuments(self):
        batch = []
        var = 10
        while var > 0:
            batch.append({"id": str(uuid.uuid1()).replace("-", ""), "text": message})
            var -= 1
        status = self.session.queueBatch(batch)
        self.failIf(status != 200 and status != 202)

    def test_GetProcessedDocuments(self):
        status = self.session.getProcessedDocuments()
        self.failIf(status is None)

    def test_CreateCollection(self):
        status = self.session.queueCollection({"id": col_id, "documents": [message, message]})
        self.failIf(status != 200 and status != 202)

    def test_GetCollection(self):
        status = self.session.getCollection(col_id)
        self.failIf(status is None)

    def test_GetProcessedCollections(self):
        status = self.session.getProcessedCollections()
        self.failIf(status is None)

    def test_Status(self):
        status = self.session.getStatus()
        self.failIf(status is None)

    def test_VerifySubscription(self):
        status = self.session.getSubscription()
        self.failIf(status is None)

    def test_CRUDConfigurations(self):
        status = self.session.getConfigurations()
        self.failIf(status is None)

        test_configuration = {
            'auto_response': False,
            'is_primary': False,
            'name': 'TEST_CONFIG_PYTHON3',
            'language': 'English',
            'document': {
                'query_topics': True,
                'concept_topics': True,
                'named_entities': True,
                'user_entities': True
            }
        }

        response = self.session.addConfigurations([test_configuration])
        self.failIf(len(response) == 0)

        obj = None
        for item in response:
            if item["name"] == "TEST_CONFIG_PYTHON3":
                obj = item
                break
        self.failIf(obj is None)
        self.failIf(obj['alphanumeric_threshold'] != 80)

        obj['alphanumeric_threshold'] = 20
        config_id = obj['id']
        del obj['modified']

        response = self.session.updateConfigurations([obj])
        self.failIf(len(response) == 0)
        obj = None
        for item in response:
            if item["id"] == config_id:
                obj = item
                break
        self.failIf(obj is None)
        self.failIf(obj['alphanumeric_threshold'] != 20)

        response = self.session.removeConfigurations([obj['id']])
        self.failIf(response != 202)

    def test_CloneConfiguration(self):
        test_configuration = {
            'name': 'TEST_CONFIG_TO_CLONE',
            'language': 'English',
            'alphanumeric_threshold': 17
        }

        response = self.session.addConfigurations([test_configuration])
        self.failIf(len(response) != 1)

        config_to_clone = response[0]
        response = self.session.cloneConfiguration('TEST CLONED CONFIG', config_to_clone['id'])
        self.failIf(len(response) != 1)

        cloned_config = response[0]
        self.failIf(cloned_config['name'] != 'TEST CLONED CONFIG')
        self.failIf(cloned_config['alphanumeric_threshold'] != 17)

        response = self.session.removeConfigurations(
            [cloned_config['id'], config_to_clone['id']])
        self.failIf(response != 202)


if __name__ == '__main__':
    # unittest.main()

    suite = unittest.TestSuite()
    suite.addTest(SemantriaSessionTest('test_Status'))
    suite.addTest(SemantriaSessionTest('test_VerifySubscription'))
    suite.addTest(SemantriaSessionTest('test_CRUDConfigurations'))
    suite.addTest(SemantriaSessionTest('test_CloneConfiguration'))
    suite.addTest(SemantriaSessionTest('test_CRUDBlacklist'))
    suite.addTest(SemantriaSessionTest('test_CRUDCategories'))
    suite.addTest(SemantriaSessionTest('test_CRUDQueries'))
    suite.addTest(SemantriaSessionTest('test_CRUDPhrases'))
    suite.addTest(SemantriaSessionTest('test_CRUDEntities'))

    suite.addTest(SemantriaSessionTest('test_CreateDocument'))
    suite.addTest(SemantriaSessionTest('test_GetDocument'))
    suite.addTest(SemantriaSessionTest('test_CreateBatchDocuments'))
    suite.addTest(SemantriaSessionTest('test_GetProcessedDocuments'))

    suite.addTest(SemantriaSessionTest('test_CreateCollection'))
    suite.addTest(SemantriaSessionTest('test_GetCollection'))
    suite.addTest(SemantriaSessionTest('test_GetProcessedCollections'))

    unittest.TextTestRunner(verbosity=2).run(suite)
