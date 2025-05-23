package com.test.travelplanner;


import com.test.travelplanner.model.entity.DestinationEntity;
import com.test.travelplanner.model.entity.Product;
import com.test.travelplanner.model.entity.trip.Trip;
import com.test.travelplanner.model.entity.trip.TripOverview;
import com.test.travelplanner.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * add init data
 *
 * 通常推荐ApplicationRunner。原因：参数解析能力更强，更易扩展功能。
 *
 * ApplicationRunner CommandLineRunner
 *
 * 你可以安全地读写数据库，插入初始数据、生成表、做基础校验等。
 *
 */
@Component
public class DevRunner implements ApplicationRunner {


    static private final Logger logger = LoggerFactory.getLogger(DevRunner.class);

    private final DestinationRepository destinationRepository;
    private final ProductRepository productRepository;
    private final TripRepository tripRepository;
    private final TripOverviewRepository tripOverviewRepository;


    public DevRunner(

            DestinationRepository destinationRepository, ProductRepository productRepository,

            TripRepository tripRepository, TripOverviewRepository tripOverviewRepository) {

        this.destinationRepository = destinationRepository;
        this.productRepository = productRepository;
        this.tripRepository = tripRepository;
        this.tripOverviewRepository = tripOverviewRepository;
    }


    @Override
    public void run(ApplicationArguments args) {
        generateSampleData();

        List<DestinationEntity> destinationEntity = destinationRepository.findAll();
        logger.info("Found {} destinations: {}", destinationEntity.size(), destinationEntity);

        List<Product> products = productRepository.findAll();
        logger.info("Found {} products: {}", products.size(), products);

    }


    private void generateSampleData() {

        destinationRepository.saveAll(List.of(
              new DestinationEntity("洛杉矶","洛杉矶","阳光明媚，电影之都的魅力","require('@/assets/images/la/santa-monica.png')",2.0),
              new DestinationEntity("悉尼","洛杉矶","悉尼歌剧院，海港大桥的壮丽景色","require('@/assets/images/sydney.jpg')",2.0),
              new DestinationEntity("北京","洛杉矶","历史悠久，长城与故宫的辉煌","require('@/assets/images/beijing.jpeg')",2.0),
              new DestinationEntity("test","test","test","require('@/assets/images/beijing.jpeg')",2.0)
        ));

        Product product = new Product();
        product.setName("测试商品");
        product.setPrice(100.0);
        product.setStock(10);
        productRepository.save( product );

        Trip trip = new Trip();
        trip.setTitle("test");
        tripRepository.save( trip );

        TripOverview tripOverview = new TripOverview();
        tripOverview.setTrip(trip);
        tripOverview.setTitle("test");
        tripOverview.setTitle("test");
        tripOverviewRepository.save(tripOverview);
    }
}
