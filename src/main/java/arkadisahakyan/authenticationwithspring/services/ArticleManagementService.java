package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.dto.ArticleCreationDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleDTO;
import arkadisahakyan.authenticationwithspring.dto.UserDTO;
import arkadisahakyan.authenticationwithspring.exceptions.ArticleNotFoundException;
import arkadisahakyan.authenticationwithspring.model.Article;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.ArticleRepository;
import arkadisahakyan.authenticationwithspring.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleManagementService implements IArticleManagementService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleManagementService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Long saveArticle(ArticleCreationDTO articleCreationDTO) {
        Article article = articleCreationDTO.toArticle();
        CustomUserDetails author = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setAuthor(new User(author.getUserId()));
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    @Override
    public ArticleDTO getArticleConvertedToHTML(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty())
            throw new ArticleNotFoundException("No article found by given id.");
        return convertToHTML(new ArticleDTO(article.get()));
    }

    @Override
    public Collection<ArticleDTO> getAllArticles() {
        Collection<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(article -> new ArticleDTO(article.getId(), article.getTitle(), article.getContent(), article.getCreatedAt(), article.getUpdatedAt(), new UserDTO(article.getAuthor())))
                .collect(Collectors.toList());
    }

    private ArticleDTO convertToHTML(ArticleDTO articleDTO) {
        String content = articleDTO.getContent();
        content = HtmlUtils.htmlEscape(content);
        // create img tags
        content = content.replaceAll("\\t/?(.+)", "<img class=\"article-image\" src=\"http://localhost:8080/$1\">\n");
        // remove extra lines
        content = content.trim().replaceAll("(?:\\r\\n)+", "\n");
        // create p tags
        content = content.replaceAll("(?:^|\\n)(?!<img)(.+)(?=$|\\n)", "<p class=\"article-paragraph\">$1</p>\n");
        articleDTO.setContent(content);
        return articleDTO;
    }
}
