package se.sdaproject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
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

    //Updates comment
    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment updatedComment) {
        Comment comment = commentRepository
                .findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedComment.setId(id);
        commentRepository.save(updatedComment);
        return ResponseEntity.ok(updatedComment);

    }







}
