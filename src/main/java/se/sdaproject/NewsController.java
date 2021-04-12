package se.sdaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.util.List;

@RequestMapping("/articles")
@RestController
public class NewsController {

    NewsRepository newsRepository;

    @Autowired
    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    //RETURNS ALL ARTICLES
    @GetMapping
    public List<News> listAllArticles(){
        List<News> articles = newsRepository.findAll();
        return articles;
    }

    //RETURNS ARTICLE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<News> getArticle(@PathVariable Long id){
        News article = newsRepository.
                findById(id).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(article);
    }

    //UPDATES ARTICLE
    @PutMapping("/{id}")
    public ResponseEntity<News> updateArticle(@PathVariable Long id, @RequestBody News updatedArticle) {
        newsRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedArticle.setId(id);
        News article = newsRepository.save(updatedArticle);
        return ResponseEntity.ok(article);
    }

    //DELETES ARTICLE
    @DeleteMapping("/{id}")
    public ResponseEntity<News> deleteArticle(@PathVariable Long id) {
        News article = newsRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        newsRepository.delete(article);
        return ResponseEntity.ok(article);
    }

    //CREATES A NEW ARTICLE
    // "save" will create an object you input and persist it. It´s a post request
    // (saves, but if in application.properties in the last line is "create" instead of "validate", the table will be dropped if we stop Springboot)
    @PostMapping
    public ResponseEntity<News> createArticle(@RequestBody News article) {
        newsRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }


}
