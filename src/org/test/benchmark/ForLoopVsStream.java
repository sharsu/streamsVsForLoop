package org.test.benchmark;

import org.test.benchmark.model.InnerMetric;
import org.test.benchmark.model.Metric;
import org.test.benchmark.model.Rule;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class ForLoopVsStream
{
    public static void main(String[] args)
    {
        ForLoopVsStream loopVsStream = new ForLoopVsStream();
        int noOfIterations = 100;
        int numberOfMetrics = 500;

        double[] timeTakenInForLoop = new double[noOfIterations];
        double[] timeTakenInStreams1 = new double[noOfIterations];
        double[] timeTakenInStreams2 = new double[noOfIterations];
        double[] timeTakenInParaStreams1 = new double[noOfIterations];
        double[] timeTakenInParaStreams2 = new double[noOfIterations];
        for (int i = 0; i < noOfIterations; i++)
        {
            List<Metric> metrics = new ArrayList<>(numberOfMetrics);
            for (int j = 0; j < numberOfMetrics; j++)
            {
                metrics.add(loopVsStream.createMetric(getRandomNumberInRange(0, 10)));
            }

            List<Metric> cloneMetrics = loopVsStream.cloneMetrics(metrics);
            timeTakenInForLoop[i] = loopVsStream.runForLoopBatch(cloneMetrics, numberOfMetrics);

            cloneMetrics = loopVsStream.cloneMetrics(metrics);
            timeTakenInStreams1[i] = loopVsStream.runStreamsWithLambdaBatch(cloneMetrics, numberOfMetrics);

            cloneMetrics = loopVsStream.cloneMetrics(metrics);
            timeTakenInStreams2[i] = loopVsStream.runStreamsWithoutLambdaBatch(cloneMetrics, numberOfMetrics);

            cloneMetrics = loopVsStream.cloneMetrics(metrics);
            timeTakenInParaStreams1[i] = loopVsStream.runParallelStreamsWithLamdbaBatch(cloneMetrics, numberOfMetrics);

            cloneMetrics = loopVsStream.cloneMetrics(metrics);
            timeTakenInParaStreams2[i] = loopVsStream.runParallelStreamsWithoutLambdaBatch(cloneMetrics, numberOfMetrics);
        }

        OptionalDouble avgTime = DoubleStream.of(timeTakenInForLoop).average();
        System.out.println("Average time(in nanos) it took to process one metric using for loop " + avgTime.getAsDouble() + " with batch size " + numberOfMetrics + " and " + noOfIterations + " iterations.");

        avgTime = DoubleStream.of(timeTakenInStreams1).average();
        System.out.println("Average time(in nanos) it took to process one metric using serial streams (with lambda) " + avgTime.getAsDouble() + " with batch size " + numberOfMetrics + " and " + noOfIterations + " iterations.");

        avgTime = DoubleStream.of(timeTakenInStreams2).average();
        System.out.println("Average time(in nanos) it took to process one metric using serial streams (without lambda) " + avgTime.getAsDouble() + " with batch size " + numberOfMetrics + " and " + noOfIterations + " iterations.");

        avgTime = DoubleStream.of(timeTakenInParaStreams1).average();
        System.out.println("Average time(in nanos) it took to process one metric using parallel streams (with lambda) " + avgTime.getAsDouble() + " with batch size " + numberOfMetrics + " and " + noOfIterations + " iterations.");

        avgTime = DoubleStream.of(timeTakenInParaStreams2).average();
        System.out.println("Average time(in nanos) it took to process one metric using parallel streams (without lambda) " + avgTime.getAsDouble() + " with batch size " + numberOfMetrics + " and " + noOfIterations + " iterations.");
    }

    private long runForLoopBatch(List<Metric> metrics, int numberOfMetrics)
    {
        long startNanos = System.nanoTime();
        this.usingForLoop(metrics);
        long endNanos = System.nanoTime();

        return (endNanos - startNanos) / numberOfMetrics;
    }

    private void usingForLoop(List<Metric> metrics)
    {
        List<Metric> metricsToIgnore = new ArrayList<>();
        for(Metric metric : metrics)
        {
            if (metric.getInnerMetrics() != null && !metric.getInnerMetrics().isEmpty())
            {
                for(InnerMetric innerMetric : metric.getInnerMetrics())
                {
                    if(innerMetric.getRules() != null && !innerMetric.getRules().isEmpty())
                    {
                        for(Rule ruleExecution : innerMetric.getRules())
                        {
                            if (!ruleExecution.isOutcome())
                            {
                                metricsToIgnore.add(metric);
                                continue;
                            }
                        }
                    }
                }
            }
        }

        if (metricsToIgnore.size() > 0)
        {
            metrics.removeAll(metricsToIgnore);
        }

        if (metrics.size() == 0)
        {
            // System.out.println("No metrics to process");
        }
    }

    private long runStreamsWithoutLambdaBatch(List<Metric> metrics, int numberOfMetrics)
    {
        long startNanos = System.nanoTime();
        this.usingStreamsWithoutLambda(metrics);
        long endNanos = System.nanoTime();

        return (endNanos - startNanos) / numberOfMetrics;
    }

    private void usingStreamsWithoutLambda(List<Metric> metrics)
    {
        List<Metric> metricsToIgnore = metrics.stream().filter(this::resolveRulesFailure).collect(Collectors.toList());
        if (metricsToIgnore.size() > 0)
        {
            metrics.removeAll(metricsToIgnore);
        }

        if (metrics.size() == 0)
        {
            // System.out.println("No metrics to process");
        }
    }

    private long runStreamsWithLambdaBatch(List<Metric> metrics, int numberOfMetrics)
    {
        long startNanos = System.nanoTime();
        this.usingStreamsWithLambda(metrics);
        long endNanos = System.nanoTime();

        return (endNanos - startNanos) / numberOfMetrics;
    }

    private void usingStreamsWithLambda(List<Metric> metrics)
    {
        List<Metric> metricsToIgnore = new ArrayList<>();
        metrics.forEach(metric -> {
            if(!resolveRulesFailure(metric))
            {
                metricsToIgnore.add(metric);
            }
        });
        if (metricsToIgnore.size() > 0)
        {
            metrics.removeAll(metricsToIgnore);
        }

        if (metrics.size() == 0)
        {
            // System.out.println("No metrics to process");
        }
    }

    private long runParallelStreamsWithLamdbaBatch(List<Metric> metrics, int numberOfMetrics)
    {
        long startNanos = System.nanoTime();
        this.usingParallelStreamsWithLamdba(metrics);
        long endNanos = System.nanoTime();

        return (endNanos - startNanos) / numberOfMetrics;
    }

    private void usingParallelStreamsWithLamdba(List<Metric> metrics)
    {
        List<Metric> metricsToIgnore = Collections.synchronizedList(new ArrayList<>());
        metrics.parallelStream().forEach(metric -> {
            if(!resolveRulesFailure(metric))
            {
                metricsToIgnore.add(metric);
            }
        });
        if (metricsToIgnore.size() > 0)
        {
            metrics.removeAll(metricsToIgnore);
        }

        if (metrics.size() == 0)
        {
            // System.out.println("No metrics to process");
        }
    }

    private long runParallelStreamsWithoutLambdaBatch(List<Metric> metrics, int numberOfMetrics)
    {
        long startNanos = System.nanoTime();
        this.usingParallelStreamsWithoutLambda(metrics);
        long endNanos = System.nanoTime();

        return (endNanos - startNanos) / numberOfMetrics;
    }

    private void usingParallelStreamsWithoutLambda(List<Metric> metrics)
    {
        List<Metric> metricsToIgnore = metrics.parallelStream().filter(this::resolveRulesFailure).collect(Collectors.toList());
        if (metricsToIgnore.size() > 0)
        {
            metrics.removeAll(metricsToIgnore);
        }

        if (metrics.size() == 0)
        {
            // System.out.println("No metrics to process");
        }
    }

    private boolean resolveRulesFailure(Metric metric)
    {
        return !Optional.ofNullable(metric.getInnerMetrics())
                .map(Collection::stream)
                .orElse(Stream.empty())
                .flatMap(c -> Optional.ofNullable(c.getRules())
                        .map(Collection::stream)
                        .orElse(Stream.empty()))
                .anyMatch(r -> !r.isOutcome());
    }

    private List<Rule> resolveRules(Metric metric)
    {
        return Optional.ofNullable(metric.getInnerMetrics())
                .map(Collection::stream)
                .orElse(Stream.empty())
                .flatMap(c -> Optional.ofNullable(c.getRules())
                        .map(Collection::stream)
                        .orElse(Stream.empty()))
                .collect(Collectors.toList());
    }

    private Metric createMetric(int numOfComponents)
    {
        Date date = new Date();
        long id = flowInvocationMetricId.getAndIncrement();

        Metric metric = new Metric();
        metric.setId(id);
        metric.set_created(date);
        metric.setBusinessDate(date);
        metric.setIdentifier("identifier_" +  id);
        metric.setIdentifierType("type");
        metric.setStartTime(date);
        metric.setEndTime(new Date());
        metric.setOutcome(1L);
        metric.setSourceKey(1L);

        if (numOfComponents > 0)
        {
            List<InnerMetric> innerMetrics = new ArrayList<>(numOfComponents);
            for (int i = 1; i <= numOfComponents; i++)
            {
                innerMetrics.add(createInnerMetric(getRandomNumberInRange(0, 48)));
            }
            metric.setInnerMetrics(innerMetrics);
        }
        return metric;
    }

    private InnerMetric createInnerMetric(int numOfRulesExecutions)
    {
        long id = componentInvocationMetricId.getAndIncrement();
        Date startTime = new Date();
        InnerMetric innerMetric = new InnerMetric();
        innerMetric.setId(id);
        innerMetric.setComponentName("name_" + id);
        innerMetric.setBeforeIdentifier("before_identifier_" + id);
        innerMetric.setAfterIdentifier("after_identifier_" + id);
        innerMetric.setStartTime(startTime);
        innerMetric.setStartTime(new Date());

        if (numOfRulesExecutions > 0)
        {
            List<Rule> ruleExecutions = new ArrayList<>(numOfRulesExecutions);
            for (int i = 1; i <= numOfRulesExecutions; i++)
            {
                ruleExecutions.add(createRuleExecution());
            }
            innerMetric.setRules(ruleExecutions);
        }

        return innerMetric;
    }

    private Rule createRuleExecution()
    {
        long id = ruleExecutionId.getAndIncrement();
        Rule rule = new Rule();
        rule.setAction("IGNORE");
        rule.setOutcome(true);
        rule.setId(id);
        rule.setRuleName("rule_name_" + id);

        return rule;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private List<Metric> cloneMetrics(List<Metric> metrics)
    {
        List<Metric> clonedMetrics = new ArrayList<>(metrics.size());
        for (Metric metric : metrics) {
            clonedMetrics.add((Metric) metric.clone());
        }
        return clonedMetrics;
    }

    private static final AtomicLong flowInvocationMetricId = new AtomicLong(1);

    private static final AtomicLong componentInvocationMetricId = new AtomicLong(1);

    private static final AtomicLong ruleExecutionId = new AtomicLong(1);
}
