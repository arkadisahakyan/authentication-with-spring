package arkadisahakyan.authenticationwithspring.controllers;

import arkadisahakyan.authenticationwithspring.dto.ArticleCreationDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleDTO;
import arkadisahakyan.authenticationwithspring.dto.ArticleUpdateDTO;
import arkadisahakyan.authenticationwithspring.exceptions.ArticleNotFoundException;
import arkadisahakyan.authenticationwithspring.services.IArticleManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;

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
    public String articles(Model model) {
        Collection<ArticleDTO> articles = articleManagementService.getAllArticles();
        model.addAttribute("articlesList", articles);
        return "articles";
    }

    @PostMapping(value = "/article/new", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createArticle(@Valid ArticleCreationDTO articleCreationDTO) {
        return "redirect:/article/" + articleManagementService.createArticle(articleCreationDTO);
    }

    @PostMapping(value = "/article/{articleId:.+}/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateArticle(@Valid ArticleUpdateDTO articleUpdateDTO, @PathVariable String articleId) {
        return "redirect:/article/" + articleManagementService.updateArticle(articleUpdateDTO, Long.valueOf(articleId));
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
