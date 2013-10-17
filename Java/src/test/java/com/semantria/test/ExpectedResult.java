package com.semantria.test;

public class ExpectedResult
{
	public static class XML
	{
		public static String addConfig =
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
			"<configurations>" +
				"<configuration>" +
					"<auto_response>true</auto_response>" +
					"<callback>https://anyapi.anydomain.com/processed/docs.json</callback>" +
					"<chars_threshold>80</chars_threshold>" +
					"<collection>" +
						"<concept_topics_limit>5</concept_topics_limit>" +
						"<facet_atts_limit>20</facet_atts_limit>" +
						"<facet_mentions_limit>0</facet_mentions_limit>" +
						"<facets_limit>15</facets_limit>" +
						"<named_entities_limit>5</named_entities_limit>" +
                        "<named_mentions_limit>10</named_mentions_limit>" +
						"<query_topics_limit>5</query_topics_limit>" +
                        "<theme_mentions_limit>10</theme_mentions_limit>" +
						"<themes_limit>0</themes_limit>" +
					"</collection>" +
					"<document>" +
                        "<auto_categories_limit>10</auto_categories_limit>" +
						"<concept_topics_limit>5</concept_topics_limit>" +
						"<detect_language>true</detect_language>" +
						"<entity_themes_limit>5</entity_themes_limit>" +
						"<named_entities_limit>5</named_entities_limit>" +
						"<named_mentions_limit>0</named_mentions_limit>" +
						"<named_opinions_limit>0</named_opinions_limit>" +
						"<named_relations_limit>0</named_relations_limit>" +
						"<phrases_limit>0</phrases_limit>" +
						"<pos_types>Noun</pos_types>" +
						"<possible_phrases_limit>0</possible_phrases_limit>" +
						"<query_topics_limit>5</query_topics_limit>" +
						"<summary_limit>0</summary_limit>" +
                        "<theme_mentions_limit>0</theme_mentions_limit>" +
						"<themes_limit>0</themes_limit>" +
						"<user_entities_limit>5</user_entities_limit>" +
						"<user_mentions_limit>0</user_mentions_limit>" +
						"<user_opinions_limit>0</user_opinions_limit>" +
						"<user_relations_limit>0</user_relations_limit>" +
					"</document>" +
					"<is_primary>false</is_primary>" +
					"<language>English</language>" +
					"<name>New configuration</name>" +
					"<one_sentence>true</one_sentence>" +
                    "<process_html>true</process_html>" +
                    "</configuration>" +
			"</configurations>";

		public static String cloneConfig =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<configurations>" +
					"<configuration>" +
						"<name>Cloned config</name>" +
						"<template>45699836</template>" +
					"</configuration>" +
				"</configurations>";

		public static String updateConfig =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<configurations>" +
					"<configuration>" +
						"<config_id>45699836</config_id>" +
						"<name>Updated config</name>" +
					"</configuration>" +
				"</configurations>";

		public static String removeConfig =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<configurations>" +
					"<configuration>45699836</configuration>" +
				"</configurations>";

		public static String blackList =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<blacklist>" +
					"<item>Added Filter 1</item>" +
				"</blacklist>";

		public static String addQuery =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<queries>" +
					"<query>" +
						"<name>Query 2</name>" +
						"<query>Something AND something</query>" +
					"</query>" +
				"</queries>";

		public static String removeQuery =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<queries>" +
					"<query>Query 2</query>" +
				"</queries>";

		public static String addEntity =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<entities>" +
					"<entity>" +
                        "<label>label 1</label>" +
						"<name>name 1</name>" +
                        "<normalized>normalized</normalized>" +
						"<type>type 1</type>" +
					"</entity>" +
				"</entities>";

		public static String addCategory =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<categories>" +
					"<category>" +
						"<name>Added Category 1</name>" +
							"<samples>" +
								"<sample>Entity 1</sample>" +
								"<sample>Entity 2</sample>" +
								"<sample>Entity 3</sample>" +
							"</samples>" +
						"<weight>0.2</weight>" +
					"</category>" +
				"</categories>";

		public static String removeCategory =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<categories>" +
					"<category>Added Category 1</category>" +
				"</categories>";

		public static String addSentimentPhrase =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<phrases>" +
					"<phrase>" +
						"<name>name 1</name>" +
						"<weight>0.3</weight>" +
					"</phrase>" +
				"</phrases>";

		public static String removeSentimentPhrase =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<phrases>" +
					"<phrase>name 1</phrase>" +
				"</phrases>";

