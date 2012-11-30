package com.semantria.interfaces;

public interface IUpdateProxy<T>
{
    public static final String REMOVE_ALL_ITEMS_MARK = "*";

	public void add(T object);
	public void remove(T id);
	public void update(T object);
	public void clone(T object);
    public void removeAll();
}
