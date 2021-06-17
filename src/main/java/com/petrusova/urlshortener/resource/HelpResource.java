package com.petrusova.urlshortener.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/help")
public class HelpResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpResource.class);

    @GetMapping()
    public String getHelp() {
        LOGGER.info("Displaying help page.");
        return "helpPage";
    }
}
