package se.sdaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TopicController {

    TopicRepository topicRepository;
    NewsRepository newsRepository;

    @Autowired
    public TopicController(TopicRepository topicRepository, NewsRepository newsRepository) {
        this.topicRepository = topicRepository;
        this.newsRepository = newsRepository;
    }

    // Creates a new topic
    @PostMapping("/topics")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(topic);
    }

    //Returns all topics
    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> listAllTopics(){
        List<Topic> topic = topicRepository.findAll();
        return ResponseEntity.ok(topic);
    }

    // Returns all topics associated with an article given by articleId.
    @GetMapping("/articles/{articleId}/topics")
    public ResponseEntity<List<Topic>> listTopicsOfArticle(@PathVariable Long articleId) {
        News article = newsRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(article.getTopic());
    }

    //Returns all articles associated with the topic given by topicId.
    @GetMapping("/topics/{topicId}/articles")
    public ResponseEntity<List<News>> listArticlesWithTopicByTopicID(@PathVariable Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        List<News> articles = topic.getArticles();
        return ResponseEntity.ok(articles);
    }

    //Deletes a topic
    @DeleteMapping("/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        topicRepository.delete(topic);
    }

    //Deletes the association of a topic for a specific article.
    //The topic and article themselves remain
    @DeleteMapping("/articles/{articleId}/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssociationOfTopicWithArticle(@PathVariable Long articleId, @PathVariable Long topicId) {
        News article = newsRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);

        if (article.getTopic().contains(topic)) {
            article.getTopic().remove(topic);
            newsRepository.save(article);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    //Updates a topic
    @PutMapping("/topics/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic updatedTopic) {
        topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedTopic.setId(id);
        Topic topic = topicRepository.save(updatedTopic);
        return ResponseEntity.ok(updatedTopic);
    }

    //Associates a topic with the article given by articleId.
    // If topic does not already exist, it is created.
    @PostMapping("/articles/{articleId}/topics")
    public ResponseEntity<News> associateTopicWithArticle(@PathVariable Long articleId, @RequestBody Topic topic) {
        News article = newsRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);

        List<Topic> associatedTopic = topicRepository
                .findByName(topic.getName());

            if (associatedTopic.isEmpty()){
                topicRepository.save(topic);
            }
            article.getTopic().add(topic);
            newsRepository.save(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }






}
