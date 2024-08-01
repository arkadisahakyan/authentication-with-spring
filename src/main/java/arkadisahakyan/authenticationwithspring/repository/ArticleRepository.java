package arkadisahakyan.authenticationwithspring.repository;

import arkadisahakyan.authenticationwithspring.dto.ArticleSummaryDTO;
import arkadisahakyan.authenticationwithspring.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    Collection<Article> findAll();

    @Query(value = "SELECT new arkadisahakyan.authenticationwithspring.dto.ArticleSummaryDTO(a.id, a.title, a.createdAt, a.updatedAt, new arkadisahakyan.authenticationwithspring.dto.UserDTO(u.id, u.username)) FROM Article a LEFT JOIN User u ON a.author.id=u.id")
    Page<ArticleSummaryDTO> getAllArticleSummaries(Pageable pageable);

    @Query(value = "SELECT new arkadisahakyan.authenticationwithspring.dto.ArticleSummaryDTO(a.id, a.title, a.createdAt, a.updatedAt, new arkadisahakyan.authenticationwithspring.dto.UserDTO(u.id, u.username)) FROM Article a LEFT JOIN User u ON a.author.id=u.id WHERE u.id=:id")
    Page<ArticleSummaryDTO> getAllArticleSummariesById(Pageable pageable, Long id);

    Collection<Article> findByAuthor_Username(String username);
}
