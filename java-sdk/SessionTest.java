import com.semantria.core.Session;
import com.semantria.interfaces.ISerializer;
import com.semantria.interfaces.IUpdateProxy;
import com.semantria.objects.configuration.*;
import com.semantria.objects.mapping.Stub_Collection;
import com.semantria.objects.mapping.Stub_Document;
import com.semantria.objects.output.*;
import com.semantria.serializer.JsonSerializer;
import com.semantria.serializer.XmlSerializer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SessionTest
{
    private String key = "";
    private String secret = "";

	@Test
	public void testCreateSessionStringStringISerializer()
	{
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
		assertEquals(Session.class, session.getClass());
	}

	@Test
	public void testGetStatus() 
	{
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
		ServiceStatus status = session.getStatus();
		assertEquals("available", status.getServiceStatus());
	}
	
	@Test
	public void testCreateUpdateConfiguration()
	{
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		IUpdateProxy<Configuration> cup = session.createConfigurationsUpdateProxy();
		
		Configuration conf = new Configuration();
		conf.setAutoResponding(true);
		conf.setIsPrimary(false);
		conf.setName("TEST_CONFIG");
		conf.setLanguage("English");
		conf.setDocument(new Stub_Document(3, 2, 1, 5, 4, 5, 6, 5));
		conf.setCollection(new Stub_Collection(4, 3, 5, 5, 5, 5));
		conf.setCallback("https://callback.url/point.xml");
		cup.add(conf);
		session.updateConfigurations(cup);
		
		Configuration created = new Configuration();
		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				created = config;
				break;
			}
		}
		assertNotNull(created.getId());
		assertEquals(created.getLanguage().toString(), new String("English"));
	}
	
	@Test
	public void testCRUDCategory()
	{
		String configId = null;
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		IUpdateProxy<Category> cup = session.createCategoriesUpdateProxy();

		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
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
		
		cup.add(category);
		session.updateCategories(cup, configId);
		
		category = null;
		for(Category cat : session.getCategories(configId))
		{
			if(cat.getName().equals("TEST_CATEGORY_NAME"))
			{
				category = cat;
			}
		}
		assertNotNull(category);
		
		cup = session.createCategoriesUpdateProxy();
		cup.remove(category);
		session.updateCategories(cup, configId);
		
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
	public void testCRUDQuery()
	{
		String configId = null;
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		IUpdateProxy<Query> qup = session.createQueriesUpdateProxy();

		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);
		
		Query query = new Query();
		query.setName("TEST_QUERY_NAME");
		query.setQuery("TEST AND QUERY");
		
		qup.add(query);
		session.updateQueries(qup, configId);
		
		query = null;
		for(Query qry : session.getQueries(configId))
		{
			if(qry.getName().equals("TEST_QUERY_NAME"))
			{
				query = qry;
			}
		}
		assertNotNull(query);
		
		qup = session.createQueriesUpdateProxy();
		qup.remove(query);
		session.updateQueries(qup, configId);
		
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
    public void testCRUDSentimentPhrase()
    {
        String configId = null;
        ISerializer xmlSerializer = new XmlSerializer();
        Session session = Session.createSession(key, secret, xmlSerializer);
        ////////////////////////////////////////////////
        session.setCallbackHandler(new CallbackHandler());
        IUpdateProxy<SentimentPhrase> proxy = session.createSentimentPhraseUpdateProxy();

        SentimentPhrase sentimentPhrase = new SentimentPhrase();
        sentimentPhrase.setTitle("TEST_NAME");
        sentimentPhrase.setWeight(0.1f);

        proxy.add(sentimentPhrase);
        session.updateSentimentPhrases(proxy, configId);

        sentimentPhrase = null;
        for(SentimentPhrase phrase : session.getSentimentPhrases(configId))
        {
            if(phrase.getTitle().equals("TEST_NAME"))
            {
                sentimentPhrase = phrase;
            }
        }
        assertNotNull(sentimentPhrase);

        proxy = session.createSentimentPhraseUpdateProxy();
        proxy.remove(sentimentPhrase);
        session.updateSentimentPhrases(proxy, configId);

        sentimentPhrase = null;
        for(SentimentPhrase phrase : session.getSentimentPhrases(configId))
        {
            if(phrase.getTitle().equals("TEST_NAME"))
            {
                sentimentPhrase = phrase;
            }
        }
        assertNull(sentimentPhrase);
    }

	@Test
	public void testCRUDBlacklist() 
	{
		String configId = null;
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		IUpdateProxy<String> bup = session.createBlacklistUpdateProxy();
		
		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG"))
			{
				configId = config.getId();
			}
		}
		assertNotNull(configId);
		
		bup.add("TEST_BLACKLISTED");
		session.updateBlacklist(bup, configId);
		
		String item = null;
		for(String bl : session.getBlacklist(configId))
		{
			if(bl.equals("TEST_BLACKLISTED"))
			{
				item = bl;
			}
		}
		assertNotNull(item);
		
		bup = session.createBlacklistUpdateProxy();
		bup.remove("TEST_BLACKLISTED");
		session.updateBlacklist(bup, configId);
		
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
	public void testCRUDEntities()
	{
		String configId = null;
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		IUpdateProxy<UserEntity> eup = session.createEntitiesUpdateProxy();
		
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
		eup.add(ue);
		session.updateEntities(eup, configId);
		
		String item = null;
		for(UserEntity en : session.getEntities(configId))
		{
			if(en.getName().equals("TEST_USER_ENTITY"))
			{
				item = en.getName();
			}
		}
		assertNotNull(item);
		
		eup = session.createEntitiesUpdateProxy();
		eup.remove(ue);
		session.updateEntities(eup, configId);
		
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
	public void testQueueTask() 
	{
		String configId = null;
		ISerializer xmlSerializer = new JsonSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
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

		session.queueDocument(new Document("TEST_ID_1", "Amazon Web Services has announced a new feature called VM£Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud. It will cost 30£"), configId);
        DocAnalyticData task = session.getDocument("TEST_ID_1", configId);
		assertEquals(TaskStatus.QUEUED, task.getStatus());
		
		session.cancelDocument("TEST_ID_1", configId);
		assertNull(session.getDocument("TEST_ID", configId));
	}

	@Test
	public void testQueueBatch() 
	{
		String configId = null;
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
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

		DocAnalyticData task = session.getDocument("BATCH_2", configId);
		assertEquals(TaskStatus.QUEUED, task.getStatus());
		task = session.getDocument("BATCH_2", configId);
		assertEquals(TaskStatus.QUEUED, task.getStatus());
		
		session.cancelDocument("BATCH_1", configId);
		session.cancelDocument("BATCH_2", configId);
		assertNull(session.getDocument("BATCH_2", configId));
	}
	
	@Test
	public void testQueueCollection()
	{
		String configId = null;
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
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
		coll.setId("TEST_ID");
		List<String> documents = new ArrayList<String>();
		documents.add("test test for processing - 1");
		documents.add("test test for processing - 2");
		documents.add("test test for processing - 3");
		coll.setDocuments(documents);
		session.queueCollection(coll, configId);
		CollAnalyticData task = session.getCollection("TEST_ID", configId);
		assertEquals(TaskStatus.QUEUED, task.getStatus());
		
		session.cancelCollection("TEST_ID", configId);
		assertNull(session.getCollection("TEST_ID", configId));
	}
	
	@Test
	public void testCleanup()
	{
		ISerializer xmlSerializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, xmlSerializer);
		////////////////////////////////////////////////
		session.setCallbackHandler(new CallbackHandler());
		IUpdateProxy<Configuration> cup = session.createConfigurationsUpdateProxy();
		
		for(Configuration config : session.getConfigurations())
		{
			if(config.getName().equals("TEST_CONFIG") || config.getName().equals("CLONED_CONFIG"))
			{
				cup.remove(config);
			}
		}
		session.updateConfigurations(cup);
		
		Configuration cfg = null;
		for(Configuration conf : session.getConfigurations())
		{
			if(conf.getName().equals("TEST_CONFIG") || conf.getName().equals("CLONED_CONFIG"))
			{
				cfg = conf;
			}
		}
		assertNull(cfg);
	}

}
