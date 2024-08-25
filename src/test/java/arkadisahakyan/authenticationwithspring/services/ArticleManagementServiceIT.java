package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.dto.ArticleCreationDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleSummaryDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleUpdateDTO;
import arkadisahakyan.authenticationwithspring.model.Article;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.ArticleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Import(ArticleManagementService.class)
class ArticleManagementServiceIT {

    @SpyBean
    private ArticleRepository articleRepository;

    @SpyBean
    private UserRepository userRepository;

    @SpyBean
    private ModelMapper modelMapper;

    @Autowired
    private IArticleManagementService articleManagementService;

    @BeforeAll
    void setUp() {
        userRepository.save(new User(0L, "user", "user", null));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldCreateArticle() {
        ArticleCreationDTO article = new ArticleCreationDTO("Welcome", "Hello, world!");
        Long articleId = articleManagementService.createArticle(article);
        Optional<Article> result = articleRepository.findById(articleId);
        assertTrue(result.isPresent());
        Article createdArticle = result.get();
        assertEquals("Welcome", createdArticle.getTitle());
        assertEquals("Hello, world!", createdArticle.getContent());
        assertEquals("user", createdArticle.getAuthor().getUsername());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldUpdateArticle() {
        ArticleCreationDTO article = new ArticleCreationDTO("Welcome", "Hello, world!");
        Long articleId = articleManagementService.createArticle(article);
        ArticleUpdateDTO articleUpdateDTO = new ArticleUpdateDTO(articleId, "New Title", "New content.");
        articleManagementService.updateArticle(articleUpdateDTO);
        Optional<Article> result = articleRepository.findById(articleId);
        assertTrue(result.isPresent());
        Article updatedArticle = result.get();
        assertEquals("New Title", updatedArticle.getTitle());
        assertEquals("New content.", updatedArticle.getContent());
        assertEquals("user", updatedArticle.getAuthor().getUsername());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldDeleteArticle() {
        ArticleCreationDTO article = new ArticleCreationDTO("Welcome", "Hello, world!");
        Long articleId = articleManagementService.createArticle(article);
        articleManagementService.deleteArticle(articleId);
        assertTrue(articleRepository.findById(articleId).isEmpty());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldReturnArticleById() {
        ArticleCreationDTO article = new ArticleCreationDTO("Welcome", "Hello, world!");
        Long articleId = articleManagementService.createArticle(article);
        ArticleDTO result = articleManagementService.getArticleById(articleId);
        assertEquals("Welcome", result.getTitle());
        assertEquals("Hello, world!", result.getContent());
        assertEquals("user", result.getAuthor().getUsername());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldReturnArticleByIdConvertedToHTML() {
        ArticleCreationDTO article = new ArticleCreationDTO("Welcome", "Hello, world!\n" +
                                                                                    "\t/my/image.jpg\n" +
                                                                                    "Another text.\n" +
                                                                                    "Yet another text.\n");
        Long articleId = articleManagementService.createArticle(article);
        ArticleDTO result = articleManagementService.getArticleByIdConvertedToHTML(articleId);
        assertEquals("Welcome", result.getTitle());
        assertEquals("<p class=\"article-paragraph\">Hello, world!</p>\n" +
                            "<img class=\"article-image\" src=\"/my/image.jpg\">\n" +
                            "<p class=\"article-paragraph\">Another text.</p>\n" +
                            "<p class=\"article-paragraph\">Yet another text.</p>", result.getContent());
        assertEquals("user", result.getAuthor().getUsername());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldReturnAllArticleSummaries() {
        for (int i = 1; i <= 5; i++) {
            ArticleCreationDTO article = new ArticleCreationDTO("Article #" + i, "Some text.");
            articleManagementService.createArticle(article);
        }
        Page<ArticleSummaryDTO> articleSummaries = articleManagementService.getAllArticleSummaries(PageRequest.of(0, 10));
        assertEquals(5, articleSummaries.getTotalElements());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldReturnAllArticleSummariesOfCurrentUser() {
        for (int i = 1; i <= 5; i++) {
            ArticleCreationDTO article = new ArticleCreationDTO("Article #" + i, "Some text.");
            articleManagementService.createArticle(article);
        }
        Page<ArticleSummaryDTO> articleSummaries = articleManagementService.getAllArticleSummariesOfCurrentUser(PageRequest.of(0, 10));
        assertEquals(5, articleSummaries.getTotalElements());
        for (ArticleSummaryDTO articleSummary : articleSummaries.getContent()) {
            assertEquals("user", articleSummary.getAuthor().getUsername());
        }
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteAll();
    }

    @TestConfiguration
    static class Configuration {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }
}