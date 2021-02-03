package rest.controllers;

import rest.models.dto.DealRequest;
import rest.models.dto.DealResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping
public class DealController {
    @PostMapping("/deal")
    public DealResponse deal(@Valid @RequestBody DealRequest request) {
        return new DealResponse("success", new ArrayList<>());
    }
}
