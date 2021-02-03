package ru.vasabijaj.rest.controllers;

import ru.vasabijaj.rest.models.dto.DealRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping
public class DealController {
    @PostMapping("/deal")
    public void deal(@Valid @RequestBody DealRequest request) {
        // no code yet
    }
}
