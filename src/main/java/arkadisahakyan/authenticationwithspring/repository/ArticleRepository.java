package arkadisahakyan.authenticationwithspring.repository;

import arkadisahakyan.authenticationwithspring.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    Collection<Article> findAll();
    Collection<Article> findByAuthor_Username(String username);
}
