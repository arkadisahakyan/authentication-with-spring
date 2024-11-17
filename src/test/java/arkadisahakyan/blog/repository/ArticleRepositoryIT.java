package arkadisahakyan.blog.repository;

import arkadisahakyan.blog.dto.ArticleSummaryDTO;
import arkadisahakyan.blog.model.Article;
import arkadisahakyan.blog.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleRepositoryIT {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnAllArticleSummaries() {
        // prepare
        User author = userRepository.save(new User(0L, "user", "password"));
        for (int i = 1; i <= 5; i++)
            articleRepository.save(new Article(0L, "Article #" + i, "Some text.", null, null, author));
        // test
        Page<ArticleSummaryDTO> articleSummaries = articleRepository.getAllArticleSummaries(PageRequest.of(0, 10));
        assertEquals(5, articleSummaries.getTotalElements());
    }

    @Test
    void shouldReturnAllArticleSummariesByUserId() {
        // prepare
        User author = userRepository.save(new User(0L, "user", "password"));
        for (int i = 1; i <= 5; i++)
            articleRepository.save(new Article(0L, "Article #" + i, "Some text.", null, null, author));
        User otherAuthor = userRepository.save(new User(0L, "other_user", "password"));
        for (int i = 1; i <= 5; i++)
            articleRepository.save(new Article(0L, "Article #" + i, "Some text.", null, null, otherAuthor));
        // test
        Page<ArticleSummaryDTO> articleSummaries = articleRepository.getAllArticleSummariesByUserId(PageRequest.of(0, 10), author.getId());
        assertEquals(5, articleSummaries.getTotalElements());
        for (ArticleSummaryDTO articleSummary : articleSummaries.getContent())
            assertEquals(author.getUsername(), articleSummary.getAuthor().getUsername());
    }
}