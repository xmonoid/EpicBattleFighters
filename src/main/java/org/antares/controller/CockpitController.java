package org.antares.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

@Controller
public class CockpitController {
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/")
    public String cockpit(Model model) throws IOException {
        var resource = resourceLoader.getResource("classpath:images");
        var file = resource.getFile();
        var options = new ArrayList<>(Stream.of(file.listFiles())
                .map(File::getName)
                .map((name) -> name.substring(0, name.lastIndexOf(".")))
                .filter(name -> !name.equalsIgnoreCase("Nobody")
                        && !name.equalsIgnoreCase("background")
                        && !name.equalsIgnoreCase("winner"))
                .toList());
        model.addAttribute("options", options);
        return "cockpit";
    }

    @GetMapping("/board")
    public String board() {
        return "board";
    }

    @RequestMapping(value="/do-stuff")
    public void doStuffMethod() {
        System.out.println("Success");
    }
}
