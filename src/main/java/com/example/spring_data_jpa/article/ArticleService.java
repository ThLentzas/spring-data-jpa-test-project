package com.example.spring_data_jpa.article;

import com.example.spring_data_jpa.exception.DuplicateResourceException;
import com.example.spring_data_jpa.exception.ResourceNotFoundException;
import com.example.spring_data_jpa.exception.StatusConflictException;
import com.example.spring_data_jpa.topic.Topic;
import com.example.spring_data_jpa.topic.TopicDTO;
import com.example.spring_data_jpa.topic.TopicRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final TopicRepository topicRepository;
    private static final ArticleDTOMapper mapper = new ArticleDTOMapper();
    private static final String ARTICLE_NOT_FOUND_ERROR_MSG = "Article was not found with id: ";

    ArticleDTO createArticle(ArticleCreateRequest createRequest) {
        if (this.articleRepository.existsByTitle(createRequest.title())) {
            throw new DuplicateResourceException("Article already exists with title: " + createRequest.title());
        }

        Set<Topic> topics = processTopics(createRequest.topics());
        if (topics.isEmpty()) {
            throw new IllegalArgumentException("You must provide at least one topic");
        }

        Article article = new Article(createRequest.title(), createRequest.content(), topics);
        article = this.articleRepository.save(article);

        return mapper.apply(article);
    }

    void updateArticle(Long articleId, ArticleUpdateRequest updateRequest) {
        Article article = this.articleRepository.findById(articleId).orElseThrow(() ->
                new ResourceNotFoundException(ARTICLE_NOT_FOUND_ERROR_MSG + articleId));

        if(article.getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new StatusConflictException("Article is already published and cannot be modified");
        }

        if (updateRequest.title() != null && !updateRequest.title().isBlank()) {
            if (this.articleRepository.existsByTitle(updateRequest.title())) {
                throw new DuplicateResourceException(
                        "Article already exists with title: " + updateRequest.title());
            }
            article.setTitle(updateRequest.title());
        }

        if (updateRequest.content() != null && !updateRequest.content().isBlank()) {
            article.setContent(updateRequest.content());
        }

        /*
            By calling article.setTopics(topics); Hibernate will delete the previous associations between
            the specific article and any topics and will create in the table article_topic relations between
            the specific article and the new topics.

            By calling article.getTopics().addAll(topics) Hibernate will keep the previous relations and
            will also create the new ones on the article_topic table.

            The choice on which to choose depends on the business logic
         */
        if (updateRequest.topics() != null && !updateRequest.topics().isEmpty()) {
            Set<TopicDTO> topicsDTO = updateRequest.topics().stream()
                    .filter(topicDTO -> topicDTO.name() != null && !topicDTO.name().isBlank())
                    .collect(Collectors.toSet());
            Set<Topic> topics = processTopics(topicsDTO);
            article.setTopics(topics);
        }

        this.articleRepository.save(article);
    }

    void updateArticleStatus(Long articleId, ArticleUpdateStatusRequest updateStatusRequest) {
        Article article = this.articleRepository.findById(articleId).orElseThrow(() ->
                new ResourceNotFoundException(ARTICLE_NOT_FOUND_ERROR_MSG + articleId));
        if(article.getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new StatusConflictException("Article already published");
        }

        if(article.getStatus().equals(ArticleStatus.CREATED)
                && !updateStatusRequest.status().equals(ArticleStatus.SUBMITTED)) {
            throw new IllegalArgumentException("Article status is: " + article.getStatus().name().toLowerCase());
        }

        if(article.getStatus().equals(ArticleStatus.SUBMITTED)
                && !updateStatusRequest.status().equals(ArticleStatus.APPROVED)) {
            throw new IllegalArgumentException("Article status is: " + article.getStatus().name().toLowerCase());
        }

        if(article.getStatus().equals(ArticleStatus.APPROVED)
                && !updateStatusRequest.status().equals(ArticleStatus.PUBLISHED)) {
            throw new IllegalArgumentException("Article status is: " + article.getStatus().name().toLowerCase());
        }

        article.setStatus(updateStatusRequest.status());
        if(article.getStatus().equals(ArticleStatus.PUBLISHED)) {
            article.setPublishedDate(LocalDate.now());
        }

        this.articleRepository.save(article);
    }

    /*
        Finds articles with a given title and/or content
     */
    List<ArticleDTO> findArticlesByTitleAndOrContent(String title, String content) {
        List<Article> articles;

        if(title.isBlank() && content.isBlank()) {
            throw new IllegalArgumentException("You have to provide either the title or the content of the article");
        }

        if(title.isBlank()) {
            articles = this.articleRepository.findByContentContainingIgnoringCase(content);

            return articles.stream()
                    .map(mapper)
                    .toList();
        }

        if(content.isBlank()) {
            articles = this.articleRepository.findByTitleContainingIgnoringCase(title);

            return articles.stream()
                    .map(mapper)
                    .toList();
        }

        /*
            We have to create a Set to remove articles that we would include them when searching by title and include
            them again when searching by content.
         */
        articles = this.articleRepository.findByTitleContainingIgnoringCase(title);
        articles.addAll(this.articleRepository.findByContentContainingIgnoringCase(content));
        Set<Article> articleSet = new HashSet<>(articles);

        return articleSet.stream()
                .map(mapper)
                .toList();
    }

    ArticleDTO findArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new ResourceNotFoundException(ARTICLE_NOT_FOUND_ERROR_MSG + articleId));

        return mapper.apply(article);
    }

    List<ArticleDTO> findAllArticles(ArticleStatus status, LocalDate startDate, LocalDate endDate) {
        List<Article> articles;

        if(status != null && startDate != null && endDate != null) {
            throw new IllegalArgumentException("Select 1 filter at a time");
        }

        /*
            No filtering all articles returned.
         */
        if(status == null && startDate == null && endDate == null) {
            articles = findAllArticlesNoFilter();

            return articles.stream()
                    .map(mapper)
                    .toList();
        }

        if(status == null && (startDate == null || endDate == null)) {
            throw new IllegalArgumentException("You must provide both dates");
        }

        /*
            Filter is date range. We return the published articles where their published date is in the provided date
            range. For the rest of the articles we return based on their created date.
         */
        if(status == null) {
            articles = findAllArticlesInDateRange(startDate, endDate);

            return articles.stream()
                    .map(mapper)
                    .toList();
        }

        articles = findAllArticlesByStatus(status);

        return articles.stream()
                .map(mapper)
                .toList();

    }

    private List<Article> findAllArticlesByStatus(ArticleStatus status) {
        /*
            If the provided status is published we return all published articles in descending order based on their
            published date. For any other status we return those articles with the given status in descending order
            based on their created date.
         */
        if(status.equals(ArticleStatus.PUBLISHED)) {
            return this.articleRepository.findAllByStatusOrderByPublishedDateDesc(ArticleStatus.PUBLISHED);
        }
        return this.articleRepository.findAllByStatusOrderByCreatedDateDesc(status);
    }

    private List<Article> findAllArticlesInDateRange(LocalDate startDate, LocalDate endDate) {
        List<Article> articles = new ArrayList<>();
        List<Article >publishedArticles = this.articleRepository.findAllByStatusAndPublishedDateBetween(
                ArticleStatus.PUBLISHED,
                startDate,
                endDate);
        List<Article> nonPublishedArticles = this.articleRepository.findAllByStatusNotAndCreatedDateBetween(
                ArticleStatus.PUBLISHED,
                startDate,
                endDate);

        articles.addAll(publishedArticles);
        articles.addAll(nonPublishedArticles);

        return articles;
    }

    /*
        This method gets called when GET all articles is called without any filters(status or date range).

        Fetching the Published articles and sorting them by their published date.
        Fetching the rest of the articles and sorting them by their status first and then their created date.
        Combining both lists will keep the correct order first the published ones and then the rest.
     */
    private List<Article> findAllArticlesNoFilter() {
        List<Article> articles = new ArrayList<>();
        List<Article> publishedArticles = this.articleRepository.findAllByStatusOrderByPublishedDateDesc(
                ArticleStatus.PUBLISHED);
        List<Article> nonPublishedArticles = this.articleRepository.findAllNonPublishedOrderByStatusAndCreatedDateDesc(
                ArticleStatus.PUBLISHED);

        articles.addAll(publishedArticles);
        articles.addAll(nonPublishedArticles);

        return articles;
    }

    private Set<Topic> processTopics(Set<TopicDTO> topicsDTO) {
        Set<Topic> topics = new HashSet<>();

        /*
            For every topic that was submitted, we will associate the article with those that their name is not null nor
            blank. If none of the topic has a valid name an empty set is returned.

            Case 1: Topic already exists, we add it to the list of topics to be associated with the article.
            Case 2: Topic doesn't exist, so we create it first and then add it to the list.
         */
        for (TopicDTO topicDTO : topicsDTO) {
            if (topicDTO.name() != null && !topicDTO.name().isBlank()) {
                this.topicRepository.findByNameIgnoreCase(topicDTO.name()).ifPresentOrElse(
                        topics::add,
                        () -> {
                            Topic topic = new Topic(topicDTO.name());
                            this.topicRepository.save(topic);
                            topics.add(topic);
                        });
            }
        }
        return topics;
    }
}
