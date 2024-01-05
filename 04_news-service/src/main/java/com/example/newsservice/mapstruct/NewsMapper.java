package com.example.newsservice.mapstruct;

import com.example.newsservice.model.dto.NewsListResponse;
import com.example.newsservice.model.dto.NewsResponse;
import com.example.newsservice.model.dto.CreateNewsRequest;
import com.example.newsservice.model.entity.Category;
import com.example.newsservice.model.entity.News;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentMapper.class})
@DecoratedWith(NewsMapperDelegate.class)
public interface NewsMapper {

    News requestToNews(CreateNewsRequest request, String userName);
    Category map(String value);

    @Mapping(source = "newsId", target = "id")
    News requestToNews(Long newsId, CreateNewsRequest request, String userName);

    @Mapping(source = "user.id", target = "userId")
    NewsResponse newsToResponse(News news);

    List<NewsResponse> newsListToResponseList(List<News> newsList);

    default NewsListResponse newsListToNewsListResponse(List<News> newsList) {
        NewsListResponse response = new NewsListResponse();
        response.setNewsList(newsListToResponseList(newsList));
        return response;
    }
}