		public static String serviceStatus =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<status>" +
					"<service_status>online</service_status>" +
					"<api_version>3.0</api_version>" +
					"<service_version>3.0.0.1251</service_version>" +
					"<supported_encoding>UTF-8</supported_encoding>" +
					"<supported_compression>gzip,deflate</supported_compression>" +
					"<supported_languages>" +
						"<language>English</language>" +
						"<language>French</language>" +
						"<language>Spanish</language>" +
						"<language>Portuguese</language>" +
						"<language>German</language>" +
					"</supported_languages>" +
				"</status>";

		public static String subscription =
				"<subscription>" +
					"<name>Subscriber</name>" +
					"<status>active</status>" +
					"<billing_settings>" +
						"<priority>normal</priority>" +
						"<expiration_date>1293883200</expiration_date>" +
						"<limit_type>pay-as-you-go</limit_type>" +
						"<calls_balance>87</calls_balance>" +
						"<calls_limit>100</calls_limit>" +
						"<calls_limit_interval>60</calls_limit_interval>" +
						"<docs_balance>49897</docs_balance>" +
						"<docs_limit>0</docs_limit>" +
						"<docs_limit_interval>0</docs_limit_interval>" +
                        "<docs_suggested>0</docs_suggested>"+
                        "<docs_suggested_interval>0</docs_suggested_interval>" +
					"</billing_settings>" +
				    "<basic_settings>" +
						"<configurations_limit>10</configurations_limit>" +
                        "<output_data_limit>10</output_data_limit>" +
						"<blacklist_limit>100</blacklist_limit>" +
						"<categories_limit>100</categories_limit>" +
						"<category_samples_limit>10</category_samples_limit>" +
						"<return_source_text>false</return_source_text>" +
						"<queries_limit>100</queries_limit>" +
						"<entities_limit>1000</entities_limit>" +
						"<sentiment_limit>1000</sentiment_limit>" +
						"<characters_limit>8192</characters_limit>" +
						"<batch_limit>10</batch_limit>" +
						"<collection_limit>100</collection_limit>" +
						"<auto_response_limit>2</auto_response_limit>" +
						"<processed_batch_limit>100</processed_batch_limit>" +
						"<callback_batch_limit>100</callback_batch_limit>" +
					"</basic_settings>" +
					"<feature_settings>" +
                        "<html_processing>true</html_processing>" +
						"<document>" +
							"<summary>true</summary>" +
                            "<auto_categories>true</auto_categories>" +
							"<themes>true</themes>" +
							"<named_entities>true</named_entities>" +
							"<user_entities>true</user_entities>" +
							"<entity_themes>false</entity_themes>" +
							"<named_relations>false</named_relations>" +
							"<user_relations>false</user_relations>" +
							"<query_topics>true</query_topics>" +
							"<concept_topics>false</concept_topics>" +
							"<sentiment_phrases>true</sentiment_phrases>" +
							"<phrases_detection>false</phrases_detection>" +
							"<pos_tagging>false</pos_tagging>" +
							"<language_detection>false</language_detection>" +
                            "<mentions>false</mentions>" +
                            "<opinions>false</opinions>" +
						"</document>" +
						"<collection>" +
							"<facets>true</facets>" +
							"<themes>true</themes>" +
							"<named_entities>true</named_entities>" +
							"<query_topics>true</query_topics>" +
							"<concept_topics>false</concept_topics>" +
						"</collection>" +
						"<supported_languages>English, French, Spanish</supported_languages>" +
					"</feature_settings>" +
				"</subscription>";

