#!/usr/bin/env ruby
# encoding: utf-8
$LOAD_PATH << File.dirname(__FILE__) unless $LOAD_PATH.include?(File.dirname(__FILE__)) 

require "test/unit"

require 'semantria/session'
require 'semantria/jsonserializer'
require 'semantria/xmlserializer'

class SemantriaSerializerTest < Test::Unit::TestCase
  def setup
    @session = Session.new("123", "123", XmlSerializer.new())
  end

  def test_XmlSerializingConfiguration
        expectedResult = '<configurations>' +
                         '<added>' +
                            '<configuration>' +
                               '<name>A test configuration</name>' +
                               '<is_primary>true</is_primary>' +
							   '<one_sentence>true</one_sentence>' +
                               '<auto_responding>true</auto_responding>' +
                               '<language>English</language>' +
                               '<chars_threshold>80</chars_threshold>' +
                               '<callback>https://anyapi.anydomain.com/processed/docs.json</callback>' +
                               '<document>' +
                                  '<concept_topics_limit>5</concept_topics_limit>' +
                                  '<query_topics_limit>5</query_topics_limit>' +
                                  '<named_entities_limit>5</named_entities_limit>' +
                                  '<user_entities_limit>5</user_entities_limit>' +
                                  '<themes_limit>0</themes_limit>' +
                                  '<entity_themes_limit>5</entity_themes_limit>' +
                                  '<summary_limit>0</summary_limit>' +
                                  '<phrases_limit>0</phrases_limit>' +
                               '</document>' +
                               '<collection>' +
                                  '<facets_limit>15</facets_limit>' +
                                  '<facet_atts_limit>5</facet_atts_limit>' +
                                  '<concept_topics_limit>5</concept_topics_limit>' +
                                  '<query_topics_limit>5</query_topics_limit>' +
                                  '<named_entities_limit>5</named_entities_limit>' +
                                  '<themes_limit>0</themes_limit>' +
                               '</collection>' +
                            '</configuration>' +
							'<configuration>' +
                               '<name>Cloned configuration</name>' +
                               '<is_primary>true</is_primary>' +
							   '<one_sentence>false</one_sentence>' +
							   '<template>123</template>' +
							'</configuration>' +
                         '</added>' +
                         '<removed>' +
                            '<configuration>45699836</configuration>' +
                         '</removed>' +
                      '</configurations>';

        expectedResult = expectedResult.squeeze(" ")

        config = {}
        config["name"] = "A test configuration";
        config["is_primary"] = true;
		config["one_sentence"] = true;
        config["auto_responding"] = true;
        config["language"] = "English";
        config["chars_threshold"] = 80;
        config["callback"] = "https://anyapi.anydomain.com/processed/docs.json";

        config["document"] = {}
        config["document"]["concept_topics_limit"] = 5;
        config["document"]["query_topics_limit"] = 5;
        config["document"]["named_entities_limit"] = 5;
        config["document"]["user_entities_limit"] = 5;
        config["document"]["themes_limit"] = 0;
        config["document"]["entity_themes_limit"] = 5;
        config["document"]["summary_limit"] = 0;
        config["document"]["phrases_limit"] = 0;

        config["collection"] = {}
        config["collection"]["facets_limit"] = 15;
        config["collection"]["facet_atts_limit"] = 5;
        config["collection"]["concept_topics_limit"] = 5;
        config["collection"]["query_topics_limit"] = 5;
        config["collection"]["named_entities_limit"] = 5;
        config["collection"]["themes_limit"] = 0;
		
		clonedConfig = {}
		clonedConfig["config_id"] = "123";
        clonedConfig["name"] = "Cloned configuration";
        clonedConfig["is_primary"] = true;
		clonedConfig["one_sentence"] = false;

        serializer = XmlSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << config
        proxy["removed"] << "45699836"
		proxy["cloned"] << clonedConfig

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"configurations", "added"=>"configuration", "removed"=>"configuration"}))
  end

  def test_JsonSerializingConfiguration
        expectedResult = '{' +
                         '"added":[' +
                          '{' +
                            '"name":"A test configuration",' +
                            '"is_primary":true,' +
							'"one_sentence":true,' +
                            '"auto_responding":true,' +
                            '"language":"English",' +
                            '"chars_threshold":80,' +
                            '"callback":"https://anyapi.anydomain.com/processed/docs.json",' +
                            '"document":{' +
                              '"concept_topics_limit":5,' +
                              '"query_topics_limit":5,' +
                              '"named_entities_limit":5,' +
                              '"user_entities_limit":5,' +
                              '"themes_limit":0,' +
                              '"entity_themes_limit":5,' +
                              '"summary_limit":0,' +
                              '"phrases_limit":0' +
                            '},' +
                            '"collection":{' +
                              '"facets_limit":15,' +
                              '"facet_atts_limit":5,' +
                              '"concept_topics_limit":5,' +
                              '"query_topics_limit":5,' +
                              '"named_entities_limit":5,' +
                              '"themes_limit":0' +
                            '}' +
                          '},' +
						  '{' +
                            '"name":"Cloned configuration",' +
                            '"is_primary":true,' +
							'"one_sentence":false,' +
							'"template":"123"' +
						  '}' +
                        '],' +
                         '"removed":["45699836"]' +
                      '}'

        expectedResult = expectedResult.squeeze(" ")

        config = {}
        config["name"] = "A test configuration";
        config["is_primary"] = true;
		config["one_sentence"] = true;
        config["auto_responding"] = true;
        config["language"] = "English";
        config["chars_threshold"] = 80;
        config["callback"] = "https://anyapi.anydomain.com/processed/docs.json";

        config["document"] = {}
        config["document"]["concept_topics_limit"] = 5;
        config["document"]["query_topics_limit"] = 5;
        config["document"]["named_entities_limit"] = 5;
        config["document"]["user_entities_limit"] = 5;
        config["document"]["themes_limit"] = 0;
        config["document"]["entity_themes_limit"] = 5;
        config["document"]["summary_limit"] = 0;
        config["document"]["phrases_limit"] = 0;

        config["collection"] = {}
        config["collection"]["facets_limit"] = 15;
        config["collection"]["facet_atts_limit"] = 5;
        config["collection"]["concept_topics_limit"] = 5;
        config["collection"]["query_topics_limit"] = 5;
        config["collection"]["named_entities_limit"] = 5;
        config["collection"]["themes_limit"] = 0;
		
		clonedConfig = {}
		clonedConfig["config_id"] = "123";
        clonedConfig["name"] = "Cloned configuration";
        clonedConfig["is_primary"] = true;
		clonedConfig["one_sentence"] = false;

        serializer = JsonSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << config
        proxy["removed"] << "45699836"
		proxy["cloned"] << clonedConfig

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"configurations", "added"=>"configuration", "removed"=>"configuration"}))
  end

  def test_XmlDeserializingConfiguration
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>
			<configurations>
				<configuration>
					<auto_responding>false</auto_responding>
					<callback>https://anyapi.anydomain.com/processed/docs.json</callback>
					<chars_threshold>80</chars_threshold>
					<collection>
						<concept_topics_limit>5</concept_topics_limit>
						<facet_atts_limit>20</facet_atts_limit>
						<facets_limit>15</facets_limit>
						<named_entities_limit>5</named_entities_limit>
						<query_topics_limit>5</query_topics_limit>
						<themes_limit>0</themes_limit>
					</collection>
					<document>
						<concept_topics_limit>7</concept_topics_limit>
						<entity_themes_limit>5</entity_themes_limit>
						<named_entities_limit>100</named_entities_limit>
						<phrases_limit>1</phrases_limit>
						<query_topics_limit>25</query_topics_limit>
						<summary_limit>0</summary_limit>
						<themes_limit>10</themes_limit>
						<user_entities_limit>5</user_entities_limit>
					</document>
					<config_id>66f2b9c33a63681826926de7965dc996</config_id>
					<is_primary>false</is_primary>
					<one_sentence>true</one_sentence>
					<language>English</language>
					<name>default</name>
				</configuration>
			</configurations>";

        serializer = XmlSerializer.new()
        configs = serializer.deserialize(source, GetConfigurationsHandler.new())
        config = configs[0]

        assert_equal("default", config['name']);
        assert(!config['is_primary']);
		assert(config['one_sentence']);
        assert(!config['auto_responding']);
        assert_equal("English", config['language']);
        assert_equal(80, config['chars_threshold']);
        assert_equal("https://anyapi.anydomain.com/processed/docs.json", config['callback']);

        assert_equal(7, config['document']['concept_topics_limit']);
        assert_equal(25, config['document']['query_topics_limit']);
        assert_equal(100, config['document']['named_entities_limit']);
        assert_equal(5, config['document']['user_entities_limit']);
        assert_equal(10, config['document']['themes_limit']);
        assert_equal(5, config['document']['entity_themes_limit']);
        assert_equal(1, config['document']['phrases_limit']);
        assert_equal(0, config['document']['summary_limit']);

        assert_equal(5, config['collection']['concept_topics_limit']);
        assert_equal(5, config['collection']['query_topics_limit']);
        assert_equal(5, config['collection']['named_entities_limit']);
        assert_equal(0, config['collection']['themes_limit']);
        assert_equal(15, config['collection']['facets_limit']);
        assert_equal(20, config['collection']['facet_atts_limit']);
  end

  def test_JsonDeserializingConfiguration
        source = "[\n\
                    {\n\
                    \"name\":\"default\",\n\
                    \"auto_responding\":false,\n\
                    \"is_primary\":false,\n\
					\"one_sentence\":true,\n\
                    \"language\":\"English\",\n\
                    \"chars_threshold\":80,\n\
                    \"document\":{\n\
						\"entity_themes_limit\":5,\n\
						\"summary_limit\":0,\n\
						\"phrases_limit\":1,\n\
						\"themes_limit\":10,\n\
						\"query_topics_limit\":25,\n\
						\"concept_topics_limit\":7,\n\
						\"named_entities_limit\":100,\n\
						\"user_entities_limit\":5\n\
                    },\n\
                    \"collection\":{\n\
						\"facets_limit\":15,\n\
						\"facet_atts_limit\":20,\n\
						\"themes_limit\":0,\n\
						\"query_topics_limit\":5,\n\
						\"concept_topics_limit\":5,\n\
						\"named_entities_limit\":5\n\
                    },\n\
                    \"callback\":\"https:\/\/anyapi.anydomain.com\/processed\/docs.json\"\n\
                    }\n\
                ]"

        serializer = JsonSerializer.new()
        configs = serializer.deserialize(source, GetConfigurationsHandler.new())
        config = configs[0]

        assert_equal("default", config['name']);
        assert(!config['is_primary']);
		assert(config['one_sentence']);
        assert(!config['auto_responding']);
        assert_equal("English", config['language']);
        assert_equal(80, config['chars_threshold']);
        assert_equal("https://anyapi.anydomain.com/processed/docs.json", config['callback']);

        assert_equal(7, config['document']['concept_topics_limit']);
        assert_equal(25, config['document']['query_topics_limit']);
        assert_equal(100, config['document']['named_entities_limit']);
        assert_equal(5, config['document']['user_entities_limit']);
        assert_equal(10, config['document']['themes_limit']);
        assert_equal(5, config['document']['entity_themes_limit']);
        assert_equal(1, config['document']['phrases_limit']);
        assert_equal(0, config['document']['summary_limit']);

        assert_equal(5, config['collection']['concept_topics_limit']);
        assert_equal(5, config['collection']['query_topics_limit']);
        assert_equal(5, config['collection']['named_entities_limit']);
        assert_equal(0, config['collection']['themes_limit']);
        assert_equal(15, config['collection']['facets_limit']);
        assert_equal(20, config['collection']['facet_atts_limit']);
  end

  def test_XmlSerializingBlacklist
        expectedResult = "<blacklist>" +
                          "<added>" +
                            "<item>Added Filter 1</item>" +
                          "</added>" +
                          "<removed>" +
                            "<item>Removed Filter 1</item>" +
                          "</removed>" +
                        "</blacklist>"

        expectedResult = expectedResult.squeeze(" ")

        serializer = XmlSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << "Added Filter 1"
        proxy["removed"] << "Removed Filter 1"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"blacklist", "added"=>"item", "removed"=>"item"}))
    end

  def test_JsonSerializingBlacklist
        expectedResult = "{" +
                          "\"added\":[\".*@.*com\",\".*@com\\\\.net\"]," +
                          "\"removed\":[\"http:\/\/www\\\\..*\\\\.com\"]" +
                        "}";

        expectedResult = expectedResult.squeeze(" ")

        serializer = JsonSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << ".*@.*com"
        proxy["added"] << ".*@com\\.net"
        proxy["removed"] << "http://www\\..*\\.com"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"blacklist", "added"=>"item", "removed"=>"item"}))
    end

  def test_XmlDeserializingBlacklist
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <blacklist>\n\
                    <item>Filter 1</item>\n\
                    <item>Filter 2</item>\n\
                </blacklist>"

        serializer = XmlSerializer.new()
        
        blacklists = serializer.deserialize(source, GetBlacklistHandler.new())

        assert_equal(2, blacklists.count);
        assert_equal("Filter 1", blacklists[0]);
        assert_equal("Filter 2", blacklists[1])
    end

  def test_JsonDeserializingBlacklist
        source = "[\n\
                    \".*@.*com\",\n\
                    \".*@com\\\\.net\"\n\
                 ]"

        serializer = JsonSerializer.new()
        
        blacklists = serializer.deserialize(source, GetBlacklistHandler.new())

        assert_equal(2, blacklists.count);
        assert_equal(".*@.*com", blacklists[0]);
        assert_equal(".*@com\\.net", blacklists[1]);
    end

  def test_XmlSerializingQueries
        expectedResult ="<queries>" +
                          "<added>" +
                            "<query><name>Query 1</name><query>Something AND something</query></query>" +
                          "</added>" +
                          "<removed>" +
                            "<query>Query 2</query>" +
                          "</removed>" +
                        "</queries>"
        expectedResult = expectedResult.squeeze(" ")

        serializer = XmlSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << {"name"=>"Query 1", "query"=>"Something AND something"}
        proxy["removed"] << "Query 2"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"queries", "added"=>"query", "removed"=>"query"}))
    end

  def test_JsonSerializingQueries
        expectedResult = "{" +
                          "\"added\":[" +
                            "{" +
                              "\"name\":\"Feature: Cloud service\"," +
                              "\"query\":\"Amazon AND EC2 AND Cloud\"" +
                            "}" +
                          "]," +
                          "\"removed\":[\"Features\"]" +
                        "}";
        expectedResult = expectedResult.squeeze(" ")

        serializer = JsonSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << {"name"=>"Feature: Cloud service", "query"=>"Amazon AND EC2 AND Cloud"}
        proxy["removed"] << "Features"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"queries", "added"=>"query", "removed"=>"query"}))
    end

  def test_XmlDeserializingQueries
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <queries>\n\
                    <query><name>Query 1</name><query>Something AND something</query></query>\n\
                    <query><name>Query 2</name><query>Something AND something</query></query>\n\
                </queries>"

        serializer = XmlSerializer.new()
        
        queries = serializer.deserialize(source, GetQueriesHandler.new())

        assert_equal(2, queries.count);
        assert_equal("Query 1", queries[0]["name"]);
        assert_equal("Something AND something", queries[0]["query"]);
        assert_equal("Query 2", queries[1]["name"]);
        assert_equal("Something AND something", queries[1]["query"]);
    end

  def test_JsonDeserializingQueries
        source = "[\n\
                    {\n\
                        \"name\":\"Feature: Cloud service\",\n\
                        \"query\":\"Amazon AND EC2 AND Cloud\"\n\
                    }\n\
                ]";

        serializer = JsonSerializer.new()
        
        queries = serializer.deserialize(source, GetQueriesHandler.new())

        assert_equal(1, queries.count);
        assert_equal("Feature: Cloud service", queries[0]["name"]);
        assert_equal("Amazon AND EC2 AND Cloud", queries[0]["query"]);
    end

  def test_XmlSerializingEntities
        expectedResult = "<entities>" +
                          "<added>" +
                            "<entity>" +
                              "<name>name 1</name>" +
                              "<type>type 1</type>" +
                            "</entity>" +
                          "</added>" +
                          "<removed>" +
                            "<entity>name 2</entity>" +
                          "</removed>" +
                        "</entities>"
        expectedResult = expectedResult.squeeze(" ")

        serializer = XmlSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << {"name"=>"name 1", "type"=>"type 1"}
        proxy["removed"] << "name 2"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"entities", "added"=>"entity", "removed"=>"entity"}))
    end

  def test_JsonSerializingEntities
        expectedResult = "{" +
                          "\"added\":[" +
                            "{" +
                              "\"name\":\"chair\"," +
                              "\"type\":\"furniture\"" +
                            "}" +
                          "]," +
                          "\"removed\":[\"table\"]" +
                        "}"
        expectedResult = expectedResult.squeeze(" ")

        serializer = JsonSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << {"name"=>"chair", "type"=>"furniture"}
        proxy["removed"] << "table"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"entities", "added"=>"entity", "removed"=>"entity"}))
    end

  def test_XmlDeserializingEntities
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <entities>\n\
                    <entity>\n\
                        <name>chair</name>\n\
                        <type>furniture</type> \n\
                    </entity>\n\
                </entities>"

        serializer = XmlSerializer.new()
        
        entities = serializer.deserialize(source, GetEntitiesHandler.new())

        assert_equal(1, entities.count);
        assert_equal("chair", entities[0]["name"]);
        assert_equal("furniture", entities[0]["type"]);
    end

  def test_JsonDeserializingEntities
        source = "[\n\
                    {\n\
                        \"name\":\"chair\",\n\
                        \"type\":\"furniture\"\n\
                    }\n\
                ]"

        serializer = JsonSerializer.new()
        
        entities = serializer.deserialize(source, GetEntitiesHandler.new())

        assert_equal(1, entities.count);
        assert_equal("chair", entities[0]["name"]);
        assert_equal("furniture", entities[0]["type"]);
    end

  def test_XmlSerializingCategories
        expectedResult = "<categories>" +
                          "<added>" +
                              "<category>" +
                                  "<name>Added Category 1</name>" +
                                  "<weight>0.2</weight>" +
                                  "<samples>" +
                                      "<sample>Entity 1</sample>" +
                                      "<sample>Entity 2</sample>" +
                                      "<sample>Entity 3</sample>" +
                                  "</samples>" +
                              "</category>" +
                          "</added>" +
                          "<removed>" +
                              "<category>Removed Category 1</category>" +
                          "</removed>" +
                      "</categories>"
        expectedResult = expectedResult.squeeze(" ")

        serializer = XmlSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << {"name"=>"Added Category 1", "weight"=>0.2, "samples"=>["Entity 1", "Entity 2", "Entity 3"]}
        proxy["removed"] << "Removed Category 1"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"categories", "added"=>"category", "removed"=>"category", "samples"=>"sample"}))
    end

  def test_JsonSerializingCategories
        expectedResult = "{" +
                          "\"added\":[" +
                            "{" +
                              "\"name\":\"Feature: Cloud service\"," +
                              "\"weight\":0.0," +
                              "\"samples\":[]" +
                            "}" +
                          "]," +
                          "\"removed\":[\"Features\"]" +
                        "}";

        expectedResult = expectedResult.squeeze(" ")

        serializer = JsonSerializer.new()
        

        proxy = @session.createUpdateProxy()
        proxy["added"] << {"name"=>"Feature: Cloud service", "weight"=>0.0, "samples"=>[]}
        proxy["removed"] << "Features"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"categories", "added"=>"category", "removed"=>"category", "samples"=>"sample"}))
    end

  def test_XmlDeserializingCategories
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <categories>\n\
                    <category>\n\
                        <name>Feature: Cloud service</name>\n\
                        <weight>0.75</weight>\n\
                        <samples>\n\
                            <sample>Amazon</sample>\n\
                            <sample>EC2</sample>\n\
                        </samples>\n\
                    </category>\n\
                </categories>"

        serializer = XmlSerializer.new()
        
        categories = serializer.deserialize(source, GetCategoriesHandler.new())

        assert_equal(1, categories.count);
        assert_equal("Feature: Cloud service", categories[0]["name"]);
        assert_equal(0.75, categories[0]["weight"]);
        assert_equal(2, categories[0]["samples"].count);
        assert_equal("Amazon", categories[0]["samples"][0]);
        assert_equal("EC2", categories[0]["samples"][1]);
    end

  def test_JsonDeserializingCategories
        source = "[\n\
                    {\n\
                        \"name\":\"Feature: Cloud service\",\n\
                        \"weight\":0.75,\n\
                        \"samples\":[\"Amazon\",\"EC2\"]\n\
                    }\n\
                ]"

        serializer = JsonSerializer.new()
        
        categories = serializer.deserialize(source, GetCategoriesHandler.new())

        assert_equal(1, categories.count);
        assert_equal("Feature: Cloud service", categories[0]["name"]);
        assert_equal(0.75, categories[0]["weight"]);
        assert_equal(2, categories[0]["samples"].count);
        assert_equal("Amazon", categories[0]["samples"][0]);
        assert_equal("EC2", categories[0]["samples"][1]);
    end

  def test_XmlSerializingSentimentPhrases
        expectedResult = "<phrases>" +
                          "<added>" +
                            "<phrase>" +
                              "<title>name 1</title>" +
                              "<weight>0.3</weight>" +
                            "</phrase>" +
                          "</added>" +
                          "<removed>" +
                            "<phrase>name 2</phrase>" +
                          "</removed>" +
                        "</phrases>"
        expectedResult = expectedResult.squeeze(" ")

        serializer = XmlSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << {"title"=>"name 1", "weight"=>0.3}
        proxy["removed"] << "name 2"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"phrases", "added"=>"phrase", "removed"=>"phrase"}))
  end

    def test_JsonSerializingSentimentPhrases
        expectedResult = "{" +
                          "\"added\":[" +
                            "{" +
                              "\"title\":\"Feature: Cloud service\"," +
                              "\"weight\":0.0" +
                            "}" +
                          "]," +
                          "\"removed\":[\"Features\"]" +
                        "}"
        expectedResult = expectedResult.squeeze(" ")

        serializer = JsonSerializer.new()
        proxy = @session.createUpdateProxy()
        proxy["added"] << {"title"=>"Feature: Cloud service", "weight"=>0.0}
        proxy["removed"] << "Features"

        assert_equal(expectedResult, serializer.serialize(proxy, {"root"=>"phrases", "added"=>"phrase", "removed"=>"phrase"}))
    end

    def test_XmlDeserializingSentimentPhrases
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <phrases>\n\
                    <phrase>\n\
                        <title>chair</title>\n\
                        <weight>0.3</weight>\n\
                    </phrase>\n\
                </phrases>"

        serializer = XmlSerializer.new()
        phrases = serializer.deserialize(source, GetSentimentPhrasesHandler.new())

        assert_equal(1, phrases.count);
        assert_equal("chair", phrases[0]["title"]);
        assert_equal(0.3, phrases[0]["weight"]);
    end

    def test_JsonDeserializingSentimentPhrases
        source = "[\n\
                    {\n\
                        \"title\":\"chair\",\n\
                        \"weight\":0.75\n\
                    }\n\
                ]"

        serializer = JsonSerializer.new()
        
        phrases = serializer.deserialize(source, GetSentimentPhrasesHandler.new())

        assert_equal(1, phrases.count);
        assert_equal("chair", phrases[0]["title"]);
        assert_equal(0.75, phrases[0]["weight"]);
    end

    def test_XmlDeserializingAnalyticServiceStatus
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <status>\n\
                    <service_status>online</service_status>\n\
                    <api_version>2.0</api_version>\n\
                    <service_version>1.0.2.63</service_version>\n\
                    <supported_encoding>UTF-8</supported_encoding>\n\
                    <supported_compression>gzip</supported_compression>\n\
                    <supported_languages>\n\
                        <language>English</language>\n\
                        <language>French</language>\n\
                    </supported_languages>\n\
                </status>"

        serializer = XmlSerializer.new()
        
        status = serializer.deserialize(source, GetStatusHandler.new())

        assert_equal("online", status["service_status"]);
        assert_equal("2.0", status["api_version"]);
        assert_equal("1.0.2.63", status["service_version"]);
        assert_equal("UTF-8", status["supported_encoding"]);
        assert_equal("gzip", status["supported_compression"]);
        assert_equal(2, status["supported_languages"].count);
        assert_equal("English", status["supported_languages"][0]);
        assert_equal("French", status["supported_languages"][1]);
    end

    def test_JsonDeserializingAnalyticServiceStatus
        source = "{\n\
                    \"service_status\":\"online\",\n\
                    \"api_version\":\"2.0\",\n\
                    \"service_version\":\"1.0.2.63\",\n\
                    \"supported_encoding\":\"UTF-8\",\n\
                    \"supported_compression\":\"gzip\",\n\
                    \"supported_languages\":[\n\
                        \"English\",\n\
                        \"French\"\n\
                    ]\n\
                }"

        serializer = JsonSerializer.new()
        
        status = serializer.deserialize(source, GetStatusHandler.new())

        assert_equal("online", status["service_status"]);
        assert_equal("2.0", status["api_version"]);
        assert_equal("1.0.2.63", status["service_version"]);
        assert_equal("UTF-8", status["supported_encoding"]);
        assert_equal("gzip", status["supported_compression"]);
        assert_equal(2, status["supported_languages"].count);
        assert_equal("English", status["supported_languages"][0]);
        assert_equal("French", status["supported_languages"][1]);
    end

    def test_XmlDeserializingSubscription
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <subscription>\n\
                    <name>name</name>\n\
                    <status>active</status>\n\
                    <priority>normal</priority>\n\
                    <expiration_date>1293883200</expiration_date>\n\
                    <calls_balance>87</calls_balance>\n\
                    <calls_limit>100</calls_limit>\n\
                    <calls_limit_interval>60</calls_limit_interval>\n\
                    <docs_balance>49897</docs_balance>\n\
                    <docs_limit>0</docs_limit>\n\
                    <docs_limit_interval>0</docs_limit_interval>\n\
                    <configurations_limit>10</configurations_limit>\n\
                    <blacklist_limit>100</blacklist_limit>\n\
                    <categories_limit>100</categories_limit>\n\
                    <queries_limit>100</queries_limit>\n\
                    <entities_limit>1000</entities_limit>\n\
                    <sentiment_limit>1000</sentiment_limit>\n\
                    <characters_limit>8192</characters_limit>\n\
                    <batch_limit>1</batch_limit>\n\
                    <collection_limit>10</collection_limit>\n\
                    <auto_response_limit>2</auto_response_limit>\n\
                    <processed_batch_limit>100</processed_batch_limit>\n\
                    <callback_batch_limit>100</callback_batch_limit>\n\
					<limit_type>type limit</limit_type>\n\
                </subscription>"

        serializer = XmlSerializer.new()
        
        subscription = serializer.deserialize(source, GetSubscriptionHandler.new())

        assert_equal("name", subscription["name"]);
        assert_equal("active", subscription["status"]);
        assert_equal("normal", subscription["priority"]);
        assert_equal(1293883200, subscription["expiration_date"]);
        assert_equal(87, subscription["calls_balance"]);
        assert_equal(100, subscription["calls_limit"]);
        assert_equal(60, subscription["calls_limit_interval"]);
        assert_equal(49897, subscription["docs_balance"]);
        assert_equal(0, subscription["docs_limit"]);
        assert_equal(0, subscription["docs_limit_interval"]);
        assert_equal(10, subscription["configurations_limit"]);
        assert_equal(100, subscription["blacklist_limit"]);
        assert_equal(100, subscription["categories_limit"]);
        assert_equal(100, subscription["queries_limit"]);
        assert_equal(1000, subscription["entities_limit"]);
        assert_equal(1000, subscription["sentiment_limit"]);
        assert_equal(8192, subscription["characters_limit"]);
        assert_equal(1, subscription["batch_limit"]);
        assert_equal(10, subscription["collection_limit"]);
        assert_equal(2, subscription["auto_response_limit"]);
        assert_equal(100, subscription["processed_batch_limit"]);
        assert_equal(100, subscription["callback_batch_limit"]);
		assert_equal("type limit", subscription["limit_type"]);
    end

    def test_JsonDeserializingSubscription
        source = "{\n\
                    \"name\" : \"name\",\n\
                    \"status\" : \"active\",\n\
                    \"priority\" : \"normal\",\n\
                    \"expiration_date\" : 1293883200,\n\
                    \"calls_balance\" : 87,\n\
                    \"calls_limit\" : 100,\n\
                    \"calls_limit_interval\" : 60,\n\
                    \"docs_balance\" : 49897,\n\
                    \"docs_limit\" : 0,\n\
                    \"docs_limit_interval\" : 0,\n\
                    \"configurations_limit\" : 10,\n\
                    \"blacklist_limit\" : 100,\n\
                    \"categories_limit\" : 100,\n\
                    \"queries_limit\" : 100,\n\
                    \"entities_limit\" : 1000,\n\
                    \"sentiment_limit\" : 1000,\n\
                    \"characters_limit\" : 8192,\n\
                    \"batch_limit\" : 10,\n\
                    \"collection_limit\" : 10,\n\
                    \"auto_response_limit\" : 2,\n\
                    \"processed_batch_limit\" : 100,\n\
                    \"callback_batch_limit\" : 100,\n\
					\"limit_type\" : \"type limit\"\n\
                }"

        serializer = JsonSerializer.new()
        
        subscription = serializer.deserialize(source, GetSubscriptionHandler.new())

        assert_equal("name", subscription["name"]);
        assert_equal("active", subscription["status"]);
        assert_equal("normal", subscription["priority"]);
        assert_equal(1293883200, subscription["expiration_date"]);
        assert_equal(87, subscription["calls_balance"]);
        assert_equal(100, subscription["calls_limit"]);
        assert_equal(60, subscription["calls_limit_interval"]);
        assert_equal(49897, subscription["docs_balance"]);
        assert_equal(0, subscription["docs_limit"]);
        assert_equal(0, subscription["docs_limit_interval"]);
        assert_equal(10, subscription["configurations_limit"]);
        assert_equal(100, subscription["blacklist_limit"]);
        assert_equal(100, subscription["categories_limit"]);
        assert_equal(100, subscription["queries_limit"]);
        assert_equal(1000, subscription["entities_limit"]);
        assert_equal(1000, subscription["sentiment_limit"]);
        assert_equal(8192, subscription["characters_limit"]);
        assert_equal(10, subscription["batch_limit"]);
        assert_equal(10, subscription["collection_limit"]);
        assert_equal(2, subscription["auto_response_limit"]);
        assert_equal(100, subscription["processed_batch_limit"]);
        assert_equal(100, subscription["callback_batch_limit"]);
		assert_equal("type limit", subscription["limit_type"]);
    end

    def test_XmlDeserializingDocumentAnalyticData
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                    <document>\n\
                        <config_id>23498367</config_id>\n\
                        <id>6F9619FF8B86D011B42D00CF4FC964FF</id>\n\
                        <status>PROCESSED</status>\n\
                        <sentiment_score>0.2398756</sentiment_score>\n\
                        <summary>Summary of the document's text.</summary>\n\
                        <themes>\n\
                                <theme>\n\
                                        <evidence>1</evidence>\n\
                                        <is_about>true</is_about>\n\
                                        <strength_score>0.0</strength_score>\n\
                                        <sentiment_score>0.0</sentiment_score>\n\
                                        <title>republican moderates</title>\n\
                                </theme>\n\
                        </themes>\n\
                        <entities>\n\
                                <entity>\n\
                                        <evidence>0</evidence>\n\
                                        <is_about>true</is_about>\n\
                                        <confident>true</confident>\n\
                                        <title>WASHINGTON</title>\n\
                                        <sentiment_score>1.0542796</sentiment_score>\n\
                                        <type>named</type>\n\
                                        <entity_type>Place</entity_type>\n\
                                        <themes>\n\
                                                <theme>\n\
                                                        <evidence>1</evidence>\n\
                                                        <is_about>true</is_about>\n\
                                                        <strength_score>0.0</strength_score>\n\
                                                        <sentiment_score>0.0</sentiment_score>\n\
                                                        <title>republican moderates</title>\n\
                                                </theme>\n\
                                        </themes>\n\
                                </entity>\n\
                        </entities>\n\
                        <topics>\n\
                                <topic>\n\
                                        <title>Something</title>\n\
                                        <hitcount>0</hitcount>\n\
                                        <sentiment_score>0.6133076</sentiment_score>\n\
                                        <strength_score>0.6133076</strength_score>\n\
                                        <type>concept</type>\n\
                                </topic>\n\
                        </topics>\n\
                        <phrases>\n\
                                <phrase>\n\
                                        <title>Something</title>\n\
                                        <sentiment_score>0.6133076</sentiment_score>\n\
                                        <is_negated>true</is_negated>\n\
                                        <negating_phrase>not</negating_phrase>\n\
                                </phrase>\n\
                        </phrases>\n\
                    </document>";

        serializer = XmlSerializer.new()
        doc = serializer.deserialize(source, GetDocumentHandler.new())

        # main
        assert_equal("23498367", doc['config_id']);
        assert_equal("6F9619FF8B86D011B42D00CF4FC964FF", doc['id']);
        assert_equal("PROCESSED", doc['status']);
        assert_equal(0.2398756, doc['sentiment_score']);
        assert_equal("Summary of the document's text.", doc['summary']);
        # themes
        assert_equal(1, doc['themes'].count);
        assert_equal(1, doc['themes'][0]['evidence']);
        assert(doc['themes'][0]['is_about']);
        assert_equal(0, doc['themes'][0]['strength_score']);
        assert_equal(0, doc['themes'][0]['sentiment_score']);
        assert_equal('republican moderates', doc['themes'][0]['title']);
        # entities
        assert_equal(1, doc['entities'].count);
        assert_equal(0, doc['entities'][0]['evidence']);
        assert(doc['entities'][0]['is_about']);
        assert(doc['entities'][0]['confident']);
        assert_equal('named', doc['entities'][0]['type']);
        assert_equal(1.0542796, doc['entities'][0]['sentiment_score']);
        assert_equal('WASHINGTON', doc['entities'][0]['title']);
        # entity themes
        assert_equal(1, doc['entities'][0]['themes'].count);
        theme = doc['entities'][0]['themes'][0];
        assert_equal(1, theme['evidence']);
        assert(theme['is_about']);
        assert_equal(0, theme['strength_score']);
        assert_equal(0, theme['sentiment_score']);
        assert_equal('republican moderates', theme['title']);
        # topics
        assert_equal(1, doc['topics'].count);
        assert_equal(0, doc['topics'][0]['hitcount']);
        assert_equal('concept', doc['topics'][0]['type']);
        assert_equal(0.6133076, doc['topics'][0]['strength_score']);
        assert_equal(0.6133076, doc['topics'][0]['sentiment_score']);
        assert_equal('Something', doc['topics'][0]['title']);
        # phrases
        assert_equal(1, doc['phrases'].count);
        assert_equal('not', doc['phrases'][0]['negating_phrase']);
        assert(doc['phrases'][0]['is_negated']);
        assert_equal(0.6133076, doc['phrases'][0]['sentiment_score']);
        assert_equal('Something', doc['phrases'][0]['title']);
    end

    def test_JsonDeserializingDocumentAnalyticData
        source = "[{\n\
                    \"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\",\n\
                    \"config_id\":\"23498367\",\n\
                    \"status\":\"PROCESSED\",\n\
                    \"sentiment_score\":0.2398756,\n\
                    \"summary\":\"Summary of the document's text.\",\n\
                    \"themes\":[\n\
                        {\n\
                            \"evidence\":1,\n\
                            \"is_about\":true,\n\
                            \"strength_score\":0.0,\n\
                            \"sentiment_score\":0.0,\n\
                            \"title\":\"republican moderates\"\n\
                        }\n\
                    ],\n\
                    \"entities\":[\n\
                        {\n\
                        \"type\":\"named\",\n\
                        \"evidence\":0,\n\
                        \"is_about\":true,\n\
                        \"confident\":true,\n\
                        \"entity_type\":\"Place\",\n\
                        \"title\":\"WASHINGTON\",\n\
                        \"sentiment_score\":1.0542796,\n\
                        \"themes\":[\n\
                            {\n\
                                \"evidence\":1,\n\
                                \"is_about\":true,\n\
                                \"strength_score\":0.0,\n\
                                \"sentiment_score\":0.0,\n\
                                \"title\":\"republican moderates\"\n\
                            }\n\
                        ]\n\
                        }\n\
                    ],\n\
                    \"topics\":[\n\
                        {\n\
                            \"title\":\"Something\",\n\
                            \"type\":\"concept\",\n\
                            \"hitcount\":0,\n\
                            \"strength_score\":0.6133076,\n\
                            \"sentiment_score\":0.6133076\n\
                        }\n\
                    ],\n\
                    \"phrases\":[\n\
                        {\n\
                            \"title\":\"Something\",\n\
                            \"is_negated\":true,\n\
                            \"negating_phrase\":\"not\",\n\
                            \"sentiment_score\":0.6133076\n\
                        }\n\
                    ]\n\
                }]"

        serializer = JsonSerializer.new()
        docs = serializer.deserialize(source, GetDocumentHandler.new())
        assert_equal(1, docs.count);
        doc = docs[0]

        # main
        assert_equal("23498367", doc['config_id']);
        assert_equal("6F9619FF8B86D011B42D00CF4FC964FF", doc['id']);
        assert_equal("PROCESSED", doc['status']);
        assert_equal(0.2398756, doc['sentiment_score']);
        assert_equal("Summary of the document's text.", doc['summary']);
        # themes
        assert_equal(1, doc['themes'].count);
        assert_equal(1, doc['themes'][0]['evidence']);
        assert(doc['themes'][0]['is_about']);
        assert_equal(0, doc['themes'][0]['strength_score']);
        assert_equal(0, doc['themes'][0]['sentiment_score']);
        assert_equal('republican moderates', doc['themes'][0]['title']);
        # entities
        assert_equal(1, doc['entities'].count);
        assert_equal(0, doc['entities'][0]['evidence']);
        assert(doc['entities'][0]['is_about']);
        assert(doc['entities'][0]['confident']);
        assert_equal('named', doc['entities'][0]['type']);
        assert_equal(1.0542796, doc['entities'][0]['sentiment_score']);
        assert_equal('WASHINGTON', doc['entities'][0]['title']);
        # entity themes
        assert_equal(1, doc['entities'][0]['themes'].count);
        theme = doc['entities'][0]['themes'][0];
        assert_equal(1, theme['evidence']);
        assert(theme['is_about']);
        assert_equal(0, theme['strength_score']);
        assert_equal(0, theme['sentiment_score']);
        assert_equal('republican moderates', theme['title']);
        # topics
        assert_equal(1, doc['topics'].count);
        assert_equal(0, doc['topics'][0]['hitcount']);
        assert_equal('concept', doc['topics'][0]['type']);
        assert_equal(0.6133076, doc['topics'][0]['strength_score']);
        assert_equal(0.6133076, doc['topics'][0]['sentiment_score']);
        assert_equal('Something', doc['topics'][0]['title']);
        # phrases
        assert_equal(1, doc['phrases'].count);
        assert_equal('not', doc['phrases'][0]['negating_phrase']);
        assert(doc['phrases'][0]['is_negated']);
        assert_equal(0.6133076, doc['phrases'][0]['sentiment_score']);
        assert_equal('Something', doc['phrases'][0]['title']);
    end

    def test_XmlDeserializingCollectionAnalyticData
        source =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
			<collection>\n\
                        <config_id>23498367</config_id>\n\
                        <id>6F9619FF8B86D011B42D00CF4FC964FF</id>\n\
                        <status>PROCESSED</status>\n\
                        <facets>\n\
                                <facet>\n\
                                        <label>Something</label>\n\
                                        <count>10</count>\n\
                                        <negative_count>2</negative_count>\n\
                                        <positive_count>1</positive_count>\n\
                                        <neutral_count>7</neutral_count>\n\
                                        <attributes>\n\
                                                <attribute>\n\
                                                        <label>Attribute</label>\n\
                                                        <count>5</count>\n\
                                                </attribute>\n\
                                        </attributes>\n\
                                </facet>\n\
                        </facets>\n\
                        <themes>\n\
                                <theme>\n\
                                        <phrases_count>1</phrases_count>\n\
                                        <themes_count>1</themes_count>\n\
                                        <sentiment_score>0.0</sentiment_score>\n\
                                        <title>republican moderates</title>\n\
                                </theme>\n\
                        </themes>\n\
                        <entities>\n\
                                <entity>\n\
                                        <title>WASHINGTON</title>\n\
                                        <type>named</type>\n\
                                        <entity_type>Place</entity_type>\n\
                                        <count>1</count>\n\
                                        <negative_count>1</negative_count>\n\
                                        <neutral_count>1</neutral_count>\n\
                                        <positive_count>1</positive_count>\n\
                                </entity>\n\
                        </entities>\n\
                        <topics>\n\
                                <topic>\n\
                                        <title>Something</title>\n\
                                        <hitcount>0</hitcount>\n\
                                        <sentiment_score>0.6133076</sentiment_score>\n\
                                        <type>concept</type>\n\
                                </topic>\n\
                        </topics>\n\
                    </collection>"

        serializer = XmlSerializer.new()
        coll = serializer.deserialize(source, GetCollectionHandler.new())

        # main
        assert_equal("23498367", coll['config_id']);
        assert_equal("6F9619FF8B86D011B42D00CF4FC964FF", coll['id']);
        assert_equal("PROCESSED", coll['status']);
        # facets
        assert_equal(1, coll['facets'].count);
        facet = coll['facets'][0];
        assert_equal('Something', facet['label']);
        assert_equal(10, facet['count']);
        assert_equal(2, facet['negative_count']);
        assert_equal(1, facet['positive_count']);
        assert_equal(7, facet['neutral_count']);
        assert_equal(1, facet['attributes'].count);
        assert_equal('Attribute', facet['attributes'][0]['label']);
        assert_equal(5, facet['attributes'][0]['count']);
        # themes
        assert_equal(1, coll['themes'].count);
        assert_equal(1, coll['themes'][0]['phrases_count']);
        assert_equal(1, coll['themes'][0]['themes_count']);
        assert_equal(0.0, coll['themes'][0]['sentiment_score']);
        assert_equal('republican moderates', coll['themes'][0]['title']);
        # entities
        assert_equal(1, coll['entities'].count);
        assert_equal('WASHINGTON', coll['entities'][0]['title']);
        assert_equal('named', coll['entities'][0]['type']);
        assert_equal('Place', coll['entities'][0]['entity_type']);
        assert_equal(1, coll['entities'][0]['count']);
        assert_equal(1, coll['entities'][0]['negative_count']);
        assert_equal(1, coll['entities'][0]['positive_count']);
        assert_equal(1, coll['entities'][0]['neutral_count']);
        # topics
        assert_equal(1, coll['topics'].count);
        assert_equal(0, coll['topics'][0]['hitcount']);
        assert_equal('concept', coll['topics'][0]['type']);
        assert_equal(0.6133076, coll['topics'][0]['sentiment_score']);
        assert_equal('Something', coll['topics'][0]['title']);
    end

    def test_JsonDeserializingCollectionAnalyticData
        source =  "{\n\
                    \"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\",\n\
                    \"config_id\":\"23498367\",\n\
                    \"status\":\"PROCESSED\",\n\
                    \"facets\":[\n\
                        {\n\
                            \"label\":\"Something\",\n\
                            \"count\":10,\n\
                            \"negative_count\":2,\n\
                            \"positive_count\":1,\n\
                            \"neutral_count\":7,\n\
                            \"attributes\":[\n\
                                {\n\
                                    \"label\":\"Attribute\",\n\
                                    \"count\":5\n\
                                }\n\
                            ]\n\
                        }\n\
                    ],\n\
                    \"themes\":[\n\
                       {\n\
                            \"phrases_count\":1,\n\
                            \"themes_count\":1,\n\
                            \"sentiment_score\":0.0,\n\
                           \"title\":\"republican moderates\"\n\
                        }\n\
                    ],\n\
                    \"entities\":[\n\
                        {\n\
                            \"type\":\"named\",\n\
                            \"entity_type\":\"Place\",\n\
                            \"title\":\"WASHINGTON\",\n\
                            \"count\":1,\n\
                            \"negative_count\":1,\n\
                            \"neutral_count\":1,\n\
                            \"positive_count\":1\n\
                        }\n\
                    ],\n\
                    \"topics\":[\n\
                        {\n\
                            \"title\":\"Something\",\n\
                            \"type\":\"concept\",\n\
                            \"hitcount\":0,\n\
                            \"sentiment_score\":0.6133076\n\
                        }\n\
                    ]\n\
                }"

        serializer = JsonSerializer.new()
        coll = serializer.deserialize(source, GetCollectionHandler.new())

        # main
        assert_equal("23498367", coll['config_id']);
        assert_equal("6F9619FF8B86D011B42D00CF4FC964FF", coll['id']);
        assert_equal("PROCESSED", coll['status']);
        # facets
        assert_equal(1, coll['facets'].count);
        facet = coll['facets'][0];
        assert_equal('Something', facet['label']);
        assert_equal(10, facet['count']);
        assert_equal(2, facet['negative_count']);
        assert_equal(1, facet['positive_count']);
        assert_equal(7, facet['neutral_count']);
        assert_equal(1, facet['attributes'].count);
        assert_equal('Attribute', facet['attributes'][0]['label']);
        assert_equal(5, facet['attributes'][0]['count']);
        # themes
        assert_equal(1, coll['themes'].count);
        assert_equal(1, coll['themes'][0]['phrases_count']);
        assert_equal(1, coll['themes'][0]['themes_count']);
        assert_equal(0.0, coll['themes'][0]['sentiment_score']);
        assert_equal('republican moderates', coll['themes'][0]['title']);
        # entities
        assert_equal(1, coll['entities'].count);
        assert_equal('WASHINGTON', coll['entities'][0]['title']);
        assert_equal('named', coll['entities'][0]['type']);
        assert_equal('Place', coll['entities'][0]['entity_type']);
        assert_equal(1, coll['entities'][0]['count']);
        assert_equal(1, coll['entities'][0]['negative_count']);
        assert_equal(1, coll['entities'][0]['positive_count']);
        assert_equal(1, coll['entities'][0]['neutral_count']);
        # topics
        assert_equal(1, coll['topics'].count);
        assert_equal(0, coll['topics'][0]['hitcount']);
        assert_equal('concept', coll['topics'][0]['type']);
        assert_equal(0.6133076, coll['topics'][0]['sentiment_score']);
        assert_equal('Something', coll['topics'][0]['title']);
    end

end