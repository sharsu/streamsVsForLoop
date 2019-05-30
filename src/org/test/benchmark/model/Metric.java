package org.test.benchmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Metric implements Serializable, Cloneable
{
    private Long id;
    private Date _created;

    private String moduleName = "";
    private String flowName = "";
    private long sourceKey;

    private long outcome;

    private String identifierType = "";
    private String identifier = "";

    private Date metricDate;
    private Date startTime;
    private Date endTime;

    private List<InnerMetric> innerMetrics;

    public Metric()
    {

    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date get_created()
    {
        return _created;
    }

    public void set_created(Date _created)
    {
        this._created = _created;
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public void setModuleName(String moduleName)
    {
        this.moduleName = moduleName;
    }

    public String getFlowName()
    {
        return flowName;
    }

    public void setFlowName(String flowName)
    {
        this.flowName = flowName;
    }

    public String getIdentifierType()
    {
        return identifierType;
    }

    public void setIdentifierType(String identifierType)
    {
        this.identifierType = identifierType;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public Date getBusinessDate()
    {
        return metricDate;
    }

    public void setBusinessDate(Date businessDate)
    {
        this.metricDate = businessDate;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public List<InnerMetric> getInnerMetrics()
    {
        return innerMetrics;
    }

    public void setInnerMetrics(List<InnerMetric> innerMetrics)
    {
        this.innerMetrics = innerMetrics;
    }

    public long getSourceKey()
    {
        return sourceKey;
    }

    public void setSourceKey(long sourceKey)
    {
        this.sourceKey = sourceKey;
    }

    public long getOutcome()
    {
        return outcome;
    }

    public void setOutcome(long outcome)
    {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        String value =  "FlowInvocationMetric{" +
                "id=" + id +
                ", _created=" + _created +
                ", moduleName='" + moduleName + '\'' +
                ", flowName='" + flowName + '\'' +
                ", identifierType='" + identifierType + '\'' +
                ", identifier='" + identifier + '\'' +
                ", businessDate=" + metricDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", innerMetrics=" ;

        for(InnerMetric metric: innerMetrics)
        {
            value += "[" + metric + "]\n";
        }

        value += "'}'";

        return value;
    }

    @Override
    public Object clone() {
        Metric clone = null;
        try
        {
            clone = (Metric) super.clone();
            if (this.innerMetrics != null)
            {
                List<InnerMetric> innerMetrics = new ArrayList<>(this.innerMetrics.size());
                for (InnerMetric innerMetric : this.getInnerMetrics())
                {
                    innerMetrics.add((InnerMetric) innerMetric.clone());
                }
                clone.setInnerMetrics(innerMetrics);
            }
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
