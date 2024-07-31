package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.dto.ArticleCreationDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface IArticleManagementService {
    Long createArticle(ArticleCreationDTO articleCreationDTO);
    Long updateArticle(ArticleUpdateDTO articleUpdateDTO, Long articleId);
    void deleteArticle(Long articleId);
    ArticleDTO getArticleById(Long id);
    ArticleDTO getArticleByIdConvertedToHTML(Long id);
    Collection<ArticleDTO> getAllArticles();
    Page<ArticleDTO> getAllArticles(Pageable pageable);
    Collection<ArticleDTO> getAllArticlesOfCurrentUser();
}
