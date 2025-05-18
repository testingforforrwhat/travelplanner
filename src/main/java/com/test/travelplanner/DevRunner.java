package com.test.travelplanner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class DevRunner implements ApplicationRunner {


    static private final Logger logger = LoggerFactory.getLogger(DevRunner.class);


    public DevRunner(

    ) {

    }


    @Override
    public void run(ApplicationArguments args) {
        generateSampleData();


    }


    private void generateSampleData() {

    }
}
