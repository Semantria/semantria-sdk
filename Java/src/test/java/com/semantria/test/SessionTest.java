package com.semantria.test;

import com.semantria.Session;
import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.Collection;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.*;
import com.semantria.mapping.output.*;
import com.semantria.serializer.JsonSerializer;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SessionTest
{
    private String key = "";
    private String secret = "";

	private Configuration m_config = null;
	private ISerializer serializer = new JsonSerializer();

	@Test
	public void test01CreateSessionStringStringISerializer()
	{
		Session session = Session.createSession(key, secret, serializer);
		assertEquals(Session.class, session.getClass());
	}

	@Test
	public void test02GetStatus() 
	{
		Session session = Session.createSession(key, secret, serializer);
		session.setCallbackHandler(new CallbackHandler());
		ServiceStatus status = session.getStatus();
		assertEquals("available", status.getServiceStatus());
	}

    @Test
    public void test03GetSupportedFeatures()
    {
        Session session = Session.createSession(key, secret, serializer);
        session.setCallbackHandler(new CallbackHandler());
        List<FeaturesSet> features = session.getSupportedFeatures("English");
        assertEquals("English", features.get(0).getLanguage());
    }

	@Test
	public void test04GetSubscription()
	{
		Session session = Session.createSession(key, secret, serializer);
		session.setCallbackHandler(new CallbackHandler());
		Subscription subscription = session.getSubscription();
		assertEquals("active", subscription.getStatus());
	}

	@Test
	public void test05Statistics()
	{
		Session session = Session.createSession(key, secret, serializer);
		session.setCallbackHandler(new CallbackHandler());
		Statistics statistics = session.getStatistics("day");
		assertEquals("active", statistics.getStatus());
	}

	@Test
	public void test06CreateUpdateCloneConfiguration()
	{
		Session session = Session.createSession(key, secret, serializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());

		Configuration conf = new Configuration();
		conf.setAutoResponse(false);
		conf.setIsPrimary(false);
		conf.setName("TEST_CONFIG");
		conf.setLanguage("English");
		conf.setDocument(new DocConfiguration(false, false, 0, 5, 5, 5, 5, 5, 5, 5, true, 0, "", 0, 0, 0, 0, 0, 0, 0, 5));
		conf.setCollection(new CollConfiguration(5, 5, 5, 5, 0, 5, 5, 0, 0, 0, 0, 0));

		int status = session.addConfigurations(Arrays.asList(conf));
		System.out.println(status);
		
		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				m_config = config;
				break;
			}
		}
		assertNotNull(m_config.getId());
		assertEquals(m_config.getLanguage(), new String("English"));
		assertEquals(new Integer(80), m_config.getCharsThreshold());

		m_config.setCharsThreshold(20);
		session.updateConfigurations( Arrays.asList( m_config ) );
		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				m_config = config;
				break;
			}
		}
		assertEquals(new Integer(20), m_config.getCharsThreshold());

		m_config.setTemplate( m_config.getId() );
		m_config.setName( "CLONED_TEST_CONFIG" );

		session.addConfigurations( Arrays.asList( m_config ) );

		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("CLONED_TEST_CONFIG"))
			{
				m_config = config;
				break;
			}
		}

		assertEquals("CLONED_TEST_CONFIG", m_config.getName());
		assertEquals(new Integer(20), m_config.getCharsThreshold());
	}

	@Test
	public void test07CRUDCategory()
	{
		String configId = null;
		Session session = Session.createSession(key, secret, serializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());

		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("CLONED_TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);
		
		Category category = new Category();
		category.setName("TEST_CATEGORY_NAME");
		List<String> samples = new ArrayList<String>();
		samples.add("TEST_CATEGORY_SAMPLE");
		category.setSamples(samples);
		
		session.addCategories(Arrays.asList(category), configId);
		
		category = null;
		for(Category cat : session.getCategories(configId))
		{
			if(cat.getName().equals("TEST_CATEGORY_NAME"))
			{
				category = cat;
			}
		}
		assertNotNull(category);

		category.setWeight(1.0F);
		session.updateCategories(Arrays.asList(category), configId);
		
		category = null;
		for(Category cat : session.getCategories(configId))
		{
			if(cat.getName().equals("TEST_CATEGORY_NAME"))
			{
				category = cat;
			}
		}
		assertEquals( new Float(1.0), category.getWeight());

		session.removeCategories(Arrays.asList(category), configId);
		category = null;
		for(Category cat : session.getCategories(configId))
		{
			if(cat.getName().equals("TEST_CATEGORY_NAME"))
			{
				category = cat;
			}
		}
		assertNull(category);
	}

	@Test
	public void test08CRUDQuery()
	{
		String configId = null;
		Session session = Session.createSession(key, secret, serializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());

		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("CLONED_TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);
		
		Query query = new Query();
		query.setName("TEST_QUERY_NAME");
		query.setQuery("TEST AND QUERY");

		session.addQueries(Arrays.asList(query), configId);

		query = null;
		for(Query qry : session.getQueries(configId))
		{
			if(qry.getName().equals("TEST_QUERY_NAME"))
			{
				query = qry;
			}
		}
		assertNotNull(query);

		query.setQuery("TEST AND QUERY AND UPDATE");
		session.updateQueries(Arrays.asList(query), configId);

		query = null;
		for(Query qry : session.getQueries(configId))
		{
			if(qry.getName().equals("TEST_QUERY_NAME"))
			{
				query = qry;
			}
		}
		assertNotNull(query);
		assertEquals("TEST AND QUERY AND UPDATE", query.getQuery());

		session.removeQueries(Arrays.asList(query), configId);

		query = null;
		for(Query qry : session.getQueries(configId))
		{
			if(qry.getName().equals("TEST_QUERY_NAME"))
			{
				query = qry;
			}
		}
		assertNull(query);
	}

    @Test
    public void test09CRUDSentimentPhrase()
    {
        String configId = null;
        Session session = Session.createSession(key, secret, serializer);
        ////////////////////////////////////////////////
        session.setCallbackHandler(new CallbackHandler());

        SentimentPhrase sentimentPhrase = new SentimentPhrase();
        sentimentPhrase.setName("TEST_NAME");
        sentimentPhrase.setWeight(0.1f);

        session.addSentimentPhrases(Arrays.asList(sentimentPhrase), configId);

        sentimentPhrase = null;
	    List<SentimentPhrase> phrases =  session.getSentimentPhrases(configId);
	    assertNotNull( phrases );
        for(SentimentPhrase phrase : phrases )
        {
            if(phrase.getName().equals("TEST_NAME"))
            {
                sentimentPhrase = phrase;
            }
        }
        assertNotNull(sentimentPhrase);

        session.removeSentimentPhrases(Arrays.asList(sentimentPhrase), configId);

        sentimentPhrase = null;
        for(SentimentPhrase phrase : session.getSentimentPhrases(configId))
        {
            if(phrase.getName().equals("TEST_NAME"))
            {
                sentimentPhrase = phrase;
            }
        }
        assertNull(sentimentPhrase);
    }

	@Test
	public void test10CRUDBlacklist()
	{
		String configId = null;
		Session session = Session.createSession(key, secret, serializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());

		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);
		
		session.addBlacklist(Arrays.asList("TEST_BLACKLISTED"), configId);
		
		String item = null;
		for(String bl : session.getBlacklist(configId))
		{
			if(bl.equals("TEST_BLACKLISTED"))
			{
				item = bl;
			}
		}
		assertNotNull(item);
		
		session.removeBlacklist(Arrays.asList("TEST_BLACKLISTED"), configId);
		
		item = null;
		for(String bl : session.getBlacklist(configId))
		{
			if(bl.equals("TEST_BLACKLISTED"))
			{
				item = bl;
			}
		}
		assertNull(item);
	}

	@Test
	public void test11CRUDEntities()
	{
		String configId = null;
		Session session = Session.createSession(key, secret, serializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());

		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);

		UserEntity ue = new UserEntity();
		ue.setName("TEST_USER_ENTITY");
		ue.setType("TEST_USER_ENTITY_TYPE");

		session.addEntities(Arrays.asList(ue), configId);
		
		String item = null;
		for(UserEntity en : session.getEntities(configId))
		{
			if(en.getName().equals("TEST_USER_ENTITY"))
			{
				item = en.getName();
			}
		}
		assertNotNull(item);

		ue.setType("TEST_USER_ENTITY_TYPE_UPDATED");
		session.updateEntities(Arrays.asList(ue), configId);

		ue = null;
		for(UserEntity el : session.getEntities(configId))
		{
			if(el.getName().equals("TEST_USER_ENTITY"))
			{
				ue = el;
			}
		}
		assertNotNull(ue);
		assertEquals("TEST_USER_ENTITY_TYPE_UPDATED", ue.getType());

		session.removeEntities(Arrays.asList(ue), configId);
		
		item = null;
		for(UserEntity el : session.getEntities(configId))
		{
			if(el.getName().equals("TEST_USER_ENTITY"))
			{
				item = el.getName();
			}
		}
		assertNull(item);
	}

	@Test
	public void test12AnalyzeSingleDocument()
	{
		String configId = null;
		Session session = Session.createSession(key, secret, serializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);

		session.queueDocument(new Document("TEST_ID_1", "Amazon Web Services has announced a new feature called VM£Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud. It will cost 30£", "tag"), configId);

		DocAnalyticData task = null;
		for ( int i = 0; i < 5; i++ )
		{
			task = session.getDocument("TEST_ID_1", configId);
			if (task != null)
			{
				break;
			}
			
			try
			{
				Thread.sleep(1000L);
			} catch (InterruptedException e)
			{
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}

		assertEquals(  TaskStatus.PROCESSED, task.getStatus() );
	}

	@Test
	public void test13AnalyzeBatchOfDocuments()
	{
		String configId = null;
		Session session = Session.createSession(key, secret, serializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);

		List<Document> tasks = new ArrayList<Document>();
		tasks.add(new Document("BATCH_1", "DUMMY_TEXT"));
		tasks.add(new Document("BATCH_2", "DUMMY_TEXT"));
		
		session.queueBatch(tasks, configId);
		
		List<DocAnalyticData> data = null;
		for ( int i = 0; i < 5; i++ )
		{
			data = session.getProcessedDocuments(configId);
			if (data != null)
			{
				break;
			}
			
			try
			{
				Thread.sleep(1000L);
			} catch (InterruptedException e)
			{
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
		
		DocAnalyticData doc = null;
		if( data != null && data.isEmpty() == false )
		{
			for (DocAnalyticData docAnalyticData : data)
			{
				if( docAnalyticData.getId().equals("BATCH_1") || docAnalyticData.getId().equals("BATCH_2") )
				{
					doc = docAnalyticData;
				}
			}
		}
		
		assertNotNull(doc);
	}

	@Test
	public void test14AnalyzeCollection()
	{
		String configId = null;
		Session session = Session.createSession(key, secret, serializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		
		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);
		
		Collection coll = new Collection();
		coll.setId("TEST_COLLECTION_ID");
		List<String> documents = new ArrayList<String>();
		documents.add("test test for processing - 1");
		documents.add("test test for processing - 2");
		documents.add("test test for processing - 3");
		coll.setDocuments(documents);
		
		session.queueCollection(coll, configId);
		
		List<CollAnalyticData> data = null;
		for ( int i = 0; i < 5; i++ )
		{
			data = session.getProcessedCollections(configId);
			if (data != null)
			{
				break;
			}
			
			try
			{
				Thread.sleep(1000L);
			} catch (InterruptedException e)
			{
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
		
		CollAnalyticData col = null;
		if( data != null && data.isEmpty() == false )
		{
			for (CollAnalyticData colAnalyticData : data)
			{
				if( colAnalyticData.getId().equals("TEST_COLLECTION_ID") )
				{
					col = colAnalyticData;
				}
			}
		}
		
		assertNotNull(col);
	}

	@Test
	public void test15Cleanup()
	{
		Session session = Session.createSession(key, secret, serializer);

		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());

		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG") || config.getName().equals("CLONED_TEST_CONFIG"))
			{
				session.removeConfigurations(Arrays.asList(config));
			}
		}

		Configuration cfg = null;
		for(Configuration conf : session.getConfigurations())
		{
			if(conf.getName().equals("TEST_CONFIG") || conf.getName().equals("CLONED_TEST_CONFIG"))
			{
				cfg = conf;
			}
		}
		assertNull(cfg);
	}
}
