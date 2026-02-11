package com.velox.onlinefilemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping({"/", "/file-manager"})
    public String fileManagerPage(){
        return "forward:/file-manager.html";
    }
}
