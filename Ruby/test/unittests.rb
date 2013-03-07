#!/usr/bin/env ruby
# encoding: utf-8

require 'test/unit'
require File.expand_path('lib/semantria')

# the consumer key and secret
CONSUMER_KEY = ''
CONSUMER_SECRET = ''

$id = rand(10 ** 10).to_s.rjust(10, '0')
$message = 'Amazon Web Services has announced a new feature called VM₤Ware Import, which
  allows IT departments to move virtual machine images from their internal data centers to the cloud.'

class SessionCallbackHandler < CallbackHandler
  def onRequest(sender, args)
    print 'Request: ', args, "\n"
  end

  def onResponse(sender, args)
    print 'Response: ', args, "\n"
  end

  def onError(sender, args)
    print 'Error: ', args, "\n"
  end

  def onDocsAutoResponse(sender, args)
    print 'DocsAutoResponse: ', args.length, args, "\n"
  end

  def onCollsAutoResponse(sender, args)
    print 'CollsAutoResponse: ', args.length, args, "\n"
  end
end

#Semantria API tests
class SemantriaSessionTest < Test::Unit::TestCase
  def setup
    @session = Session.new(CONSUMER_KEY, CONSUMER_SECRET, nil, true)
    @callback = SessionCallbackHandler.new()
    @session.setCallbackHandler(@callback)
  end

  def test_a_status
    print '*** Get status ', "\n"
    status = @session.getStatus()
    print 'RESULT:', status, "\n"
    assert(status != nil)
  end

  def test_a_get_subscription
    print '*** Get subscription: ', "\n"
    status = @session.getSubscription
    print 'RESULT:', status, "\n"
    assert(status != nil)
  end

  def test_b_get_configurations
    print '*** Get configuration: ', "\n"
    status = @session.getConfigurations()
    print 'RESULT:', status, "\n"
    assert(status.is_a? Array)
  end

  def test_b_update_configuration
    print '*** Update configuration: ', "\n"
    status = @session.getConfigurations()
    print 'RESULT:', status, "\n"
    assert(status.is_a? Array)

    default = nil
    status.each do |item|
      if item['name'] == 'default'
        item['auto_response'] = false
        default = item
      end
    end

    assert(default != nil)
    status = @session.updateConfigurations([default])
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_c_add_blacklist
    print '*** Add blacklist: ', "\n"
    status = @session.addBlacklist(['some item'])
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_c_get_blacklist
    print '*** Get blacklist: ', "\n"
    status = @session.getBlacklist()
    assert(status.is_a? Array)
  end

  def test_c_remove_blacklist
    print '*** Remove blacklist: ', "\n"
    status = @session.removeBlacklist(['some item'])
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_d_get_categories
    print '*** Get categories: ', "\n"
    status = @session.getCategories()
    assert(status.is_a? Array)
  end

  def test_d_add_categories
    print '*** Add categories: ', "\n"
    data = [
        {'name' => 'Service', 'samples' => %w(Amazon VMWare)},
        {'name' => 'Web', 'samples' => %w(Google Yahoo)}
    ]
    status = @session.updateCategories(data)
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_d_remove_categories
    print '*** Remove categories: ', "\n"
    status = @session.removeCategories(%w(Web))
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_e_get_queries
    print '*** Get queries: ', "\n"
    status = @session.getQueries()
    assert(status.is_a? Array)
  end

  def test_e_add_queries
    print '*** Add queries: ', "\n"
    data = [
        {'name' => 'Web', 'query' => 'Amazon'},
        {'name' => 'Test', 'query' => 'test'}
    ]
    status = @session.addQueries(data)
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_e_remove_queries
    print '*** Remove queries: ', "\n"
    status = @session.removeQueries(%w(any))
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end


  def test_f_get_entities
    print '*** Get entities: ', "\n"
    status = @session.getEntities()
    assert(status.is_a? Array)
  end

  def test_f_add_entities
    print '*** Add entities: ', "\n"
    data = [
        {'name' => 'Amazon', 'type' => 'Web'},
        {'name' => 'any', 'type' => 'any'}
    ]
    status = @session.addEntities(data)
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_f_remove_entities
    print '*** Remove entities: ', "\n"
    status = @session.removeEntities(%w(any))
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_g_create_document
    print "\n", '*** Create document: ', $id, "\n"
    status = @session.queueDocument({'id' => $id, 'text' => $message})
    print 'RESULT:', status, "\n"
    assert(status = 200 || status == 202)
  end

  def test_g_get_document
    print "\n", '*** Get document: ', $id, "\n"
    status = @session.getDocument($id)
    print 'RESULT:', status, "\n"
    assert(status != nil)
  end

  def test_i_create_batch
    print '*** Create batch: ', "\n"
    batch = []
    var = Array.new(10) { |index| index }
    var.each do |item|
      id = rand((item+10) ** 10).to_s.rjust(10, '0')
      batch.push({'id' => id, 'text' => $message})
    end
    status = @session.queueBatch(batch)
    print 'RESULT:', status, "\n"
    assert(status == 200 || status == 202)
  end

  def test_i_get_processed_documents
    print '*** Get processed documents: ', "\n"
    status = @session.getProcessedDocuments()
    print 'RESULT:', status.length, status, "\n"
    assert(status != nil)
  end

  def test_j_create_collection
    print "\n", '*** Create collection: ', $id, "\n"
    status = @session.queueCollection({'id' => $id, 'documents' => [$message, $message]})
    print 'RESULT:', status, "\n"
    assert(status = 200 || status == 202)
  end

  def test_j_get_collection
    print "\n", '*** Get collection: ', $id, "\n"
    status = @session.getCollection($id)
    print 'RESULT:', status, "\n"
    assert(status != nil)
  end

  def test_l_get_processed_coll
    print '*** Get processed collections: ', "\n"
    status = @session.getProcessedCollections()
    print 'RESULT:', status.length, status, "\n"
    assert(status != nil)
  end

  def test_m_get_phrases
    print '*** Get sentiment phrases: ', "\n"
    status = @session.getPhrases()
    assert(status != nil)
  end

  def test_n_add_phrases
    print '*** Add sentiment phrases: ', "\n"
    data = [
        {'name' => 'Web', 'weight' => 0.1},
        {'name' => 'any', 'weight' => 0.4}
    ]
    status = @session.addPhrases(data)
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

  def test_o_remove_phrases
    print '*** Remove sentiment phrases: ', "\n"
    status = @session.removePhrases(%w(any))
    print 'RESULT:', status, "\n"
    assert(status == 202)
  end

end
