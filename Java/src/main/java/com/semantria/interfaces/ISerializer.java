package com.semantria.interfaces;

public interface ISerializer
{
	public String getType();
	public String serialize(Object object);
	public Object deserialize(String string, Class<?> type);
}
