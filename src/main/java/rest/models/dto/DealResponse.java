package rest.models.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DealResponse {
    private final String status;
    private final List<String> errors;

    public DealResponse(String status, List<String> errors) {
        this.status = status;
        this.errors = errors;
    }
}
