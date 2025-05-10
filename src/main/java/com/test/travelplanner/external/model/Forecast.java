package com.test.travelplanner.external.model;

import lombok.Data;

import java.util.List;

@Data
public class Forecast {  
    private String province;  
    private String city;  
    private String district;  
    private Integer adcode;  
    private String update_time;  
    private List<Info> infos;
}  