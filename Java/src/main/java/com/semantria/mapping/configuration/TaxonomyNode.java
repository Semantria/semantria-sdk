package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class TaxonomyNode {
    private String id = null;
    private String name = "";
    private Boolean enforce_parent_matching = null;
    private List<TaxonomyTopic> topics;
    private List<TaxonomyNode> nodes;

    @XmlElement(name="id")
    public String getId() { return id; }
    @XmlElement(name="name")
    public String getName() { return name; }
    @XmlElement(name="enforce_parent_matching")
    public Boolean getEnforceParentMatching() { return enforce_parent_matching; }
    @XmlElementWrapper(name="topics")
    @XmlElement(name="topic")
    public List<TaxonomyTopic> getTopics() { return topics; }
    @XmlElementWrapper(name="nodes")
    @XmlElement(name="node")
    public List<TaxonomyNode> getNodes() { return nodes; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEnforceParentMatching(Boolean enforceParentMatching) { this.enforce_parent_matching = enforceParentMatching; }
    public void setTopics(List<TaxonomyTopic> topics) { this.topics = topics; }
    public void setNodes(List<TaxonomyNode> nodes) { this.nodes = nodes; }
}
