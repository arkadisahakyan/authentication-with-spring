package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.dto.ArticleCreationDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleSummaryDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IArticleManagementService {
    Long createArticle(ArticleCreationDTO articleCreationDTO);
    Long updateArticle(ArticleUpdateDTO articleUpdateDTO);
    void deleteArticle(Long articleId);
    ArticleDTO getArticleById(Long id);
    ArticleDTO getArticleByIdConvertedToHTML(Long id);
    Page<ArticleSummaryDTO> getAllArticleSummaries(Pageable pageable);
    Page<ArticleSummaryDTO> getAllArticleSummariesOfCurrentUser(Pageable pageable);
}
