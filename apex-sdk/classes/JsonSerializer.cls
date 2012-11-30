public class JsonSerializer extends Serializer {
	private static final String SERIALIZER_TYPE = 'json';
	
	public override String getType() {
		return SERIALIZER_TYPE;
	}

	public override String serialize(Object obj, String className) {
		return JSON.serialize(obj);	
	}
	
	public override Object deserialize(String source, String className) {
		source = escapeSource(source);
		if ('BlacklistItem'.equals(className)) {
            return parseBlacklist(source);
		} else if ('Query'.equals(className)) {
            return parseQuery(source);
		} else if ('UserEntity'.equals(className)) {
            return parseUserEntity(source);
		} else if ('SentimentPhrase'.equals(className)) {
			return parseSentimentPhrase(source);
		} else if ('Category'.equals(className)) {
            return parseCategory(source);
		} else if ('Configuration'.equals(className)) {
            return parseConfiguration(source);
		} else if ('ServiceStatus'.equals(className)) {
            return parseAnalyticServiceStatus(source);
		} else if ('Subscription'.equals(className)) {
            return parseSubscription(source);
		} else if ('DocAnalyticData'.equals(className)) {
            return parseDocAnalyticData(source);
		} else if ('DocAnalyticDataList'.equals(className)) {
            return parseDocAnalyticDataList(source);
		} else if ('CollAnalyticData'.equals(className)) {
            return parseCollAnalyticData(source);
		} else if ('CollAnalyticDataList'.equals(className)) {
            return parseCollAnalyticDataList(source);
		} else {
			throw new IllegalStateException('Unsupported class: ' + className);
		}
	}
	
	private String escapeSource(String originalSource) {
		return originalSource.replaceAll('\\\\','\\\\\\\\');
	}
	
	private List<String> parseBlacklist(String source) {
		if (isCorrectSource(source)) {
        	return (List<String>)JSON.deserialize(source, List<String>.class);
		} else {
			return new List<String>();
		}
	}
	
	private Boolean isCorrectSource(String source) {
		return (null != source && !''.equals(source) && (source.startsWith('{') || source.startsWith('[')));
	}
	
	private List<Query> parseQuery(String source) {
		if (isCorrectSource(source)) {
        	return (List<Query>)JSON.deserialize(source, List<Query>.class);
		} else {
			return new List<Query>();
		}
	}
	
	private List<UserEntity> parseUserEntity(String source) {
		if (isCorrectSource(source)) {
        	return (List<UserEntity>)JSON.deserialize(source, List<UserEntity>.class);
		} else {
			return new List<UserEntity>();
		}
	}
	
	private List<SentimentPhrase> parseSentimentPhrase(String source) {
        if (isCorrectSource(source)) {
            return (List<SentimentPhrase>)JSON.deserialize(source, List<SentimentPhrase>.class);
        } else {
            return new List<SentimentPhrase>();
        }
    }
	
	private List<Category> parseCategory(String source) {
		if (isCorrectSource(source)) {
        	return (List<Category>)JSON.deserialize(source, List<Category>.class);
		} else {
			return new List<Category>();
		}
	}
	
	private List<Configuration> parseConfiguration(String source) {
		if (isCorrectSource(source)) {
        	return (List<Configuration>)JSON.deserialize(source, List<Configuration>.class);
		} else {
			return new List<Configuration>();
		}
	}
	
	private ServiceStatus parseAnalyticServiceStatus(String source) {
		if (isCorrectSource(source)) {
        	return (ServiceStatus)JSON.deserialize(source, ServiceStatus.class);
		} else {
			return null;
		}
	}
	
	private Subscription parseSubscription(String source) {
		if (isCorrectSource(source)) {
        	return (Subscription)JSON.deserialize(source, Subscription.class);
		} else {
			return null;
		}
	}
	
	private DocAnalyticData parseDocAnalyticData(String source) {
		if (isCorrectSource(source)) {
        	return (DocAnalyticData)JSON.deserialize(source, DocAnalyticData.class);
		} else {
			return null;
		}
	}
	
	private List<DocAnalyticData> parseDocAnalyticDataList(String source) {
		if (isCorrectSource(source)) {
        	return (List<DocAnalyticData>)JSON.deserialize(source, List<DocAnalyticData>.class);
		} else {
			return new List<DocAnalyticData>();
		}
	}
	
	private CollAnalyticData parseCollAnalyticData(String source) {
		if (isCorrectSource(source)) {
        	return (CollAnalyticData)JSON.deserialize(source, CollAnalyticData.class);
		} else {
			return null;
		}
	}
	
	private List<CollAnalyticData> parseCollAnalyticDataList(String source) {
		System.debug(Logginglevel.ERROR, source);
		if (isCorrectSource(source)) {
        	return (List<CollAnalyticData>)JSON.deserialize(source, List<CollAnalyticData>.class);
		} else {
			return new List<CollAnalyticData>();
		}
	}
}