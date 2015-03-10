package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public final class DocIntention
{
	private String type = null;
	private String evidence_phrase = null;
	private String what = null;
    private String who = null;

	@XmlElement(name="type")
	public String getType() { return type; }
	@XmlElement(name="evidence_phrase")
	public String getEvidencePhrase() { return evidence_phrase; }
	@XmlElement(name="what")
	public String getWhat() { return what; }
    @XmlElement(name="who")
    public String getWho() { return who; }

	public void setType(String type) { this.type = type; }
	public void setEvidencePhrase(String evidence_phrase) { this.evidence_phrase = evidence_phrase; }
	public void setWhat(String what) { this.what = what; }
	public void setWho(String who) { this.who = who; }
}
