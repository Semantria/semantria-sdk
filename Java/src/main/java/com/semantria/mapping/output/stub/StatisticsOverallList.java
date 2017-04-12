package com.semantria.mapping.output.stub;

import com.semantria.mapping.output.statistics.StatisticsOverall;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="statistics")
public final class StatisticsOverallList
{
    private List<StatisticsOverall> statistics = new ArrayList<StatisticsOverall>();

    public StatisticsOverallList() {}

    public StatisticsOverallList(List<StatisticsOverall> statistics)
    {
        this.statistics = statistics;
    }

    @XmlElement(name="statistics")
    public List<StatisticsOverall> getStatistics() { return statistics; }

    public void setStatistics(List<StatisticsOverall> statistics) { this.statistics = statistics; }
}
