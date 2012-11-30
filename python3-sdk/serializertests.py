
import unittest
import semantria
import re

from semantria.mapping import *

class SemantriaSerializerTest(unittest.TestCase):
    def test_XmlSerializingConfiguration(self):
        expectedResult = "<configurations><removed><configuration>45699836</configuration>\n\
        </removed><added><configuration><name>A test configuration</name><language>English</language>\n\
        <is_primary>true</is_primary><one_sentence>true</one_sentence><auto_responding>true</auto_responding>\n\
        <callback>https://anyapi.anydomain.com/processed/docs.json</callback>\n\
        <chars_threshold>80</chars_threshold><collection><facet_atts_limit>5</facet_atts_limit>\n\
        <named_entities_limit>5</named_entities_limit><themes_limit>0</themes_limit>\n\
        <concept_topics_limit>5</concept_topics_limit><query_topics_limit>5</query_topics_limit><facets_limit>15</facets_limit></collection><document>\n\
        <summary_limit>0</summary_limit><phrases_limit>0</phrases_limit><named_entities_limit>5</named_entities_limit>\n\
        <entity_themes_limit>5</entity_themes_limit><user_entities_limit>5</user_entities_limit>\n\
        <concept_topics_limit>5</concept_topics_limit><query_topics_limit>5</query_topics_limit>\n\
        <themes_limit>0</themes_limit></document></configuration><configuration><is_primary>true</is_primary>\n\
        <one_sentence>false</one_sentence><name>Cloned configuration</name><template>123</template></configuration>\n\
        </added></configurations>";
                        
        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        config = {}
        config["name"] = "A test configuration";
        config["one_sentence"] = True;
        config["is_primary"] = True;
        config["auto_responding"] = True;
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
        clonedConfig["one_sentence"] = False;
        clonedConfig["is_primary"] = True;

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append(config)
        proxy["removed"].append("45699836")
        proxy["cloned"].append(clonedConfig)
        
        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"configurations", "added":"configuration", "removed":"configuration"}))

    def test_JsonSerializingConfiguration(self):
        expectedResult = "{\"removed\": [\"45699836\"], \"added\": [{\"name\": \"A test configuration\", \"language\": \"English\", \"is_primary\": true, \"one_sentence\": true, \"auto_responding\": true, \"callback\": \"https://anyapi.anydomain.com/processed/docs.json\", \"chars_threshold\": 80, \"collection\": {\"facet_atts_limit\": 5, \"named_entities_limit\": 5, \"themes_limit\": 0, \"concept_topics_limit\": 5, \"query_topics_limit\": 5, \"facets_limit\": 15}, \"document\": {\"summary_limit\": 0, \"phrases_limit\": 0, \"named_entities_limit\": 5, \"entity_themes_limit\": 5, \"user_entities_limit\": 5, \"concept_topics_limit\": 5, \"query_topics_limit\": 5, \"themes_limit\": 0}}, {\"is_primary\": true, \"one_sentence\": false, \"name\": \"Cloned configuration\", \"template\": \"123\"}]}";

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        config = {}
        config["name"] = "A test configuration";
        config["one_sentence"] = True;
        config["is_primary"] = True;
        config["auto_responding"] = True;
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
        clonedConfig["one_sentence"] = False;
        clonedConfig["is_primary"] = True;

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append(config)
        proxy["removed"].append("45699836")
        proxy["cloned"].append(clonedConfig)

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"configurations", "added":"configuration", "removed":"configuration"}))


    def test_XmlDeserializingConfiguration(self):
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
			<configurations>\n\
				<configuration>\n\
					<auto_responding>false</auto_responding>\n\
					<callback>https://anyapi.anydomain.com/processed/docs.json</callback>\n\
					<chars_threshold>80</chars_threshold>\n\
					<collection>\n\
						<concept_topics_limit>5</concept_topics_limit>\n\
						<facet_atts_limit>20</facet_atts_limit>\n\
						<facets_limit>15</facets_limit>\n\
						<named_entities_limit>5</named_entities_limit>\n\
						<query_topics_limit>5</query_topics_limit>\n\
						<themes_limit>0</themes_limit>\n\
					</collection>\n\
					<document>\n\
						<concept_topics_limit>7</concept_topics_limit>\n\
						<entity_themes_limit>5</entity_themes_limit>\n\
						<named_entities_limit>100</named_entities_limit>\n\
						<phrases_limit>1</phrases_limit>\n\
						<query_topics_limit>25</query_topics_limit>\n\
						<summary_limit>0</summary_limit>\n\
						<themes_limit>10</themes_limit>\n\
						<user_entities_limit>5</user_entities_limit>\n\
					</document>\n\
					<config_id>66f2b9c33a63681826926de7965dc996</config_id>\n\
					<is_primary>false</is_primary>\n\
					<one_sentence>true</one_sentence>\n\
					<language>English</language>\n\
					<name>default</name>\n\
				</configuration>\n\
			</configurations>"

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        configs = serializer.deserialize(source.encode('utf-8'), GetConfigurationsHandler())
        config = configs[0]

        self.assertEqual("default", config['name']);
        self.assertTrue(config['one_sentence']);
        self.assertFalse(config['is_primary']);
        self.assertFalse(config['auto_responding']);
        self.assertEqual("English", config['language']);
        self.assertEqual(80, config['chars_threshold']);
        self.assertEqual("https://anyapi.anydomain.com/processed/docs.json", config['callback']);

        self.assertEqual(7, config['document']['concept_topics_limit']);
        self.assertEqual(25, config['document']['query_topics_limit']);
        self.assertEqual(100, config['document']['named_entities_limit']);
        self.assertEqual(5, config['document']['user_entities_limit']);
        self.assertEqual(10, config['document']['themes_limit']);
        self.assertEqual(5, config['document']['entity_themes_limit']);
        self.assertEqual(1, config['document']['phrases_limit']);
        self.assertEqual(0, config['document']['summary_limit']);

        self.assertEqual(5, config['collection']['concept_topics_limit']);
        self.assertEqual(5, config['collection']['query_topics_limit']);
        self.assertEqual(5, config['collection']['named_entities_limit']);
        self.assertEqual(0, config['collection']['themes_limit']);
        self.assertEqual(15, config['collection']['facets_limit']);
        self.assertEqual(20, config['collection']['facet_atts_limit']);

    def test_JsonDeserializingConfiguration(self):
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

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        configs = serializer.deserialize(source.encode('utf-8'), GetConfigurationsHandler())
        config = configs[0]

        self.assertEqual("default", config['name']);
        self.assertTrue(config['one_sentence']);
        self.assertFalse(config['is_primary']);
        self.assertFalse(config['auto_responding']);
        self.assertEqual("English", config['language']);
        self.assertEqual(80, config['chars_threshold']);
        self.assertEqual("https://anyapi.anydomain.com/processed/docs.json", config['callback']);

        self.assertEqual(7, config['document']['concept_topics_limit']);
        self.assertEqual(25, config['document']['query_topics_limit']);
        self.assertEqual(100, config['document']['named_entities_limit']);
        self.assertEqual(5, config['document']['user_entities_limit']);
        self.assertEqual(10, config['document']['themes_limit']);
        self.assertEqual(5, config['document']['entity_themes_limit']);
        self.assertEqual(1, config['document']['phrases_limit']);
        self.assertEqual(0, config['document']['summary_limit']);

        self.assertEqual(5, config['collection']['concept_topics_limit']);
        self.assertEqual(5, config['collection']['query_topics_limit']);
        self.assertEqual(5, config['collection']['named_entities_limit']);
        self.assertEqual(0, config['collection']['themes_limit']);
        self.assertEqual(15, config['collection']['facets_limit']);
        self.assertEqual(20, config['collection']['facet_atts_limit']);

    def test_XmlSerializingBlacklist(self):
        expectedResult = "<blacklist>\n\
				<removed>\n\
					<item>Removed Filter 1</item>\n\
				</removed>\n\
                                <added>\n\
					<item>Added Filter 1</item>\n\
				</added>\n\
			</blacklist>";

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append("Added Filter 1")
        proxy["removed"].append("Removed Filter 1")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"blacklist", "added":"item", "removed":"item"}))

    def test_JsonSerializingBlacklist(self):
        expectedResult = "{\n\
                            \"removed\": [\"http://www\\\\..*\\\\.com\"], \"added\": [\".*@.*com\", \".*@com\\\\.net\"]\n\
			}";

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append(".*@.*com")
        proxy["added"].append(".*@com\\.net")
        proxy["removed"].append("http://www\\..*\\.com")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"blacklist", "added":"item", "removed":"item"}))

    def test_XmlDeserializingBlacklist(self):
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <blacklist>\n\
                    <item>Filter 1</item>\n\
                    <item>Filter 2</item>\n\
                </blacklist>"

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        blacklists = serializer.deserialize(source.encode('utf-8'), GetBlacklistHandler())

        self.assertEqual(2, len(blacklists));
        self.assertEqual("Filter 1", blacklists[0]);
        self.assertEqual("Filter 2", blacklists[1]);

    def test_JsonDeserializingBlacklist(self):
        source = "[\n\
                    \".*@.*com\",\n\
                    \".*@com\\\\.net\"\n\
                 ]"

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        blacklists = serializer.deserialize(source.encode('utf-8'), GetBlacklistHandler())

        self.assertEqual(2, len(blacklists));
        self.assertEqual(".*@.*com", blacklists[0]);
        self.assertEqual(".*@com\\.net", blacklists[1]);

    def test_XmlSerializingQueries(self):
        expectedResult = "<queries>\n\
                            <removed>\n\
                                <query>Query 2</query>\n\
                            </removed>\n\
                            <added>\n\
                                <query><query>Something AND something</query><name>Query 1</name></query>\n\
                            </added>\n\
			</queries>"

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append({"name":"Query 1", "query":"Something AND something"})
        proxy["removed"].append("Query 2")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"queries", "added":"query", "removed":"query"}))

    def test_JsonSerializingQueries(self):
        expectedResult = "{\"removed\": [\"Features\"], \"added\": [{\"query\": \"Amazon AND EC2 AND Cloud\", \"name\": \"Feature: Cloud service\"}]}";

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append({"name":"Feature: Cloud service", "query":"Amazon AND EC2 AND Cloud"})
        proxy["removed"].append("Features")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"queries", "added":"query", "removed":"query"}))

    def test_XmlDeserializingQueries(self):
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <queries>\n\
                    <query><name>Query 1</name><query>Something AND something</query></query>\n\
                    <query><name>Query 2</name><query>Something AND something</query></query>\n\
                </queries>"

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        queries = serializer.deserialize(source.encode('utf-8'), GetQueriesHandler())

        self.assertEqual(2, len(queries));
        self.assertEqual("Query 1", queries[0]["name"]);
        self.assertEqual("Something AND something", queries[0]["query"]);
        self.assertEqual("Query 2", queries[1]["name"]);
        self.assertEqual("Something AND something", queries[1]["query"]);

    def test_JsonDeserializingQueries(self):
        source = "[\n\
                    {\n\
                        \"name\":\"Feature: Cloud service\",\n\
                        \"query\":\"Amazon AND EC2 AND Cloud\"\n\
                    }\n\
                ]";

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        queries = serializer.deserialize(source.encode('utf-8'), GetQueriesHandler())

        self.assertEqual(1, len(queries));
        self.assertEqual("Feature: Cloud service", queries[0]["name"]);
        self.assertEqual("Amazon AND EC2 AND Cloud", queries[0]["query"]);

    def test_XmlSerializingEntities(self):
        expectedResult = "<entities>\n\
                            <removed>\n\
                                <entity>name 2</entity>\n\
                            </removed>\n\
                            <added>\n\
                                <entity>\n\
                                    <type>type 1</type>\n\
                                    <name>name 1</name>\n\
                                </entity>\n\
                            </added>\n\
			</entities>";

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append({"name":"name 1", "type":"type 1"})
        proxy["removed"].append("name 2")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"entities", "added":"entity", "removed":"entity"}))

    def test_JsonSerializingEntities(self):
        expectedResult = "{\"removed\": [\"table\"], \"added\": [{\"type\": \"furniture\", \"name\": \"chair\"}]}";

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append({"name":"chair", "type":"furniture"})
        proxy["removed"].append("table")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"entities", "added":"entity", "removed":"entity"}))

    def test_XmlDeserializingEntities(self):
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <entities>\n\
                    <entity>\n\
                        <name>chair</name>\n\
                        <type>furniture</type> \n\
                    </entity>\n\
                </entities>"

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        entities = serializer.deserialize(source.encode('utf-8'), GetEntitiesHandler())

        self.assertEqual(1, len(entities));
        self.assertEqual("chair", entities[0]["name"]);
        self.assertEqual("furniture", entities[0]["type"]);

    def test_JsonDeserializingEntities(self):
        source = "[\n\
                    {\n\
                        \"name\":\"chair\",\n\
                        \"type\":\"furniture\"\n\
                    }\n\
                ]"

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        entities = serializer.deserialize(source.encode('utf-8'), GetEntitiesHandler())

        self.assertEqual(1, len(entities));
        self.assertEqual("chair", entities[0]["name"]);
        self.assertEqual("furniture", entities[0]["type"]);

    def test_XmlSerializingCategories(self):
        expectedResult = "<categories>\n\
                            <removed>\n\
                                <category>Removed Category 1</category>\n\
                            </removed>\n\
                            <added>\n\
                                <category>\n\
                                    <samples>\n\
                                        <sample>Entity 1</sample>\n\
                                        <sample>Entity 2</sample>\n\
                                        <sample>Entity 3</sample>\n\
                                    </samples>\n\
                                    <name>Added Category 1</name>\n\
                                    <weight>0.2</weight>\n\
                                </category>\n\
                            </added>\n\
                        </categories>";

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append({"name":"Added Category 1", "weight":0.2, "samples":["Entity 1", "Entity 2", "Entity 3"]})
        proxy["removed"].append("Removed Category 1")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"categories", "added":"category", "removed":"category", "samples":"sample"}))

    def test_JsonSerializingCategories(self):
        expectedResult = "{\"removed\": [\"Features\"], \"added\": [{\"samples\": [], \"name\": \"Feature: Cloud service\", \"weight\": 0.0}]}";

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append({"name":"Feature: Cloud service", "weight":0.0, "samples":[]})
        proxy["removed"].append("Features")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"categories", "added":"category", "removed":"category", "samples":"sample"}))

    def test_XmlDeserializingCategories(self):
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

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        categories = serializer.deserialize(source.encode('utf-8'), GetCategoriesHandler())

        self.assertEqual(1, len(categories));
        self.assertEqual("Feature: Cloud service", categories[0]["name"]);
        self.assertEqual(0.75, categories[0]["weight"]);
        self.assertEqual(2, len(categories[0]["samples"]));
        self.assertEqual("Amazon", categories[0]["samples"][0]);
        self.assertEqual("EC2", categories[0]["samples"][1]);

    def test_JsonDeserializingCategories(self):
        source = "[\n\
                    {\n\
                        \"name\":\"Feature: Cloud service\",\n\
                        \"weight\":0.75,\n\
                        \"samples\":[\"Amazon\",\"EC2\"]\n\
                    }\n\
                ]"

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        categories = serializer.deserialize(source.encode('utf-8'), GetCategoriesHandler())

        self.assertEqual(1, len(categories));
        self.assertEqual("Feature: Cloud service", categories[0]["name"]);
        self.assertEqual(0.75, categories[0]["weight"]);
        self.assertEqual(2, len(categories[0]["samples"]));
        self.assertEqual("Amazon", categories[0]["samples"][0]);
        self.assertEqual("EC2", categories[0]["samples"][1]);

    def test_XmlSerializingSentimentPhrases(self):
        expectedResult = "<phrases>\n\
                            <removed>\n\
                                <phrase>name 2</phrase>\n\
                            </removed>\n\
                            <added>\n\
                                <phrase>\n\
                                    <weight>0.3</weight>\n\
                                    <title>name 1</title>\n\
                                </phrase>\n\
                            </added>\n\
			</phrases>"

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append({"title":"name 1", "weight":0.3})
        proxy["removed"].append("name 2")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"phrases", "added":"phrase", "removed":"phrase"}))

    def test_JsonSerializingSentimentPhrases(self):
        expectedResult = "{\"removed\": [\"Features\"], \"added\": [{\"weight\": 0.0, \"title\": \"Feature: Cloud service\"}]}"

        expectedResult = re.sub("\s{2,}" , "", expectedResult)

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)

        proxy = session.createUpdateProxy()
        proxy["added"].append({"title":"Feature: Cloud service", "weight":0.0})
        proxy["removed"].append("Features")

        self.assertEqual(expectedResult.encode('utf-8'), serializer.serialize(proxy, {"root":"phrases", "added":"phrase", "removed":"phrase"}))

    def test_XmlDeserializingSentimentPhrases(self):
        source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\
                <phrases>\n\
                    <phrase>\n\
                        <title>chair</title>\n\
                        <weight>0.3</weight>\n\
                    </phrase>\n\
                </phrases>"

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        phrases = serializer.deserialize(source.encode('utf-8'), GetSentimentPhrasesHandler())

        self.assertEqual(1, len(phrases));
        self.assertEqual("chair", phrases[0]["title"]);
        self.assertEqual(0.3, phrases[0]["weight"]);

    def test_JsonDeserializingSentimentPhrases(self):
        source = "[\n\
                    {\n\
                        \"title\":\"chair\",\n\
                        \"weight\":0.75\n\
                    }\n\
                ]"

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        phrases = serializer.deserialize(source.encode('utf-8'), GetSentimentPhrasesHandler())

        self.assertEqual(1, len(phrases));
        self.assertEqual("chair", phrases[0]["title"]);
        self.assertEqual(0.75, phrases[0]["weight"]);

    def test_XmlDeserializingAnalyticServiceStatus(self):
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

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        status = serializer.deserialize(source.encode('utf-8'), GetStatusHandler())

        self.assertEqual("online", status["service_status"]);
        self.assertEqual("2.0", status["api_version"]);
        self.assertEqual("1.0.2.63", status["service_version"]);
        self.assertEqual("UTF-8", status["supported_encoding"]);
        self.assertEqual("gzip", status["supported_compression"]);
        self.assertEqual(2, len(status["supported_languages"]));
        self.assertEqual("English", status["supported_languages"][0]);
        self.assertEqual("French", status["supported_languages"][1]);

    def test_JsonDeserializingAnalyticServiceStatus(self):
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

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        status = serializer.deserialize(source.encode('utf-8'), GetStatusHandler())

        self.assertEqual("online", status["service_status"]);
        self.assertEqual("2.0", status["api_version"]);
        self.assertEqual("1.0.2.63", status["service_version"]);
        self.assertEqual("UTF-8", status["supported_encoding"]);
        self.assertEqual("gzip", status["supported_compression"]);
        self.assertEqual(2, len(status["supported_languages"]));
        self.assertEqual("English", status["supported_languages"][0]);
        self.assertEqual("French", status["supported_languages"][1]);

    def test_XmlDeserializingSubscription(self):
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

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        subscription = serializer.deserialize(source.encode('utf-8'), GetSubscriptionHandler())

        self.assertEqual("name", subscription["name"]);
        self.assertEqual("active", subscription["status"]);
        self.assertEqual("normal", subscription["priority"]);
        self.assertEqual(1293883200, subscription["expiration_date"]);
        self.assertEqual(87, subscription["calls_balance"]);
        self.assertEqual(100, subscription["calls_limit"]);
        self.assertEqual(60, subscription["calls_limit_interval"]);
        self.assertEqual(49897, subscription["docs_balance"]);
        self.assertEqual(0, subscription["docs_limit"]);
        self.assertEqual(0, subscription["docs_limit_interval"]);
        self.assertEqual(10, subscription["configurations_limit"]);
        self.assertEqual(100, subscription["blacklist_limit"]);
        self.assertEqual(100, subscription["categories_limit"]);
        self.assertEqual(100, subscription["queries_limit"]);
        self.assertEqual(1000, subscription["entities_limit"]);
        self.assertEqual(1000, subscription["sentiment_limit"]);
        self.assertEqual(8192, subscription["characters_limit"]);
        self.assertEqual(1, subscription["batch_limit"]);
        self.assertEqual(10, subscription["collection_limit"]);
        self.assertEqual(2, subscription["auto_response_limit"]);
        self.assertEqual(100, subscription["processed_batch_limit"]);
        self.assertEqual(100, subscription["callback_batch_limit"]);
        self.assertEqual("type limit", subscription["limit_type"]);


    def test_JsonDeserializingSubscription(self):
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

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        subscription = serializer.deserialize(source.encode('utf-8'), GetSubscriptionHandler())

        self.assertEqual("name", subscription["name"]);
        self.assertEqual("active", subscription["status"]);
        self.assertEqual("normal", subscription["priority"]);
        self.assertEqual(1293883200, subscription["expiration_date"]);
        self.assertEqual(87, subscription["calls_balance"]);
        self.assertEqual(100, subscription["calls_limit"]);
        self.assertEqual(60, subscription["calls_limit_interval"]);
        self.assertEqual(49897, subscription["docs_balance"]);
        self.assertEqual(0, subscription["docs_limit"]);
        self.assertEqual(0, subscription["docs_limit_interval"]);
        self.assertEqual(10, subscription["configurations_limit"]);
        self.assertEqual(100, subscription["blacklist_limit"]);
        self.assertEqual(100, subscription["categories_limit"]);
        self.assertEqual(100, subscription["queries_limit"]);
        self.assertEqual(1000, subscription["entities_limit"]);
        self.assertEqual(1000, subscription["sentiment_limit"]);
        self.assertEqual(8192, subscription["characters_limit"]);
        self.assertEqual(10, subscription["batch_limit"]);
        self.assertEqual(10, subscription["collection_limit"]);
        self.assertEqual(2, subscription["auto_response_limit"]);
        self.assertEqual(100, subscription["processed_batch_limit"]);
        self.assertEqual(100, subscription["callback_batch_limit"]);
        self.assertEqual("type limit", subscription["limit_type"]);

    def test_XmlDeserializingDocumentAnalyticData(self):
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

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        doc = serializer.deserialize(source.encode('utf-8'), GetDocumentHandler())

        # main
        self.assertEqual("23498367", doc['config_id']);
        self.assertEqual("6F9619FF8B86D011B42D00CF4FC964FF", doc['id']);
        self.assertEqual("PROCESSED", doc['status']);
        self.assertEqual(0.2398756, doc['sentiment_score']);
        self.assertEqual("Summary of the document's text.", doc['summary']);
        # themes
        self.assertEqual(1, len(doc['themes']));
        self.assertEqual(1, doc['themes'][0]['evidence']);
        self.assertTrue(doc['themes'][0]['is_about']);
        self.assertEqual(0, doc['themes'][0]['strength_score']);
        self.assertEqual(0, doc['themes'][0]['sentiment_score']);
        self.assertEqual('republican moderates', doc['themes'][0]['title']);
        # entities
        self.assertEqual(1, len(doc['entities']));
        self.assertEqual(0, doc['entities'][0]['evidence']);
        self.assertTrue(doc['entities'][0]['is_about']);
        self.assertTrue(doc['entities'][0]['confident']);
        self.assertEqual('named', doc['entities'][0]['type']);
        self.assertEqual(1.0542796, doc['entities'][0]['sentiment_score']);
        self.assertEqual('WASHINGTON', doc['entities'][0]['title']);
        # entity themes
        self.assertEqual(1, len(doc['entities'][0]['themes']));
        theme = doc['entities'][0]['themes'][0];
        self.assertEqual(1, theme['evidence']);
        self.assertTrue(theme['is_about']);
        self.assertEqual(0, theme['strength_score']);
        self.assertEqual(0, theme['sentiment_score']);
        self.assertEqual('republican moderates', theme['title']);
        # topics
        self.assertEqual(1, len(doc['topics']));
        self.assertEqual(0, doc['topics'][0]['hitcount']);
        self.assertEqual('concept', doc['topics'][0]['type']);
        self.assertEqual(0.6133076, doc['topics'][0]['strength_score']);
        self.assertEqual(0.6133076, doc['topics'][0]['sentiment_score']);
        self.assertEqual('Something', doc['topics'][0]['title']);
        # phrases
        self.assertEqual(1, len(doc['phrases']));
        self.assertEqual('not', doc['phrases'][0]['negating_phrase']);
        self.assertTrue(doc['phrases'][0]['is_negated']);
        self.assertEqual(0.6133076, doc['phrases'][0]['sentiment_score']);
        self.assertEqual('Something', doc['phrases'][0]['title']);

    def test_JsonDeserializingDocumentAnalyticData(self):
        source = "[{\n\
                    \"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\",\n\
                    \"config_id\":\"23498367\",\n\
                    \"status\":\"PROCESSED\",\n\
                    \"sentiment_score\":0.2398756,\n\
                    \"summary\":\"Summary of the doc?ument's text.\",\n\
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

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        docs = serializer.deserialize(source.encode('utf-8'), GetDocumentHandler())
        self.assertEqual(1, len(docs));
        doc = docs[0]
        
        # main
        self.assertEqual("23498367", doc['config_id']);
        self.assertEqual("6F9619FF8B86D011B42D00CF4FC964FF", doc['id']);
        self.assertEqual("PROCESSED", doc['status']);
        self.assertEqual(0.2398756, doc['sentiment_score']);
        self.assertEqual("Summary of the doc?ument's text.", doc['summary']);
        # themes
        self.assertEqual(1, len(doc['themes']));
        self.assertEqual(1, doc['themes'][0]['evidence']);
        self.assertTrue(doc['themes'][0]['is_about']);
        self.assertEqual(0, doc['themes'][0]['strength_score']);
        self.assertEqual(0, doc['themes'][0]['sentiment_score']);
        self.assertEqual('republican moderates', doc['themes'][0]['title']);
        # entities
        self.assertEqual(1, len(doc['entities']));
        self.assertEqual(0, doc['entities'][0]['evidence']);
        self.assertTrue(doc['entities'][0]['is_about']);
        self.assertTrue(doc['entities'][0]['confident']);
        self.assertEqual('named', doc['entities'][0]['type']);
        self.assertEqual(1.0542796, doc['entities'][0]['sentiment_score']);
        self.assertEqual('WASHINGTON', doc['entities'][0]['title']);
        # entity themes
        self.assertEqual(1, len(doc['entities'][0]['themes']));
        theme = doc['entities'][0]['themes'][0];
        self.assertEqual(1, theme['evidence']);
        self.assertTrue(theme['is_about']);
        self.assertEqual(0, theme['strength_score']);
        self.assertEqual(0, theme['sentiment_score']);
        self.assertEqual('republican moderates', theme['title']);
        # topics
        self.assertEqual(1, len(doc['topics']));
        self.assertEqual(0, doc['topics'][0]['hitcount']);
        self.assertEqual('concept', doc['topics'][0]['type']);
        self.assertEqual(0.6133076, doc['topics'][0]['strength_score']);
        self.assertEqual(0.6133076, doc['topics'][0]['sentiment_score']);
        self.assertEqual('Something', doc['topics'][0]['title']);
        # phrases
        self.assertEqual(1, len(doc['phrases']));
        self.assertEqual('not', doc['phrases'][0]['negating_phrase']);
        self.assertTrue(doc['phrases'][0]['is_negated']);
        self.assertEqual(0.6133076, doc['phrases'][0]['sentiment_score']);
        self.assertEqual('Something', doc['phrases'][0]['title']);

    def test_XmlDeserializingCollectionAnalyticData(self):
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

        serializer = semantria.XmlSerializer()
        session = semantria.Session("123", "123", serializer)
        coll = serializer.deserialize(source.encode('utf-8'), GetCollectionHandler())

        # main
        self.assertEqual("23498367", coll['config_id']);
        self.assertEqual("6F9619FF8B86D011B42D00CF4FC964FF", coll['id']);
        self.assertEqual("PROCESSED", coll['status']);
        # facets
        self.assertEqual(1, len(coll['facets']));
        facet = coll['facets'][0];
        self.assertEqual('Something', facet['label']);
        self.assertEqual(10, facet['count']);
        self.assertEqual(2, facet['negative_count']);
        self.assertEqual(1, facet['positive_count']);
        self.assertEqual(7, facet['neutral_count']);
        self.assertEqual(1, len(facet['attributes']));
        self.assertEqual('Attribute', facet['attributes'][0]['label']);
        self.assertEqual(5, facet['attributes'][0]['count']);
        # themes
        self.assertEqual(1, len(coll['themes']));
        self.assertEqual(1, coll['themes'][0]['phrases_count']);
        self.assertEqual(1, coll['themes'][0]['themes_count']);
        self.assertEqual(0.0, coll['themes'][0]['sentiment_score']);
        self.assertEqual('republican moderates', coll['themes'][0]['title']);
        # entities
        self.assertEqual(1, len(coll['entities']));
        self.assertEqual('WASHINGTON', coll['entities'][0]['title']);
        self.assertEqual('named', coll['entities'][0]['type']);
        self.assertEqual('Place', coll['entities'][0]['entity_type']);
        self.assertEqual(1, coll['entities'][0]['count']);
        self.assertEqual(1, coll['entities'][0]['negative_count']);
        self.assertEqual(1, coll['entities'][0]['positive_count']);
        self.assertEqual(1, coll['entities'][0]['neutral_count']);
        # topics
        self.assertEqual(1, len(coll['topics']));
        self.assertEqual(0, coll['topics'][0]['hitcount']);
        self.assertEqual('concept', coll['topics'][0]['type']);
        self.assertEqual(0.6133076, coll['topics'][0]['sentiment_score']);
        self.assertEqual('Something', coll['topics'][0]['title']);

    def test_JsonDeserializingCollectionAnalyticData(self):
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

        serializer = semantria.JsonSerializer()
        session = semantria.Session("123", "123", serializer)
        coll = serializer.deserialize(source.encode('utf-8'), GetCollectionHandler())

        # main
        self.assertEqual("23498367", coll['config_id']);
        self.assertEqual("6F9619FF8B86D011B42D00CF4FC964FF", coll['id']);
        self.assertEqual("PROCESSED", coll['status']);
        # facets
        self.assertEqual(1, len(coll['facets']));
        facet = coll['facets'][0];
        self.assertEqual('Something', facet['label']);
        self.assertEqual(10, facet['count']);
        self.assertEqual(2, facet['negative_count']);
        self.assertEqual(1, facet['positive_count']);
        self.assertEqual(7, facet['neutral_count']);
        self.assertEqual(1, len(facet['attributes']));
        self.assertEqual('Attribute', facet['attributes'][0]['label']);
        self.assertEqual(5, facet['attributes'][0]['count']);
        # themes
        self.assertEqual(1, len(coll['themes']));
        self.assertEqual(1, coll['themes'][0]['phrases_count']);
        self.assertEqual(1, coll['themes'][0]['themes_count']);
        self.assertEqual(0.0, coll['themes'][0]['sentiment_score']);
        self.assertEqual('republican moderates', coll['themes'][0]['title']);
        # entities
        self.assertEqual(1, len(coll['entities']));
        self.assertEqual('WASHINGTON', coll['entities'][0]['title']);
        self.assertEqual('named', coll['entities'][0]['type']);
        self.assertEqual('Place', coll['entities'][0]['entity_type']);
        self.assertEqual(1, coll['entities'][0]['count']);
        self.assertEqual(1, coll['entities'][0]['negative_count']);
        self.assertEqual(1, coll['entities'][0]['positive_count']);
        self.assertEqual(1, coll['entities'][0]['neutral_count']);
        # topics
        self.assertEqual(1, len(coll['topics']));
        self.assertEqual(0, coll['topics'][0]['hitcount']);
        self.assertEqual('concept', coll['topics'][0]['type']);
        self.assertEqual(0.6133076, coll['topics'][0]['sentiment_score']);
        self.assertEqual('Something', coll['topics'][0]['title']);

