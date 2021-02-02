package rest.models.dto;

import java.util.List;

public class DealResponse {
    private final String status;
    private final List<String> errors;

    public DealResponse(String status, List<String> errors) {
        this.status = status;
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getErrors() {
        return errors;
    }
}
