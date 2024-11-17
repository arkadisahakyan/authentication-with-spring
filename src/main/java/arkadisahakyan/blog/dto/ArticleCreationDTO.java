package arkadisahakyan.blog.dto;

import arkadisahakyan.blog.model.Article;
import jakarta.validation.constraints.NotBlank;

public class ArticleCreationDTO {

    @NotBlank(message = "Title should not be empty.")
    private String title;

    @NotBlank(message = "Content should not be empty.")
    private String content;

    public ArticleCreationDTO() {
    }

    public ArticleCreationDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public ArticleCreationDTO(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
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
