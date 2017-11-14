package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.TaxonomyNode;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="taxonomies")
public class Taxonomies
{
    private List<TaxonomyNode> taxonomies = new ArrayList<TaxonomyNode>();

    public Taxonomies() { }

    public Taxonomies(List<TaxonomyNode> taxonomies)
    {
        this.taxonomies = taxonomies;
    }

    @XmlElement(name="taxonomy")
    public List<TaxonomyNode> getTaxonomies() { return taxonomies; }

    public void setTaxonomies(List<TaxonomyNode> taxonomies) { this.taxonomies = taxonomies; }
}
