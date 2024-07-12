package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.dto.ArticleCreationDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleDTO;

import java.util.Collection;

public interface IArticleManagementService {
    Long saveArticle(ArticleCreationDTO articleCreationDTO);
    ArticleDTO getArticleConvertedToHTML(Long id);
    Collection<ArticleDTO> getAllArticles();
}
