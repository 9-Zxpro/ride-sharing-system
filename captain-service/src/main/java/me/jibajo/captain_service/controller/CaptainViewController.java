package me.jibajo.captain_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/captain/view")
public class CaptainViewController {

    @GetMapping("/ride/{id}")
    public String getCaptainById(@PathVariable Long id) {
        return "index";
    }

//    @RequestMapping("/ride/offer")
//    public String home(Model model) {
//        model.addAttribute("pageName", "This is jiba.com");
//        model.addAttribute("github", "https://github.com/9-zxpro");
//        return "index";
//    }
}
