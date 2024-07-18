package com.example.demo.controller;

import com.example.demo.model.Link;
import com.example.demo.password.PassEncTech1;
import com.example.demo.qr.GenerateQrCode;
import com.example.demo.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;
    private final String LOCAL_URL = "http://localhost:8080/";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(path = "/save")
    public ModelAndView newLink(@RequestParam String password, @RequestParam boolean qrBox, @RequestParam String urlInput) {
        Link link = new Link();
        link.setBigUrl(urlInput);
        link.setPassword(password);
        link = linkService.save(link);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("link", link);
        modelAndView.addObject("local_url", LOCAL_URL);
        String localUrl = LOCAL_URL + link.getUrl();
        if (qrBox) {
            modelAndView.addObject("qrCode", GenerateQrCode.generateQrcode(localUrl));
        }
        return modelAndView;
    }

    @GetMapping("/checkPassword/{url}")
    public ModelAndView checkPassword(@RequestParam String password, @PathVariable String url) {
        ModelAndView modelAndView;
        Link link = linkService.getLink(url);
        if (!link.getPassword()
                 .equals(PassEncTech1.encrypt(password))) {
            modelAndView = new ModelAndView("redirect:/"+url);
            modelAndView.addObject("error", "invalid password");
            return modelAndView;
        }
        modelAndView = new ModelAndView("redirect:" + link.getBigUrl());
        return modelAndView;
    }

    @GetMapping("/{url}")
    public ModelAndView redirect(@PathVariable String url, @ModelAttribute("error") String error) {
        ModelAndView modelAndView;
        Link link = linkService.getLink(url);
        if (!link.getPassword()
                 .trim()
                 .isEmpty()) {
            modelAndView = new ModelAndView("redirect");
            modelAndView.addObject("url", link.getUrl());
            return modelAndView;
        }
        modelAndView = new ModelAndView("redirect:" + link.getBigUrl());
        return modelAndView;
    }
}
