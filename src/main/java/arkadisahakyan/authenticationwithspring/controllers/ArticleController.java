package arkadisahakyan.authenticationwithspring.controllers;

import arkadisahakyan.authenticationwithspring.dto.*;
import arkadisahakyan.authenticationwithspring.exceptions.ArticleNotFoundException;
import arkadisahakyan.authenticationwithspring.services.ArticleManagementService;
import arkadisahakyan.authenticationwithspring.services.IArticleManagementService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@Controller
public class ArticleController {
    private final IArticleManagementService articleManagementService;

    @Autowired
    public ArticleController(IArticleManagementService articleManagementService) {
        this.articleManagementService = articleManagementService;
    }

    @GetMapping(value = "/article/new")
    public String articleCreationEditor(Model model) {
        ArticleDTO articleDTO = new ArticleDTO();
        model.addAttribute("article", articleDTO);
        return "create_article";
    }

    @GetMapping(value = "/article/{articleId:.+}")
    public String article(@PathVariable String articleId, Model model) {
        ArticleDTO articleDTO = articleManagementService.getArticleByIdConvertedToHTML(Long.valueOf(articleId));
        model.addAttribute("article", articleDTO);
        return "article";
    }

    @GetMapping(value = "/article/{articleId:.+}/edit")
    public String articleUpdateEditor(@PathVariable String articleId, Model model) {
        ArticleDTO articleDTO = articleManagementService.getArticleById(Long.valueOf(articleId));
        model.addAttribute("article", articleDTO);
        return "update_article";
    }

    @GetMapping(value = "/articles")
    public String articles(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(name = "pagesize", required = false) Integer pageSize, HttpServletRequest request, HttpServletResponse response) {
        // handle the 'pageSize' parameter
        if (pageSize != null) {
            response.addCookie(new Cookie("pageSize", String.valueOf(pageSize)));
        } else {
            Cookie pageSizeCookie = WebUtils.getCookie(request, "pageSize");
            pageSize = pageSizeCookie == null ? 10 : Integer.valueOf(pageSizeCookie.getValue());
        }
        // show the article list
        Page<ArticleSummaryDTO> articles = articleManagementService.getAllArticleSummaries(PageRequest.of(page - 1, pageSize));
        model.addAttribute("articles", articles.toList());
        model.addAttribute("pagination", new PaginationDTO(page, articles.getTotalPages(), ArticleManagementService.DEFAULT_PAGINATION_SIZE, pageSize));
        return "articles";
    }

    @PostMapping(value = "/article/new", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createArticle(@Valid ArticleCreationDTO articleCreationDTO) {
        return "redirect:/article/" + articleManagementService.createArticle(articleCreationDTO);
    }

    @PostMapping(value = "/article/{articleId:.+}/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateArticle(@Valid ArticleUpdateDTO articleUpdateDTO, @PathVariable Long articleId) {
        articleUpdateDTO.setId(articleId);
        return "redirect:/article/" + articleManagementService.updateArticle(articleUpdateDTO);
    }

    @PostMapping(value = "/article/{articleId:.+}/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String deleteArticle(@PathVariable String articleId) {
        articleManagementService.deleteArticle(Long.valueOf(articleId));
        return "redirect:/articles";
    }

    @ExceptionHandler(value = {ArticleNotFoundException.class, NumberFormatException.class})
    public String ifArticleNotFound() {
        return "error/404";
    }
}
