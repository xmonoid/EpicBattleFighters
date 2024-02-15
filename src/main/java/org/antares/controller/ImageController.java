package org.antares.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antares.model.Images;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ResourceLoader resourceLoader;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String fighterLeft = "Nobody";
    private String fighterRight = "Nobody";

    @GetMapping(
            value = "/{name}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] getImage(@PathVariable String name) throws IOException {
        System.out.println("Requested " + name);
        try {
            var resource = resourceLoader.getResource("classpath:images/" + name + ".png");
            var file = resource.getFile();
            return IOUtils.toByteArray(file.toURI());
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @GetMapping(
            value = "/list"
    )
    public ResponseEntity<String> getImageList() throws JsonProcessingException {
        System.out.println("Requested a set of images");
        var response = new Images(
                "background",
                fighterLeft,
                fighterRight
        );
        System.out.println("Returned " + response);
        return ResponseEntity.ok(objectMapper.writeValueAsString(response));
    }

    @PostMapping(
            value = "/change"
    )
    public void setImageList(@RequestParam("left") String fighterLeft,
                             @RequestParam("right") String fighterRight) {
        if (fighterLeft == null || fighterLeft.isBlank()) {
            fighterLeft = "Nobody";
        }
        if (fighterRight == null || fighterRight.isBlank()) {
            fighterRight = "Nobody";
        }
        System.out.println("Set left to " + fighterLeft + " and right to " + fighterRight);
        this.fighterLeft = fighterLeft;
        this.fighterRight = fighterRight;
    }
}
