// Trip.java - 主实体类
package com.test.travelplanner.model.entity.trip;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 实体关系结构：
 * 主实体 Trip 与子实体采用一对多关系
 * 每个子实体都有对应的数据库表
 * 使用 CascadeType.ALL 确保级联操作，实现自动保存/删除子实体
 * 
 * 
 * Hibernate 注解使用：
 * @Entity：标记 JPA 实体类
 * @Table：指定对应的数据库表名
 * @Column：自定义列名或属性
 * @OneToMany：一对多关系映射
 * @ManyToOne：多对一关系映射
 * 
 * 
 * 优化设计：
 * 使用 FetchType.LAZY 延迟加载提高性能
 * 使用 orphanRemoval=true 自动清理孤立数据
 * 添加辅助方法方便实体对象关系维护
 * 数据库添加索引提高查询性能
 * 
 */
@Entity
@Table(name = "trips")
@Data
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "banner_image")
    private String bannerImage;
    
    private Integer duration;
    
    private String location;
    
    private BigDecimal price;
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripOverview> overview = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripHighlight> highlights = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC")
    private List<TripItinerary> itinerary = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripPriceInclude> priceIncludes = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripPriceExclude> priceExcludes = new ArrayList<>();
    
    // 辅助方法，简化添加关联实体
    public void addOverview(TripOverview overview) {
        this.overview.add(overview);
        overview.setTrip(this);
    }
    
    public void addHighlight(TripHighlight highlight) {
        this.highlights.add(highlight);
        highlight.setTrip(this);
    }
    
    public void addItinerary(TripItinerary itinerary) {
        this.itinerary.add(itinerary);
        itinerary.setTrip(this);
    }
    
    public void addPriceInclude(TripPriceInclude priceInclude) {
        this.priceIncludes.add(priceInclude);
        priceInclude.setTrip(this);
    }
    
    public void addPriceExclude(TripPriceExclude priceExclude) {
        this.priceExcludes.add(priceExclude);
        priceExclude.setTrip(this);
    }
}