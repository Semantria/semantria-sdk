package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.TaxonomyNode;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="taxonomies")
public class TaxonomiesDeleteReq
{
    private List<String> taxonomies = new ArrayList<String>();

    public TaxonomiesDeleteReq() {}

    public TaxonomiesDeleteReq(List<TaxonomyNode> taxonomies)
    {
        if( taxonomies != null && !taxonomies.isEmpty())
        {
            for (TaxonomyNode taxonomyNode : taxonomies)
            {
                this.taxonomies.add(taxonomyNode.getId());
            }
        }
    }

    @XmlElement(name="id")
    public List<String> getTaxonomies() { return taxonomies; }

    public void setTaxonomies(List<String> taxonomies) { this.taxonomies = taxonomies; }
}
