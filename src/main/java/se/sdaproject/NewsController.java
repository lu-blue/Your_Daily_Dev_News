package se.sdaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NewsController {

    NewsRepository newsRepository;

    @Autowired
    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    //RETURNS ALL ARTICLES
    @GetMapping("/articles")
    public List<News> listAllArticles(){
        List<News> articles = newsRepository.findAll();
        return articles;
    }

    //RETURNS ARTICLE BY ID
    @GetMapping("/articles/{id}")
    public ResponseEntity<News> getArticle(@PathVariable Long id){
        News article = newsRepository.
                findById(id).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(article);
    }

    //UPDATES ARTICLE
    @PutMapping("/articles/{id}")
    public ResponseEntity<News> updateArticle(@PathVariable Long id, @RequestBody News updatedArticle) {
        newsRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedArticle.setId(id);
        News article = newsRepository.save(updatedArticle);
        return ResponseEntity.ok(article);

    }

    //CREATES A NEW ARTICLE
    // "save" will create an object you input and persist it. It´s a post request
    // (saves, but if in application.properties in the last line is "create" instead of "validate", the table will be dropped if we stop Springboot)
    @PostMapping("/articles")
    public ResponseEntity<News> createArticle(@RequestBody News article) {
        newsRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }


}