		public static String docAnalyticData =
				"<document>" +
					"<config_id>cd2e7341-a3c2-4fb4-9d3a-779e8b0a5eff</config_id>" +
					"<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" +
                    "<tag>tag</tag>" +
					"<status>PROCESSED</status>" +
					"<source_text>See Output Data Details chapter</source_text>" +
					"<language>English</language>" +
                    "<language_score>0.2398756</language_score>" +
					"<sentiment_score>0.2398756</sentiment_score>" +
					"<sentiment_polarity>positive</sentiment_polarity>" +
					"<summary>Summary of the document's text.</summary>" +
                    "<details>" +
                        "<sentence>" +
                            "<is_imperative>false</is_imperative>" +
                            "<is_polar>false</is_polar>" +
                            "<words>" +
                                "<word>" +
                                    "<tag>NNP</tag>" +
                                    "<type>Noun</type>" +
                                    "<title>Aaron</title>" +
                                    "<stemmed>Aaron</stemmed>" +
                                    "<sentiment_score>0.569</sentiment_score>" +
                                    "<is_negated>true</is_negated>" +
                                "</word>" +
                            "</words>" +
                        "</sentence>" +
                    "</details>" +
					"<phrases>" +
						"<phrase>" +
							"<title>friendly</title>" +
							"<type>detected</type>" +
							"<sentiment_score>-0.4</sentiment_score>" +
							"<sentiment_polarity>negative</sentiment_polarity>" +
							"<is_negated>true</is_negated>" +
							"<negating_phrase>not</negating_phrase>" +
						"</phrase>" +
					"</phrases >" +
					"<themes>" +
						"<theme>" +
							"<evidence>1</evidence>" +
							"<is_about>true</is_about>" +
							"<strength_score>0.0</strength_score>" +
							"<sentiment_score>0.0</sentiment_score>" +
							"<sentiment_polarity>neutral</sentiment_polarity>" +
							"<title>republican moderates</title>" +
                            "<mentions>" +
                                "<mention>" +
                                    "<label>Label</label>" +
                                    "<is_negated>Is negated</is_negated>" +
                                    "<negating_phrase>Phrase</negating_phrase>" +
                                    "<locations>" +
                                        "<location>" +
                                            "<offset>1</offset>" +
                                            "<length>2</length>" +
                                        "</location>"+
                                    "</locations>" +
                                "</mention>" +
                            "</mentions>" +
						"</theme>" +
					"</themes>" +
					"<entities>" +
						"<entity>" +
							"<evidence>0</evidence>" +
							"<confident>false</confident>" +
							"<is_about>true</is_about>" +
							"<title>WASHINGTON</title>" +
							"<sentiment_score>1.0542796</sentiment_score>" +
							"<sentiment_polarity>positive</sentiment_polarity>" +
							"<type>named</type>" +
                            "<label>Label</label>" +
							"<entity_type>Place</entity_type>" +
                            "<mentions>" +
                                "<mention>" +
                                "<label>Label</label>" +
                                "<is_negated>Is negated</is_negated>" +
                                "<negating_phrase>Phrase</negating_phrase>" +
                                    "<locations>" +
                                        "<location>" +
                                            "<offset>1</offset>" +
                                            "<length>2</length>" +
                                        "</location>"+
                                    "</locations>" +
                                "</mention>" +
                            "</mentions>" +
							"<themes>" +
								"<theme>" +
									"<evidence>1</evidence>" +
									"<is_about>true</is_about>" +
									"<strength_score>0.0</strength_score>" +
									"<sentiment_score>0.0</sentiment_score>" +
									"<sentiment_polarity>neutral</sentiment_polarity>" +
									"<title>republican moderates</title>" +
                                    "<mentions>" +
                                        "<mention>" +
                                        "<label>Label</label>" +
                                        "<is_negated>Is negated</is_negated>" +
                                        "<negating_phrase>Phrase</negating_phrase>" +
                                            "<locations>" +
                                                "<location>" +
                                                    "<offset>1</offset>" +
                                                    "<length>2</length>" +
                                                "</location>"+
                                            "</locations>" +
                                        "</mention>" +
                                    "</mentions>" +
								"</theme>" +
							"</themes>" +
						"</entity>" +
					"</entities>" +
					"<relations>" +
						"<relation>" +
							"<type>named</type>" +
							"<relation_type>Occupation</relation_type>" +
							"<confidence_score>1.0</confidence_score>" +
							"<extra>took</extra>" +
							"<entities>" +
								"<entity>" +
									"<title>head judge</title>" +
									"<entity_type>Job Title</entity_type>" +
								"</entity>" +
								"<entity>" +
									"<title>John Snow</title>" +
									"<entity_type>Person</entity_type>" +
								"</entity>" +
							"</entities>" +
						"</relation>" +
					"</relations>" +
					"<topics>" +
						"<topic>" +
							"<title>Something</title>" +
							"<hitcount>0</hitcount>" +
							"<sentiment_score>0.6133076</sentiment_score>" +
							"<sentiment_polarity>positive</sentiment_polarity>" +
							"<strength_score>0.6133076</strength_score>" +
							"<type>concept</type>" +
						"</topic>" +
					"</topics>" +
                    "<opinions>" +
                        "<opinion>" +
                            "<quotation>Quotation</quotation>" +
                            "<type>type</type>" +
                            "<speaker>Speaker</speaker>" +
                            "<topic></topic>" +
                            "<sentiment_score>0.5</sentiment_score>" +
                            "<sentiment_polarity>neutral</sentiment_polarity>" +
                        "</opinion>" +
                    "</opinions>" +
                    "<auto_categories>" +
                        "<category>" +
                            "<title>title</title>" +
                            "<type>type</type>" +
                            "<strength_score>0.1</strength_score>" +
                            "<categories>" +
                                "<category>" +
                                    "<title>title</title>" +
                                    "<type>type</type>" +
                                    "<strength_score>0.1</strength_score>" +
                                "</category>" +
                            "</categories>" +
                        "</category>" +
                    "</auto_categories>" +
				"</document>";

