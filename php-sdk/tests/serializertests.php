<?php

require_once 'PHPUnit/Autoload.php';

require_once('Semantria/xmlhandlers.php');
//require_once('Semantria/XmlHandler/Status.php'); // todo: see PSR-0 autoloader spl_autoloader

class SerializerTest extends PHPUnit_Framework_TestCase
{
    /**
     * Set up
     *
     * @access public
     * @return void
     */
    function setUp() {
        $serializer = new Semantria_JsonSerializer();
        $this->session = new Semantria_Session('123', '123', $serializer);
    }

    function testXmlSerializingConfiguration() {
        $expectedResult =
            '<configurations>' .
           '<added>' .
              '<configuration>' .
                 '<name>A test configuration</name>' .
                 '<is_primary>true</is_primary>' .
                 '<one_sentence>true</one_sentence>' .
                 '<auto_responding>true</auto_responding>' .
                 '<language>English</language>' .
                 '<chars_threshold>80</chars_threshold>' .
                 '<callback>https://anyapi.anydomain.com/processed/docs.json</callback>' .
                 '<document>' .
                    '<concept_topics_limit>5</concept_topics_limit>' .
                    '<query_topics_limit>5</query_topics_limit>' .
                    '<named_entities_limit>5</named_entities_limit>' .
                    '<user_entities_limit>5</user_entities_limit>' .
                    '<themes_limit>0</themes_limit>' .
                    '<entity_themes_limit>5</entity_themes_limit>' .
                    '<phrases_limit>0</phrases_limit>' .
                    '<summary_limit>0</summary_limit>' .
                 '</document>' .
                 '<collection>' .
                    '<facets_limit>15</facets_limit>' .
                    '<facet_atts_limit>5</facet_atts_limit>' .
                    '<concept_topics_limit>5</concept_topics_limit>' .
                    '<query_topics_limit>5</query_topics_limit>' .
                    '<named_entities_limit>5</named_entities_limit>' .
                    '<themes_limit>0</themes_limit>' .
                 '</collection>' .
              '</configuration>' .
              '<configuration>' .
                 '<name>Cloned configuration</name>' .
                 '<is_primary>true</is_primary>' .
                 '<one_sentence>false</one_sentence>' .
                 '<template>123</template>' .
              '</configuration>' .
           '</added>' .
           '<removed>' .
              '<configuration>45699836</configuration>' .
           '</removed>' .
        '</configurations>';

        $config["name"] = "A test configuration";
        $config["is_primary"] = true;
        $config["one_sentence"] = true;
        $config["auto_responding"] = true;
        $config["language"] = "English";
        $config["chars_threshold"] = 80;
        $config["callback"] = "https://anyapi.anydomain.com/processed/docs.json";

        $config["document"]["concept_topics_limit"] = 5;
        $config["document"]["query_topics_limit"] = 5;
        $config["document"]["named_entities_limit"] = 5;
        $config["document"]["user_entities_limit"] = 5;
        $config["document"]["themes_limit"] = 0;
        $config["document"]["entity_themes_limit"] = 5;
        $config["document"]["phrases_limit"] = 0;
        $config["document"]["summary_limit"] = 0;

        $config["collection"]["facets_limit"] = 15;
        $config["collection"]["facet_atts_limit"] = 5;
        $config["collection"]["concept_topics_limit"] = 5;
        $config["collection"]["query_topics_limit"] = 5;
        $config["collection"]["named_entities_limit"] = 5;
        $config["collection"]["themes_limit"] = 0;

        $clonedConfig["config_id"] = '123';
        $clonedConfig["name"] = "Cloned configuration";
        $clonedConfig["is_primary"] = true;
        $clonedConfig["one_sentence"] = false;

        $serializer = new Semantria_XmlSerializer();
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], $config);
        array_push($proxy["removed"], "45699836");
        array_push($proxy["cloned"], $clonedConfig);

        $this->assertEquals($expectedResult, $serializer->serialize($proxy, array("root"=>"configurations", "added"=>"configuration", "removed"=>"configuration")));
    }

    function testJsonSerializingConfiguration() {
        $expectedResult =
            '{' .
           '"added":[' .
                '{' .
                    '"name":"A test configuration",' .
                    '"is_primary":true,' .
                    '"one_sentence":true,' .
                    '"auto_responding":true,' .
                    '"language":"English",' .
                    '"chars_threshold":80,' .
                    '"callback":"https:\/\/anyapi.anydomain.com\/processed\/docs.json",' .
                    '"document":{' .
                        '"concept_topics_limit":5,' .
                        '"query_topics_limit":5,' .
                        '"named_entities_limit":5,' .
                        '"user_entities_limit":5,' .
                        '"themes_limit":0,' .
                        '"entity_themes_limit":5,' .
                        '"phrases_limit":0,' .
                        '"summary_limit":0' .
                    '},' .
                    '"collection":{' .
                        '"facets_limit":15,' .
                        '"facet_atts_limit":5,' .
                        '"concept_topics_limit":5,' .
                        '"query_topics_limit":5,' .
                        '"named_entities_limit":5,' .
                        '"themes_limit":0' .
                    '}' .
                '},' .
                '{' .
                    '"name":"Cloned configuration",' .
                    '"is_primary":true,' .
                    '"one_sentence":false,' .
                    '"template":"123"' .
                '}' .
            '],' .
           '"removed":["45699836"]' .
        '}';

        $config["name"] = "A test configuration";
        $config["is_primary"] = true;
        $config["one_sentence"] = true;
        $config["auto_responding"] = true;
        $config["language"] = "English";
        $config["chars_threshold"] = 80;
        $config["callback"] = "https://anyapi.anydomain.com/processed/docs.json";

        $config["document"]["concept_topics_limit"] = 5;
        $config["document"]["query_topics_limit"] = 5;
        $config["document"]["named_entities_limit"] = 5;
        $config["document"]["user_entities_limit"] = 5;
        $config["document"]["themes_limit"] = 0;
        $config["document"]["entity_themes_limit"] = 5;
        $config["document"]["phrases_limit"] = 0;
        $config["document"]["summary_limit"] = 0;

        $config["collection"]["facets_limit"] = 15;
        $config["collection"]["facet_atts_limit"] = 5;
        $config["collection"]["concept_topics_limit"] = 5;
        $config["collection"]["query_topics_limit"] = 5;
        $config["collection"]["named_entities_limit"] = 5;
        $config["collection"]["themes_limit"] = 0;

        $clonedConfig["config_id"] = '123';
        $clonedConfig["name"] = "Cloned configuration";
        $clonedConfig["is_primary"] = true;
        $clonedConfig["one_sentence"] = false;

        $serializer = new Semantria_JsonSerializer();
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], $config);
        array_push($proxy["removed"], "45699836");
        array_push($proxy["cloned"], $clonedConfig);

        $this->assertEquals($expectedResult, $serializer->serialize($proxy, array("root"=>"configurations", "added"=>"configuration", "removed"=>"configuration")));
    }

    function testXmlDeserializingConfiguration() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>
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
                    <one_sentence>false</one_sentence>
                    <language>English</language>
                    <name>default</name>
                </configuration>
            </configurations>";

        $serializer = new Semantria_XmlSerializer();
        $configs = $serializer->deserialize($source, new GetConfigurationsHandler());
        $config = $configs[0];

        $this->assertNotNull($config);
        $this->assertEquals("default", $config['name']);
        $this->assertFalse($config['is_primary']);
        $this->assertFalse($config['one_sentence']);
        $this->assertFalse($config['auto_responding']);
        $this->assertEquals("English", $config['language']);
        $this->assertEquals(80, $config['chars_threshold']);
        $this->assertEquals("https://anyapi.anydomain.com/processed/docs.json", $config['callback']);

        $this->assertEquals(7, $config['document']['concept_topics_limit']);
        $this->assertEquals(25, $config['document']['query_topics_limit']);
        $this->assertEquals(100, $config['document']['named_entities_limit']);
        $this->assertEquals(5, $config['document']['user_entities_limit']);
        $this->assertEquals(10, $config['document']['themes_limit']);
        $this->assertEquals(5, $config['document']['entity_themes_limit']);
        $this->assertEquals(1, $config['document']['phrases_limit']);
        $this->assertEquals(0, $config['document']['summary_limit']);

        $this->assertEquals(5, $config['collection']['concept_topics_limit']);
        $this->assertEquals(5, $config['collection']['query_topics_limit']);
        $this->assertEquals(5, $config['collection']['named_entities_limit']);
        $this->assertEquals(0, $config['collection']['themes_limit']);
        $this->assertEquals(15, $config['collection']['facets_limit']);
        $this->assertEquals(20, $config['collection']['facet_atts_limit']);
    }

    function testJsonDeserializingConfiguration() {
        $source = "[" .
                    "{" .
                    "\"name\":\"A test configuration\"," .
                    "\"auto_responding\":true," .
                    "\"one_sentence\":true," .
                    "\"is_primary\":true," .
                    "\"language\":\"English\"," .
                    "\"chars_threshold\":80," .
                    "\"document\":{" .
                        "\"entity_themes_limit\":5," .
                        "\"summary_limit\":0," .
                        "\"phrases_limit\":0," .
                        "\"themes_limit\":0," .
                        "\"query_topics_limit\":5," .
                        "\"concept_topics_limit\":5," .
                        "\"named_entities_limit\":5," .
                        "\"user_entities_limit\":5" .
                    "}," .
                    "\"collection\":{" .
                        "\"facets_limit\":15," .
                        "\"facet_atts_limit\":5," .
                        "\"themes_limit\":0," .
                        "\"query_topics_limit\":5," .
                        "\"concept_topics_limit\":5," .
                        "\"named_entities_limit\":5" .
                    "}," .
                    "\"callback\":\"https:\/\/anyapi.anydomain.com\/processed\/docs.json\"" .
                    "}" .
                "]";

        $serializer = new Semantria_JsonSerializer();
        $configs = $serializer->deserialize($source, new GetConfigurationsHandler());
        $config = $configs[0];
        $this->assertNotNull($config);
        $this->assertEquals("A test configuration", $config['name']);
        $this->assertTrue($config['is_primary']);
        $this->assertTrue($config['one_sentence']);
        $this->assertTrue($config['auto_responding']);
        $this->assertEquals("English", $config['language']);
        $this->assertEquals(80, $config['chars_threshold']);
        $this->assertEquals("https://anyapi.anydomain.com/processed/docs.json", $config['callback']);

        $this->assertEquals(5, $config['document']['concept_topics_limit']);
        $this->assertEquals(5, $config['document']['query_topics_limit']);
        $this->assertEquals(5, $config['document']['named_entities_limit']);
        $this->assertEquals(5, $config['document']['user_entities_limit']);
        $this->assertEquals(5, $config['document']['entity_themes_limit']);
        $this->assertEquals(0, $config['document']['phrases_limit']);
        $this->assertEquals(0, $config['document']['summary_limit']);
        $this->assertEquals(0, $config['document']['themes_limit']);

        $this->assertEquals(5, $config['collection']['concept_topics_limit']);
        $this->assertEquals(5, $config['collection']['query_topics_limit']);
        $this->assertEquals(5, $config['collection']['named_entities_limit']);
        $this->assertEquals(0, $config['collection']['themes_limit']);
        $this->assertEquals(15, $config['collection']['facets_limit']);
        $this->assertEquals(5, $config['collection']['facet_atts_limit']);
    }

    function testXmlSerializingBlacklist() {
        $expectedResult =
            "<blacklist>" .
                "<added>" .
                    "<item>Added Filter 1</item>" .
                "</added>" .
                "<removed>" .
                    "<item>Removed Filter 1</item>" .
                "</removed>" .
            "</blacklist>";

        $serializer = new Semantria_XmlSerializer();
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], 'Added Filter 1');
        array_push($proxy["removed"], "Removed Filter 1");

        $this->assertEquals($expectedResult, $serializer->serialize($proxy, array("root"=>"blacklist", "added"=>"item", "removed"=>"item")));
    }

    function testJsonSerializingBlacklist() {
        $expectedResult =
            "{" .
                "\"added\":[\".*@.*com\",\".*@com\\\\.net\"]," .
                "\"removed\":[\"http:\/\/www\\\\..*\\\\.com\"]" .
            "}";

        $serializer = new Semantria_JsonSerializer();
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], ".*@.*com");
        array_push($proxy["added"], ".*@com\\.net");
        array_push($proxy["removed"], "http://www\\..*\\.com");

        $this->assertEquals($expectedResult, $serializer->serialize($proxy));
    }

    function testXmlDeserializingBlacklist() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
                "<blacklist>" .
                    "<item>Filter 1</item>" .
                    "<item>Filter 2</item>" .
                "</blacklist>";

        $serializer = new Semantria_XmlSerializer();
        $blacklist = $serializer->deserialize($source, new GetBlacklistHandler());
        $this->assertEquals(2, count($blacklist));
        $this->assertEquals("Filter 1", $blacklist[0]);
        $this->assertEquals("Filter 2", $blacklist[1]);
    }

    function testJsonDeserializingBlacklist() {
        $source =
                "[" .
                    "\".*@.*com\"," .
                    "\".*@com\\\\.net\"" .
                "]";

        $serializer = new Semantria_JsonSerializer();
        $blacklist = $serializer->deserialize($source);
        $this->assertEquals(2, count($blacklist));
        $this->assertEquals(".*@.*com", $blacklist[0]);
        $this->assertEquals(".*@com\\.net", $blacklist[1]);
    }

    function testXmlSerializingQueries() {
        $expectedResult =
            "<queries>" .
                "<added>" .
                    "<query><name>Query 1</name><query>Something AND something</query></query>" .
                "</added>" .
                "<removed>" .
                    "<query>Query 2</query>" .
                "</removed>" .
            "</queries>";

        $serializer = new Semantria_XmlSerializer();
        $proxy = $this->session->createUpdateProxy();
        $addedQuery["name"] = "Query 1";
        $addedQuery["query"] = "Something AND something";
        array_push($proxy["added"], $addedQuery);
        array_push($proxy["removed"], 'Query 2');

        $this->assertEquals($expectedResult, $serializer->serialize($proxy, array("root"=>"queries", "added"=>"query", "removed"=>"query")));
    }

    function testJsonSerializingQueries() {
        $expectedResult =
            "{" .
                "\"added\":[" .
                    "{" .
                        "\"name\":\"Feature: Cloud service\"," .
                        "\"query\":\"Amazon AND EC2 AND Cloud\"" .
                    "}" .
                "]," .
                "\"removed\":[\"Features\"]" .
            "}";

        $serializer = new Semantria_JsonSerializer();
        $proxy = $this->session->createUpdateProxy();
        $addedQuery["name"] = "Feature: Cloud service";
        $addedQuery["query"] = "Amazon AND EC2 AND Cloud";
        array_push($proxy["added"], $addedQuery);
        array_push($proxy["removed"], 'Features');

        $this->assertEquals($expectedResult, $serializer->serialize($proxy));
    }

    function testXmlDeserializingQueries() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
                "<queries>" .
                    "<query><name>Query 1</name><query>Something AND something</query></query>" .
                    "<query><name>Query 2</name><query>Something AND something</query></query>" .
                "</queries>";

        $serializer = new Semantria_XmlSerializer();
        $queries = $serializer->deserialize($source, new GetQueriesHandler());
        $this->assertEquals(2, count($queries));
        $this->assertEquals("Query 1", $queries[0]['name']);
        $this->assertEquals("Something AND something", $queries[0]['query']);
        $this->assertEquals("Query 2", $queries[1]['name']);
        $this->assertEquals("Something AND something", $queries[1]['query']);
    }

    function testJsonDeserializingQueries() {
        $source =
            "[" .
                "{" .
                    "\"name\":\"Feature: Cloud service\"," .
                    "\"query\":\"Amazon AND EC2 AND Cloud\"" .
                "}" .
            "]";

        $serializer = new Semantria_JsonSerializer();
        $queries = $serializer->deserialize($source, new GetQueriesHandler());
        $this->assertEquals(1, count($queries));
        $this->assertEquals("Feature: Cloud service", $queries[0]['name']);
        $this->assertEquals("Amazon AND EC2 AND Cloud", $queries[0]['query']);
    }

    function testXmlSerializingEntities() {
        $expectedResult =
            "<entities>" .
                "<added>" .
                    "<entity>" .
                        "<name>name 1</name>" .
                        "<type>type 1</type>" .
                    "</entity>" .
                "</added>" .
                "<removed>" .
                    "<entity>name 2</entity>" .
                "</removed>" .
            "</entities>";

        $serializer = new Semantria_XmlSerializer();
        $proxy = $this->session->createUpdateProxy();
        $addedQuery["name"] = "name 1";
        $addedQuery["type"] = "type 1";
        array_push($proxy["added"], $addedQuery);
        array_push($proxy["removed"], 'name 2');

        $this->assertEquals($expectedResult, $serializer->serialize($proxy, array("root"=>"entities", "added"=>"entity", "removed"=>"entity")));
    }

    function testJsonSerializingEntities() {
        $expectedResult =
            "{" .
                "\"added\":[" .
                    "{" .
                        "\"name\":\"chair\"," .
                        "\"type\":\"furniture\"" .
                    "}" .
                "]," .
                "\"removed\":[\"table\"]" .
            "}";

        $serializer = new Semantria_JsonSerializer();
        $proxy = $this->session->createUpdateProxy();
        $addedQuery["name"] = "chair";
        $addedQuery["type"] = "furniture";
        array_push($proxy["added"], $addedQuery);
        array_push($proxy["removed"], 'table');

        $this->assertEquals($expectedResult, $serializer->serialize($proxy));
    }

    function testXmlDeserializingEntities() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
                "<entities>" .
                    "<entity>" .
                        "<name>chair</name>" .
                        "<type>furniture</type> " .
                    "</entity>" .
                "</entities>";

        $serializer = new Semantria_XmlSerializer();
        $entities = $serializer->deserialize($source, new GetEntitiesHandler());
        $this->assertEquals(1, count($entities));
        $this->assertEquals("chair", $entities[0]['name']);
        $this->assertEquals("furniture", $entities[0]['type']);
    }

    function testJsonDeserializingEntities() {
        $source =
            "[" .
                "{" .
                    "\"name\":\"chair\"," .
                    "\"type\":\"furniture\"" .
                "}" .
            "]";

        $serializer = new Semantria_JsonSerializer();
        $entities = $serializer->deserialize($source, new GetEntitiesHandler());
        $this->assertEquals(1, count($entities));
        $this->assertEquals("chair", $entities[0]['name']);
        $this->assertEquals("furniture", $entities[0]['type']);
    }

    function testXmlSerializingCategories() {
        $expectedResult =
            "<categories>" .
                    "<added>" .
                        "<category>" .
                            "<name>Added Category 1</name>" .
                            "<weight>0.2</weight>" .
                            "<samples>" .
                                "<sample>Entity 1</sample>" .
                                "<sample>Entity 2</sample>" .
                                "<sample>Entity 3</sample>" .
                            "</samples>" .
                        "</category>" .
                    "</added>" .
                    "<removed>" .
                        "<category>Removed Category 1</category>" .
                    "</removed>" .
                "</categories>";

        $serializer = new Semantria_XmlSerializer();
        $proxy = $this->session->createUpdateProxy();
        $addedCategory["name"] = "Added Category 1";
        $addedCategory["weight"] = 0.2;
        $addedCategory["samples"] = array("Entity 1", "Entity 2", "Entity 3");
        array_push($proxy["added"], $addedCategory);
        array_push($proxy["removed"], 'Removed Category 1');

        $this->assertEquals($expectedResult, $serializer->serialize($proxy, array("root"=>"categories", "added"=>"category", "removed"=>"category", "samples"=>"sample")));
    }

    function testJsonSerializingCategories() {
        $expectedResult =
            "{" .
                "\"added\":[" .
                    "{" .
                        "\"name\":\"Feature: Cloud service\"," .
                        "\"weight\":0," .
                        "\"samples\":[]" .
                    "}" .
                "]," .
                "\"removed\":[\"Features\"]" .
            "}";

        $serializer = new Semantria_JsonSerializer();
        $proxy = $this->session->createUpdateProxy();
        $addedCategory["name"] = "Feature: Cloud service";
        $addedCategory["weight"] = 0.0;
        $addedCategory["samples"] = array();
        array_push($proxy["added"], $addedCategory);
        array_push($proxy["removed"], 'Features');

        $this->assertEquals($expectedResult, $serializer->serialize($proxy));
    }

    function testXmlDeserializingCategories() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
                "<categories>" .
                    "<category>" .
                        "<name>Feature: Cloud service</name>" .
                        "<weight>0.75</weight>" .
                        "<samples>" .
                            "<sample>Amazon</sample>" .
                            "<sample>EC2</sample>" .
                        "</samples>" .
                    "</category>" .
                "</categories>";

        $serializer = new Semantria_XmlSerializer();
        $categories = $serializer->deserialize($source, new GetCategoriesHandler());
        $this->assertEquals(1, count($categories));
        $this->assertEquals("Feature: Cloud service", $categories[0]['name']);
        $this->assertEquals(0.75, $categories[0]['weight']);
        $this->assertEquals(2, count($categories[0]['samples']));
        $this->assertEquals("Amazon", $categories[0]['samples'][0]);
        $this->assertEquals("EC2", $categories[0]['samples'][1]);
    }

    function testJsonDeserializingCategories() {
        $source =
            "[" .
                "{" .
                    "\"name\":\"Feature: Cloud service\"," .
                    "\"weight\":0.75," .
                    "\"samples\":[\"Amazon\",\"EC2\"]" .
                "}" .
            "]";

        $serializer = new Semantria_JsonSerializer();
        $categories = $serializer->deserialize($source, new GetCategoriesHandler());
        $this->assertEquals(1, count($categories));
        $this->assertEquals("Feature: Cloud service", $categories[0]['name']);
        $this->assertEquals(0.75, $categories[0]['weight']);
        $this->assertEquals(2, count($categories[0]['samples']));
        $this->assertEquals("Amazon", $categories[0]['samples'][0]);
        $this->assertEquals("EC2", $categories[0]['samples'][1]);
    }

    function testXmlSerializingSentimentPhrases() {
        $expectedResult =
            "<phrases>" .
                "<added>" .
                    "<phrase>" .
                        "<title>name 1</title>" .
                        "<weight>0.3</weight>" .
                    "</phrase>" .
                "</added>" .
                "<removed>" .
                    "<phrase>name 2</phrase>" .
                "</removed>" .
            "</phrases>";

        $serializer = new Semantria_XmlSerializer();
        $proxy = $this->session->createUpdateProxy();
        $addedPhrase["title"] = "name 1";
        $addedPhrase["weight"] = 0.3;
        array_push($proxy["added"], $addedPhrase);
        array_push($proxy["removed"], 'name 2');

        $this->assertEquals($expectedResult, $serializer->serialize($proxy, array("root"=>"phrases", "added"=>"phrase", "removed"=>"phrase")));
    }

    function testJsonSerializingSentimentPhrases() {
        $expectedResult =
            "{" .
                "\"added\":[" .
                    "{" .
                        "\"title\":\"Feature: Cloud service\"," .
                        "\"weight\":0" .
                    "}" .
                "]," .
                "\"removed\":[\"Features\"]" .
            "}";

        $serializer = new Semantria_JsonSerializer();
        $proxy = $this->session->createUpdateProxy();
        $addedPhrase["title"] = "Feature: Cloud service";
        $addedPhrase["weight"] = 0.0;
        array_push($proxy["added"], $addedPhrase);
        array_push($proxy["removed"], 'Features');

        $this->assertEquals($expectedResult, $serializer->serialize($proxy));
    }

    function testXmlDeserializingSentimentPhrases() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
                "<phrases>" .
                    "<phrase>" .
                        "<title>chair</title>" .
                        "<weight>0.3</weight> " .
                    "</phrase>" .
                "</phrases>";

        $serializer = new Semantria_XmlSerializer();
        $phrases = $serializer->deserialize($source, new GetSentimentPhrasesHandler());
        $this->assertEquals(1, count($phrases));
        $this->assertEquals("chair", $phrases[0]['title']);
        $this->assertEquals(0.3, $phrases[0]['weight']);
    }

    function testJsonDeserializingSentimentPhrases() {
        $source =
            "[" .
                "{" .
                    "\"title\":\"chair\"," .
                    "\"weight\":0.75" .
                "}" .
            "]";

        $serializer = new Semantria_JsonSerializer();
        $phrases = $serializer->deserialize($source, new GetSentimentPhrasesHandler());
        $this->assertEquals(1, count($phrases));
        $this->assertEquals("chair", $phrases[0]['title']);
        $this->assertEquals(0.75, $phrases[0]['weight']);
    }

    function testXmlDeserializingAnalyticServiceStatus() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
                "<status>" .
                    "<service_status>online</service_status>" .
                    "<api_version>2.0</api_version>" .
                    "<service_version>1.0.2.63</service_version>" .
                    "<supported_encoding>UTF-8</supported_encoding>" .
                    "<supported_compression>gzip</supported_compression>" .
                    "<supported_languages>" .
                        "<language>English</language>" .
                        "<language>French</language>" .
                    "</supported_languages>" .
                "</status>";

        $serializer = new Semantria_XmlSerializer();
        $status = $serializer->deserialize($source, new Semantria_XmlHandler_Status());
        $this->assertEquals("online", $status['service_status']);
        $this->assertEquals("2.0", $status['api_version']);
        $this->assertEquals("1.0.2.63", $status['service_version']);
        $this->assertEquals("UTF-8", $status['supported_encoding']);
        $this->assertEquals("gzip", $status['supported_compression']);
        $this->assertEquals(2, count($status['supported_languages']));
        $this->assertEquals("English", $status['supported_languages'][0]);
        $this->assertEquals("French", $status['supported_languages'][1]);
    }

    function testJsonDeserializingAnalyticServiceStatus() {
        $source =
            "{".
                "\"service_status\":\"online\",".
                "\"api_version\":\"2.0\",".
                "\"service_version\":\"1.0.2.63\",".
                "\"supported_encoding\":\"UTF-8\",".
                "\"supported_compression\":\"gzip\",".
                "\"supported_languages\":[".
                        "\"English\",".
                        "\"French\"".
                "]".
            "}";

        $serializer = new Semantria_JsonSerializer();
        $status = $serializer->deserialize($source, new Semantria_XmlHandler_Status());
        $this->assertEquals("online", $status['service_status']);
        $this->assertEquals("2.0", $status['api_version']);
        $this->assertEquals("1.0.2.63", $status['service_version']);
        $this->assertEquals("UTF-8", $status['supported_encoding']);
        $this->assertEquals("gzip", $status['supported_compression']);
        $this->assertEquals(2, count($status['supported_languages']));
        $this->assertEquals("English", $status['supported_languages'][0]);
        $this->assertEquals("French", $status['supported_languages'][1]);
    }

    function testXmlDeserializingSubscription() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
                "<subscription>" .
                    "<name>name</name>" .
                    "<status>active</status>" .
                    "<priority>normal</priority>" .
                    "<expiration_date>1293883200</expiration_date>" .
                    "<calls_balance>87</calls_balance>" .
                    "<calls_limit>100</calls_limit>" .
                    "<calls_limit_interval>60</calls_limit_interval>" .
                    "<docs_balance>49897</docs_balance>" .
                    "<docs_limit>0</docs_limit>" .
                    "<docs_limit_interval>0</docs_limit_interval>" .
                    "<configurations_limit>10</configurations_limit>" .
                    "<blacklist_limit>100</blacklist_limit>" .
                    "<categories_limit>100</categories_limit>" .
                    "<queries_limit>100</queries_limit>" .
                    "<entities_limit>1000</entities_limit>" .
                    "<sentiment_limit>1000</sentiment_limit>" .
                    "<characters_limit>8192</characters_limit>" .
                    "<batch_limit>1</batch_limit>" .
                    "<collection_limit>10</collection_limit>" .
                    "<auto_response_limit>2</auto_response_limit>" .
                    "<processed_batch_limit>100</processed_batch_limit>" .
                    "<callback_batch_limit>100</callback_batch_limit>" .
                    "<limit_type>type limit</limit_type>" .
                "</subscription>";

        $serializer = new Semantria_XmlSerializer();
        $subscription = $serializer->deserialize($source, new Semantria_XmlHandler_Subscription());
        $this->assertEquals("name", $subscription['name']);
        $this->assertEquals("active", $subscription['status']);
        $this->assertEquals("normal", $subscription['priority']);
        $this->assertEquals(1293883200, $subscription['expiration_date']);
        $this->assertEquals(87, $subscription['calls_balance']);
        $this->assertEquals(100, $subscription['calls_limit']);
        $this->assertEquals(60, $subscription['calls_limit_interval']);
        $this->assertEquals(49897, $subscription['docs_balance']);
        $this->assertEquals(0, $subscription['docs_limit']);
        $this->assertEquals(0, $subscription['docs_limit_interval']);
        $this->assertEquals(10, $subscription['configurations_limit']);
        $this->assertEquals(100, $subscription['blacklist_limit']);
        $this->assertEquals(100, $subscription['categories_limit']);
        $this->assertEquals(100, $subscription['queries_limit']);
        $this->assertEquals(1000, $subscription['entities_limit']);
        $this->assertEquals(1000, $subscription['sentiment_limit']);
        $this->assertEquals(8192, $subscription['characters_limit']);
        $this->assertEquals(1, $subscription['batch_limit']);
        $this->assertEquals(10, $subscription['collection_limit']);
        $this->assertEquals(2, $subscription['auto_response_limit']);
        $this->assertEquals(100, $subscription['processed_batch_limit']);
        $this->assertEquals(100, $subscription['callback_batch_limit']);
        $this->assertEquals("type limit", $subscription['limit_type']);
    }

    function testJsonDeserializingSubscription() {
        $source =
            "{".
                "\"name\" : \"Subscriber\"," .
                "\"status\" : \"active\"," .
                "\"priority\" : \"normal\"," .
                "\"expiration_date\" : 1293883200," .
                "\"calls_balance\" : 87," .
                "\"calls_limit\" : 100," .
                "\"calls_limit_interval\" : 60," .
                "\"docs_balance\" : 49897," .
                "\"docs_limit\" : 0," .
                "\"docs_limit_interval\" : 0," .
                "\"configurations_limit\" : 10," .
                "\"blacklist_limit\" : 100," .
                "\"categories_limit\" : 100," .
                "\"queries_limit\" : 100," .
                "\"entities_limit\" : 1000," .
                "\"sentiment_limit\" : 1000," .
                "\"characters_limit\" : 8192," .
                "\"batch_limit\" : 10," .
                "\"collection_limit\" : 10," .
                "\"auto_response_limit\" : 2," .
                "\"processed_batch_limit\" : 100," .
                "\"callback_batch_limit\" : 100," .
                "\"limit_type\" : \"type limit\"" .
            "}";

        $serializer = new Semantria_JsonSerializer();
        $subscription = $serializer->deserialize($source, new Semantria_XmlHandler_Subscription());
        $this->assertEquals("Subscriber", $subscription['name']);
        $this->assertEquals("active", $subscription['status']);
        $this->assertEquals("normal", $subscription['priority']);
        $this->assertEquals(1293883200, $subscription['expiration_date']);
        $this->assertEquals(87, $subscription['calls_balance']);
        $this->assertEquals(100, $subscription['calls_limit']);
        $this->assertEquals(60, $subscription['calls_limit_interval']);
        $this->assertEquals(49897, $subscription['docs_balance']);
        $this->assertEquals(0, $subscription['docs_limit']);
        $this->assertEquals(0, $subscription['docs_limit_interval']);
        $this->assertEquals(10, $subscription['configurations_limit']);
        $this->assertEquals(100, $subscription['blacklist_limit']);
        $this->assertEquals(100, $subscription['categories_limit']);
        $this->assertEquals(100, $subscription['queries_limit']);
        $this->assertEquals(1000, $subscription['entities_limit']);
        $this->assertEquals(1000, $subscription['sentiment_limit']);
        $this->assertEquals(8192, $subscription['characters_limit']);
        $this->assertEquals(10, $subscription['batch_limit']);
        $this->assertEquals(10, $subscription['collection_limit']);
        $this->assertEquals(2, $subscription['auto_response_limit']);
        $this->assertEquals(100, $subscription['processed_batch_limit']);
        $this->assertEquals(100, $subscription['callback_batch_limit']);
        $this->assertEquals("type limit", $subscription['limit_type']);
    }

    function testXmlDeserializingDocumentAnalyticData() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
            "<document>" .
                "<config_id>23498367</config_id>" .
                "<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" .
                "<status>PROCESSED</status>" .
                "<sentiment_score>0.2398756</sentiment_score>" .
                "<summary>Summary of the document's text.</summary>" .
                "<themes>" .
                    "<theme>" .
                        "<evidence>1</evidence>" .
                        "<is_about>true</is_about>" .
                        "<strength_score>0.0</strength_score>" .
                        "<sentiment_score>0.0</sentiment_score>" .
                        "<title>republican moderates</title>" .
                    "</theme>" .
                "</themes>" .
                "<entities>" .
                    "<entity>" .
                        "<evidence>0</evidence>" .
                        "<is_about>true</is_about>" .
                        "<confident>true</confident>" .
                        "<title>WASHINGTON</title>" .
                        "<sentiment_score>1.0542796</sentiment_score>" .
                        "<type>named</type>" .
                        "<entity_type>Place</entity_type>" .
                        "<themes>" .
                            "<theme>" .
                                "<evidence>1</evidence>" .
                                "<is_about>true</is_about>" .
                                "<strength_score>0.0</strength_score>" .
                                "<sentiment_score>0.0</sentiment_score>" .
                                "<title>republican moderates</title>" .
                            "</theme>" .
                        "</themes>" .
                    "</entity>" .
                "</entities>" .
                "<topics>" .
                    "<topic>" .
                        "<title>Something</title>" .
                        "<hitcount>0</hitcount>" .
                        "<sentiment_score>0.6133076</sentiment_score>" .
                        "<strength_score>0.6133076</strength_score>" .
                        "<type>concept</type>" .
                    "</topic>" .
                "</topics>" .
                "<phrases>" .
                    "<phrase>" .
                        "<title>Something</title>" .
                        "<sentiment_score>0.6133076</sentiment_score>" .
                        "<is_negated>true</is_negated>" .
                        "<negating_phrase>not</negating_phrase>" .
                    "</phrase>" .
                "</phrases>" .
            "</document>";

        $serializer = new Semantria_XmlSerializer();
        $doc = $serializer->deserialize($source, new GetDocumentHandler());
        // main
        $this->assertEquals("23498367", $doc['config_id']);
        $this->assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", $doc['id']);
        $this->assertEquals("PROCESSED", $doc['status']);
        $this->assertEquals(0.2398756, $doc['sentiment_score']);
        $this->assertEquals("Summary of the document's text.", $doc['summary']);
        // themes
        $this->assertEquals(1, count($doc['themes']));
        $this->assertEquals(1, $doc['themes'][0]['evidence']);
        $this->assertTrue($doc['themes'][0]['is_about']);
        $this->assertEquals(0, $doc['themes'][0]['strength_score']);
        $this->assertEquals(0, $doc['themes'][0]['sentiment_score']);
        $this->assertEquals('republican moderates', $doc['themes'][0]['title']);
        // entities
        $this->assertEquals(1, count($doc['entities']));
        $this->assertEquals(0, $doc['entities'][0]['evidence']);
        $this->assertTrue($doc['entities'][0]['is_about']);
        $this->assertTrue($doc['entities'][0]['confident']);
        $this->assertEquals('named', $doc['entities'][0]['type']);
        $this->assertEquals(1.0542796, $doc['entities'][0]['sentiment_score']);
        $this->assertEquals('WASHINGTON', $doc['entities'][0]['title']);
        // entity themes
        $this->assertEquals(1, count($doc['entities'][0]['themes']));
        $theme = $doc['entities'][0]['themes'][0];
        $this->assertEquals(1, $theme['evidence']);
        $this->assertTrue($theme['is_about']);
        $this->assertEquals(0, $theme['strength_score']);
        $this->assertEquals(0, $theme['sentiment_score']);
        $this->assertEquals('republican moderates', $theme['title']);
        // topics
        $this->assertEquals(1, count($doc['topics']));
        $this->assertEquals(0, $doc['topics'][0]['hitcount']);
        $this->assertEquals('concept', $doc['topics'][0]['type']);
        $this->assertEquals(0.6133076, $doc['topics'][0]['strength_score']);
        $this->assertEquals(0.6133076, $doc['topics'][0]['sentiment_score']);
        $this->assertEquals('Something', $doc['topics'][0]['title']);
        // phrases
        $this->assertEquals(1, count($doc['phrases']));
        $this->assertEquals('not', $doc['phrases'][0]['negating_phrase']);
        $this->assertTrue($doc['phrases'][0]['is_negated']);
        $this->assertEquals(0.6133076, $doc['phrases'][0]['sentiment_score']);
        $this->assertEquals('Something', $doc['phrases'][0]['title']);
    }

    function testJsonDeserializingDocumentAnalyticData() {
        $source = "[{".
                "\"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\",".
                "\"config_id\":\"23498367\",".
                "\"status\":\"PROCESSED\",".
                "\"sentiment_score\":0.8295653,".
                "\"summary\":\"Summary of the document's text.\",".
                "\"themes\":[".
                    "{".
                        "\"evidence\":1,".
                        "\"is_about\":true,".
                        "\"strength_score\":0.0,".
                        "\"sentiment_score\":0.0,".
                        "\"title\":\"republican moderates\"".
                    "}".
                "],".
                "\"entities\":[".
                    "{".
                    "\"type\":\"named\",".
                    "\"evidence\":0,".
                    "\"is_about\":true,".
                    "\"confident\":true,".
                    "\"entity_type\":\"Place\",".
                    "\"title\":\"WASHINGTON\",".
                    "\"sentiment_score\":1.0542796,".
                    "\"themes\":[".
                        "{".
                            "\"evidence\":1,".
                            "\"is_about\":true,".
                            "\"strength_score\":0.0,".
                            "\"sentiment_score\":0.0,".
                            "\"title\":\"republican moderates\"".
                        "}".
                    "]".
                    "}".
                "],".
                "\"topics\":[".
                    "{".
                        "\"title\":\"Something\",".
                        "\"type\":\"concept\",".
                        "\"hitcount\":0,".
                        "\"strength_score\":0.0,".
                        "\"sentiment_score\":0.6133076".
                    "}".
                "],".
                "\"phrases\":[".
                    "{".
                        "\"title\":\"Something\",".
                        "\"is_negated\":true,".
                        "\"negating_phrase\":\"not\",".
                        "\"sentiment_score\":0.6133076".
                    "}".
                "]".
            "}]";

        $serializer = new Semantria_JsonSerializer();
        $docs = $serializer->deserialize($source, new GetProcessedDocumentsHandler());
        $this->assertEquals(1, count($docs));
        $doc = $docs[0];
        // main
        $this->assertEquals("23498367", $doc['config_id']);
        $this->assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", $doc['id']);
        $this->assertEquals("PROCESSED", $doc['status']);
        $this->assertEquals(0.8295653, $doc['sentiment_score']);
        $this->assertEquals("Summary of the document's text.", $doc['summary']);
        // themes
        $this->assertEquals(1, count($doc['themes']));
        $this->assertEquals(1, $doc['themes'][0]['evidence']);
        $this->assertTrue($doc['themes'][0]['is_about']);
        $this->assertEquals(0, $doc['themes'][0]['strength_score']);
        $this->assertEquals(0, $doc['themes'][0]['sentiment_score']);
        $this->assertEquals('republican moderates', $doc['themes'][0]['title']);
        // entities
        $this->assertEquals(1, count($doc['entities']));
        $this->assertEquals(0, $doc['entities'][0]['evidence']);
        $this->assertTrue($doc['entities'][0]['is_about']);
        $this->assertTrue($doc['entities'][0]['confident']);
        $this->assertEquals('named', $doc['entities'][0]['type']);
        $this->assertEquals(1.0542796, $doc['entities'][0]['sentiment_score']);
        $this->assertEquals('WASHINGTON', $doc['entities'][0]['title']);
        // entity themes
        $this->assertEquals(1, count($doc['entities'][0]['themes']));
        $theme = $doc['entities'][0]['themes'][0];
        $this->assertEquals(1, $theme['evidence']);
        $this->assertTrue($theme['is_about']);
        $this->assertEquals(0, $theme['strength_score']);
        $this->assertEquals(0, $theme['sentiment_score']);
        $this->assertEquals('republican moderates', $theme['title']);
        // topics
        $this->assertEquals(1, count($doc['topics']));
        $this->assertEquals(0, $doc['topics'][0]['hitcount']);
        $this->assertEquals('concept', $doc['topics'][0]['type']);
        $this->assertEquals(0.0, $doc['topics'][0]['strength_score']);
        $this->assertEquals(0.6133076, $doc['topics'][0]['sentiment_score']);
        $this->assertEquals('Something', $doc['topics'][0]['title']);
        // phrases
        $this->assertEquals(1, count($doc['phrases']));
        $this->assertEquals('not', $doc['phrases'][0]['negating_phrase']);
        $this->assertTrue($doc['phrases'][0]['is_negated']);
        $this->assertEquals(0.6133076, $doc['phrases'][0]['sentiment_score']);
        $this->assertEquals('Something', $doc['phrases'][0]['title']);
    }

    function testXmlDeserializingCollectionAnalyticData() {
        $source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" .
            "<collection>" .
                "<config_id>23498367</config_id>" .
                "<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" .
                "<status>PROCESSED</status>" .
                "<facets>" .
                    "<facet>" .
                        "<label>Something</label>" .
                        "<count>10</count>" .
                        "<negative_count>2</negative_count>" .
                        "<positive_count>1</positive_count>" .
                        "<neutral_count>7</neutral_count>" .
                        "<attributes>" .
                            "<attribute>" .
                                "<label>Attribute</label>" .
                                "<count>5</count>" .
                            "</attribute>" .
                        "</attributes>" .
                    "</facet>" .
                "</facets>" .
                "<themes>" .
                    "<theme>" .
                        "<phrases_count>1</phrases_count>" .
                        "<themes_count>1</themes_count>" .
                        "<sentiment_score>0.0</sentiment_score>" .
                        "<title>republican moderates</title>" .
                    "</theme>" .
                "</themes>" .
                "<entities>" .
                    "<entity>" .
                        "<title>WASHINGTON</title>" .
                        "<type>named</type>" .
                        "<entity_type>Place</entity_type>" .
                        "<count>1</count>" .
                        "<negative_count>1</negative_count>" .
                        "<neutral_count>1</neutral_count>" .
                        "<positive_count>1</positive_count>" .
                    "</entity>" .
                "</entities>" .
                "<topics>" .
                    "<topic>" .
                        "<title>Something</title>" .
                        "<hitcount>0</hitcount>" .
                        "<sentiment_score>0.6133076</sentiment_score>" .
                        "<type>concept</type>" .
                    "</topic>" .
                "</topics>" .
            "</collection>";

        $serializer = new Semantria_XmlSerializer();
        $coll = $serializer->deserialize($source, new GetCollectionHandler());
        // main
        $this->assertEquals("23498367", $coll['config_id']);
        $this->assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", $coll['id']);
        $this->assertEquals("PROCESSED", $coll['status']);
        // facets
        $this->assertEquals(1, count($coll['facets']));
        $facet = $coll['facets'][0];
        $this->assertEquals('Something', $facet['label']);
        $this->assertEquals(10, $facet['count']);
        $this->assertEquals(2, $facet['negative_count']);
        $this->assertEquals(1, $facet['positive_count']);
        $this->assertEquals(7, $facet['neutral_count']);
        $this->assertEquals(1, count($facet['attributes']));
        $this->assertEquals('Attribute', $facet['attributes'][0]['label']);
        $this->assertEquals(5, $facet['attributes'][0]['count']);
        // themes
        $this->assertEquals(1, count($coll['themes']));
        $this->assertEquals(1, $coll['themes'][0]['phrases_count']);
        $this->assertEquals(1, $coll['themes'][0]['themes_count']);
        $this->assertEquals(0.0, $coll['themes'][0]['sentiment_score']);
        $this->assertEquals('republican moderates', $coll['themes'][0]['title']);
        // entities
        $this->assertEquals(1, count($coll['entities']));
        $this->assertEquals('WASHINGTON', $coll['entities'][0]['title']);
        $this->assertEquals('named', $coll['entities'][0]['type']);
        $this->assertEquals('Place', $coll['entities'][0]['entity_type']);
        $this->assertEquals(1, $coll['entities'][0]['count']);
        $this->assertEquals(1, $coll['entities'][0]['negative_count']);
        $this->assertEquals(1, $coll['entities'][0]['positive_count']);
        $this->assertEquals(1, $coll['entities'][0]['neutral_count']);
        // topics
        $this->assertEquals(1, count($coll['topics']));
        $this->assertEquals(0, $coll['topics'][0]['hitcount']);
        $this->assertEquals('concept', $coll['topics'][0]['type']);
        $this->assertEquals(0.6133076, $coll['topics'][0]['sentiment_score']);
        $this->assertEquals('Something', $coll['topics'][0]['title']);
    }

    function testJsonDeserializingCollectionAnalyticData() {
        $source = "{".
                "\"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\",".
                "\"config_id\":\"23498367\",".
                "\"status\":\"PROCESSED\",".
                "\"facets\":[".
                    "{".
                        "\"label\":\"Something\",".
                        "\"count\":10,".
                        "\"negative_count\":2,".
                        "\"positive_count\":1,".
                        "\"neutral_count\":7,".
                        "\"attributes\":[".
                            "{".
                                "\"label\":\"Attribute\",".
                                "\"count\":5".
                            "}".
                        "]".
                    "}".
                "],".
                "\"themes\":[".
                    "{".
                        "\"phrases_count\":1,".
                        "\"themes_count\":1,".
                        "\"sentiment_score\":0.0,".
                        "\"title\":\"republican moderates\"".
                    "}".
                "],".
                "\"entities\":[".
                    "{".
                        "\"type\":\"named\",".
                        "\"entity_type\":\"Place\",".
                        "\"title\":\"WASHINGTON\",".
                        "\"count\":1,".
                        "\"negative_count\":1,".
                        "\"neutral_count\":1,".
                        "\"positive_count\":1".
                    "}".
                "],".
                "\"topics\":[".
                    "{".
                        "\"title\":\"Something\",".
                        "\"type\":\"concept\",".
                        "\"hitcount\":0,".
                        "\"sentiment_score\":0.6133076".
                    "}".
                "]".
                "}";

        $serializer = new Semantria_JsonSerializer();
        $coll = $serializer->deserialize($source, new GetCollectionHandler());
        // main
        $this->assertEquals("23498367", $coll['config_id']);
        $this->assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", $coll['id']);
        $this->assertEquals("PROCESSED", $coll['status']);
        // facets
        $this->assertEquals(1, count($coll['facets']));
        $facet = $coll['facets'][0];
        $this->assertEquals('Something', $facet['label']);
        $this->assertEquals(10, $facet['count']);
        $this->assertEquals(2, $facet['negative_count']);
        $this->assertEquals(1, $facet['positive_count']);
        $this->assertEquals(7, $facet['neutral_count']);
        $this->assertEquals(1, count($facet['attributes']));
        $this->assertEquals('Attribute', $facet['attributes'][0]['label']);
        $this->assertEquals(5, $facet['attributes'][0]['count']);
        // themes
        $this->assertEquals(1, count($coll['themes']));
        $this->assertEquals(1, $coll['themes'][0]['phrases_count']);
        $this->assertEquals(1, $coll['themes'][0]['themes_count']);
        $this->assertEquals(0.0, $coll['themes'][0]['sentiment_score']);
        $this->assertEquals('republican moderates', $coll['themes'][0]['title']);
        // entities
        $this->assertEquals(1, count($coll['entities']));
        $this->assertEquals('WASHINGTON', $coll['entities'][0]['title']);
        $this->assertEquals('named', $coll['entities'][0]['type']);
        $this->assertEquals('Place', $coll['entities'][0]['entity_type']);
        $this->assertEquals(1, $coll['entities'][0]['count']);
        $this->assertEquals(1, $coll['entities'][0]['negative_count']);
        $this->assertEquals(1, $coll['entities'][0]['positive_count']);
        $this->assertEquals(1, $coll['entities'][0]['neutral_count']);
        // topics
        $this->assertEquals(1, count($coll['topics']));
        $this->assertEquals(0, $coll['topics'][0]['hitcount']);
        $this->assertEquals('concept', $coll['topics'][0]['type']);
        $this->assertEquals(0.6133076, $coll['topics'][0]['sentiment_score']);
        $this->assertEquals('Something', $coll['topics'][0]['title']);
    }
}
