#!/usr/bin/env ruby
# encoding: utf-8

require 'minitest/autorun'
require 'pp'
require File.expand_path('lib/semantria')

# API Key/Secret
# Set the environment vars before calling this program
# or edit this file and put your key and secret here.
CONSUMER_KEY = ENV['SEMANTRIA_KEY']
CONSUMER_SECRET = ENV['SEMANTRIA_SECRET']

# If set will run username/password authentication test
SEMANTRIA_USERNAME = ENV['SEMANTRIA_USERNAME']
SEMANTRIA_PASSWORD = ENV['SEMANTRIA_PASSWORD']

if !defined?(CONSUMER_KEY) or !defined?(CONSUMER_SECRET)
  raise "Missing key/secret"
end


$DOC_ID = rand(10 ** 10).to_s.rjust(10, '0')
$COLLECTION_ID = rand(10 ** 10).to_s.rjust(10, '0')
$TEXT = 'Amazon Web Services has announced a new feature called VM₤Ware Import, which
  allows IT departments to move virtual machine images from their internal data centers to the cloud.'

$TEST_CONFIG_NAME = 'TEST_CONFIG_RUBY'

class SessionCallbackHandler < Semantria::CallbackHandler
  def onRequest(sender, args)
    #msg = args['message']
    #printf "Request: %s %s\n", args['method'], args['url']
    #if msg
    #  printf "    %s\n", msg[0..80]
    #end
  end

  def onResponse(sender, args)
    #msg = args['message']
    #printf "RESPONSE: %s - %s\n", args['status'], args['reason']
    #if msg
    #  printf "    %s\n", msg[0..80]
    #end
  end

  def onError(sender, args)
    print 'ERROR: ', args, "\n"
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
    @session = Semantria::Session.new(CONSUMER_KEY, CONSUMER_SECRET, use_compression: true)
    @callback = SessionCallbackHandler.new()
    @session.setCallbackHandler(@callback)
  end

  # tests are not required to be in order, but it's more convenient
  def self.test_order
    :alpha
  end

  def get_or_create_test_config
    configs = @session.getConfigurations()
    assert(configs.is_a? Array)

    configs.each do |item|
      if item['name'] == $TEST_CONFIG_NAME
        # printf "\nfound config: %s %s\n", item['id'], item['name'] # DDT
        return item['id']
      end
    end

    test_configuration = {
      'name' => $TEST_CONFIG_NAME,
      'language' => 'English',
      'document' => {
        'query_topics' => true,
        'concept_topics' => true,
        'named_entities' => true,
        'user_entities' => true
      }
    }
    response = @session.addConfigurations([test_configuration])
    assert(response.is_a? Array)
      # printf "\nCreated config: %s %s\n", response[0]['id'], response[0]['name']
    return response[0]['id']
  end

  def test_a_status
    status = @session.getStatus()
    assert(status != nil)
  end

  def test_a_supported_features
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
      'name' => $TEST_CONFIG_NAME,
      'language' => 'English',
      'alphanumeric_threshold' => 85,
      'document' => {
        'query_topics' => true,
        'concept_topics' => true,
        'named_entities' => true,
        'user_entities' => true
      }
    }

    response = @session.addConfigurations([test_configuration])
    assert(response.is_a? Array)

    config = nil
    response.each do |item|
      if item['name'] == $TEST_CONFIG_NAME
        config = item
        break
      end
    end
    assert(config != nil)
    assert(config['alphanumeric_threshold'] == 85)

    config_id = config['id']
    new_config = {
      'id' => config_id,
      'alphanumeric_threshold' => 20
    }

    response = @session.updateConfigurations([new_config])
    assert(response.is_a? Array)

    config = nil
    response.each do |item|
      if item['id'] == config_id
        config = item
        break
      end
    end
    assert(config != nil)
    assert(config['alphanumeric_threshold'] == 20)

    response = @session.removeConfigurations([config['id']])
    assert(response == 202)
  end

  def test_c_crud_blacklist
    config_id = get_or_create_test_config
    response = @session.getBlacklist(config_id)
    assert(response.is_a? Array)

    response = @session.addBlacklist([{ name: 'ruby*' }], config_id)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item['name'] == 'ruby*'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeBlacklist([object['id']], config_id)
    assert(response == 202)
  end

  def test_d_crud_categories
    config_id = get_or_create_test_config
    response = @session.getCategories(config_id)
    assert(response.is_a? Array)

    data = [{'name' => 'TEST_CATEGORY_RUBY', 'samples' => %w(Amazon VMWare)}]
    response = @session.addCategories(data, config_id)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item['name'] == 'TEST_CATEGORY_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeCategories([object['id']], config_id)
    assert(response == 202)
  end

  def test_e_crud_queries
    config_id = get_or_create_test_config
    response = @session.getQueries(config_id)
    assert(response.is_a? Array)

    data = [{'name' => 'TEST_QUERY_RUBY', 'query' => 'Amazon'}]
    response = @session.addQueries(data, config_id)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item['name'] == 'TEST_QUERY_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeQueries([object['id']], config_id)
    assert(response == 202)
  end

  def test_f_crud_entities
    config_id = get_or_create_test_config
    response = @session.getEntities(config_id)
    assert(response.is_a? Array)

    data = [
        {'name' => 'TEST_ENTITY_RUBY', 'type' => 'Web'},
    ]
    response = @session.addEntities(data, config_id)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item['name'] == 'TEST_ENTITY_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeEntities([object['id']], config_id)
    assert(response == 202)
  end

  def test_g_create_document
    config_id = get_or_create_test_config
    status = @session.queueDocument({'id' => $DOC_ID, 'text' => $TEXT}, config_id)
    assert(status = 200 || status == 202)
  end

  def test_g_get_document
    config_id = get_or_create_test_config
    status = @session.getDocument($DOC_ID, config_id)
    assert(status != nil)
  end

  def test_i_create_batch
    config_id = get_or_create_test_config
    batch = []
    var = Array.new(10) { |index| index }
    var.each do |item|
      id = rand((item+10) ** 10).to_s.rjust(10, '0')
      batch.push({'id' => id, 'text' => $TEXT})
    end
    status = @session.queueBatch(batch, config_id)
    assert(status == 200 || status == 202)
  end

  def test_i_get_processed_documents
    config_id = get_or_create_test_config
    status = @session.getProcessedDocuments(config_id)
    assert(status != nil)
  end

  def test_j_create_collection
    config_id = get_or_create_test_config
    status = @session.queueCollection({'id' => $COLLECTION_ID, 'documents' => [$TEXT, $TEXT]}, config_id)
    assert(status = 200 || status == 202)
  end

  def test_j_get_collection
    config_id = get_or_create_test_config
    status = @session.getCollection($COLLECTION_ID, config_id)
    assert(status != nil)
  end

  def test_l_get_processed_coll
    config_id = get_or_create_test_config
    status = @session.getProcessedCollections(config_id)
    assert(status != nil)
  end

  def test_m_crud_phrases
    config_id = get_or_create_test_config
    response = @session.getPhrases(config_id)
    assert(response != nil)

    data = [
        {'name' => 'TEST_PHRASE_RUBY', 'weight' => 0.1},
    ]
    response = @session.addPhrases(data, config_id)
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item['name'] == 'TEST_PHRASE_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removePhrases([object['id']], config_id)
    assert(response == 202)
  end

  def test_n_crud_taxonomy
    config_id = get_or_create_test_config
    response = @session.getTaxonomy(config_id);
    assert(response.is_a? Array)

    new_taxonomy = [{name: 'TEST_TAXONOMY_RUBY'}]
    response = @session.addTaxonomy(new_taxonomy, config_id);
    assert(response.is_a? Array)

    object = nil
    response.each do |item|
      if item['name'] == 'TEST_TAXONOMY_RUBY'
        object = item
        break
      end
    end
    assert(object != nil)

    response = @session.removeTaxonomy([object['id']], config_id)
    assert(response == 202)
  end
  
  def test_o_user_directory
    config_id = get_or_create_test_config

    query_query = 'dog AND cat'
    data = [{'name' => 'q1', 'query' => query_query}]
    response = @session.addQueries(data, config_id)
    assert(response.is_a? Array)
    query_id = response[0]['id']
    
    bytes = @session.getUserDirectory(config_id:config_id, format:'tar')
    # Check that bytes contains query.
    # (This works because tar is an uncompressed format and query contains only single byte chars.)
    assert(bytes.include?(query_query))
    
    response = @session.removeQueries([query_id], config_id)
    assert(response == 202)
  end

  def test_p_username_password_authentication
    if !SEMANTRIA_USERNAME or !SEMANTRIA_PASSWORD
      p 'username/password not set -- skipping test.'
      return
    end
    user_session = Semantria::Session.new(nil, nil,
                                          username: SEMANTRIA_USERNAME,
                                          password: SEMANTRIA_PASSWORD)
    user_session.setCallbackHandler(@callback)
    status = user_session.getStatus()
    assert(status != nil)
  end


end
