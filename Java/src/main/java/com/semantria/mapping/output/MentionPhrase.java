package com.semantria.mapping.output;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlElement;


public class MentionPhrase {

    private String title;
    private int document;
    private int sentence;
    private int word;
    private int length;
    @SerializedName("byte_offset")
    private int byteOffset;
    @SerializedName("byte_length")
    private int byteLength;
    @SerializedName("is_negated")
    private boolean isNegated;
    private String negator;
    private int type;
    private int section;

    public MentionPhrase() {
    }

    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    @XmlElement(name = "document")
    public int getDocument() {
        return document;
    }

    @XmlElement(name = "sentence")
    public int getSentence() {
        return sentence;
    }

    @XmlElement(name = "word")
    public int getWord() {
        return word;
    }

    @XmlElement(name = "length")
    public int getLength() {
        return length;
    }

    @XmlElement(name = "byte_offset")
    public int getByteOffset() {
        return byteOffset;
    }

    @XmlElement(name = "byte_length")
    public int getByteLength() {
        return byteLength;
    }

    @XmlElement(name = "is_negated")
    public boolean isNegated() {
        return isNegated;
    }

    @XmlElement(name = "negator")
    public String getNegator() {
        return negator;
    }

    @XmlElement(name = "type")
    public int getType() {
        return type;
    }

    @XmlElement(name = "section")
    public int getSection() {
        return section;
    }

    public void setTitle(String value) {
        title = value;
    }

    public void setDocument(int value) {
        document = value;
    }

    public void setSentence(int value) {
        sentence = value;
    }

    public void setWord(int value) {
        word = value;
    }

    public void setLength(int value) {
        length = value;
    }

    public void setByteOffset(int value) {
        byteOffset = value;
    }

    public void setByteLength(int value) {
        byteLength = value;
    }

    public void setIsNegated(boolean value) {
        isNegated = value;
    }

    public void setNegator(String value) {
        negator = value;
    }

    public void setType(int value) {
        type = value;
    }

    public void setSection(int value) {
        section = value;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", getTitle())
                .toString();
    }

}
