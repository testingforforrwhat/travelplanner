package com.test.travelplanner.social.model;

public record LikeResponse(
        int code,
        String message,
        LikeData data
) {
    public record LikeData(
            boolean liked,
            int likeCount
    ) {}
}
