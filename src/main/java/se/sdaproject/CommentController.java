package se.sdaproject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    //Updates a comment
    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment updatedComment) {
        Comment comment = commentRepository
                .findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedComment.setId(id);
        commentRepository.save(updatedComment);
        return ResponseEntity.ok(updatedComment);

    }

    //Returns all comments on article given by articleID
    @GetMapping("/articles/{articleID}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long articleID) {
        News article = newsRepository.findById(articleID).orElseThrow(ResourceNotFoundException::new);
        List<Comment> comment = article.getComments();
        return ResponseEntity.ok(comment);
    }

    //Returns all comments made by author given by authorName
    @GetMapping(value = "/comments", params = {"authorName"})
    public ResponseEntity<List<Comment>> getCommentByAuthor(@RequestParam (value = "authorName") String authorName) {
        List<Comment> sortedComments = commentRepository.findByAuthorName(authorName);
        return ResponseEntity.ok(sortedComments);

    }

    //Deletes a comment
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        commentRepository.delete(comment);
    }



}
