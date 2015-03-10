package com.semantria.mapping.output.stub;

import com.semantria.mapping.output.FeaturesSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2/11/2015.
 */

@XmlRootElement(name="supported_features")
public final class FeaturesList
{
    private List<FeaturesSet> features = new ArrayList<FeaturesSet>();

    public FeaturesList() {}

    public FeaturesList(List<FeaturesSet> features)
    {
        this.features = features;
    }

    @XmlElement(name="features")
    public List<FeaturesSet> getFeatures() { return features; }

    public void setFeatures(List<FeaturesSet> features) { this.features = features; }
}
