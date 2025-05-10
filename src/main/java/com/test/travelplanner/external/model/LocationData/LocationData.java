package com.test.travelplanner.external.model.LocationData;

import lombok.Data;
import java.util.List;  


/**  
 * 地点信息数据  
 */  
@Data  
public class LocationData {  
    private Integer status;  
    private String message;   
    private String request_id;  
    private Integer count;  
    private List<PlaceItem> data;  
}

