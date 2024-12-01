package vttp.ssf.day16workshop.controllers;

import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp.ssf.day16workshop.services.IndexService;

import static vttp.ssf.day16workshop.Constants.*;

@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private IndexService indexSvc;

    @GetMapping(path = { "/", "index.html" })
    public String getIndex(Model model) {

        // Get Map of <Country Name, Country Code> from REST COUNTRIES API
        SortedMap<String, String> countryNameCca2Map = indexSvc.getCountryNameAndCca2();

        model.addAttribute(ATTR_COUNTRY_NAME_AND_CODE_MAP, countryNameCca2Map);

        return "index";
    }

}
