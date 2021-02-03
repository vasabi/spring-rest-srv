package ru.vasabijaj.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.vasabijaj.rest.models.dto.DealRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vasabijaj.rest.service.ThreadSafeService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@RestController
@RequestMapping
public class DealController {
    private final ThreadSafeService<String, String> service = new ThreadSafeService<>();

    @PostMapping("/deal")
    public ResponseEntity<Object> deal(@Valid @RequestBody DealRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        List<String> firstSeenProductCodes = new ArrayList<>();
        body.put("status", HttpStatus.OK);

        request.getProducts().forEach(product -> {
            String productName = product.getName() != null ? product.getName() : "unknown";
            Future<String> future = service.compute(productName, s -> s.concat(":").concat(product.getCode()));

            try {
                firstSeenProductCodes.add(future.get());
            } catch (Exception e) {
                body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
                body.put("errors", Collections.singletonList(e.getMessage()));
            }
        });

        body.put("products", firstSeenProductCodes);

        return new ResponseEntity<>(body, (HttpStatus) body.get("status"));
    }
}
