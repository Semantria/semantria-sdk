#!/usr/bin/env ruby
# encoding: ISO-8859-1
$LOAD_PATH << File.dirname(__FILE__) unless $LOAD_PATH.include?(File.dirname(__FILE__)) 

require "test/unit"

require 'semantria/session'
require 'semantria/jsonserializer'
require 'semantria/xmlserializer'

# the consumer key and secret
$consumerKey = ""
$consumerSecret = ""

$id = rand(10 ** 10).to_s.rjust(10,'0') 
$message = "Amazon Web Services has announced a new feature called VM₤Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud."

class SessionCallbackHandler < CallbackHandler
  def onRequest(sender, args)
    print "Request: ", args, "\n"
  end
  
  def onResponse(sender, args)
    print "Response: ", args, "\n"
  end
    
  def onError(sender, args)
    print "Error: ", args, "\n"
  end
  
  def onDocsAutoResponse(sender, args)
    print "DocsAutoResponse: ", args.length, args, "\n"
  end
    
  def onCollsAutoResponse(sender, args)
    print "CollsAutoResponse: ", args.length, args, "\n"
  end
end

#Semantria API tests
class SemantriaSessionTest < Test::Unit::TestCase
  def setup
    @serializer = JsonSerializer.new()
    @session = Session.new($consumerKey, $consumerSecret, @serializer)
    @callback = SessionCallbackHandler.new()
    @session.setCallbackHandler(@callback)
  end

  def test_A_Status
    print "*** Get status ", "\n"
    status = @session.getStatus()
    print "RESULT:", status, "\n"
    assert(status != nil)
  end
 
  def test_A_VerifySubscription
    print "*** Verify subscription: ", "\n"
    status = @session.verifySubscription()
    print "RESULT:", status, "\n"
    assert(status != nil)
  end

  def test_B_GetConfigurations
    print "*** Get configuration: ", "\n"
    status = @session.getConfigurations()
    print "RESULT:", status, "\n"
    assert(status != nil)
  end
  
  def test_B_UpdateConfiguration
    print "*** Update configuration: ", "\n"
    status = @session.getConfigurations()
    print "RESULT:", status, "\n"
    assert(status != nil)
    
    default = nil
    status.each do |item|
      if (item["name"] == "default")
        item["auto_responding"] = false
        default = item
      end
    end
     
    assert(default != nil)
    proxy = @session.createUpdateProxy()
    proxy["added"].push(default)
    status = @session.updateConfigurations(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end

  def test_C_AddBlacklist
    print "*** Add blacklist: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["added"].push("50123")
    status = @session.updateBlacklist(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end
  
  def test_C_GetBlacklist
    print "*** Get blacklist: ", "\n"
    status = @session.getBlacklist()
    assert(status != nil)
	print "receivied: ", status[status.count - 1]
  end

  def test_C_RemoveBlacklist
    print "*** Remove blacklist: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["removed"].push("http*")
    status = @session.updateBlacklist(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end

  def test_D_GetCategories
    print "*** Get categories: ", "\n"
    status = @session.getCategories()
    assert(status != nil)
  end
 
  def test_D_AddCategories
    print "*** Add categories: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["added"].push({"name"=>"Service", "samples"=>["Amazon", "VMWare"]})
    proxy["added"].push({"name"=>"Web", "samples"=>["Google", "Yahoo"]})
    status = @session.updateCategories(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end
 
  def test_D_RemoveCategories
    print "*** Remove categories: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["removed"].push("Web")
    status = @session.updateCategories(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end
  
  def test_E_GetQueries
    print "*** Get queries: ", "\n"
    status = @session.getQueries()
    assert(status != nil)
  end
  
  def test_E_AddQueries
    print "*** Add queries: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["added"].push({"name"=>"Web", "query"=>"Amazon"})
    proxy["added"].push({"name"=>"any", "query"=>"any"})
    status = @session.updateQueries(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end

  def test_E_RemoveQueries
    print "*** Remove queries: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["removed"].push("any")
    status = @session.updateQueries(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end

  
  def test_F_GetEntities
    print "*** Get entities: ", "\n"
    status = @session.getEntities()
    assert(status != nil)
  end
  
  def test_F_AddEntities
    print "*** Add entities: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["added"].push({"name"=>"Amazon", "type"=>"Web"})
    proxy["added"].push({"name"=>"any", "type"=>"any"})
    status = @session.updateEntities(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end

  def test_F_RemoveEntities
    print "*** Remove entities: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["removed"].push("any")
    status = @session.updateEntities(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end
 
  def test_G_CreateDocument
    print "\n", "*** Create document: ", $id, "\n"
    status = @session.queueDocument({"id"=>$id, "text"=>$message})
    print "RESULT:", status, "\n"
    assert(status = 200 || status == 202)
  end
 
  def test_G_GetDocument
    print "\n", "*** Get document: ", $id, "\n"
    status = @session.getDocument($id)
    print "RESULT:", status, "\n"
    assert(status != nil)
  end

  def test_H_CancelDocument
    print "\n", "*** Cancel document: ", $id, "\n"
    status = @session.cancelDocument($id)
    print "RESULT:", status, "\n"
    assert(status == 200 || status == 202)
  end
  
  def test_I_CreateBatchDocuments
    print "*** Create batch: ", "\n"
    batch = []
    var = 10
    while (var > 0)
      id = rand((var+10) ** 10).to_s.rjust(10,'0')
      batch.push({"id"=>id, "text"=>$message})             
      var -= 1
    end        
    status = @session.queueBatchOfDocuments(batch)
    print "RESULT:", status, "\n"
    assert(status == 200 || status == 202)
  end

  def test_I_GetProcessedDocuments
    print "*** Get processed documents: ", "\n"
    status = @session.getProcessedDocuments()
    print "RESULT:", status.length, status, "\n"
    assert(status != nil)
  end
    
  def test_J_CreateCollection
    print "\n", "*** Create collection: ", $id, "\n"
    status = @session.queueCollection({"id"=>$id, "documents"=>[$message, $message]})
    print "RESULT:", status, "\n"
    assert(status = 200 || status == 202)
  end
  
  def test_J_GetCollection
    print "\n", "*** Get collection: ", $id, "\n"
    status = @session.getCollection($id)
    print "RESULT:", status, "\n"
    assert(status != nil)
  end
  
  def test_K_CancelCollection
    print "\n", "*** Cancel collection: ", $id, "\n"
    status = @session.cancelCollection($id)
    print "RESULT:", status, "\n"
    assert(status == 200 || status == 202)
  end
    
  def test_L_GetProcessedCollections
    print "*** Get processed collections: ", "\n"
    status = @session.getProcessedCollections()
    print "RESULT:", status.length, status, "\n"
    assert(status != nil)
  end

  def test_M_GetSentimentPhrases
    print "*** Get sentiment phrases: ", "\n"
    status = @session.getSentimentPhrases()
    assert(status != nil)
  end
  
  def test_N_AddSentimentPhrases
    print "*** Add sentiment phrases: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["added"].push({"title"=>"Web", "weight"=>0.1})
    proxy["added"].push({"title"=>"any", "weight"=>0.4})
    status = @session.updateSentimentPhrases(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end

  def test_O_RemoveSentimentPhrases
    print "*** Remove sentiment phrases: ", "\n"
    proxy = @session.createUpdateProxy()
    proxy["removed"].push("any")
    status = @session.updateSentimentPhrases(proxy)
    print "RESULT:", status, "\n"
    assert(status == 202)
  end

end
