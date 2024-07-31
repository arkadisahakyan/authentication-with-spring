package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.dto.ArticleCreationDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleUpdateDTO;
import arkadisahakyan.authenticationwithspring.dto.UserDTO;
import arkadisahakyan.authenticationwithspring.exceptions.ArticleNotFoundException;
import arkadisahakyan.authenticationwithspring.model.Article;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.ArticleRepository;
import arkadisahakyan.authenticationwithspring.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleManagementService implements IArticleManagementService {
    public static final Integer DEFAULT_PAGINATION_SIZE = 10;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleManagementService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Long createArticle(ArticleCreationDTO articleCreationDTO) {
        Article article = articleCreationDTO.toArticle();
        CustomUserDetails author = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setAuthor(new User(author.getUserId()));
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    @Override
    public Long updateArticle(ArticleUpdateDTO articleUpdateDTO, Long articleId) {
        Optional<Article> currentArticle = articleRepository.findById(articleId);
        if (currentArticle.isEmpty())
            throw new ArticleNotFoundException("No article found by given id.");
        CustomUserDetails author = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (author.getUserId() != currentArticle.get().getAuthor().getId())
            throw new AccessDeniedException("You can't change someone else's article.");
        Article articleToUpdate = articleUpdateDTO.toArticle();
        articleToUpdate.setId(currentArticle.get().getId());
        articleToUpdate.setAuthor(currentArticle.get().getAuthor());
        articleToUpdate.setCreatedAt(currentArticle.get().getCreatedAt());
        return articleRepository.save(articleToUpdate).getId();
    }

    @Override
    public void deleteArticle(Long articleId) {
        Optional<Article> currentArticle = articleRepository.findById(articleId);
        if (!currentArticle.isEmpty()) {
            CustomUserDetails author = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (author.getUserId() == currentArticle.get().getAuthor().getId())
                articleRepository.delete(currentArticle.get());
        } else
            throw new ArticleNotFoundException("No article found by given id.");
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty())
            throw new ArticleNotFoundException("No article found by given id.");
        return new ArticleDTO(article.get());
    }

    @Override
    public ArticleDTO getArticleByIdConvertedToHTML(Long id) {
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

    @Override
    public Page<ArticleDTO> getAllArticles(Pageable pageable) {
        Page<Article> articlesTemp = articleRepository.findAll(pageable);
        List<ArticleDTO> articlesAsList = articlesTemp.stream()
                .map(article -> new ArticleDTO(article.getId(), article.getTitle(), article.getContent(), article.getCreatedAt(), article.getUpdatedAt(), new UserDTO(article.getAuthor())))
                .collect(Collectors.toList());
        Page<ArticleDTO> articles = new PageImpl<>(articlesAsList, pageable, articlesTemp.getTotalElements());
        return articles;
    }

    @Override
    public Collection<ArticleDTO> getAllArticlesOfCurrentUser() {
        CustomUserDetails author = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<Article> articles = articleRepository.findByAuthor_Username(author.getUsername());
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
