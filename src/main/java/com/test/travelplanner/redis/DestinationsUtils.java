package com.test.travelplanner.redis;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class DestinationsUtils {


    @Resource
    private RedisUtil redisUtil;




    public void incrementClickCount(String destination_id) {
        redisUtil.incr("destination:clickCountByWeekByDestinationId:" + destination_id, 1);
    }

}