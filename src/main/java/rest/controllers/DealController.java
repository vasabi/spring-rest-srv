package rest.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import rest.models.dto.DealRequest;
import rest.models.dto.DealResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rest.models.dto.Product;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class DealController {
    @PostMapping("/deal")
    public DealResponse deal(@Valid @RequestBody DealRequest request) {
        return new DealResponse("success", new ArrayList<>());
    }

    private List<String> validate(DealRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.getSeller() == null)
            errors.add("\"seller\" required");
        else if (request.getSeller().length() != 9)
            errors.add(String.format(
                    "\"seller\" must contain 9 characters instead of %d",
                    request.getSeller().length()
            ));

        if (request.getCustomer() == null)
            errors.add("\"customer\" required");
        else if (request.getCustomer().length() != 9)
            errors.add(String.format(
                    "\"customer\" must contain 9 characters instead of %d",
                    request.getCustomer().length()
            ));

        if (request.getProducts() == null)
            errors.add("\"products\" required");
        else if (request.getProducts().isEmpty())
            errors.add("\"products\" must contain at least one item");
        else {
            errors.addAll(validateProducts(request.getProducts()));
        }

        return errors;
    }
    
    private List<String> validateProducts(List<Product> products) {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            String ifProductKnown = String.format("\"code\" of product %s", products.get(i).getName());
            String ifProductUnknown = String.format("\"code\" of product at position %d", i + 1);

            if (products.get(i).getCode() == null)
                errors.add(
                        products.get(i).getName() != null &&
                                !products.get(i).getName().isEmpty() ?
                                ifProductKnown : ifProductUnknown + "required"
                );
            else if (products.get(i).getCode().length() != 13)
                errors.add(String.format(
                        products.get(i).getName() != null &&
                                !products.get(i).getName().isEmpty() ?
                                ifProductKnown : ifProductUnknown +
                                "\"code\" of product (position %d)+ must contain 13 characters instead of %d",
                        products.get(i).getCode().length()
                ));
        }

        return errors;
    }
}
