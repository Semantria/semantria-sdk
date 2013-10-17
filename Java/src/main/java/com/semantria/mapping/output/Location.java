package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class Location
{
	private Integer offset = null;
	private Integer length = null;
    private Integer index = null;

	public Location() {}

	public Location(Integer offset, Integer length, Integer index)
	{
		this.offset = offset;
		this.length = length;
        this.index = index;
	}

    @XmlElement(name = "offset")
	public Integer getOffset() { return offset; }
    @XmlElement(name = "length")
	public Integer getLength() { return length; }
    @XmlElement(name = "index")
    public Integer getIndex() { return index; }

	public void setOffset(Integer offset) { this.offset = offset; }
	public void setLength(Integer length) { this.length = length;	}
    public void setIndex(Integer index) { this.index = index; }
}