		public static String collAnalyticData =
				"<collection>" +
					"<config_id>23498367</config_id>" +
					"<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" +
                    "<tag>tag</tag>" +
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
                            "<mentions>" +
                                "<mention>" +
                                    "<label>Label</label>" +
                                    "<is_negated>Is negated</is_negated>" +
                                    "<negating_phrase>Phrase</negating_phrase>" +
                                    "<locations>" +
                                        "<location>" +
                                            "<offset>1</offset>" +
                                            "<length>2</length>" +
                                            "<index>1</index>" +
                                        "</location>"+
                                    "</locations>" +
                                "</mention>" +
                            "</mentions>" +
						"</facet>" +
					"</facets>" +
					"<themes>" +
						"<theme>" +
							"<phrases_count>1</phrases_count>" +
							"<themes_count>1</themes_count>" +
							"<sentiment_score>0.0</sentiment_score>" +
							"<title>republican moderates</title>" +
							"<sentiment_polarity>positive</sentiment_polarity>" +
						"</theme>" +
					"</themes>" +
					"<entities>" +
						"<entity>" +
							"<title>WASHINGTON</title>" +
							"<type>named</type>" +
                            "<label>Label</label>" +
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
							"<sentiment_polarity>positive</sentiment_polarity>" +
						"</topic>" +
					"</topics>" +
					"</collection>";

		public static String statistics =
				"<statistics>" +
					"<name>Subscriber</name>" +
					"<status>active</status>" +
					"<overall_texts>129863</overall_texts>" +
					"<overall_batches>1300</overall_batches>" +
					"<overall_calls>13769</overall_calls>" +
					"<calls_system>21</calls_system>" +
					"<calls_data>13748</calls_data>" +
					"<overall_docs>121863</overall_docs>" +
					"<docs_processed>121860</docs_processed>" +
					"<docs_failed>3</docs_failed>" +
					"<docs_responded>121863</docs_responded>" +
					"<overall_colls>8</overall_colls>" +
					"<colls_processed>8</colls_processed>" +
					"<colls_failed>0</colls_failed>" +
					"<colls_responded>8</colls_responded>" +
					"<colls_documents>8000</colls_documents>" +
					"<latest_used_app>.Net</latest_used_app>" +
					"<used_apps>.Net,Excel add-in x86,Python</used_apps>" +
                    "<overall_exceeded>0</overall_exceeded>" +
					"<configurations>" +
						"<configuration>" +
							"<config_id>cd2e7341-a3c2-4fb4-9d3a-779e8b0a5eff</config_id>" +
							"<name>A test configuration</name>" +
							"<overall_texts>129863</overall_texts>" +
							"<overall_batches>1300</overall_batches>" +
							"<overall_calls>13769</overall_calls>" +
							"<calls_system>21</calls_system>" +
							"<calls_data>13748</calls_data>" +
							"<overall_docs>121863</overall_docs>" +
							"<docs_processed>121860</docs_processed>" +
							"<docs_failed>3</docs_failed>" +
							"<docs_responded>121863</docs_responded>" +
							"<overall_colls>8</overall_colls>" +
							"<colls_processed>8</colls_processed>" +
							"<colls_failed>0</colls_failed>" +
							"<colls_responded>8</colls_responded>" +
							"<colls_documents>8000</colls_documents>" +
							"<latest_used_app>.Net</latest_used_app>" +
							"<used_apps>.Net,Excel add-in x86,Python</used_apps>" +
                            "<overall_exceeded>0</overall_exceeded>" +
						"</configuration>" +
					"</configurations>" +
				"</statistics>";

		public static String collsAnalyticData =
				"<collections>" +
					collAnalyticData +
				"</collections>";

		public static String docsAnalyticData =
				"<documents>" +
						docAnalyticData +
				"</documents>";

