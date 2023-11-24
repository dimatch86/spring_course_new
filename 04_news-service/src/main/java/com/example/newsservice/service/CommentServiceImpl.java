package com.example.newsservice.service;

import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.model.entity.Comment;
import com.example.newsservice.model.entity.News;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final NewsService newsService;
    @Override
    public List<Comment> findAllByNews(Long id) {
        return commentRepository.findCommentByNewsId(id);
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Comment with id {0} not found", id)));
    }

    @Override
    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Comment updateComment(Comment comment) {
        News news = newsService.findById(comment.getNews().getId());
        Comment existedComment = findById(comment.getId());
        BeanUtil.copyNonNullProperties(comment, existedComment);
        existedComment.setNews(news);
        return commentRepository.save(existedComment);
    }
}
