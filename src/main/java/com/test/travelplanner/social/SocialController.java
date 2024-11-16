package com.test.travelplanner.social;

import com.test.travelplanner.social.model.Comment;
import com.test.travelplanner.social.model.Like;
import com.test.travelplanner.social.model.CommentRequest;
import com.test.travelplanner.social.model.CommentResponse;
import com.test.travelplanner.social.model.LikeResponse;
import com.test.travelplanner.social.model.ShareResponse;
import com.test.travelplanner.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trips")
public class SocialController{
    @Autowired
    private SocialService socialService;

    @Autowired
    private LikeRepository likeRepository;

    // add comment
    @PostMapping("/{tripId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long tripId,
            @RequestBody CommentRequest commentRequest,
            @RequestParam Long userId) {
        Comment comment = socialService.addComment(userId, tripId, commentRequest.content());
        CommentResponse response = new CommentResponse(0, "Comment added successfully",
                new CommentResponse.CommentData(comment.getId(), comment.getContent(), comment.getCreatedAt(),
                        new CommentResponse.UserResponse(comment.getUserId(), "johndoe")));  // 这里假设用户名固定为 "johndoe" 示例
        return ResponseEntity.ok(response);
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

