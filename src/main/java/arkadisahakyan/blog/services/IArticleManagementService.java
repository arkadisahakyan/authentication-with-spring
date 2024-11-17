package arkadisahakyan.blog.services;

import arkadisahakyan.blog.dto.ArticleCreationDTO;
import arkadisahakyan.blog.dto.ArticleDTO;
import arkadisahakyan.blog.dto.ArticleSummaryDTO;
import arkadisahakyan.blog.dto.ArticleUpdateDTO;
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
