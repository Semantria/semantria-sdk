# -*- coding: UTF-8 -*-
import unittest
import uuid
import thread

import semantria

# the consumer key and secret
consumerKey = ""
consumerSecret = ""

id = str(uuid.uuid1()).replace("-", "")
message = "Amazon Web Services has announced a new feature called VMWare Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud."

def onRequest(sender, result):
    print "\n", "REQUEST: ", result

def onResponse(sender, result):
    print "\n", "RESPONSE: ", result

def onError(sender, result):
    print "\n", "ERROR: ", result

def onDocsAutoResponse(sender, result):
    print "\n", "AUTORESPONSE: ", len(result), result

def onCollsAutoResponse(sender, result):
    print "\n", "AUTORESPONSE: ", len(result), result

class SemantriaSessionTest(unittest.TestCase):
    def setUp(self):
        self.serializer = semantria.XmlSerializer()
        self.session = semantria.Session(consumerKey, consumerSecret, self.serializer)

        self.session.Request += onRequest
        self.session.Response += onResponse
        self.session.Error += onError
        self.session.DocsAutoResponse += onDocsAutoResponse
        self.session.CollsAutoResponse += onCollsAutoResponse

    def test_AddBlacklist(self):
        proxy = self.session.createUpdateProxy()
        proxy["added"].append("27123")
        status = self.session.updateBlacklist(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)
		
    def test_GetBlacklist(self):
        status = self.session.getBlacklist()
        self.failIf(status == None)
        print "receivid: ", status[len(status)-1]

    def test_RemoveBlacklist(self):
        proxy = self.session.createUpdateProxy()
        proxy["removed"].append("http*")
        status = self.session.updateBlacklist(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)

    def test_GetCategories(self):
        status = self.session.getCategories()
        self.failIf(status == None)
 
    def test_AddCategories(self):
        proxy = self.session.createUpdateProxy()
        proxy["added"].append({"name":"Service", "samples":["Amazon", "VMWare"], "weight":0.1})
        proxy["added"].append({"name":"Web", "samples":["Google", "Yahoo"], "weight":0.2})
        status = self.session.updateCategories(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)
 
    def test_RemoveCategories(self):
        proxy = self.session.createUpdateProxy()
        proxy["removed"].append("Web")
        status = self.session.updateCategories(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)

    def test_GetQueries(self):
        status = self.session.getQueries()
        self.failIf(status == None)

    def test_AddQueries(self):
        proxy = self.session.createUpdateProxy()
        proxy["added"].append({"name":"Web", "query":"Amazon"})
        status = self.session.updateQueries(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)

    def test_RemoveQueries(self):
        proxy = self.session.createUpdateProxy()
        proxy["removed"].append("any")
        status = self.session.updateQueries(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)

    def test_GetSentimentPhrases(self):
        status = self.session.getSentimentPhrases()
        self.failIf(status == None)

    def test_AddSentimentPhrases(self):
        proxy = self.session.createUpdateProxy()
        proxy["added"].append({"title":"Web", "weight":0.1})
        proxy["added"].append({"title":"any", "weight":0.2})
        status = self.session.updateSentimentPhrases(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)

    def test_RemoveSentimentPhrases(self):
        proxy = self.session.createUpdateProxy()
        proxy["removed"].append("any")
        status = self.session.updateSentimentPhrases(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)	

    def test_GetEntities(self):
        status = self.session.getEntities()
        self.failIf(status == None)

    def test_AddEntities(self):
        proxy = self.session.createUpdateProxy()
        proxy["added"].append({"name":"Amazon", "type":"Web"})
        proxy["added"].append({"name":"any", "type":"any"})
        status = self.session.updateEntities(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)

    def test_RemoveEntities(self):
        proxy = self.session.createUpdateProxy()
        proxy["removed"].append("any")
        status = self.session.updateEntities(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)

    def test_CreateDocument(self):
        print "\n", "*** Create document: ", id, "\n"
        status = self.session.queueDocument({"id":id, "text":message})
        print "RESULT:", status, "\n"
        self.failIf(status != 200 and status != 202)

    def test_GetDocument(self):
        print "\n", "*** Get document: ", id, "\n"
        status = self.session.getDocument(id)
        print "RESULT:", status, "\n"
        self.failIf(status == None)

    def test_CancelDocument(self):
        print "\n", "*** Cancel document: ", id, "\n"
        status = self.session.cancelDocument(id)
        print "RESULT:", status, "\n"
        self.failIf(status != 200 and status != 202)

    def test_CreateBatchDocuments(self):
        batch = []
        var = 10
        while var > 0:
           batch.append({"id":str(uuid.uuid1()).replace("-", ""), "text":message})             
           var = var -1
        status = self.session.queueBatchOfDocuments(batch)
        print "RESULT:", status, "\n"
        self.failIf(status != 200 and status != 202)

    def test_GetProcessedDocuments(self):
        status = self.session.getProcessedDocuments()
        print "RESULT:", len(status), status, "\n"
        self.failIf(status == None)

    def test_CreateCollection(self):
        print "\n", "*** Create collection: ", id, "\n"
        status = self.session.queueCollection({"id":id, "documents":[message, message]})
        print "RESULT:", status, "\n"
        self.failIf(status != 200 and status != 202)

    def test_GetCollection(self):
        print "\n", "*** Get collection: ", id, "\n"
        status = self.session.getCollection(id)
        print "RESULT:", status, "\n"
        self.failIf(status == None)

    def test_CancelCollection(self):
        print "\n", "*** Cancel collection: ", id, "\n"
        status = self.session.cancelCollection(id)
        print "RESULT:", status, "\n"
        self.failIf(status != 200 and status != 202)

    def test_GetProcessedCollections(self):
        status = self.session.getProcessedCollections()
        print "RESULT:", len(status), status, "\n"
        self.failIf(status == None)

    def test_Status(self):
        status = self.session.getStatus()
        print "RESULT:", status, "\n"
        self.failIf(status == None)

    def test_VerifySubscription(self):
        status = self.session.verifySubscription()
        print "RESULT:", status, "\n"
        self.failIf(status == None)

    def test_GetConfigurations(self):
        status = self.session.getConfigurations()
        print "RESULT:", status, "\n"
        self.failIf(status == None)

    def test_UpdateConfiguration(self):
        status = self.session.getConfigurations()
        print "RESULT:", status
        self.failIf(status == None)

        default = None
        for item in status:
            if item["name"] == "default":
                item["auto_responding"] = False
                default = item

        self.failIf(default == None)
        proxy = self.session.createUpdateProxy()
        proxy["added"].append(default)
        status = self.session.updateConfigurations(proxy)
        print "RESULT:", status, "\n"
        self.failIf(status != 202)

"""	
    def test_Multithread(self):
        x = 100
        while x > 0:
           thread.start_new_thread(multiThread, ("Thread_%s" % x, ))
           x = x -1

        while 1:
           pass
        
# Function for the thread
def multiThread(name):
    print "Thread:", name, "started ...", "\n"
    serializer = semantria.JsonSerializer()
    session = semantria.Session(consumerKey, consumerSecret, serializer)
    id = str(uuid.uuid1()).replace("-", "")
    status = session.queueDocument({"id":id, "text":message})
    print "Thread:", name, status, "\n"
"""

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
    suite.addTest(SemantriaSessionTest('test_CancelDocument'))
    suite.addTest(SemantriaSessionTest('test_CreateBatchDocuments'))
    suite.addTest(SemantriaSessionTest('test_GetProcessedDocuments'))
    suite.addTest(SemantriaSessionTest('test_CreateCollection'))
    suite.addTest(SemantriaSessionTest('test_GetCollection'))
    suite.addTest(SemantriaSessionTest('test_CancelCollection'))
    suite.addTest(SemantriaSessionTest('test_GetProcessedCollections'))
    #suite.addTest(SemantriaSessionTest('test_Multithread'))
    
    unittest.TextTestRunner(verbosity=2).run(suite)
