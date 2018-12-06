package com.exhibition.reserve.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReserveController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

}
