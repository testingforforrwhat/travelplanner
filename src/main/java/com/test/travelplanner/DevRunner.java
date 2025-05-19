package com.test.travelplanner;


import com.test.travelplanner.model.entity.DestinationEntity;
import com.test.travelplanner.repository.DestinationRepository;
import com.test.travelplanner.repository.UserRepository;
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


    public DevRunner(

            DestinationRepository destinationRepository

    ) {

        this.destinationRepository = destinationRepository;
    }


    @Override
    public void run(ApplicationArguments args) {
        generateSampleData();

        List<DestinationEntity> destinationEntity = destinationRepository.findAll();
        logger.info("Found {} destinations: {}", destinationEntity.size(), destinationEntity);

    }


    private void generateSampleData() {

        destinationRepository.saveAll(List.of(
              new DestinationEntity("洛杉矶","洛杉矶","阳光明媚，电影之都的魅力","@/assets/images/sydney.jpg",2.0),
              new DestinationEntity("洛杉矶","洛杉矶","阳光明媚，电影之都的魅力","@/assets/images/sydney.jpg",2.0)
        ));

    }
}
