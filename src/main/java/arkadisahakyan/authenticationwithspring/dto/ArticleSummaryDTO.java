package arkadisahakyan.authenticationwithspring.dto;

import java.util.Date;

public class ArticleSummaryDTO {
    private Long id;
    private String title;
    private Date createdAt;
    private Date updatedAt;
    private UserDTO author;

    public ArticleSummaryDTO() {
    }

    public ArticleSummaryDTO(Long id, String title, Date createdAt, Date updatedAt, UserDTO author) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }
}
