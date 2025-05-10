package com.test.travelplanner.external.model;

import lombok.Data;

import java.util.List;

@Data
public class Result {  
    private List<Forecast> forecast;
}  