package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.dto.*;
import arkadisahakyan.authenticationwithspring.exceptions.ArticleNotFoundException;
import arkadisahakyan.authenticationwithspring.model.Article;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.ArticleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import arkadisahakyan.authenticationwithspring.userdetails.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Optional;

@Service
public class ArticleManagementService implements IArticleManagementService {
    public static final Integer DEFAULT_PAGINATION_SIZE = 10;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ArticleManagementService(ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long createArticle(ArticleCreationDTO articleCreationDTO) {
        Article article = modelMapper.map(articleCreationDTO, Article.class);
        Authentication author = SecurityContextHolder.getContext().getAuthentication();
        article.setAuthor(userRepository.findByUsername(author.getName()));
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    @Override
    public Long updateArticle(ArticleUpdateDTO articleUpdateDTO) {
        Optional<Article> currentArticle = articleRepository.findById(articleUpdateDTO.getId());
        if (currentArticle.isEmpty())
            throw new ArticleNotFoundException("No article found by given id.");
        Authentication author = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.findByUsername(author.getName()).equals(currentArticle.get().getAuthor()))
            throw new AccessDeniedException("You can't change someone else's article.");
        Article articleToUpdate = modelMapper.map(articleUpdateDTO, Article.class);
        articleToUpdate.setId(currentArticle.get().getId());
        articleToUpdate.setAuthor(currentArticle.get().getAuthor());
        articleToUpdate.setCreatedAt(currentArticle.get().getCreatedAt());
        return articleRepository.save(articleToUpdate).getId();
    }

    @Override
    public void deleteArticle(Long articleId) {
        Optional<Article> currentArticle = articleRepository.findById(articleId);
        if (!currentArticle.isEmpty()) {
            Authentication author = SecurityContextHolder.getContext().getAuthentication();
            if (userRepository.findByUsername(author.getName()).equals(currentArticle.get().getAuthor()))
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
    public Page<ArticleSummaryDTO> getAllArticleSummaries(Pageable pageable) {
        Page<ArticleSummaryDTO> articles = articleRepository.getAllArticleSummaries(pageable);
        return articles;
    }

    @Override
    public Page<ArticleSummaryDTO> getAllArticleSummariesOfCurrentUser(Pageable pageable) {
        Authentication author = SecurityContextHolder.getContext().getAuthentication();
        Page<ArticleSummaryDTO> articles = articleRepository.getAllArticleSummariesById(pageable, userRepository.findByUsername(author.getName()).getId());
        return articles;
    }
    private ArticleDTO convertToHTML(ArticleDTO articleDTO) {
        String content = articleDTO.getContent();
        content = HtmlUtils.htmlEscape(content);
        // remove extra lines
        content = content.trim().replaceAll("(?:\\r\\n)+", "\n");
        // create img tags
        content = content.replaceAll("(?<=^|\\n)\\t(.+)(?=$|\\n)", "<img class=\"article-image\" src=\"$1\">");
        // create p tags
        content = content.replaceAll("(?<=^|\\n)(?!<img)(.+)(?=$|\\n)", "<p class=\"article-paragraph\">$1</p>");
        articleDTO.setContent(content);
        return articleDTO;
    }
}
