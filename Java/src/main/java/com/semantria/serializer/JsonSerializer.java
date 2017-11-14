package com.semantria.serializer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.configuration.*;
import com.semantria.mapping.configuration.stub.*;
import com.semantria.mapping.output.*;
import com.semantria.mapping.output.statistics.StatisticsGrouped;
import com.semantria.mapping.output.statistics.StatisticsOverall;
import com.semantria.mapping.output.stub.*;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class JsonSerializer implements ISerializer
{

	public String serialize(Object object)
	{
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	public Object deserialize(String string, Class<?> type)
	{
		Object object = null;
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        // @NOTE: processing case with timestamp without milliseconds
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonPrimitive el = json.getAsJsonPrimitive();
                if (el != null) {
                    String date = el.getAsString();
                    if (date.length() == 10) {
                        date += "000";
                    }
                    return new Date(Long.valueOf(date));
                } else {
                    return null;
                }
            }
        });
        Gson gson = builder.create();

		if(string.length() > 0)
		{
			if(type.equals(Blacklists.class))
			{
				Type listType = new TypeToken<List<BlacklistItem>>() {}.getType();
				object = new Blacklists( (List<BlacklistItem>)gson.fromJson(string, listType) );
			}
			else if(type.equals(Categories.class))
			{
				Type listType = new TypeToken<List<Category>>() {}.getType();
				object = new Categories( (List<Category>)gson.fromJson(string, listType));
			}
			else if(type.equals(Configurations.class))
			{
				Type listType = new TypeToken<List<Configuration>>() {}.getType();
				object = new Configurations( (List<Configuration>)gson.fromJson(string, listType));
			}
			else if(type.equals(Queries.class))
			{
				Type listType = new TypeToken<List<Query>>() {}.getType();
				object = new Queries( (List<Query>)gson.fromJson(string, listType));
			}
			else if(type.equals(SentimentPhrases.class))
			{
				Type listType = new TypeToken<List<SentimentPhrase>>() {}.getType();
				object = new SentimentPhrases( (List<SentimentPhrase>)gson.fromJson(string, listType));
			}
            else if(type.equals(Taxonomies.class))
            {
                Type listType = new TypeToken<List<TaxonomyNode>>() {}.getType();
                object = new Taxonomies( (List<TaxonomyNode>)gson.fromJson(string, listType));
            }
			else if(type.equals(DocsAnalyticData.class))
			{
				Type listType = new TypeToken<List<DocAnalyticData>>() {}.getType();
				object = new DocsAnalyticData( (List<DocAnalyticData>)gson.fromJson(string, listType));
			}
			else if(type.equals(CollsAnalyticData.class))
			{
				Type listType = new TypeToken<List<CollAnalyticData>>() {}.getType();
				object = new CollsAnalyticData( (List<CollAnalyticData>)gson.fromJson(string, listType));
			}
			else if(type.equals(DocAnalyticData.class))
			{
				object = gson.fromJson(string, DocAnalyticData.class);
			}
			else if(type.equals(ServiceStatus.class))
			{
				object = gson.fromJson(string, ServiceStatus.class);
			}
			else if(type.equals(Subscription.class))
			{
				object = gson.fromJson(string, Subscription.class);
			}
			else if(type.equals(UserEntities.class))
			{
				Type listType = new TypeToken< List<UserEntity>>() {}.getType();
				object = new UserEntities( (List<UserEntity>)gson.fromJson(string, listType));
			}
			else if(type.equals(CollAnalyticData.class))
			{
				object = gson.fromJson(string, CollAnalyticData.class);
			}
            else if(type.equals(StatisticsOverallList.class))
            {
                Type listType = new TypeToken< List<StatisticsOverall>>() {}.getType();
                object = new StatisticsOverallList( (List<StatisticsOverall>)gson.fromJson(string, listType));
            }
            else if(type.equals(StatisticsGroupedList.class))
            {
                Type listType = new TypeToken< List<StatisticsGrouped>>() {}.getType();
                object = new StatisticsGroupedList( (List<StatisticsGrouped>)gson.fromJson(string, listType));
            }
            else if(type.equals(FeaturesList.class))
            {
                Type listType = new TypeToken< List<FeaturesSet>>() {}.getType();
                object = new FeaturesList( (List<FeaturesSet>)gson.fromJson(string, listType));
            }

		}
		return object;
	}

	public String getType()
	{
		return "json";
	}

}