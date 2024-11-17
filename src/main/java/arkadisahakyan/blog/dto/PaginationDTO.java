package arkadisahakyan.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaginationDTO {
    private int currentPage;
    private int pageCount;
    private int paginationSize;
    private int pageSize;
}
