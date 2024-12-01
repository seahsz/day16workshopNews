package vttp.ssf.day16workshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.ssf.day16workshop.models.News;
import vttp.ssf.day16workshop.models.SearchParam;
import vttp.ssf.day16workshop.services.NewsSearchService;

import static vttp.ssf.day16workshop.Constants.*;

@Controller
@RequestMapping
public class NewsSearchController {

    @Autowired
    private NewsSearchService newsSearchSvc;

    @GetMapping("/search")
    public String getSearch(
            Model model,
            @RequestParam MultiValueMap<String, String> formValue) {

        SearchParam searchParam = new SearchParam(
                formValue.getFirst("query"),
                formValue.getFirst("country"),
                formValue.getFirst("category"));

        List<News> newsList = newsSearchSvc.getNews(searchParam);

        model.addAttribute(ATTR_NEWS_LIST, newsList); 
        model.addAttribute(ATTR_SEARCH_PARAM, searchParam);      

        return "searchResult";
    }

}