		public static String batch =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<documents>" +
						"<document>" +
							"<id>1232142</id>" +
							"<text>Text for analizing</text>" +
						"</document>" +
				"</documents>";

		public static String document =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<document>" +
						"<id>1232142</id>" +
						"<text>Text for analizing</text>" +
				"</document>";

	}

	public static class JSON
	{
		public static String addConfig =
					"[" +
						"{" +
							"\"name\":\"New configuration\"," +
							"\"auto_response\":true," +
							"\"is_primary\":false," +
							"\"one_sentence\":true," +
							"\"language\":\"English\"," +
							"\"chars_threshold\":80," +
							"\"document\":{" +
								"\"entity_themes_limit\":5," +
								"\"summary_limit\":0," +
								"\"themes_limit\":0," +
								"\"query_topics_limit\":5," +
								"\"concept_topics_limit\":5," +
								"\"named_entities_limit\":5," +
								"\"user_entities_limit\":5," +
								"\"phrases_limit\":0," +
								"\"detect_language\":true," +
								"\"possible_phrases_limit\":0," +
								"\"pos_types\":\"Noun\"," +
								"\"named_relations_limit\":0," +
								"\"user_relations_limit\":0," +
								"\"named_mentions_limit\":0," +
                                "\"theme_mentions_limit\":0," +
                                "\"user_mentions_limit\":0," +
								"\"named_opinions_limit\":0," +
								"\"user_opinions_limit\":0," +
                                "\"auto_categories_limit\":10" +
							"}," +
							"\"collection\":{" +
								"\"facets_limit\":15," +
								"\"facet_atts_limit\":20," +
								"\"themes_limit\":0," +
								"\"named_entities_limit\":5," +
								"\"query_topics_limit\":5," +
								"\"concept_topics_limit\":5," +
								"\"facet_mentions_limit\":0," +
                                "\"theme_mentions_limit\":10," +
                                "\"named_mentions_limit\":10" +
							"}," +
							"\"callback\":\"https://anyapi.anydomain.com/processed/docs.json\"," +
                            "\"process_html\":true" +
						"}" +
					"]";

		public static String cloneConfig =
				"[" +
						"{" +
							"\"template\":\"45699836\"," +
							"\"name\":\"Cloned config\"" +
						"}" +
				"]";

		public static String updateConfig =
				"[" +
						"{" +
							"\"config_id\":\"45699836\"," +
							"\"name\":\"Updated config\"" +
						"}" +
				"]";

		public static String removeConfig =
				"[" +
					"\"45699836\"" +
				"]";

		public static String blackList =
				"[" +
					"\"Added Filter 1\"" +
				"]";

		public static String addQuery =
				"[" +
					"{" +
						"\"name\":\"Feature: Cloud service\"," +
						"\"query\":\"Amazon AND EC2 AND Cloud\"" +
					"}" +
				"]";

		public static String removeQuery =
				"[" +
					"\"Feature: Cloud service\"" +
				"]";

		public static String addEntity =
				"[" +
					"{" +
						"\"name\":\"name 1\"," +
						"\"type\":\"type 1\"," +
                        "\"label\":\"label 1\"," +
                        "\"normalized\":\"normalized\"" +
                    "}" +
				"]";

		public static String addCategory =
				"[" +
					"{" +
						"\"name\":\"Added Category 1\"," +
						"\"weight\":0.2," +
						"\"samples\":[" +
							"\"Entity 1\"," +
							"\"Entity 2\"," +
							"\"Entity 3\"" +
						"]" +
					"}" +
				"]";

		public static String removeCategory =
				"[" +
					"\"Added Category 1\"" +
				"]";

		public static String addSentimentPhrase =
				"[" +
					"{" +
						"\"name\":\"name 1\"," +
						"\"weight\":0.3" +
					"}" +
				"]";

		public static String removeSentimentPhrase =
				"[" +
					"\"name 1\"" +
				"]";

		public static String serviceStatus =
				"{" +
					"\"service_status\" : \"online\"," +
					"\"api_version\" : \"3.0\"," +
					"\"service_version\" : \"3.0.0.1251\"," +
					"\"supported_encoding\" : \"UTF-8\"," +
					"\"supported_compression\" : \"gzip,deflate\"," +
					"\"supported_languages\" : [" +
						"\"English\"," +
						"\"French\"," +
						"\"Spanish\"," +
						"\"Portuguese\"," +
						"\"German\"" +
					"]" +
				"}";

