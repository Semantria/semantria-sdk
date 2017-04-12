package com.semantria.mapping.output.statistics;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement(name = "statistics")
public class StatisticsGroup extends StatisticsBase {
    private Timestamp time = null;

    @XmlElement(name = "time")
    public Timestamp getTime() { return time; }

    public void setTime(Timestamp time) { this.time = time; }
}
