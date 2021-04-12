package se.sdaproject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CommentController {

        CommentRepository commentRepository;
        NewsRepository newsRepository;

    public CommentController(CommentRepository commentRepository, NewsRepository newsRepository) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
    }

    // Creates a new comment on article given by articleId
    @PostMapping("/articles/{articleID}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable Long articleID, @RequestBody Comment comment) {
        News article = newsRepository
                .findById(articleID).orElseThrow(ResourceNotFoundException::new);
        comment.setCommentedArticle(article);
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }





}
