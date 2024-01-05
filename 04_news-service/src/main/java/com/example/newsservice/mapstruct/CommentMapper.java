package com.example.newsservice.mapstruct;

import com.example.newsservice.model.dto.*;
import com.example.newsservice.model.entity.Comment;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(CommentMapperDelegate.class)
public interface CommentMapper {

    Comment requestToComment(UpsertCommentRequest request, String userName);
    Comment requestToComment(CommentUpdateRequest request);
    Comment requestToComment(Long commentId, CommentUpdateRequest request);

    @Mapping(source = "news.id", target = "newsId")
    CommentResponse commentToResponse(Comment comment);

    List<CommentResponse> commentListToResponseList(List<Comment> commentList);

    default CommentListResponse commentListToCommentListResponse(List<Comment> commentList) {
        CommentListResponse response = new CommentListResponse();
        response.setCommentResponseList(commentListToResponseList(commentList));
        return response;
    }
}
