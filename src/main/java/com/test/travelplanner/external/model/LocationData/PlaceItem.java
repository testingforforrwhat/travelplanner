package com.test.travelplanner.external.model.LocationData;

import lombok.Data;


/**
 * 单个地点信息项  
 */  
@Data  
public class PlaceItem {  
    private String id;  
    private String title;  
    private String address;  
    private String category;  
    private Integer type;  
    private Location location;  
    private Integer adcode;  
    private String province;  
    private String city;  
    private String district;  
}