if __name__ == '__main__':
    suite = unittest.TestSuite()
    suite.addTest(SemantriaSessionTest(test_XmlSerializingConfiguration))
    suite.addTest(SemantriaSessionTest(test_JsonSerializingConfiguration))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingConfiguration))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingConfiguration))
    suite.addTest(SemantriaSessionTest(test_XmlSerializingBlacklist))
    suite.addTest(SemantriaSessionTest(test_JsonSerializingBlacklist))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingBlacklist))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingBlacklist))
    suite.addTest(SemantriaSessionTest(test_XmlSerializingQueries))
    suite.addTest(SemantriaSessionTest(test_JsonSerializingQueries))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingQueries))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingQueries))
    suite.addTest(SemantriaSessionTest(test_XmlSerializingEntities))
    suite.addTest(SemantriaSessionTest(test_JsonSerializingEntities))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingEntities))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingEntities))
    suite.addTest(SemantriaSessionTest(test_XmlSerializingCategories))
    suite.addTest(SemantriaSessionTest(test_JsonSerializingCategories))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingCategories))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingCategories))
    suite.addTest(SemantriaSessionTest(test_XmlSerializingSentimentPhrases))
    suite.addTest(SemantriaSessionTest(test_JsonSerializingSentimentPhrases))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingSentimentPhrases))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingSentimentPhrases))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingAnalyticServiceStatus))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingAnalyticServiceStatus))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingSubscription))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingSubscription))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingDocumentAnalyticData))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingDocumentAnalyticData))
    suite.addTest(SemantriaSessionTest(test_XmlDeserializingCollectionAnalyticData))
    suite.addTest(SemantriaSessionTest(test_JsonDeserializingCollectionAnalyticData))

    
    unittest.TextTestRunner(verbosity=2).run(suite)
