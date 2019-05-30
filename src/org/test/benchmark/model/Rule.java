package org.test.benchmark.model;

import java.io.Serializable;

public class Rule implements Serializable, Cloneable
{
    private Long id;
    private String ruleName = "";
    private boolean outcome;
    private String action = "";

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getRuleName()
    {
        return ruleName;
    }

    public void setRuleName(String ruleName)
    {
        this.ruleName = ruleName;
    }

    public boolean isOutcome()
    {
        return outcome;
    }

    public void setOutcome(boolean outcome)
    {
        this.outcome = outcome;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    @Override
    public String toString() {
        return "RuleExecution{" +
                "id=" + id +
                ", ruleName='" + ruleName + '\'' +
                ", outcome=" + outcome +
                ", action='" + action + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
