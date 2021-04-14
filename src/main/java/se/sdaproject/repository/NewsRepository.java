package se.sdaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sdaproject.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}