		public static String subscription =
			"{" +
				"\"name\" : \"Subscriber\"," +
				"\"status\" : \"active\"," +
				"\"billing_settings\" : {" +
					"\"priority\" : \"normal\"," +
					"\"expiration_date\" : 1293883200," +
					"\"limit_type\" : \"pay-as-you-go\"," +
					"\"calls_balance\" : 87," +
					"\"calls_limit\" : 100," +
					"\"calls_limit_interval\" : 60," +
					"\"docs_balance\" : 49897," +
					"\"docs_limit\" : 0," +
					"\"docs_limit_interval\" : 0," +
                    "\"docs_suggested\" : 0," +
                    "\"docs_suggested_interval\" : 0" +
				"}," +
				"\"basic_settings\" : {" +
                    "\"output_data_limit\" : 10," +
					"\"configurations_limit\" : 10," +
					"\"blacklist_limit\" : 100," +
					"\"categories_limit\" : 100," +
					"\"category_samples_limit\" : 10," +
					"\"return_source_text\" : false," +
					"\"queries_limit\" : 100," +
					"\"entities_limit\" : 1000," +
					"\"sentiment_limit\" : 1000," +
					"\"characters_limit\" : 8192," +
					"\"batch_limit\" : 10," +
					"\"collection_limit\" : 100," +
					"\"auto_response_limit\" : 2," +
					"\"processed_batch_limit\" : 100," +
					"\"callback_batch_limit\" : 100" +
				"}," +
				"\"feature_settings\" : {" +
                    "\"html_processing\" : true," +
					"\"document\" : {" +
						"\"summary\" : true," +
                        "\"auto_categories\" : true," +
						"\"themes\" : true," +
						"\"named_entities\" : true," +
						"\"user_entities\" : true," +
						"\"entity_themes\" : false," +
						"\"named_relations\" : false," +
						"\"user_relations\" : false," +
						"\"query_topics\" : true," +
						"\"concept_topics\" : false," +
						"\"sentiment_phrases\" : true," +
						"\"phrases_detection\" : false," +
						"\"pos_tagging\" : false," +
						"\"language_detection\" : false," +
                        "\"mentions\" : false," +
                        "\"opinions\" : false" +
					"}," +
					"\"collection\" : {" +
						"\"facets\" : true," +
						"\"themes\" : true," +
						"\"named_entities\" : true," +
						"\"user_entities\" : true," +
						"\"query_topics\" : true," +
						"\"concept_topics\" : false" +
					"}," +
					"\"supported_languages\" : \"English, French, Spanish\"" +
				"}" +
			"}";

