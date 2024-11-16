package com.test.travelplanner.social.model;

import java.time.LocalDateTime;

public record CommentResponse(
        int code,
        String message,
        CommentData data
) {
    public record CommentData(
            Long commentId,
            String content,
            LocalDateTime createdAt,
            UserResponse user
    ) {}

    public record UserResponse(
            Long userId,
            String username
    ) {}
}
