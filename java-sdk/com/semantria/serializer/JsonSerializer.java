package com.semantria.serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.*;
import com.google.gson.Gson;
import com.semantria.interfaces.ISerializer;
import com.semantria.objects.configuration.*;
import com.semantria.objects.mapping.*;
import com.semantria.objects.output.CollAnalyticData;
import com.semantria.objects.output.DocAnalyticData;
import com.semantria.objects.output.ServiceStatus;
import com.semantria.objects.output.Subscription;

public final class JsonSerializer implements ISerializer 
{

	public String serialize(Object object)
	{
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	public Object deserialize(String string, Class<?> type)
	{
		Object object = null;
		Gson gson = new Gson();
		if(string.length() > 0)
		{
			if(type.equals(Stub_Blacklist.class))
			{
				Type listType = new TypeToken<List<String>>() {}.getType();
				List<String> list = gson.fromJson(string, listType);
				Stub_Blacklist bl = new Stub_Blacklist();
				bl.setBlacklist(list);
				object = (Object)bl;
			}
			if(type.equals(Stub_Categories.class))
			{
				Type listType = new TypeToken<List<Category>>() {}.getType();
				List<Category> list = gson.fromJson(string, listType);
				Stub_Categories cats = new Stub_Categories();
				cats.setCategories(list);
				object = (Object)cats;
			}
			if(type.equals(Stub_Configurations.class))
			{
				Type listType = new TypeToken<List<Configuration>>() {}.getType();
				List<Configuration> list = gson.fromJson(string, listType);
				Stub_Configurations confs = new Stub_Configurations();
				confs.setConfigurations(list);
				object = (Object)confs;
			}
			if(type.equals(Stub_Queries.class))
			{
				Type listType = new TypeToken<List<Query>>() {}.getType();
				List<Query> list = gson.fromJson(string, listType);
				Stub_Queries queries = new Stub_Queries();
				queries.setQueries(list);
				object = (Object)queries;
			}
            if(type.equals(Stub_SentimentPhrase.class))
            {
                Type listType = new TypeToken<List<SentimentPhrase>>() {}.getType();
                List<SentimentPhrase> list = gson.fromJson(string, listType);
                Stub_SentimentPhrase phrases = new Stub_SentimentPhrase();
                phrases.setSentimentPhrases(list);
                object = (Object)phrases;
            }
			if(type.equals(Stub_DocAnalyticDatas.class))
			{
				Type listType = new TypeToken<List<DocAnalyticData>>() {}.getType();
				List<DocAnalyticData> list = gson.fromJson(string, listType);
				Stub_DocAnalyticDatas tasks = new Stub_DocAnalyticDatas();
				tasks.setDocuments(list);
				object = (Object)tasks;
			}
			if(type.equals(Stub_CollAnalyticDatas.class))
			{
				Type listType = new TypeToken<List<CollAnalyticData>>() {}.getType();
				List<CollAnalyticData> list = gson.fromJson(string, listType);
				Stub_CollAnalyticDatas tasks = new Stub_CollAnalyticDatas();
				tasks.setDocuments(list);
				object = (Object)tasks;
			}
			if(type.equals(DocAnalyticData.class))
			{
				DocAnalyticData task = gson.fromJson(string, DocAnalyticData.class);
				object = (Object)task;
			}
			if(type.equals(ServiceStatus.class))
			{
				ServiceStatus status = gson.fromJson(string, ServiceStatus.class);
				object = (Object)status;
			}
			if(type.equals(Subscription.class))
			{
				Subscription subscription = gson.fromJson(string, Subscription.class);
				object = (Object)subscription;
			}
			if(type.equals(Stub_Entities.class))
			{
                Type listType = new TypeToken<List<UserEntity>>() {}.getType();
                List<UserEntity> list = gson.fromJson(string, listType);
                Stub_Entities entities = new Stub_Entities();
                entities.setEntities(list);
                object = (Object)entities;
			}
			if(type.equals(CollAnalyticData.class))
			{
				CollAnalyticData collection = gson.fromJson(string, CollAnalyticData.class);
				object = (CollAnalyticData)collection;
			}
			
		}
		return object;
	}
	
	public String getType()
	{
		return "json";
	}

}
