package com.test.travelplanner.controller;

import com.test.travelplanner.model.entity.Comment;
import com.test.travelplanner.model.entity.Like;
import com.test.travelplanner.model.CommentRequest;
import com.test.travelplanner.model.CommentResponse;
import com.test.travelplanner.model.LikeResponse;
import com.test.travelplanner.model.ShareResponse;
import com.test.travelplanner.redis.RedisUtil;
import com.test.travelplanner.repository.LikeRepository;
import com.test.travelplanner.service.impl.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trips")
public class SocialController{
    @Autowired
    private SocialService socialService;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private RedisUtil redisUtil;

    // add comment
    @PostMapping("/{tripId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @RequestHeader String authorization,
            @PathVariable Long tripId,
            @RequestBody CommentRequest commentRequest,
            @RequestParam Long userId) {

        if (redisUtil.hashKey( authorization ) ) {

            Comment comment = socialService.addComment(userId, tripId, commentRequest.content());

            CommentResponse response =
                    new CommentResponse(
                            0,
                            "Comment added successfully",
                            new CommentResponse.CommentData(
                                    comment.getId(),
                                    comment.getContent(),
                                    comment.getCreatedAt(),
                                    new CommentResponse.UserResponse(
                                            comment.getUserId(),
                                            "johndoe")
                            )
                    );  // 这里假设用户名固定为 "johndoe" 示例

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // like
    @PostMapping("/{tripId}/like")
    public ResponseEntity<LikeResponse> toggleLike(
            @PathVariable Long tripId,
            @RequestParam Long userId) {
        Like like = socialService.toggleLike(userId, tripId);
        LikeResponse response = new LikeResponse(0, "Like toggled successfully",
                new LikeResponse.LikeData(like.isLiked(), likeRepository.countByTripId(tripId)));
        return ResponseEntity.ok(response);
    }

    // share trip
    @PostMapping("/{tripId}/share")
    public ResponseEntity<ShareResponse> shareTrip(@PathVariable Long tripId) {
        String message = socialService.shareTrip(tripId);
        ShareResponse response = new ShareResponse(0, message, null);
        return ResponseEntity.ok(response);
    }

}

