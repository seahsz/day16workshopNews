package vttp.ssf.day16workshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.ssf.day16workshop.models.searchParam;

@Controller
@RequestMapping
public class newsSearchController {

    @GetMapping("/search")
    public String getSearch(
            Model model,
            @RequestParam MultiValueMap<String, String> formValue) {

        searchParam searchParam = new searchParam(
                formValue.getFirst("query"),
                formValue.getFirst("country"),
                formValue.getFirst("category"));

        

        return "";
    }

}
