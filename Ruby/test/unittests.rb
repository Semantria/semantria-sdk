#!/usr/bin/env ruby
# encoding: utf-8

require 'minitest/autorun'
require File.expand_path('lib/semantria')

# the consumer key and secret
CONSUMER_KEY = ''
CONSUMER_SECRET = ''

$id = rand(10 ** 10).to_s.rjust(10, '0')
$message = 'Amazon Web Services has announced a new feature called VM₤Ware Import, which
  allows IT departments to move virtual machine images from their internal data centers to the cloud.'

class SessionCallbackHandler < Semantria::CallbackHandler
  def onRequest(sender, args)
    # print 'Request: ', args, "\n"
  end

  def onResponse(sender, args)
    # print 'Response: ', args, "\n"
  end

  def onError(sender, args)
    print 'Error: ', args, "\n"
  end

  def onDocsAutoResponse(sender, args)
    # print 'DocsAutoResponse: ', args.length, args, "\n"
  end

  def onCollsAutoResponse(sender, args)
    # print 'CollsAutoResponse: ', args.length, args, "\n"
  end
end

#Semantria API tests
class SemantriaSessionTest < Minitest::Test
  def setup
    @session = Semantria::Session.new(CONSUMER_KEY, CONSUMER_SECRET, nil, true)
    @callback = SessionCallbackHandler.new()
    @session.setCallbackHandler(@callback)
  end

  def test_a_status
    status = @session.getStatus()
    assert(status != nil)
  end

  def test_supported_features
    status = @session.getSupportedFeatures("English")
    assert(status != nil)
  end

  def test_a_get_subscription
    status = @session.getSubscription
    assert(status != nil)
  end

  def test_b_crud_configurations
    response = @session.getConfigurations()
    assert(response.is_a? Array)

    test_configuration = {
      'auto_response' => false,
      'is_primary' => false,
      'name' => 'TEST_CONFIG_RUBY',
      'language' => 'English',
      'document' => {
        'query_topics_limit' => 5,
        'concept_topics_limit' => 5,
        'named_entities_limit' => 5,
        'user_entities_limit' => 5
      }
    }

    response = @session.addConfigurations([test_configuration])
    assert(response.is_a? Array)

    config = nil
    response.each do |item|
      if item[:name] == 'TEST_CONFIG_RUBY'
        config = item
        break
      end
    end
    assert(config != nil)
    assert(config[:chars_threshold] == 80)

    config_id = config[:id]
    config[:chars_threshold] = 20

    response = @session.updateConfigurations([config])
    assert(response.is_a? Array)

    config = nil
    response.each do |item|
      if item[:id] == config_id
        config = item
        break
      end
    end
    assert(config != nil)
    assert(config[:chars_threshold] == 20)

    response = @session.removeConfigurations([config[:id]])
    assert(response == 202)
  end

  def test_c_crud_blacklist
    response = @session.getBlacklist()
    assert(response.is_a? Array)

    response = @session.addBlacklist([{ name: 'ruby*' }])
    assert(response == 202)

    object = nil
    response.each do |item|
      if item[:name] == 'ruby*'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeBlacklist([object[:id]])
    assert(response == 202)
  end

  def test_d_crud_categories
    response = @session.getCategories()
    assert(response.is_a? Array)

    data = [
        {'name' => 'TEST_CATEGORY_RUBY', 'samples' => %w(Amazon VMWare)},
    ]
    response = @session.updateCategories(data)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item[:name] == 'TEST_CATEGORY_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeCategories([object[:id]])
    assert(response == 202)
  end

  def test_e_crud_queries
    response = @session.getQueries()
    assert(response.is_a? Array)

    data = [
        {'name' => 'TEST_QUERY_RUBY', 'query' => 'Amazon'},
    ]
    response = @session.addQueries(data)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item[:name] == 'TEST_QUERY_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeQueries(object[:id])
    assert(response == 202)
  end

  def test_f_crud_entities
    response = @session.getEntities()
    assert(response.is_a? Array)

    data = [
        {'name' => 'TEST_ENTITY_RUBY', 'type' => 'Web'},
    ]
    response = @session.addEntities(data)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item[:name] == 'TEST_ENTITY_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeEntities(object[:id])
    assert(response == 202)
  end

  def test_g_create_document
    status = @session.queueDocument({'id' => $id, 'text' => $message})
    assert(status = 200 || status == 202)
  end

  def test_g_get_document
    status = @session.getDocument($id)
    assert(status != nil)
  end

  def test_i_create_batch
    batch = []
    var = Array.new(10) { |index| index }
    var.each do |item|
      id = rand((item+10) ** 10).to_s.rjust(10, '0')
      batch.push({'id' => id, 'text' => $message})
    end
    status = @session.queueBatch(batch)
    assert(status == 200 || status == 202)
  end

  def test_i_get_processed_documents
    status = @session.getProcessedDocuments()
    assert(status != nil)
  end

  def test_j_create_collection
    status = @session.queueCollection({'id' => $id, 'documents' => [$message, $message]})
    assert(status = 200 || status == 202)
  end

  def test_j_get_collection
    status = @session.getCollection($id)
    assert(status != nil)
  end

  def test_l_get_processed_coll
    status = @session.getProcessedCollections()
    assert(status != nil)
  end

  def test_m_crud_phrases
    response = @session.getPhrases()
    assert(response != nil)

    data = [
        {'name' => 'TEST_PHRASE_RUBY', 'weight' => 0.1},
    ]
    response = @session.addPhrases(data)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item[:name] == 'TEST_PHRASE_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removePhrases(object[:id])
    assert(response == 202)
  end

  def test_n_crud_taxonomy
    response = @session.getTaxonomy();
    assert(response.is_a? Array)

    response = @session.addTaxonomy([
      {name: 'TEST_TAXONOMY_RUBY'},
    ]);
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item[:name] == 'TEST_TAXONOMY_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeTaxonomy(object[:id])
    assert(response == 202)
  end
end
