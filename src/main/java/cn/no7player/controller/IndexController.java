package cn.no7player.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping(value = "/index")
public class IndexController {


    @RequestMapping(value = "",method = RequestMethod.GET)
    private String index()
    {
        return "index";
    }
}
