package arkadisahakyan.blog.repository;

import arkadisahakyan.blog.dto.ArticleSummaryDTO;
import arkadisahakyan.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    Collection<Article> findAll();

    @Query(value = "SELECT new arkadisahakyan.blog.dto.ArticleSummaryDTO(a.id, a.title, a.createdAt, a.updatedAt, new arkadisahakyan.blog.dto.UserDTO(u.id, u.username)) FROM Article a LEFT JOIN User u ON a.author.id=u.id")
    Page<ArticleSummaryDTO> getAllArticleSummaries(Pageable pageable);

    @Query(value = "SELECT new arkadisahakyan.blog.dto.ArticleSummaryDTO(a.id, a.title, a.createdAt, a.updatedAt, new arkadisahakyan.blog.dto.UserDTO(u.id, u.username)) FROM Article a LEFT JOIN User u ON a.author.id=u.id WHERE u.id=:id")
    Page<ArticleSummaryDTO> getAllArticleSummariesByUserId(Pageable pageable, Long id);

    Collection<Article> findByAuthor_Username(String username);
}
