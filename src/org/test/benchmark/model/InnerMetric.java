package org.test.benchmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InnerMetric implements Serializable, Cloneable
{
    private long id;

    private String componentName = "";
    private String beforeIdentifier = "";
    private String afterIdentifier = "";
    private Date startTime;
    private Date endTime;

    private List<Rule> rules;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getComponentName()
    {
        return componentName;
    }

    public void setComponentName(String componentName)
    {
        this.componentName = componentName;
    }

    public String getBeforeIdentifier()
    {
        return beforeIdentifier;
    }

    public void setBeforeIdentifier(String beforeIdentifier)
    {
        this.beforeIdentifier = beforeIdentifier;
    }

    public String getAfterIdentifier()
    {
        return afterIdentifier;
    }

    public void setAfterIdentifier(String afterIdentifier)
    {
        this.afterIdentifier = afterIdentifier;
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

    public List<Rule> getRules()
    {
        return rules;
    }

    public void setRules(List<Rule> rules)
    {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "ComponentInvocationMetric{" +
                "id=" + id +
                ", componentName='" + componentName + '\'' +
                ", beforeIdentifier='" + beforeIdentifier + '\'' +
                ", afterIdentifier='" + afterIdentifier + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", rules=" + rules +
                '}';
    }

    @Override
    public Object clone() {
        InnerMetric clone = null;
        try
        {
            clone = (InnerMetric) super.clone();
            if (this.rules != null)
            {
                List<Rule> rules = new ArrayList<>(this.rules.size());
                for (Rule rule : this.getRules())
                {
                    rules.add((Rule) rule.clone());
                }
                clone.setRules(rules);
            }
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
