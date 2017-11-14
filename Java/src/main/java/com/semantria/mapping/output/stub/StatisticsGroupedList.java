package com.semantria.mapping.output.stub;

import com.semantria.mapping.output.statistics.StatisticsGrouped;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="statistics")
public class StatisticsGroupedList
{
    private List<StatisticsGrouped> statistics = new ArrayList<StatisticsGrouped>();

    public StatisticsGroupedList() {}

    public StatisticsGroupedList(List<StatisticsGrouped> statistics)
    {
        this.statistics = statistics;
    }

    @XmlElement(name="statistics")
    public List<StatisticsGrouped> getStatistics() { return statistics; }

    public void setStatistics(List<StatisticsGrouped> statistics) { this.statistics = statistics; }
}
