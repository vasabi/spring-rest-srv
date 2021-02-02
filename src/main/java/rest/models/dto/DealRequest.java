package rest.models.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class DealRequest {
    @NotNull(message = "seller required")
    @Size(min = 9, max = 9, message = "seller must contain 9 characters")
    private String seller;
    @NotNull(message = "customer required")
    @Size(min = 9, max = 9, message = "customer must contain 9 characters")
    private String customer;
    @NotEmpty(message = "products required")
    @Valid
    private List<Product> products;
}
