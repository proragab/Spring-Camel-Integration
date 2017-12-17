package com.springCamel.model;

import java.util.ArrayList;
import java.util.List;


public class GeocodeResponseFromJson {

    private String status;
    private List<Result> results = new ArrayList<>();
    private Result result;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