		public static String docAnalyticData =
			"{" +
				"\"id\" : \"6F9619FF8B86D011B42D00CF4FC964FF\"," +
				"\"config_id\" : \"cd2e7341-a3c2-4fb4-9d3a-779e8b0a5eff\"," +
				"\"status\" : \"PROCESSED\"," +
                "\"tag\":\"tag\","+
				"\"source_text\" : \"See Output Data Details chapter\"," +
				"\"language\" : \"English\"," +
                "\"language_score\" : 0.2398756," +
				"\"sentiment_score\" : 0.2398756," +
				"\"sentiment_polarity\" : \"positive\"," +
				"\"summary\" : \"Summary of the document's text.\"," +
				"\"details\" : [" +
					"{" +
						"\"is_imperative\" : false," +
						"\"is_polar\" : false," +
						"\"words\" : [" +
							"{" +
								"\"tag\" : \"NNP\"," +
								"\"type\" : \"Noun\"," +
								"\"title\" : \"Aaron\"," +
								"\"stemmed\" : \"Aaron\"," +
								"\"sentiment_score\" : 0.569," +
                                "\"is_negated\" : \"true\"" +
							"}" +
						"]" +
					"}" +
				"]," +
				"\"phrases\" : [" +
					"{" +
						"\"title\" : \"friendly\"," +
						"\"type\" : \"detected\"," +
						"\"sentiment_score\" : -0.4," +
						"\"sentiment_polarity\" : \"negative\"," +
						"\"is_negated\" : true," +
						"\"negating_phrase\" : \"not\"" +
					"}" +
				"]," +
				"\"themes\" : [" +
					"{" +
						"\"evidence\" : 1," +
						"\"is_about\" : true," +
						"\"strength_score\" : 0.0," +
						"\"sentiment_score\" : 0.0," +
						"\"sentiment_polarity\" : \"neutral\"," +
						"\"title\" : \"republican moderates\"," +
                        "\"mentions\": [" +
                            "{" +
                                "\"label\":\"Label\"," +
                                "\"is_negated\":true," +
                                "\"negating_phrase\":\"negator\"," +
                                "\"locations\": [" +
                                    "{" +
                                        "\"offset\":1," +
                                        "\"length\":1" +
                                    "}" +
                                "]" +
                            "}"+
                        "]"+
					"}" +
				"]," +
				"\"entities\" : [" +
					"{" +
						"\"type\" : \"named\"," +
						"\"evidence\" : 0," +
						"\"confident\" : false," +
						"\"is_about\" : true," +
						"\"entity_type\" : \"Place\"," +
						"\"title\" : \"WASHINGTON\"," +
                        "\"label\" : \"Label\"," +
						"\"sentiment_score\" : 1.0542796," +
						"\"sentiment_polarity\" : \"positive\"," +
                    "\"mentions\": [" +
                        "{" +
                            "\"label\":\"Label\"," +
                            "\"is_negated\":true," +
                            "\"negating_phrase\":\"negator\"," +
                            "\"locations\": [" +
                                "{" +
                                    "\"offset\":1," +
                                    "\"length\":1" +
                                "}" +
                            "]" +
                        "}"+
                    "],"+
                    "\"themes\" : [" +
							"{" +
                                "\"evidence\" : 1," +
                                "\"is_about\" : true," +
                                "\"strength_score\" : 0.0," +
                                "\"sentiment_score\" : 0.0," +
                                "\"sentiment_polarity\" : \"neutral\"," +
                                "\"title\" : \"republican moderates\"," +
                                "\"mentions\": [" +
                                    "{" +
                                        "\"label\":\"Label\"," +
                                        "\"is_negated\":true," +
                                        "\"negating_phrase\":\"negator\"," +
                                        "\"locations\": [" +
                                            "{" +
                                                "\"offset\":1," +
                                                "\"length\":1" +
                                            "}" +
                                        "]" +
                                    "}"+
                                "]"+
                            "}" +
						"]" +
					"}" +
				"]," +
				"\"relations\" : [" +
					"{" +
						"\"type\" : \"named\"," +
						"\"relation_type\" : \"Occupation\"," +
						"\"confidence_score\" : 1.0," +
						"\"extra\" : \"took\"," +
						"\"entities\" : [" +
							"{" +
								"\"title\" : \"head judge\"," +
								"\"entity_type\" : \"Job Title\"" +
							"}," +
							"{" +
								"\"title\" : \"John Snow\"," +
								"\"entity_type\" : \"Person\"" +
							"}" +
						"]" +
					"}" +
				"]," +
				"\"topics\" : [" +
					"{" +
						"\"title\" : \"Something\"," +
						"\"type\" : \"concept\"," +
						"\"hitcount\" : 0," +
						"\"strength_score\" : 0.6133076," +
						"\"sentiment_score\" : 0.6133076," +
						"\"sentiment_polarity\" : \"positive\"" +
					"}" +
				"]," +
                "\"opinions\" : [" +
                    "{" +
                        "\"quotation\" : \"Quotation\"," +
                        "\"type\" : \"Type\"," +
                        "\"speaker\" : \"Speaker\"," +
                        "\"topic\" : \"Topic\"," +
                        "\"sentiment_score\" : 0.5," +
                        "\"sentiment_polarity\" : \"neutral\"" +
                    "}"+
                "]," +
                "\"auto_categories\" : [" +
                    "{" +
                        "\"title\" : \"title\"," +
                        "\"type\" : \"type\"," +
                        "\"strength_score\" : 0.1," +
                        "\"categories\" : [" +
                            "{" +
                                "\"title\" : \"title\"," +
                                "\"type\" : \"type\"," +
                                "\"strength_score\" : 0.1" +
                            "}"+
                        "]" +
                    "}"+
                "]" +
        "}";

