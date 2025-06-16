package com.ai.model;

public class TestCase {

    private String id;
    private String scenario;
    private String expectedResult;
    private String label;
    private String status;

    public TestCase(String id, String scenario, String expectedResult, String label) {
        this.id = id;
        this.scenario = scenario;
        this.expectedResult = expectedResult;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getScenario() {
        return scenario;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public String getLabel() {
        return label;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
