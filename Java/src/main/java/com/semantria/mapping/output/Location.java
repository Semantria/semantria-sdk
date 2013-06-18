package com.semantria.mapping.output;

public class Location
{
	private Integer m_offset = null;
	private Integer m_length = null;

	public Location() {}

	public Location(Integer offset, Integer length)
	{
		m_offset = offset;
		m_length = length;
	}

	public Integer getOffset() { return m_offset; }
	public Integer getLength() { return m_length; }

	public void setOffset(Integer offset) { m_offset = offset; }
	public void setLength(Integer length) { m_length = length;	}
}
