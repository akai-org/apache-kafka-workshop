package pl.org.akai.kafkaintro.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String home() {
        return "list";
    }

    @GetMapping("/add")
    public String addView() {
        return "add";
    }
}


