package org.antares.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.antares.Application.getCurrentPath;

@Controller
public class CockpitController {

    @GetMapping("/")
    public String cockpit(Model model) throws URISyntaxException {
        String path = getCurrentPath();
        System.out.println("Current path: " + path);
        var dir = new File(path);
        var options = new ArrayList<>(Stream.of(dir.listFiles())
                .filter(File::isFile)
                .filter(file -> {
                    var name = file.getName();
                    return name.substring(name.lastIndexOf(".")+1).equalsIgnoreCase("png");
                })
                .map(File::getName)
                .map((name) -> name.substring(0, name.lastIndexOf(".")))
                .filter(name -> !name.equalsIgnoreCase("nobody")
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
