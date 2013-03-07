package com.semantria.utils;

import com.semantria.mapping.Batch;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.*;
import com.semantria.mapping.configuration.stub.*;

import java.util.ArrayList;
import java.util.List;

public class ObjProxy
{
	public static Object wrap(List<?> obj, Class<?> type, String method)
	{
		return wrap(obj, type, method, "Xml");
	}
	public static Object wrap(List<?> obj, Class<?> type, String method, String format)
	{
		Object res = obj;

		if( format.equals("Xml") )
		{
			if( type.equals(Configurations.class) )
			{
				if( method.equals("DELETE") )
				{
					res = new ConfigurationsDeleteReq( (List<Configuration>)obj );
				}
				else
				{
					res = new Configurations( (List<Configuration>)obj );
				}
			}
			else if( type.equals(Blacklists.class) )
			{
				res = new Blacklists( (List<String>)obj );
			}
			else if( type.equals(Categories.class) )
			{
				if( method.equals("DELETE") )
				{
					res = new CategoriesDeleteReq( (List<Category>)obj );
				}
				else
				{
					res = new Categories( (List<Category>)obj );
				}
			}
			else if( type.equals(Queries.class) )
			{
				if( method.equals("DELETE") )
				{
					res = new QueriesDeleteReq( (List<Query>)obj );
				}
				else
				{
					res = new Queries( (List<Query>)obj );
				}
			}
			else if( type.equals(SentimentPhrases.class) )
			{
				if( method.equals("DELETE") )
				{
					res = new SentimentPhrasesDeleteReq( (List<SentimentPhrase>)obj );
				}
				else
				{
					res = new SentimentPhrases( (List<SentimentPhrase>)obj );
				}
			}
			else if( type.equals(UserEntities.class) )
			{
				if( method.equals("DELETE") )
				{
					res = new UserEntitiesDeleteReq( (List<UserEntity>)obj );
				}
				else
				{
					res = new UserEntities( (List<UserEntity>)obj );
				}
			}
			else if( type.equals(Batch.class) )
			{
				res = new Batch( (List<Document>) obj);
			}
		}
		else if( format.equals("Json") )
		{
			if( type.equals(Configurations.class) )
			{
				if( method.equals("DELETE") )
				{
					List<Configuration> configurations = (List<Configuration>)obj;
					if( obj != null && !obj.isEmpty() )
					{
						List<String> list = new ArrayList<String>();

						for (Configuration configuration : configurations)
						{
							list.add( configuration.getId() );
						}

						res = list;
					}
				}
			}
			else if( type.equals(Blacklists.class) )
			{
				//res = (List<String>)obj;
			}
			else if( type.equals(Categories.class) )
			{
				if( method.equals("DELETE") )
				{
					List<Category> categories = (List<Category>)obj;
					if( obj != null && !obj.isEmpty() )
					{
						List<String> list = new ArrayList<String>();

						for (Category category : categories)
						{
							list.add( category.getName() );
						}

						res = list;
					}
				}
			}
			else if( type.equals(Queries.class) )
			{
				if( type.equals(Queries.class) )
				{
					if( method.equals("DELETE") )
					{
						List<Query> queries = (List<Query>)obj;
						if( obj != null && !obj.isEmpty() )
						{
							List<String> list = new ArrayList<String>();

							for (Query query : queries)
							{
								list.add( query.getName() );
							}

							res = list;
						}
					}
				}
			}
			else if( type.equals(SentimentPhrases.class) )
			{
				if( method.equals("DELETE") )
				{
					List<SentimentPhrase> phrases = (List<SentimentPhrase>)obj;
					if( obj != null && !obj.isEmpty() )
					{
						List<String> list = new ArrayList<String>();

						for (SentimentPhrase phrase : phrases)
						{
							list.add( phrase.getName() );
						}

						res = list;
					}
				}
			}
			else if( type.equals(UserEntities.class) )
			{
				if( method.equals("DELETE") )
				{
					//res = new UserEntitiesDeleteReq( (List<UserEntity>)obj );
					List<UserEntity> entities = (List<UserEntity>)obj;
					if( obj != null && !obj.isEmpty() )
					{
						List<String> list = new ArrayList<String>();

						for (UserEntity entity : entities)
						{
							list.add( entity.getName() );
						}

						res = list;
					}
				}
			}
			else if( type.equals(Batch.class) )
			{
				res = new Batch( (List<Document>) obj);
			}
		}

		return res;
	}

	public static List<?> unwrap(Object obj)
	{
		List<?> res = null;

		if( obj instanceof Configurations )
		{
			res = ((Configurations)obj).getConfigurations();
		}
		else if( obj instanceof Blacklists )
		{
			res = ((Blacklists)obj).getBlacklist();
		}
		else if( obj instanceof Categories )
		{
			res = ((Categories)obj).getCategories();
		}
		else if( obj instanceof Queries )
		{
			res = ((Queries)obj).getQueries();
		}
		else if( obj instanceof  SentimentPhrases )
		{
			res = ((SentimentPhrases)obj).getSentimentPhrases();
		}
		else if( obj instanceof UserEntities )
		{
			res = ((UserEntities)obj).getEntities();
		}
		else if( obj instanceof Batch )
		{
			res = ((Batch)obj).getDocuments();
		}

		return res;
	}
}
