# -*- coding: utf-8 -*-

from __future__ import print_function
import io, os, sys, unittest, uuid, tarfile

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
    sys.stdout.flush()
    sys.stderr.flush()

def onDocsAutoResponse(sender, result):
    # print("\n", "AUTORESPONSE: ", len(result), result)
    pass

def onCollsAutoResponse(sender, result):
    # print("\n", "AUTORESPONSE: ", len(result), result)
    pass


def isSuccess(code):
    return (code >= 200) and (code < 300)


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
        self.assertIsNotNone(response, msg="can't get blacklist")

        response = self.session.addBlacklist({"name": "python3*"})
        self.assertNotEqual(len(response), 0, msg="can't add to blacklist")

        blacklist = None
        for item in response:
            if item['name'] == 'python3*':
                blacklist = item
                break
        self.assertIsNotNone(blacklist)

        status = self.session.removeBlacklist([blacklist['id']])
        self.assertTrue(isSuccess(status))

    def test_CRUDCategories(self):
        response = self.session.getCategories()
        self.assertIsNotNone(response)
        response = self.session.addCategories([{
            'name': "TEST_CATEGORY_PYTHON3",
            'samples': ["Amazon", "VMWare"],
            'weight': 0.1,
        }])
        self.assertNotEqual(len(response), 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_CATEGORY_PYTHON3':
                obj = item
                break
        self.assertIsNotNone(obj)

        response = self.session.removeCategories([obj['id']])
        self.assertTrue(isSuccess(response))

    def test_CRUDQueries(self):
        response = self.session.getQueries()
        self.assertIsNotNone(response)

        response = self.session.addQueries([{"name": "TEST_QUERY_PYTHON3", "query": "Amazon"}])
        self.assertNotEqual(len(response), 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_QUERY_PYTHON3':
                obj = item
                break
        self.assertIsNotNone(obj)

        response = self.session.removeQueries([obj['id']])
        self.assertTrue(isSuccess(response))

    def test_CRUDPhrases(self):
        response = self.session.getPhrases()
        self.assertIsNotNone(response)

        response = self.session.addPhrases([
            {"name": "TEST_PHRASE_PYTHON3", "weight": 0.1},
        ])
        self.assertNotEqual(len(response), 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_PHRASE_PYTHON3':
                obj = item
                break
        self.assertIsNotNone(obj)

        response = self.session.removePhrases([obj['id']])
        self.assertTrue(isSuccess(response))

    def test_CRUDEntities(self):
        response = self.session.getEntities()
        self.assertIsNotNone(response)

        response = self.session.addEntities([
            {"name": "TEST_ENTITY_PYTHON3", "type": "Web"},
        ])
        self.assertNotEqual(len(response), 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_ENTITY_PYTHON3':
                obj = item
                break
        self.assertIsNotNone(obj)

        response = self.session.removeEntities([obj['id']])
        self.assertTrue(isSuccess(response))

    def test_CRUDTaxonomy(self):
        response = self.session.getTaxonomy()
        self.assertIsNotNone(response)

        response = self.session.addTaxonomy([
            {"name": "TEST_TAXONOMY_PYTHON3"},
        ])
        self.assertNotEqual(len(response), 0)

        obj = None
        for item in response:
            if item['name'] == 'TEST_TAXONOMY_PYTHON3':
                obj = item
                break
        self.assertIsNotNone(obj)

        response = self.session.removeTaxonomy([obj['id']])
        self.assertTrue(isSuccess(response))

    def test_CreateDocument(self):
        status = self.session.queueDocument({"id": doc_id, "text": message})
        self.assertTrue(isSuccess(status))

    def test_GetDocument(self):
        status = self.session.getDocument(doc_id)
        self.assertIsNotNone(status)

    def test_CreateBatchDocuments(self):
        batch = []
        var = 10
        while var > 0:
            batch.append({"id": str(uuid.uuid1()).replace("-", ""), "text": message})
            var -= 1
        status = self.session.queueBatch(batch)
        self.assertTrue(isSuccess(status))

    def test_GetProcessedDocuments(self):
        status = self.session.getProcessedDocuments()
        self.assertIsNotNone(status)

    def test_CreateCollection(self):
        status = self.session.queueCollection({"id": col_id, "documents": [message, message]})
        self.assertTrue(isSuccess(status))

    def test_GetCollection(self):
        status = self.session.getCollection(col_id)
        self.assertIsNotNone(status)

    def test_GetProcessedCollections(self):
        status = self.session.getProcessedCollections()
        self.assertIsNotNone(status)

    def test_Status(self):
        status = self.session.getStatus()
        self.assertIsNotNone(status)

    def test_VerifySubscription(self):
        status = self.session.getSubscription()
        self.assertIsNotNone(status)

    def test_CRUDConfigurations(self):
        status = self.session.getConfigurations()
        self.assertIsNotNone(status)

        test_configuration = {
            'auto_response': False,
            'is_primary': False,
            'name': 'TEST_CONFIG_PYTHON3',
            'language': 'English',
            'alphanumeric_threshold': 80,
            'document': {
                'query_topics': True,
                'concept_topics': True,
                'named_entities': True,
                'user_entities': True
            }
        }

        response = self.session.addConfigurations([test_configuration])
        self.assertNotEqual(len(response), 0)

        test_config = None
        for item in response:
            if item["name"] == "TEST_CONFIG_PYTHON3":
                test_config = item
                break
        self.assertIsNotNone(test_config)
        self.assertEqual(test_config['alphanumeric_threshold'], 80)

        config_id = test_config['id']
        new_config = {'id': config_id,
                      'alphanumeric_threshold': 20}

        response = self.session.updateConfigurations([new_config])
        self.assertNotEqual(len(response), 0)
        test_config = None
        for item in response:
            if item["id"] == config_id:
                test_config = item
                break
        self.assertIsNotNone(test_config)
        self.assertEqual(test_config['alphanumeric_threshold'], 20)

        response = self.session.removeConfigurations([config_id])
        self.assertTrue(isSuccess(response))

    def test_CloneConfiguration(self):
        test_configuration = {
            'name': 'TEST_CONFIG_TO_CLONE',
            'language': 'English',
            'alphanumeric_threshold': 17
        }

        response = self.session.addConfigurations([test_configuration])
        self.assertEqual(len(response), 1)

        config_to_clone = response[0]
        response = self.session.cloneConfiguration('TEST CLONED CONFIG', config_to_clone['id'])
        self.assertEqual(len(response), 1)

        cloned_config = response[0]
        self.assertEqual(cloned_config['name'], 'TEST CLONED CONFIG')
        self.assertEqual(cloned_config['alphanumeric_threshold'], 17)

        response = self.session.removeConfigurations(
            [cloned_config['id'], config_to_clone['id']])
        self.assertTrue(isSuccess(response))

    # Creates a config, adds some queries, then checks that the query
    # file exists in the tar archive. (zip is the default format, so
    # checking a different format.)
    def test_GetUserDirectory(self):
        test_configuration = {
            'name': 'TEST_CONFIG_FOR_ARCHIVE',
            'language': 'English'
        }
        queries = [
            {"name": "query1", "query": "dog"},
            {"name": "query2", "query": "dog OR cat"}
        ]

        response = self.session.addConfigurations([test_configuration])
        self.assertEqual(len(response), 1)
        config_id = response[0]['id']

        response = self.session.addQueries(queries, config_id=config_id)
        self.assertNotEqual(len(response), 0)

        tar_bytes = self.session.getUserDirectory(config_id=config_id, format='tar')
        tar = tarfile.TarFile(fileobj=io.BytesIO(tar_bytes),
                              mode='r', format=tarfile.PAX_FORMAT,
                              encoding='utf-8')
        filenames = tar.getnames()
        tar.close()

        # Assume that if the file is there then its content is correct
        self.assertTrue('imports/query-topics.dat' in filenames)
        self.assertTrue('manifest.txt' in filenames)

        response = self.session.removeConfigurations([config_id])
        self.assertTrue(isSuccess(response))


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

    suite.addTest(SemantriaSessionTest('test_GetUserDirectory'))

    unittest.TextTestRunner(verbosity=2).run(suite)
