package arkadisahakyan.authenticationwithspring.controllers;

import arkadisahakyan.authenticationwithspring.dto.ArticleCreationDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleDTO;
import arkadisahakyan.authenticationwithspring.dto.UserDTO;
import arkadisahakyan.authenticationwithspring.exceptions.ArticleNotFoundException;
import arkadisahakyan.authenticationwithspring.services.IArticleManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class ArticleController {
    private final IArticleManagementService articleManagementService;

    @Autowired
    public ArticleController(IArticleManagementService articleManagementService) {
        this.articleManagementService = articleManagementService;
    }

    @GetMapping(value = "/article/new")
    public String articleCreationEditor() {
        return "create_article";
    }

    @GetMapping(value = "/article/{articleId:.+}")
    public String article(@PathVariable String articleId, Model model) {
        ArticleDTO articleDTO = articleManagementService.getArticleConvertedToHTML(Long.valueOf(articleId));
        model.addAttribute("article", articleDTO);
        return "article";
    }

    @GetMapping(value = "/articles")
    public String articles(Model model) {
        Collection<ArticleDTO> articles = articleManagementService.getAllArticles();
        model.addAttribute("articlesList", articles);
        return "articles";
    }

    @PostMapping(value = "/article/new", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createArticle(@Valid ArticleCreationDTO articleDTO) {
        return "redirect:/article/" + articleManagementService.saveArticle(articleDTO);
    }

    @ExceptionHandler(value = {ArticleNotFoundException.class, NumberFormatException.class})
    public String ifArticleNotFound() {
        return "error/404";
    }
}
