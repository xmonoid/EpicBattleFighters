package org.antares.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antares.model.Images;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.antares.Application.getCurrentPath;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String fighterLeft = "Nobody";
    private String fighterRight = "Nobody";
    private boolean leftWins = false;
    private boolean rightWins = false;

    @GetMapping(
            value = "/{name}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] getImage(@PathVariable String name) throws IOException, URISyntaxException {
        System.out.println("Requested " + name);
        if (name.equalsIgnoreCase("undefined")) {
            return null;
        }
        try {
            String path = getCurrentPath();
            var file = new File(new ClassPathResource(path + name + ".png").getPath());
            return IOUtils.toByteArray(file.toURI());
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private Images previous;
    @GetMapping(
            value = "/list"
    )
    public ResponseEntity<String> getImageList() throws JsonProcessingException {
        var response = new Images(
                "background",
                fighterLeft,
                fighterRight,
                leftWins,
                rightWins
        );
        if (!response.equals(previous)) {
            previous = response;
            System.out.println("Returned " + response);
        }
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
        System.out.println("left = '" + fighterLeft + "'" + System.lineSeparator() + "right = '" + fighterRight + "'");
        this.fighterLeft = fighterLeft;
        this.fighterRight = fighterRight;
    }

    @PostMapping(
            value = "/winner/{winner}"
    )
    public void setWinner(@PathVariable String winner) {
        switch (winner) {
            case "left":
                leftWins = true;
                rightWins = false;
                break;
            case "right":
                leftWins = false;
                rightWins = true;
                break;
            case "cancel":
                leftWins = false;
                rightWins = false;
        }
    }
}
