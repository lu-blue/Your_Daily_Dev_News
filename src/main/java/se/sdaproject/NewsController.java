package se.sdaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NewsController {

    NewsRepository newsRepository;

    @Autowired
    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    //will show an empty array for people
    @RequestMapping("/articles")
    public List<News> listAllArticles(){
        List<News> articles = newsRepository.findAll();
        return articles;
    }

    // "save" will create an object you input and persist it
    // (saves, but if in application.properties in the last line is "create" instead of "validate", the table will be dropped if we stop Springboot)
    @RequestMapping("/articles/create/{title}/{body}/{authorName}")
    public News createArticle(@PathVariable("title") String title, @PathVariable("body") String body, @PathVariable("authorName") String authorName) {
        News article = new News(title, body, authorName);
        newsRepository.save(article);
        return article;
    }


}
