package com.semantria.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.semantria.interfaces.ISerializer;
import com.semantria.objects.mapping.*;
import com.semantria.objects.output.CollAnalyticData;
import com.semantria.objects.output.Collection;
import com.semantria.objects.output.DocAnalyticData;
import com.semantria.objects.output.ServiceStatus;
import com.semantria.objects.output.Subscription;
import com.semantria.objects.output.Document;
import com.semantria.proxies.*;
import com.semantria.proxies.EntityUpdateProxy;

public final class XmlSerializer implements ISerializer
{
	public Object deserialize(String input, Class<?> type)
	{
		Object obj = null;
		if(input.length() > 0)
		{
			try
			{
				ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
				JAXBContext jc = JAXBContext.newInstance(Stub_Blacklist.class, Stub_Categories.class, Stub_SentimentPhrase.class,
                        Stub_Configurations.class, Stub_Queries.class, Stub_DocAnalyticDatas.class, Stub_CollAnalyticDatas.class,
                        DocAnalyticData.class, ServiceStatus.class, Subscription.class, Stub_Entities.class, CollAnalyticData.class);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				obj = unmarshaller.unmarshal(inputStream);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		return obj;
	}
	
	public String serialize(Object obj)
	{
		String res = null;
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			JAXBContext jc = JAXBContext.newInstance(CategoryUpdateProxy.class, BlacklistUpdateProxy.class,
                    ConfigurationUpdateProxy.class, QueryUpdateProxy.class, EntityUpdateProxy.class, Stub_Tasks.class,
                    Collection.class, SentimentPhraseUpdateProxy.class);
			Marshaller marshaller = jc.createMarshaller();
			if((obj.getClass().equals(List.class)) || (obj.getClass().equals(ArrayList.class)))
			{
				ArrayList<Document> tasks = (ArrayList<Document>)obj;
				Stub_Tasks taskList = new Stub_Tasks();
				taskList.setTasks(tasks);
				marshaller.marshal(taskList, os);
				res = os.toString();
			}
			else
			{
				marshaller.marshal(obj, os);
				res = os.toString();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return res;
	}
	
	public String getType()
	{
		return "xml";
	}
}
