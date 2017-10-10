package com.semantria.interfaces;

public interface ISerializer
{
	String getType();
	String serialize(Object object);
	Object deserialize(String string, Class<?> type);
}
