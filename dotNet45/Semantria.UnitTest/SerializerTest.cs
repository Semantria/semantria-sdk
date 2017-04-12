using System;
using System.Text;
using System.Collections.Generic;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Semantria.Com.Serializers;
using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Configuration.Stub;
using System.Text.RegularExpressions;
using Semantria.Com.Mapping.Output;

namespace Semantria.Com.TestUnitApi
{
    [TestClass]
    public class SerializerTest
    {
        [TestMethod]
        public void testXmlSerializingConfiguration()
        {
            Configuration config = new Configuration();
            config.Name = "test";
            config.OneSentence = false;
            config.IsPrimary = true;
            config.AutoResponse = true;
            config.Language = "English";
            config.CharsThreshold = 80;
            config.Callback = "https://anyapi.anydomain.com/processed/docs.json";

            DocConfiguration doc = new DocConfiguration();
            doc.ConceptTopicsLimit = 5;
            doc.QueryTopicsLimit = 5;
            doc.NamedEntitiesLimit = 5;
            doc.UserEntitiesLimit = 5;
            doc.ThemesLimit = 0;
            doc.EntityThemesLimit = 5;
            doc.PhrasesLimit = 0;
            doc.SummaryLimit = 0;
            config.Document = doc;

            CollConfiguration coll = new CollConfiguration();
            coll.FacetsLimit = 15;
            coll.FacetAttsLimit = 20;
            coll.ConceptTopicsLimit = 5;
            coll.QueryTopicsLimit = 5;
            coll.NamedEntitiesLimit = 5;
            coll.ThemesLimit = 0;
            config.Collection = coll;

            Configuration removedConfig = new Configuration();
            removedConfig.ConfigId = "45699836";

            Configuration clonedConfig = new Configuration();
            clonedConfig.ConfigId = "45699836";
            clonedConfig.Name = "Cloned Config";
            clonedConfig.IsPrimary = false;
            clonedConfig.OneSentence = true;
            clonedConfig.Language = "French";

            String expectedResult =
                    "<?xml version=\"1.0\"?>" +
                    "<configurations xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                        "<added>" +
                            "<configuration>" + 
                              "<name>" + config.Name + "</name>" +
                              "<one_sentence>" + config.OneSentence.ToString().ToLower() + "</one_sentence>" +
                              "<is_primary>" + config.IsPrimary.ToString().ToLower() + "</is_primary>" +
                              "<auto_response>" + config.AutoResponse.ToString().ToLower() + "</auto_response>" +
                              "<language>" + config.Language + "</language>" +
                              "<chars_threshold>" + config.CharsThreshold + "</chars_threshold>" +
                              "<callback>" + config.Callback + "</callback>" +
                              "<document>" +
                                "<concept_topics_limit>" + config.Document.ConceptTopicsLimit + "</concept_topics_limit>" +
                                "<query_topics_limit>" + config.Document.QueryTopicsLimit + "</query_topics_limit>" +
                                "<named_entities_limit>" + config.Document.NamedEntitiesLimit + "</named_entities_limit>" +
                                "<user_entities_limit>" + config.Document.UserEntitiesLimit + "</user_entities_limit>" +
                                "<entity_themes_limit>" + config.Document.EntityThemesLimit + "</entity_themes_limit>" +
                                "<themes_limit>" + config.Document.ThemesLimit + "</themes_limit>" +
                                "<phrases_limit>" + config.Document.PhrasesLimit + "</phrases_limit>" +
                                "<summary_limit>" + config.Document.SummaryLimit + "</summary_limit>" +
                              "</document>" +
                              "<collection>" +
                                "<facets_limit>" + config.Collection.FacetsLimit+ "</facets_limit>" +
                                "<facet_atts_limit>" + config.Collection.FacetAttsLimit + "</facet_atts_limit>" +
                                "<concept_topics_limit>" + config.Collection.ConceptTopicsLimit + "</concept_topics_limit>" +
                                "<query_topics_limit>" + config.Collection.ConceptTopicsLimit + "</query_topics_limit>" +
                                "<named_entities_limit>" + config.Collection.NamedEntitiesLimit + "</named_entities_limit>" +
                                "<themes_limit>" + config.Collection.ThemesLimit + "</themes_limit>" +
                              "</collection>" +
                            "</configuration>" +
                            "<configuration>" +
                              "<template>" + clonedConfig.ConfigId + "</template>" +
                              "<name>" + clonedConfig.Name + "</name>" +
                              "<one_sentence>" + clonedConfig.OneSentence.ToString().ToLower() + "</one_sentence>" +
                              "<is_primary>" + clonedConfig.IsPrimary.ToString().ToLower() + "</is_primary>" +
                              "<auto_response>" + clonedConfig.AutoResponse.ToString().ToLower() + "</auto_response>" +
                              "<language>" + clonedConfig.Language + "</language>" +
                              "<chars_threshold>" + clonedConfig.CharsThreshold + "</chars_threshold>" +
                            "</configuration>" +
                        "</added>" +
                        "<removed>" +
                            "<configuration>" + removedConfig.ConfigId + "</configuration>" +
                        "</removed>" +
                    "</configurations>";

            ISerializer serializer = new XmlSerializer();
            IUpdateProxy<Configuration> proxy = Session.CreateSession("", "", serializer).CreateConfigurationsUpdateProxy();
            proxy.Add(config);
            proxy.Remove(removedConfig);
            proxy.Clone(clonedConfig);

            ConfigurationManagable cm = (ConfigurationManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testXmlDeserializingConfiguration() 
        {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<configurations>" +
                        "<configuration>" +
                            "<auto_response>true</auto_response>" +
                            "<callback>https://anyapi.anydomain.com/processed/docs.json</callback>" +
                            "<chars_threshold>80</chars_threshold>" +
                            "<collection>" +
                            "<concept_topics_limit>5</concept_topics_limit>" +
                            "<facet_atts_limit>20</facet_atts_limit>" +
                            "<facets_limit>15</facets_limit>" +
                            "<named_entities_limit>5</named_entities_limit>" +
                            "<query_topics_limit>5</query_topics_limit>" +
                            "<themes_limit>0</themes_limit>" +
                            "<user_entities_limit>5</user_entities_limit>" +
                            "</collection>" +
                            "<document>" +
                            "<concept_topics_limit>5</concept_topics_limit>" +
                            "<entity_themes_limit>5</entity_themes_limit>" +
                            "<named_entities_limit>5</named_entities_limit>" +
                            "<phrases_limit>0</phrases_limit>" +
                            "<query_topics_limit>5</query_topics_limit>" +
                            "<summary_limit>0</summary_limit>" +
                            "<themes_limit>0</themes_limit>" +
                            "<user_entities_limit>5</user_entities_limit>" +
                            "</document>" +
                            "<is_primary>true</is_primary>" +
                            "<one_sentence>true</one_sentence>" +
                            "<language>English</language>" +
                            "<name>A test configuration</name>" +
                        "</configuration>" +
                    "</configurations>";

            ISerializer serializer = new XmlSerializer();
            Configuration config = serializer.Deserialize<Configurations>(source).Data[0];

            Assert.AreEqual("A test configuration", config.Name);
            Assert.IsTrue(config.IsPrimary);
            Assert.IsTrue(config.OneSentence);
            Assert.IsTrue(config.AutoResponse);
            Assert.AreEqual("English", config.Language);
            Assert.AreEqual(80, config.CharsThreshold);
            Assert.AreEqual("https://anyapi.anydomain.com/processed/docs.json", config.Callback);

            DocConfiguration doc = config.Document;
            Assert.AreEqual(5, doc.ConceptTopicsLimit);
            Assert.AreEqual(5, doc.QueryTopicsLimit);
            Assert.AreEqual(5, doc.NamedEntitiesLimit);
            Assert.AreEqual(5, doc.UserEntitiesLimit);
            Assert.AreEqual(0, doc.ThemesLimit);
            Assert.AreEqual(5, doc.EntityThemesLimit);
            Assert.AreEqual(0, doc.PhrasesLimit);
            Assert.AreEqual(0, doc.SummaryLimit);

            CollConfiguration coll = config.Collection;
            Assert.AreEqual(5, coll.ConceptTopicsLimit);
            Assert.AreEqual(5, coll.QueryTopicsLimit);
            Assert.AreEqual(5, coll.NamedEntitiesLimit);
            Assert.AreEqual(0, coll.ThemesLimit);
            Assert.AreEqual(15, coll.FacetsLimit);
            Assert.AreEqual(20, coll.FacetAttsLimit);
        }

        [TestMethod]
        public void testJsonSerializingConfiguration()
        {
            Configuration config = new Configuration();
            config.ConfigId = null;
            config.Name = "test";
            config.IsPrimary = true;
            config.OneSentence = false;
            config.AutoResponse = true;
            config.Language = "English";
            config.CharsThreshold = 80;
            config.Callback = "https://anyapi.anydomain.com/processed/docs.json";

            DocConfiguration doc = new DocConfiguration();
            doc.ConceptTopicsLimit = 5;
            doc.QueryTopicsLimit = 5;
            doc.NamedEntitiesLimit = 5;
            doc.UserEntitiesLimit = 5;
            doc.ThemesLimit = 0;
            doc.EntityThemesLimit = 5;
            doc.PhrasesLimit = 0;
            doc.SummaryLimit = 0;
            config.Document = doc;

            CollConfiguration coll = new CollConfiguration();
            coll.FacetsLimit = 15;
            coll.FacetAttsLimit = 20;
            coll.ConceptTopicsLimit = 5;
            coll.QueryTopicsLimit = 5;
            coll.NamedEntitiesLimit = 5;
            coll.ThemesLimit = 0;
            config.Collection = coll;

            Configuration removedConfig = new Configuration();
            removedConfig.ConfigId = "45699836";

            Configuration clonedConfig = new Configuration();
            clonedConfig.ConfigId = "45699836";
            clonedConfig.Name = "Cloned Config";
            clonedConfig.IsPrimary = false;
            clonedConfig.OneSentence = true;

            String expectedResult =
                    "{\"added\":[{\"auto_response\":true,\"callback\":\"https:\\/\\/anyapi.anydomain.com\\/processed\\/docs.json\",\"chars_threshold\":80,\"collection\":{\"concept_topics_limit\":5," +
                    "\"facet_atts_limit\":20,\"facets_limit\":15,\"named_entities_limit\":5,\"query_topics_limit\":5,\"themes_limit\":0},\"document\":" +
                    "{\"concept_topics_limit\":5,\"entity_themes_limit\":5,\"named_entities_limit\":5,\"phrases_limit\":0,\"query_topics_limit\":5,\"summary_limit\":0,\"themes_limit\":0," +
                    "\"user_entities_limit\":5},\"is_primary\":true,\"language\":\"English\",\"name\":\"test\",\"one_sentence\":false},{\"auto_response\":false,\"callback\":null,\"collection\":null,\"document\":null" + 
                    ",\"is_primary\":false,\"language\":null,\"name\":\"Cloned Config\",\"one_sentence\":true,\"template\":\"45699836\"}],\"removed\":[\"45699836\"]}";

            ISerializer serializer = new JsonSerializer();
            IUpdateProxy<Configuration> proxy = Session.CreateSession("", "", serializer).CreateConfigurationsUpdateProxy();
            proxy.Add(config);
            proxy.Remove(removedConfig);
            proxy.Clone(clonedConfig);

            ConfigurationManagable cm = (ConfigurationManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testJsonDeserializingConfiguration()
        {
            String source =
                    "[" +
                        "{" +
                            "\"name\":\"A test configuration\"," +
                            "\"auto_responding\":true," +
                            "\"is_primary\":true," +
                            "\"language\":\"English\"," +
                            "\"chars_threshold\":80," +
                            "\"document\":{" +
                                "\"entity_themes_limit\":5," +
                                "\"summary_limit\":0," +
                                "\"phrases_limit\":0," +
                                "\"themes_limit\":0," +
                                "\"query_topics_limit\":5," +
                                "\"concept_topics_limit\":5," +
                                "\"named_entities_limit\":5," +
                                "\"user_entities_limit\":5" +
                            "}," +
                            "\"collection\":{" +
                                "\"facets_limit\":15," +
                                "\"facet_atts_limit\":5," +
                                "\"themes_limit\":0," +
                                "\"query_topics_limit\":5," +
                                "\"concept_topics_limit\":5," +
                                "\"named_entities_limit\":5" +
                            "}," +
                            "\"callback\":\"https://anyapi.anydomain.com/processed/docs.json\"" +
                        "}" +
                    "]";

            ISerializer serializer = new JsonSerializer();
            Configuration config = serializer.Deserialize<List<Configuration>>(source)[0];

            Assert.AreEqual("A test configuration", config.Name);
            Assert.IsTrue(config.IsPrimary);
            Assert.IsTrue(config.AutoResponse);
            Assert.AreEqual("English", config.Language);
            Assert.AreEqual(80, config.CharsThreshold);
            Assert.AreEqual("https://anyapi.anydomain.com/processed/docs.json", config.Callback);

            DocConfiguration doc = config.Document;
            Assert.AreEqual(5, doc.ConceptTopicsLimit);
            Assert.AreEqual(5, doc.QueryTopicsLimit);
            Assert.AreEqual(5, doc.NamedEntitiesLimit);
            Assert.AreEqual(5, doc.UserEntitiesLimit);
            Assert.AreEqual(0, doc.ThemesLimit);
            Assert.AreEqual(5, doc.EntityThemesLimit);
            Assert.AreEqual(0, doc.PhrasesLimit);
            Assert.AreEqual(0, doc.SummaryLimit);

            CollConfiguration coll = config.Collection;
            Assert.AreEqual(5, coll.ConceptTopicsLimit);
            Assert.AreEqual(5, coll.QueryTopicsLimit);
            Assert.AreEqual(5, coll.NamedEntitiesLimit);
            Assert.AreEqual(0, coll.ThemesLimit);
            Assert.AreEqual(15, coll.FacetsLimit);
            Assert.AreEqual(5, coll.FacetAttsLimit);
        }

        [TestMethod]
        public void testXmlSerializingBlacklist() {
            ISerializer serializer = new XmlSerializer();
            String expectedResult =
                    "<?xml version=\"1.0\"?>" +
                    "<blacklist xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                        "<added>" +
                            "<item>Added Filter 1</item>" +
                        "</added>" +
                        "<removed>" +
                            "<item>Removed Filter 1</item>" +
                        "</removed>" +
                    "</blacklist>";

            IUpdateProxy<String> proxy = Session.CreateSession("", "", serializer).CreateBlacklistUpdateProxy();
            proxy.Add("Added Filter 1");
            proxy.Remove("Removed Filter 1");

            BlacklistManagable cm = (BlacklistManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testJsonSerializingBlacklist() {
            ISerializer serializer = new JsonSerializer();
            String expectedResult =
            "{" +
                "\"added\":[\".*@.*com\",\".*@com\\\\.net\"]," +
                "\"removed\":[\"http:\\/\\/www\\\\..*\\\\.com\"]" +
            "}";

            IUpdateProxy<String> proxy = Session.CreateSession("", "", serializer).CreateBlacklistUpdateProxy();
            proxy.Add(".*@.*com");
            proxy.Add(".*@com\\.net");
            proxy.Remove("http://www\\..*\\.com");

            BlacklistManagable cm = (BlacklistManagable)proxy.Stub;
            Assert.AreEqual(expectedResult, serializer.Serialize(cm));
        }

        [TestMethod]
        public void testXmlDeserializingBlacklist() {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<blacklist>" +
                        "<item>Filter 1</item>" +
                        "<item>Filter 2</item>" +
                    "</blacklist>";

            ISerializer serializer = new XmlSerializer();
            List<string> blacklists = serializer.Deserialize<Blacklists>(source).Data;
            Assert.AreEqual(2, blacklists.Count);
        }

        [TestMethod]
        public void testJsonDeserializingBlacklist() {
            String source =
                    "[" +
                        "\".*@.*com\"," +
                        "\".*@com\\\\.net\"" +
                    "]";

            ISerializer serializer = new JsonSerializer();
            List<string> blacklists = serializer.Deserialize<List<string>>(source);
            Assert.AreEqual(2, blacklists.Count);
        }

        [TestMethod]
        public void testXmlSerializingQueries() {
            String expectedResult =
                    "<?xml version=\"1.0\"?>" +
                    "<queries xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                        "<added>" +
                            "<query><name>Query 1</name><query>Something AND something</query></query>" +
                        "</added>" +
                        "<removed>" +
                            "<query>Query 2</query>" +
                        "</removed>" +
                    "</queries>";

            ISerializer serializer = new XmlSerializer();
            IUpdateProxy<Query> proxy = Session.CreateSession("", "", serializer).CreateQueriesUpdateProxy();
            Query addedQuery = new Query();
            addedQuery.Name = "Query 1";
            addedQuery.Content = "Something AND something";
            proxy.Add(addedQuery);

            Query removedQuery = new Query();
            removedQuery.Name = "Query 2";
            proxy.Remove(removedQuery);

            QueryManagable cm = (QueryManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testJsonSerializingQueries() {
            String expectedResult =
                    "{" +
                        "\"added\":[" +
                            "{" +
                                "\"name\":\"Feature: Cloud service\"," +
                                "\"query\":\"Amazon AND EC2 AND Cloud\"" +
                            "}" +
                        "]," +
                        "\"removed\":[\"Features\"]" +
                    "}";

            ISerializer serializer = new JsonSerializer();
            IUpdateProxy<Query> proxy = Session.CreateSession("", "", serializer).CreateQueriesUpdateProxy();
            Query addedQuery = new Query();
            addedQuery.Name = "Feature: Cloud service";
            addedQuery.Content = "Amazon AND EC2 AND Cloud";
            proxy.Add(addedQuery);

            Query removedQuery = new Query();
            removedQuery.Name = "Features";
            proxy.Remove(removedQuery);

            QueryManagable cm = (QueryManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testXmlDeserializingQueries() {
            ISerializer serializer = new XmlSerializer();
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<queries>" +
                            "<query><name>Query 1</name><query>Something AND something</query></query>" +
                            "<query><name>Query 2</name><query>Something AND something</query></query>" +
                        "</queries>";

            List<Query> queries = serializer.Deserialize<Queries>(source).Data;
            Assert.AreEqual(2, queries.Count);
            Assert.IsTrue(queries[0].Name.StartsWith("Query"));
        }
    
        [TestMethod] 
        public void testJsonDeserializingQueries() {
            ISerializer serializer = new JsonSerializer();
            String source =
                "[" +
                    "{" +
                        "\"name\":\"Feature: Cloud service\"," +
                        "\"query\":\"Amazon AND EC2 AND Cloud\"" +
                    "}" +
                "]";

            List<Query> queries = serializer.Deserialize<List<Query>>(source);
            Assert.AreEqual(1, queries.Count);
            Assert.AreEqual("Feature: Cloud service", queries[0].Name);
            Assert.AreEqual("Amazon AND EC2 AND Cloud", queries[0].Content);
        }

        [TestMethod] 
        public void testXmlSerializingEntities() {
            String expectedResult =
                    "<?xml version=\"1.0\"?>" +
                    "<entities xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                        "<added>" +
                            "<entity>" +
                                "<name>name 1</name>" +
                                "<type>type 1</type>" +
                            "</entity>" +
                        "</added>" +
                        "<removed>" +
                            "<entity>name 2</entity>" +
                        "</removed>" +
                    "</entities>";

            ISerializer serializer = new XmlSerializer();
            IUpdateProxy<UserEntity> proxy = Session.CreateSession("", "", serializer).CreateEntitiesUpdateProxy();
            UserEntity addedEntity = new UserEntity();
            addedEntity.Name = "name 1";
            addedEntity.Type = "type 1";
            proxy.Add(addedEntity);
            UserEntity removedEntity = new UserEntity();
            removedEntity.Name = "name 2";
            proxy.Remove(removedEntity);

            EntityManagable cm = (EntityManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod] 
        public void testJsonSerializingEntities() {
            String expectedResult =
                    "{" +
                        "\"added\":[" +
                            "{" +
                                "\"name\":\"chair\"," +
                                "\"type\":\"furniture\"" +
                            "}" +
                        "]," +
                        "\"removed\":[\"table\"]" +
                    "}";

            ISerializer serializer = new JsonSerializer();
            IUpdateProxy<UserEntity> proxy = Session.CreateSession("", "", serializer).CreateEntitiesUpdateProxy();
            UserEntity addedEntity = new UserEntity();
            addedEntity.Name = "chair";
            addedEntity.Type = "furniture";
            proxy.Add(addedEntity);
            UserEntity removedEntity = new UserEntity();
            removedEntity.Name = "table";
            proxy.Remove(removedEntity);

            EntityManagable cm = (EntityManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod] 
        public void testXmlDeserializingEntities() {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<entities>" +
                        "<entity>" +
                            "<name>chair</name>" +
                            "<type>furniture</type> " +
                        "</entity>" +
                    "</entities>";

            ISerializer serializer = new XmlSerializer();
            List<UserEntity> entities = serializer.Deserialize<UserEntities>(source).Data;
            Assert.AreEqual(1, entities.Count);
            Assert.AreEqual("chair", entities[0].Name);
            Assert.AreEqual("furniture", entities[0].Type);
        }

        [TestMethod] 
        public void testJsonDeserializingEntities() {
            String source =
                    "[" +
                        "{" +
                            "\"name\":\"chair\"," +
                            "\"type\":\"furniture\"" +
                        "}" +
                    "]";

            ISerializer serializer = new JsonSerializer();
            List<UserEntity> entities = serializer.Deserialize<List<UserEntity>>(source);
            Assert.AreEqual(1, entities.Count);
            Assert.AreEqual("chair", entities[0].Name);
            Assert.AreEqual("furniture", entities[0].Type);
        }

        [TestMethod]
        public void testXmlSerializingCategories()
        {
            String expectedResult =
                    "<?xml version=\"1.0\"?>" +
                    "<categories xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
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
                    "</categories>";

            ISerializer serializer = new XmlSerializer();
            IUpdateProxy<Category> proxy = Session.CreateSession("", "", serializer).CreateCategoriesUpdateProxy();
            Category addedCategory = new Category();
            addedCategory.Name = "Added Category 1";
            addedCategory.Weight = 0.2f;
            addedCategory.Samples = new List<string>() { "Entity 1", "Entity 2", "Entity 3" };
            proxy.Add(addedCategory);
            Category removedCategory = new Category();
            removedCategory.Name = "Removed Category 1";
            proxy.Remove(removedCategory);

            CategoryManagable cm = (CategoryManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testJsonSerializingCategories()
        {
            String expectedResult =
                    "{" +
                        "\"added\":[" +
                            "{" +
                                "\"name\":\"Feature: Cloud service\"," +
                                "\"samples\":[\"Entity 1\"]," +
                                "\"weight\":0" +
                            "}" +
                        "]," +
                        "\"removed\":[\"Features\"]" +
                    "}";

            ISerializer serializer = new JsonSerializer();
            IUpdateProxy<Category> proxy = Session.CreateSession("", "", serializer).CreateCategoriesUpdateProxy();
            Category addedCategory = new Category();
            addedCategory.Name = "Feature: Cloud service";
            addedCategory.Weight = 0.0f;
            addedCategory.Samples = new List<string>() { "Entity 1" };
            proxy.Add(addedCategory);
            Category removedCategory = new Category();
            removedCategory.Name = "Features";
            proxy.Remove(removedCategory);

            CategoryManagable cm = (CategoryManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testXmlDeserializingCategories()
        {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<categories>" +
                        "<category>" +
                            "<name>Feature: Cloud service</name>" +
                            "<weight>0.75</weight>" +
                            "<samples>" +
                                "<sample>Amazon</sample>" +
                                "<sample>EC2</sample>" +
                            "</samples>" +
                        "</category>" +
                    "</categories>";

            ISerializer serializer = new XmlSerializer();
            List<Category> categories = serializer.Deserialize<Categories>(source).Data;
            Assert.AreEqual(1, categories.Count);
            Assert.AreEqual("Feature: Cloud service", categories[0].Name);
            Assert.AreEqual(0.75, categories[0].Weight);
            Assert.AreEqual(2, categories[0].Samples.Count);
            Assert.AreEqual("Amazon", categories[0].Samples[0]);
            Assert.AreEqual("EC2", categories[0].Samples[1]);
        }

        [TestMethod]
        public void testJsonDeserializingCategories()
        {
            String source =
                    "[" +
                        "{" +
                            "\"name\":\"Feature: Cloud service\"," +
                            "\"weight\":0.75," +
                            "\"samples\":[\"Amazon\",\"EC2\"]" +
                        "}" +
                    "]";

            ISerializer serializer = new JsonSerializer();
            List<Category> categories = serializer.Deserialize<List<Category>>(source);
            Assert.AreEqual(1, categories.Count);
            Assert.AreEqual("Feature: Cloud service", categories[0].Name);
            Assert.AreEqual(0.75, categories[0].Weight);
            Assert.AreEqual(2, categories[0].Samples.Count);
            Assert.AreEqual("Amazon", categories[0].Samples[0]);
            Assert.AreEqual("EC2", categories[0].Samples[1]);
        }

        [TestMethod]
        public void testXmlSerializingSentimentPhrases()
        {
            String expectedResult =
                    "<?xml version=\"1.0\"?>" +
                    "<phrases xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                        "<added>" +
                            "<phrase>" +
                                "<title>name 1</title>" +
                                "<weight>0.3</weight>" +
                            "</phrase>" +
                        "</added>" +
                        "<removed>" +
                            "<phrase>name 2</phrase>" +
                        "</removed>" +
                    "</phrases>";

            ISerializer serializer = new XmlSerializer();
            IUpdateProxy<SentimentPhrase> proxy = Session.CreateSession("", "", serializer).CreateSentimentPhrasesUpdateProxy();
            SentimentPhrase addedPhrase = new SentimentPhrase();
            addedPhrase.Title = "name 1";
            addedPhrase.Weight = 0.3f;
            proxy.Add(addedPhrase);
            SentimentPhrase removedPhrase = new SentimentPhrase();
            removedPhrase.Title = "name 2";
            proxy.Remove(removedPhrase);

            SentimentPhraseManagable cm = (SentimentPhraseManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testJsonSerializingSentimentPhrases()
        {
            String expectedResult =
                    "{" +
                        "\"added\":[" +
                            "{" +
                                "\"title\":\"Feature: Cloud service\"," +
                                "\"weight\":0" +
                            "}" +
                        "]," +
                        "\"removed\":[\"Features\"]" +
                    "}";

            ISerializer serializer = new JsonSerializer();
            IUpdateProxy<SentimentPhrase> proxy = Session.CreateSession("", "", serializer).CreateSentimentPhrasesUpdateProxy();
            SentimentPhrase addedPhrase = new SentimentPhrase();
            addedPhrase.Title = "Feature: Cloud service";
            addedPhrase.Weight = 0.0f;
            proxy.Add(addedPhrase);
            SentimentPhrase removedPhrase = new SentimentPhrase();
            removedPhrase.Title = "Features";
            proxy.Remove(removedPhrase);

            SentimentPhraseManagable cm = (SentimentPhraseManagable)proxy.Stub;
            Regex rgx = new Regex("\\s{2,}");
            Assert.AreEqual(expectedResult, rgx.Replace(serializer.Serialize(cm), ""));
        }

        [TestMethod]
        public void testXmlDeserializingSentimentPhrases()
        {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<phrases>" +
                        "<phrase>" +
                            "<title>chair</title>" +
                            "<weight>0.75</weight> " +
                        "</phrase>" +
                    "</phrases>";

            ISerializer serializer = new XmlSerializer();
            List<SentimentPhrase> phrases = serializer.Deserialize<SentimentPhrases>(source).Data;
            Assert.AreEqual(1, phrases.Count);
            Assert.AreEqual("chair", phrases[0].Title);
            Assert.AreEqual(0.75, phrases[0].Weight);
        }

        [TestMethod]
        public void testJsonDeserializingSentimentPhrases()
        {
            String source =
                    "[" +
                        "{" +
                            "\"title\":\"chair\"," +
                            "\"weight\":0.75" +
                        "}" +
                    "]";

            ISerializer serializer = new JsonSerializer();
            List<SentimentPhrase> phrases = serializer.Deserialize<List<SentimentPhrase>>(source);
            Assert.AreEqual(1, phrases.Count);
            Assert.AreEqual("chair", phrases[0].Title);
            Assert.AreEqual(0.75, phrases[0].Weight);
        }

        [TestMethod]
        public void testXmlDeserializingAnalyticServiceStatus()
        {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<status>" +
                        "<service_status>online</service_status>" +
                        "<api_version>2.0</api_version>" +
                        "<service_version>1.0.2.63</service_version>" +
                        "<supported_encoding>UTF-8</supported_encoding>" +
                        "<supported_compression>gzip</supported_compression>" +
                        "<supported_languages>" +
                            "<language>English</language>" +
                            "<language>French</language>" +
                        "</supported_languages>" +
                    "</status>";

            ISerializer serializer = new XmlSerializer();
            Status status = serializer.Deserialize<Status>(source);
            Assert.AreEqual("online", status.Service);
            Assert.AreEqual("2.0", status.ApiVersion);
            Assert.AreEqual("1.0.2.63", status.ServiceVersion);
            Assert.AreEqual("UTF-8", status.SupportedEncoding);
            Assert.AreEqual("gzip", status.SupportedCompression);
            Assert.AreEqual(2, status.SupportedLanguages.Count);
            Assert.AreEqual("English", status.SupportedLanguages[0]);
            Assert.AreEqual("French", status.SupportedLanguages[1]);
        }

        [TestMethod]
        public void testJsonDeserializingAnalyticServiceStatus()
        {
            String source =
                    "{" +
                        "\"service_status\":\"online\"," +
                        "\"api_version\":\"2.0\"," +
                        "\"service_version\":\"1.0.2.63\"," +
                        "\"supported_encoding\":\"UTF-8\"," +
                        "\"supported_compression\":\"gzip\"," +
                        "\"supported_languages\":[" +
                                "\"English\"," +
                                "\"French\"" +
                        "]" +
                    "}";

            ISerializer serializer = new JsonSerializer();
            Status status = serializer.Deserialize<Status>(source);
            Assert.AreEqual("online", status.Service);
            Assert.AreEqual("2.0", status.ApiVersion);
            Assert.AreEqual("1.0.2.63", status.ServiceVersion);
            Assert.AreEqual("UTF-8", status.SupportedEncoding);
            Assert.AreEqual("gzip", status.SupportedCompression);
            Assert.AreEqual(2, status.SupportedLanguages.Count);
            Assert.AreEqual("English", status.SupportedLanguages[0]);
            Assert.AreEqual("French", status.SupportedLanguages[1]);
        }

        [TestMethod]
        public void testXmlDeserializingSubscription()
        {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<subscription>" +
                        "<name>name</name>" +
                        "<status>active</status>" +
                        "<priority>normal</priority>" +
                        "<expiration_date>1293883200</expiration_date>" +
                        "<calls_balance>87</calls_balance>" +
                        "<calls_limit>100</calls_limit>" +
                        "<calls_limit_interval>60</calls_limit_interval>" +
                        "<docs_balance>49897</docs_balance>" +
                        "<docs_limit>0</docs_limit>" +
                        "<docs_limit_interval>0</docs_limit_interval>" +
                        "<configurations_limit>10</configurations_limit>" +
                        "<blacklist_limit>100</blacklist_limit>" +
                        "<categories_limit>100</categories_limit>" +
                        "<queries_limit>100</queries_limit>" +
                        "<entities_limit>1000</entities_limit>" +
                        "<sentiment_limit>1000</sentiment_limit>" +
                        "<characters_limit>8192</characters_limit>" +
                        "<incoming_batch_limit>1</incoming_batch_limit>" +
                        "<collection_limit>10</collection_limit>" +
                        "<auto_response_limit>2</auto_response_limit>" +
                        "<processed_batch_limit>100</processed_batch_limit>" +
                        "<callback_batch_limit>100</callback_batch_limit>" +
                        "<limit_type>throughput</limit_type>" +
                    "</subscription>";

            ISerializer serializer = new XmlSerializer();
            Subscription subscription = serializer.Deserialize<Subscription>(source);
            Assert.AreEqual("name", subscription.Name);
            Assert.AreEqual("active", subscription.Status);
            Assert.AreEqual("normal", subscription.Priority);
            Assert.AreEqual(1293883200, subscription.ExpirationDate);
            Assert.AreEqual(87, subscription.CallsBalance);
            Assert.AreEqual(100, subscription.CallsLimit);
            Assert.AreEqual(60, subscription.CallsLimitInterval);
            Assert.AreEqual(49897, subscription.DocsBalance);
            Assert.AreEqual(0, subscription.DocsLimit);
            Assert.AreEqual(0, subscription.DocsLimitInterval);
            Assert.AreEqual(10, subscription.ConfigurationsLimit);
            Assert.AreEqual(100, subscription.BlacklistLimit);
            Assert.AreEqual(100, subscription.CategoriesLimit);
            Assert.AreEqual(100, subscription.QueriesLimit);
            Assert.AreEqual(1000, subscription.EntitiesLimit);
            Assert.AreEqual(1000, subscription.SentimentLimit);
            Assert.AreEqual(8192, subscription.CharactersLimit);
            Assert.AreEqual(1, subscription.BatchLimit);
            Assert.AreEqual(10, subscription.CollectionLimit);
            Assert.AreEqual(2, subscription.AutoResponseLimit);
            Assert.AreEqual(100, subscription.ProcessedBatchLimit);
            Assert.AreEqual(100, subscription.CallbackBatchLimit);
            Assert.AreEqual("throughput", subscription.LimitType);
        }

        [TestMethod]
        public void testJsonDeserializingSubscription()
        {
            String source =
                    "{" +
                        "\"name\" : \"Subscriber\"," +
                        "\"status\" : \"active\"," +
                        "\"priority\" : \"normal\"," +
                        "\"expiration_date\" : 1293883200," +
                        "\"calls_balance\" : 87," +
                        "\"calls_limit\" : 100," +
                        "\"calls_limit_interval\" : 60," +
                        "\"docs_balance\" : 49897," +
                        "\"docs_limit\" : 0," +
                        "\"docs_limit_interval\" : 0," +
                        "\"configurations_limit\" : 10," +
                        "\"blacklist_limit\" : 100," +
                        "\"categories_limit\" : 100," +
                        "\"queries_limit\" : 100," +
                        "\"entities_limit\" : 1000," +
                        "\"sentiment_limit\" : 1000," +
                        "\"characters_limit\" : 8192," +
                        "\"incoming_batch_limit\" : 10," +
                        "\"collection_limit\" : 10," +
                        "\"auto_response_limit\" : 2," +
                        "\"processed_batch_limit\" : 100," +
                        "\"callback_batch_limit\" : 100," +
                        "\"limit_type\" : \"throughput\"" +
                    "}";

            ISerializer serializer = new JsonSerializer();
            Subscription subscription = serializer.Deserialize<Subscription>(source);
            Assert.AreEqual("Subscriber", subscription.Name);
            Assert.AreEqual("active", subscription.Status);
            Assert.AreEqual("normal", subscription.Priority);
            Assert.AreEqual(1293883200, subscription.ExpirationDate);
            Assert.AreEqual(87, subscription.CallsBalance);
            Assert.AreEqual(100, subscription.CallsLimit);
            Assert.AreEqual(60, subscription.CallsLimitInterval);
            Assert.AreEqual(49897, subscription.DocsBalance);
            Assert.AreEqual(0, subscription.DocsLimit);
            Assert.AreEqual(0, subscription.DocsLimitInterval);
            Assert.AreEqual(10, subscription.ConfigurationsLimit);
            Assert.AreEqual(100, subscription.BlacklistLimit);
            Assert.AreEqual(100, subscription.CategoriesLimit);
            Assert.AreEqual(100, subscription.QueriesLimit);
            Assert.AreEqual(1000, subscription.EntitiesLimit);
            Assert.AreEqual(1000, subscription.SentimentLimit);
            Assert.AreEqual(8192, subscription.CharactersLimit);
            Assert.AreEqual(10, subscription.BatchLimit);
            Assert.AreEqual(10, subscription.CollectionLimit);
            Assert.AreEqual(2, subscription.AutoResponseLimit);
            Assert.AreEqual(100, subscription.ProcessedBatchLimit);
            Assert.AreEqual(100, subscription.CallbackBatchLimit);
            Assert.AreEqual("throughput", subscription.LimitType);
        }

        [TestMethod]
        public void testXmlDeserializingDocumentAnalyticData()
        {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<document>" +
                        "<config_id>23498367</config_id>" +
                        "<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" +
                        "<status>PROCESSED</status>" +
                        "<sentiment_score>0.75</sentiment_score>" +
                        "<summary>Summary of the document’s text.</summary>" +
                        "<themes>" +
                            "<theme>" +
                                "<evidence>1</evidence>" +
                                "<is_about>true</is_about>" +
                                "<strength_score>0.0</strength_score>" +
                                "<sentiment_score>0.0</sentiment_score>" +
                                "<title>republican moderates</title>" +
                            "</theme>" +
                        "</themes>" +
                        "<entities>" +
                            "<entity>" +
                                "<evidence>0</evidence>" +
                                "<is_about>true</is_about>" +
                                "<confident>true</confident>" +
                                "<title>WASHINGTON</title>" +
                                "<sentiment_score>1.5</sentiment_score>" +
                                "<type>named</type>" +
                                "<entity_type>Place</entity_type>" +
                                "<themes>" +
                                    "<theme>" +
                                        "<evidence>1</evidence>" +
                                        "<is_about>true</is_about>" +
                                        "<strength_score>0.0</strength_score>" +
                                        "<sentiment_score>0.0</sentiment_score>" +
                                        "<title>republican moderates</title>" +
                                    "</theme>" +
                                "</themes>" +
                            "</entity>" +
                        "</entities>" +
                        "<topics>" +
                            "<topic>" +
                                "<title>Something</title>" +
                                "<hitcount>0</hitcount>" +
                                "<sentiment_score>0.6</sentiment_score>" +
                                "<strength_score>0.6</strength_score>" +
                                "<type>concept</type>" +
                            "</topic>" +
                        "</topics>" +
                        "<phrases>" +
                            "<phrase>" +
                                "<title>Something</title>" +
                                "<sentiment_score>0.6</sentiment_score>" +
                                "<is_negated>true</is_negated>" +
                                "<negating_phrase>not</negating_phrase>" +
                            "</phrase>" +
                        "</phrases>" +
                    "</document>";

            ISerializer serializer = new XmlSerializer();
            DocAnalyticData analyticData = serializer.Deserialize<DocAnalyticData>(source);
            Assert.AreEqual("23498367", analyticData.ConfigId);
            Assert.AreEqual("6F9619FF8B86D011B42D00CF4FC964FF", analyticData.Id);
            Assert.AreEqual(TaskStatus.PROCESSED, analyticData.Status);
            Assert.AreEqual(0.75, analyticData.SentimentScore);
            Assert.AreEqual("Summary of the document’s text.", analyticData.Summary);
            // themes
            Assert.AreEqual(1, analyticData.Themes.Count);
            Assert.AreEqual(1, analyticData.Themes[0].Evidence);
            Assert.IsTrue(analyticData.Themes[0].IsAbout);
            Assert.AreEqual(0, analyticData.Themes[0].SentimentScore);
            Assert.AreEqual(0, analyticData.Themes[0].StrengthScore);
            Assert.AreEqual("republican moderates", analyticData.Themes[0].Title);
            // entities
            Assert.AreEqual(1, analyticData.Entities.Count);
            Assert.AreEqual(0, analyticData.Entities[0].Evidence);
            Assert.IsTrue(analyticData.Entities[0].IsAbout);
            Assert.IsTrue(analyticData.Entities[0].Confident);
            Assert.AreEqual("WASHINGTON", analyticData.Entities[0].Title);
            Assert.AreEqual("named", analyticData.Entities[0].Type);
            Assert.AreEqual(1.5, analyticData.Entities[0].SentimentScore);
            Assert.AreEqual("Place", analyticData.Entities[0].EntityType);
            // entity themes
            Assert.AreEqual(1, analyticData.Entities[0].Themes.Count);
            Assert.AreEqual(1, analyticData.Entities[0].Themes[0].Evidence);
            Assert.IsTrue(analyticData.Entities[0].Themes[0].IsAbout);
            Assert.AreEqual(0, analyticData.Entities[0].Themes[0].SentimentScore);
            Assert.AreEqual(0, analyticData.Entities[0].Themes[0].StrengthScore);
            Assert.AreEqual("republican moderates", analyticData.Entities[0].Themes[0].Title);
            // topics
            Assert.AreEqual(1, analyticData.Topics.Count);
            Assert.AreEqual("Something", analyticData.Topics[0].Title);
            Assert.AreEqual(0.6f, analyticData.Topics[0].SentimentScore);
            Assert.AreEqual(0.6f, analyticData.Topics[0].StrengthScore);
            Assert.AreEqual("concept", analyticData.Topics[0].Type);
            Assert.AreEqual(0, analyticData.Topics[0].Hitcount);
            // phrases
            Assert.AreEqual(1, analyticData.Phrases.Count);
            Assert.AreEqual("Something", analyticData.Phrases[0].Title);
            Assert.AreEqual(0.6f, analyticData.Phrases[0].SentimentScore);
            Assert.IsTrue(analyticData.Phrases[0].IsNegated);
            Assert.AreEqual("not", analyticData.Phrases[0].NegatingPhrase);
        }

        [TestMethod]
        public void testJsonDeserializingDocumentAnalyticData()
        {
            String source =
                    "[{" +
                        "\"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\"," +
                        "\"config_id\":\"23498367\"," +
                        "\"status\":\"PROCESSED\"," +
                        "\"sentiment_score\":0.8295653," +
                        "\"summary\":\"Summary of the document’s text.\"," +
                        "\"themes\":[" +
                            "{" +
                                "\"evidence\":1," +
                                "\"is_about\":true," +
                                "\"strength_score\":0.0," +
                                "\"sentiment_score\":0.0," +
                                "\"title\":\"republican moderates\"" +
                            "}" +
                        "]," +
                        "\"entities\":[" +
                            "{" +
                            "\"type\":\"named\"," +
                            "\"evidence\":0," +
                            "\"is_about\":true," +
                            "\"confident\":true," +
                            "\"entity_type\":\"Place\"," +
                            "\"title\":\"WASHINGTON\"," +
                            "\"sentiment_score\":1.0542796," +
                            "\"themes\":[" +
                                "{" +
                                    "\"evidence\":1," +
                                    "\"is_about\":true," +
                                    "\"strength_score\":0.0," +
                                    "\"sentiment_score\":0.0," +
                                    "\"title\":\"republican moderates\"" +
                                "}" +
                            "]" +
                            "}" +
                        "]," +
                        "\"topics\":[" +
                            "{" +
                                "\"title\":\"Something\"," +
                                "\"type\":\"concept\"," +
                                "\"hitcount\":0," +
                                "\"strength_score\":0.0," +
                                "\"sentiment_score\":0.6133076" +
                            "}" +
                        "]," +
                        "\"phrases\":[" +
                            "{" +
                                "\"title\":\"Something\"," +
                                "\"is_negated\":true," +
                                "\"negating_phrase\":\"not\"," +
                                "\"sentiment_score\":0.6133076" +
                            "}" +
                        "]" +
                    "}]";

            ISerializer serializer = new JsonSerializer();
            List<DocAnalyticData> analyticDatas = serializer.Deserialize<List<DocAnalyticData>>(source);
            Assert.AreEqual(1, analyticDatas.Count);
            DocAnalyticData analyticData = analyticDatas[0];
            Assert.AreEqual("23498367", analyticData.ConfigId);
            Assert.AreEqual("6F9619FF8B86D011B42D00CF4FC964FF", analyticData.Id);
            Assert.AreEqual(TaskStatus.PROCESSED, analyticData.Status);
            Assert.AreEqual(0.8295653f, analyticData.SentimentScore);
            Assert.AreEqual("Summary of the document’s text.", analyticData.Summary);
            // themes
            Assert.AreEqual(1, analyticData.Themes.Count);
            Assert.AreEqual(1, analyticData.Themes[0].Evidence);
            Assert.IsTrue(analyticData.Themes[0].IsAbout);
            Assert.AreEqual(0, analyticData.Themes[0].SentimentScore);
            Assert.AreEqual(0, analyticData.Themes[0].StrengthScore);
            Assert.AreEqual("republican moderates", analyticData.Themes[0].Title);
            // entities
            Assert.AreEqual(1, analyticData.Entities.Count);
            Assert.AreEqual(0, analyticData.Entities[0].Evidence);
            Assert.IsTrue(analyticData.Entities[0].IsAbout);
            Assert.IsTrue(analyticData.Entities[0].Confident);
            Assert.AreEqual("WASHINGTON", analyticData.Entities[0].Title);
            Assert.AreEqual("named", analyticData.Entities[0].Type);
            Assert.AreEqual(1.0542796f, analyticData.Entities[0].SentimentScore);
            Assert.AreEqual("Place", analyticData.Entities[0].EntityType);
            // entity themes
            Assert.AreEqual(1, analyticData.Entities[0].Themes.Count);
            Assert.AreEqual(1, analyticData.Entities[0].Themes[0].Evidence);
            Assert.IsTrue(analyticData.Entities[0].Themes[0].IsAbout);
            Assert.AreEqual(0, analyticData.Entities[0].Themes[0].SentimentScore);
            Assert.AreEqual(0, analyticData.Entities[0].Themes[0].StrengthScore);
            Assert.AreEqual("republican moderates", analyticData.Entities[0].Themes[0].Title);
            // topics
            Assert.AreEqual(1, analyticData.Topics.Count);
            Assert.AreEqual("Something", analyticData.Topics[0].Title);
            Assert.AreEqual(0.6133076f, analyticData.Topics[0].SentimentScore);
            Assert.AreEqual(0.0f, analyticData.Topics[0].StrengthScore);
            Assert.AreEqual("concept", analyticData.Topics[0].Type);
            Assert.AreEqual(0, analyticData.Topics[0].Hitcount);
            // phrases
            Assert.AreEqual(1, analyticData.Phrases.Count);
            Assert.AreEqual("Something", analyticData.Phrases[0].Title);
            Assert.AreEqual(0.6133076f, analyticData.Phrases[0].SentimentScore);
            Assert.IsTrue(analyticData.Phrases[0].IsNegated);
            Assert.AreEqual("not", analyticData.Phrases[0].NegatingPhrase);
        }

        [TestMethod]
        public void testXmlDeserializingCollectionAnalyticData()
        {
            String source =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<collection>" +
                        "<config_id>23498367</config_id>" +
                        "<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" +
                        "<status>PROCESSED</status>" +
                        "<facets>" +
                            "<facet>" +
                                "<label>Something</label>" +
                                "<count>10</count>" +
                                "<negative_count>2</negative_count>" +
                                "<positive_count>1</positive_count>" +
                                "<neutral_count>7</neutral_count>" +
                                "<attributes>" +
                                    "<attribute>" +
                                        "<label>Attribute</label>" +
                                        "<count>5</count>" +
                                    "</attribute>" +
                                "</attributes>" +
                            "</facet>" +
                        "</facets>" +
                        "<themes>" +
                            "<theme>" +
                                "<phrases_count>1</phrases_count>" +
                                "<themes_count>1</themes_count>" +
                                "<sentiment_score>0.0</sentiment_score>" +
                                "<title>republican moderates</title>" +
                            "</theme>" +
                        "</themes>" +
                        "<entities>" +
                            "<entity>" +
                                "<title>WASHINGTON</title>" +
                                "<type>named</type>" +
                                "<entity_type>Place</entity_type>" +
                                "<count>1</count>" +
                                "<negative_count>1</negative_count>" +
                                "<neutral_count>1</neutral_count>" +
                                "<positive_count>1</positive_count>" +
                            "</entity>" +
                        "</entities>" +
                        "<topics>" +
                            "<topic>" +
                                "<title>Something</title>" +
                                "<hitcount>0</hitcount>" +
                                "<sentiment_score>0.6133076</sentiment_score>" +
                                "<type>concept</type>" +
                            "</topic>" +
                        "</topics>" +
                    "</collection>";

            ISerializer serializer = new XmlSerializer();
            CollAnalyticData analyticData = serializer.Deserialize<CollAnalyticData>(source);
            Assert.AreEqual("23498367", analyticData.ConfigId);
            Assert.AreEqual("6F9619FF8B86D011B42D00CF4FC964FF", analyticData.Id);
            Assert.AreEqual(TaskStatus.PROCESSED, analyticData.Status);
            // facets
            Assert.AreEqual(1, analyticData.Facets.Count);
            Assert.AreEqual("Something", analyticData.Facets[0].Label);
            Assert.AreEqual(10, analyticData.Facets[0].Count);
            Assert.AreEqual(2, analyticData.Facets[0].NegativeCount);
            Assert.AreEqual(7, analyticData.Facets[0].NeutralCount);
            Assert.AreEqual(1, analyticData.Facets[0].PositiveCount);
            Assert.AreEqual(1, analyticData.Facets[0].Attributes.Count);
            Assert.AreEqual("Attribute", analyticData.Facets[0].Attributes[0].Label);
            Assert.AreEqual(5, analyticData.Facets[0].Attributes[0].Count);
            // themes
            Assert.AreEqual(1, analyticData.Themes.Count);
            Assert.AreEqual(1, analyticData.Themes[0].PhrasesCount);
            Assert.AreEqual(1, analyticData.Themes[0].ThemesCount);
            Assert.AreEqual(0, analyticData.Themes[0].SentimentScore);
            Assert.AreEqual("republican moderates", analyticData.Themes[0].Title);
            // entities
            Assert.AreEqual(1, analyticData.Entities.Count);
            Assert.AreEqual("WASHINGTON", analyticData.Entities[0].Title);
            Assert.AreEqual("named", analyticData.Entities[0].Type);
            Assert.AreEqual("Place", analyticData.Entities[0].EntityType);
            Assert.AreEqual(1, analyticData.Entities[0].Count);
            Assert.AreEqual(1, analyticData.Entities[0].NegativeCount);
            Assert.AreEqual(1, analyticData.Entities[0].NeutralCount);
            Assert.AreEqual(1, analyticData.Entities[0].PositiveCount);
            // topics
            Assert.AreEqual(1, analyticData.Topics.Count);
            Assert.AreEqual("Something", analyticData.Topics[0].Title);
            Assert.AreEqual(0.6133076f, analyticData.Topics[0].SentimentScore);
            Assert.AreEqual("concept", analyticData.Topics[0].Type);
            Assert.AreEqual(0, analyticData.Topics[0].Hitcount);
        }

        [TestMethod]
        public void testJsonDeserializingCollectionAnalyticData()
        {
            String source =
                    "{" +
                        "\"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\"," +
                        "\"config_id\":\"23498367\"," +
                        "\"status\":\"PROCESSED\"," +
                        "\"facets\":[" +
                            "{" +
                                "\"label\":\"Something\"," +
                                "\"count\":10," +
                                "\"negative_count\":2," +
                                "\"positive_count\":1," +
                                "\"neutral_count\":7," +
                                "\"attributes\":[" +
                                    "{" +
                                        "\"label\":\"Attribute\"," +
                                        "\"count\":5" +
                                    "}" +
                                "]" +
                            "}" +
                        "]," +
                        "\"themes\":[" +
                            "{" +
                                "\"phrases_count\":1," +
                                "\"themes_count\":1," +
                                "\"sentiment_score\":0.0," +
                                "\"title\":\"republican moderates\"" +
                            "}" +
                        "]," +
                        "\"entities\":[" +
                            "{" +
                                "\"type\":\"named\"," +
                                "\"entity_type\":\"Place\"," +
                                "\"title\":\"WASHINGTON\"," +
                                "\"count\":1," +
                                "\"negative_count\":1," +
                                "\"neutral_count\":1," +
                                "\"positive_count\":1" +
                            "}" +
                        "]," +
                        "\"topics\":[" +
                            "{" +
                                "\"title\":\"Something\"," +
                                "\"type\":\"concept\"," +
                                "\"hitcount\":0," +
                                "\"sentiment_score\":0.6133076" +
                            "}" +
                        "]" +
                    "}";

            ISerializer serializer = new JsonSerializer();
            CollAnalyticData analyticData = serializer.Deserialize<CollAnalyticData>(source);
            Assert.AreEqual("23498367", analyticData.ConfigId);
            Assert.AreEqual("6F9619FF8B86D011B42D00CF4FC964FF", analyticData.Id);
            Assert.AreEqual(TaskStatus.PROCESSED, analyticData.Status);
            // facets
            Assert.AreEqual(1, analyticData.Facets.Count);
            Assert.AreEqual("Something", analyticData.Facets[0].Label);
            Assert.AreEqual(10, analyticData.Facets[0].Count);
            Assert.AreEqual(2, analyticData.Facets[0].NegativeCount);
            Assert.AreEqual(7, analyticData.Facets[0].NeutralCount);
            Assert.AreEqual(1, analyticData.Facets[0].PositiveCount);
            Assert.AreEqual(1, analyticData.Facets[0].Attributes.Count);
            Assert.AreEqual("Attribute", analyticData.Facets[0].Attributes[0].Label);
            Assert.AreEqual(5, analyticData.Facets[0].Attributes[0].Count);
            // themes
            Assert.AreEqual(1, analyticData.Themes.Count);
            Assert.AreEqual(1, analyticData.Themes[0].PhrasesCount);
            Assert.AreEqual(1, analyticData.Themes[0].ThemesCount);
            Assert.AreEqual(0, analyticData.Themes[0].SentimentScore);
            Assert.AreEqual("republican moderates", analyticData.Themes[0].Title);
            // entities
            Assert.AreEqual(1, analyticData.Entities.Count);
            Assert.AreEqual("WASHINGTON", analyticData.Entities[0].Title);
            Assert.AreEqual("named", analyticData.Entities[0].Type);
            Assert.AreEqual("Place", analyticData.Entities[0].EntityType);
            Assert.AreEqual(1, analyticData.Entities[0].Count);
            Assert.AreEqual(1, analyticData.Entities[0].NegativeCount);
            Assert.AreEqual(1, analyticData.Entities[0].NeutralCount);
            Assert.AreEqual(1, analyticData.Entities[0].PositiveCount);
            // topics
            Assert.AreEqual(1, analyticData.Topics.Count);
            Assert.AreEqual("Something", analyticData.Topics[0].Title);
            Assert.AreEqual(0.6133076f, analyticData.Topics[0].SentimentScore);
            Assert.AreEqual("concept", analyticData.Topics[0].Type);
            Assert.AreEqual(0, analyticData.Topics[0].Hitcount);
        }
    }
}
