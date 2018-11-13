package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class Location
{
    private Integer offset = null;
    private Integer length = null;
    private Integer index = null;
    private Integer sentence = null;
    private Integer token_index = null;
    private Integer token_count = null;

    public Location() {}

    public Location(Integer offset, Integer length, Integer index)
    {
        this.offset = offset;
        this.length = length;
        this.index = index;
    }
    
    public Location(Integer offset, Integer length, Integer index, Integer sentence, Integer token_index, Integer token_count)
    {
        this.offset = offset;
        this.length = length;
        this.index = index;
        this.sentence = sentence;
        this.token_index = token_index;
        this.token_count = token_count;
    }

    @XmlElement(name = "offset")
    public Integer getOffset() { return offset; }
    @XmlElement(name = "length")
    public Integer getLength() { return length; }
    @XmlElement(name = "index")
    public Integer getIndex() { return index; }
    @XmlElement(name = "sentence")
    public Integer getSentence() { return sentence; }
    @XmlElement(name = "token_index")
    public Integer getTokenIndex() { return token_index; }
    @XmlElement(name = "token_count")
    public Integer getTokenCount() { return token_count; }

    public void setOffset(Integer offset) { this.offset = offset; }
    public void setLength(Integer length) { this.length = length;	}
    public void setIndex(Integer index) { this.index = index; }
    public void setSentence(Integer sentence) { this.sentence = sentence; }
    public void setTokenIndex(Integer token_index) { this.token_index = token_index; }
    public void setTokenCount(Integer token_count) { this.token_count = token_count; }
}
