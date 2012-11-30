public abstract class Serializer {
	public abstract String getType();
	public abstract String serialize(Object obj, String className); 
	public abstract Object deserialize(String source, String className);
}