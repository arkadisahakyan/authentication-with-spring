package arkadisahakyan.authenticationwithspring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleUpdateDTO {

    private Long id;

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

    public ArticleUpdateDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
