package arkadisahakyan.authenticationwithspring.dto;

import arkadisahakyan.authenticationwithspring.model.Article;
import jakarta.validation.constraints.NotBlank;

public class ArticleUpdateDTO implements IArticleDTO {

    @NotBlank(message = "Title should not be empty.")
    private String title;

    @NotBlank(message = "Content should not be empty.")
    private String content;

    public ArticleUpdateDTO() {
    }

    public ArticleUpdateDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public Article toArticle() {
        return new Article(0L, title, content, null, null, null);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