		public static String collAnalyticData =
				"{"+
					"\"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\","+
					"\"config_id\":\"23498367\","+
					"\"status\":\"PROCESSED\","+
                    "\"tag\":\"tag\","+
					"\"facets\":["+
						"{"+
							"\"label\":\"Something\","+
							"\"count\":10,"+
							"\"negative_count\":2,"+
							"\"positive_count\":1,"+
							"\"neutral_count\":7,"+
							"\"attributes\":["+
								"{"+
									"\"label\":\"Attribute\","+
									"\"count\":5"+
								"}"+
							"],"+
							"\"mentions\":" +
							"[" +
								"{" +
									"\"label\":\"Label\"," +
									"\"is_negated\":true," +
									"\"negating_phrase\":\"negator\"," +
									"\"locations\":" +
									"[" +
										"{" +
                                            "\"offset\":1," +
                                            "\"length\":1," +
                                            "\"index\":1" +
                                        "}" +
									"]" +
								"}"+
							"]"+
						"}" +
					"]," +
					"\"themes\":["+
						"{"+
							"\"phrases_count\":1,"+
							"\"themes_count\":1,"+
							"\"sentiment_score\":0.0,"+
							"\"title\":\"republican moderates\","+
							"\"sentiment_polarity\":\"positive\","+
                            "\"mentions\": [" +
                                "{" +
                                    "\"label\":\"Label\"," +
                                    "\"is_negated\":true," +
                                    "\"negating_phrase\":\"negator\"," +
                                    "\"locations\": [" +
                                        "{" +
                                            "\"offset\":1," +
                                            "\"length\":1," +
                                            "\"index\":1" +
                                        "}" +
                                    "]" +
                                "}"+
                            "]"+
                        "}"+
					"],"+
					"\"entities\":["+
						"{"+
							"\"type\":\"named\","+
							"\"entity_type\":\"Place\","+
							"\"title\":\"WASHINGTON\","+
                            "\"label\" : \"Label\"," +
							"\"count\":1,"+
							"\"negative_count\":1,"+
							"\"neutral_count\":1,"+
							"\"positive_count\":1,"+
                            "\"mentions\": [" +
                                "{" +
                                    "\"label\":\"Label\"," +
                                    "\"is_negated\":true," +
                                    "\"negating_phrase\":\"negator\"," +
                                    "\"locations\": [" +
                                        "{" +
                                            "\"offset\":1," +
                                            "\"length\":1," +
                                            "\"index\":1" +
                                        "}" +
                                    "]" +
                                "}"+
                            "]"+
                        "}"+
					"],"+
					"\"topics\":["+
						"{"+
							"\"title\":\"Something\","+
							"\"type\":\"concept\","+
							"\"hitcount\":0,"+
							"\"sentiment_score\":0.6133076,"+
							"\"sentiment_polarity\":\"positive\""+
						"}"+
					"]"+
				"}";

		public static String statistics =
				"{" +
					"\"name\" : \"Subscriber\"," +
					"\"status\" : \"active\"," +
					"\"overall_texts\" : 129863," +
					"\"overall_batches\" : 1300," +
					"\"overall_calls\" : 13769," +
					"\"calls_system\" : 21," +
					"\"calls_data\" : 13748," +
					"\"overall_docs\" : 121863," +
					"\"docs_processed\" : 121860," +
					"\"docs_failed\" : 3," +
					"\"docs_responded\" : 121863," +
					"\"overall_colls\" : 8," +
					"\"colls_processed\" : 8," +
					"\"colls_failed\" : 0," +
					"\"colls_responded\" : 8," +
					"\"colls_documents\" : 8000," +
					"\"latest_used_app\" : \".Net\"," +
					"\"used_apps\" : \".Net,Excel add-in x86,Python\"," +
                    "\"overall_exceeded\" : 0," +
					"\"configurations\" : [" +
						"{" +
						"\"config_id\" : \"cd2e7341-a3c2-4fb4-9d3a-779e8b0a5eff\"," +
							"\"name\" : \"A test configuration\"," +
							"\"overall_texts\" : 129863," +
							"\"overall_batches\" : 1300," +
							"\"overall_calls\" : 13769," +
							"\"calls_system\" : 21," +
							"\"calls_data\" : 13748," +
							"\"overall_docs\" : 121863," +
							"\"docs_processed\" : 121860," +
							"\"docs_failed\" : 3," +
							"\"docs_responded\" : 121863," +
							"\"overall_colls\" : 8," +
							"\"colls_processed\" : 8," +
							"\"colls_failed\" : 0," +
							"\"colls_responded\" : 8," +
							"\"colls_documents\" : 8000," +
							"\"latest_used_app\" : \".Net\"," +
							"\"used_apps\" : \".Net,Excel add-in x86,Python\"," +
                            "\"overall_exceeded\" : 0" +
						"}" +
					"]" +
				"}";

		public static String collsAnalyticData =
				"[" +
					collAnalyticData +
				"]";

		public static String docsAnalyticData =
				"[" +
					docAnalyticData +
				"]";
	}
}