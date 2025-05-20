package com.test.travelplanner.service.impl;

import com.test.travelplanner.model.entity.Comment;
import com.test.travelplanner.model.entity.Like;
import com.test.travelplanner.repository.CommentRepository;
import com.test.travelplanner.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SocialService{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    // add comment
    public Comment addComment(Long userId, Long tripId, String content){
        Comment comment = new Comment(content, userId);
        comment.setCreatedAt(LocalDateTime.now());
        // save comment
        return commentRepository.save(comment);
    }

    // like status
    public Optional<Like> getLikeStatus(Long userId, Long tripId) {
        return likeRepository.findByUserIdAndTripId(userId, tripId);
    }

    // switch status
    public Like toggleLike(Long userId, Long tripId) {
        Optional<Like> optionalLike = likeRepository.findByUserIdAndTripId(userId, tripId);
        Like like;

        if (optionalLike.isPresent()) {
            like = optionalLike.get();
            like.setLiked(!like.isLiked());
        } else {
            like = new Like(userId, tripId, true);
        }

        return likeRepository.save(like);
    }

    // share
    public String shareTrip(Long tripId){
        return "Trip shared successfully";
    }
}

