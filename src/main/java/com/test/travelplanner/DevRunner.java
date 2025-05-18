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


    }


    private void generateSampleData() {

        destinationRepository.saveAll(List.of(
              new DestinationEntity("洛杉矶","洛杉矶","阳光明媚，电影之都的魅力","@/assets/images/sydney.jpg",2.0),
              new DestinationEntity("洛杉矶","洛杉矶","阳光明媚，电影之都的魅力","@/assets/images/sydney.jpg",2.0)
        ));

    }
}
