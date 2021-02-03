package ru.vasabijaj.rest.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vasabijaj.rest.Application;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes={Application.class})
@ActiveProfiles("test")
public class DealControllerTest {
    @Autowired
    private TestRestTemplate template;

    @Test
    public void responseOk() {

        String request = "{\"seller\":\"123534251\",\"customer\":\"648563524\",\"products\":[{\"name\":\"milk\",\"code\":\"2364758363546\"},{\"name\":\"water\",\"code\":\"3656352437590\"}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        String expectedJson = "{\"status\":\"OK\",\"products\":[\"milk:2364758363546\",\"water:3656352437590\"]}";
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    @Test
    public void responseOkNoProductName() {

        String request = "{\"seller\":\"123534251\",\"customer\":\"648563524\",\"products\":[{\"code\":\"2364758363546\"},{\"name\":\"water\",\"code\":\"3656352437590\"}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        String expectedJson = "{\"status\":\"OK\",\"products\":[\"unknown:2364758363546\",\"water:3656352437590\"]}";
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    @Test
    public void responseBadRequestSellerNull() {

        String request = "{\"customer\":\"648563524\",\"products\":[{\"name\":\"milk\",\"code\":\"2364758363546\"},{\"name\":\"water\",\"code\":\"3656352437590\"}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        String expectedJson = "{\"status\":400,\"errors\":[\"seller required\"]}";
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    @Test
    public void responseBadRequestCustomerNull() {

        String request = "{\"seller\":\"123534251\",\"products\":[{\"name\":\"milk\",\"code\":\"2364758363546\"},{\"name\":\"water\",\"code\":\"3656352437590\"}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        String expectedJson = "{\"status\":400,\"errors\":[\"customer required\"]}";
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    @Test
    public void responseBadRequestProductsNull() {

        String request = "{\"seller\":\"123534251\",\"customer\":\"648563524\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        String expectedJson = "{\"status\":400,\"errors\":[\"products required\"]}";
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    @Test
    public void responseBadRequestProductsEmpty() {

        String request = "{\"seller\":\"123534251\",\"customer\":\"648563524\",\"products\":[]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        String expectedJson = "{\"status\":400,\"errors\":[\"products required\"]}";
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    @Test
    public void responseBadRequestSellerAndCustomerNot9CharAndProductsNot13Char() {

        String request = "{\"seller\":\"123\",\"customer\":\"123\",\"products\":[{\"name\":\"milk\",\"code\":\"123\"},{\"name\":\"water\",\"code\":\"3656352437590\"}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        String expectedJson = "{\"status\":400,\"errors\":[\"customer must contain 9 characters\",\"seller must contain 9 characters\",\"product code must contain 13 characters\"]}";
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    @Test
    public void responseBadRequestMalformedBody() {

        String request = "some";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(null, response.getBody(), false);
    }

    @Test
    public void responseOkFirstSeenCodesOfAllRepeatedProducts() {

        String request = "{\"seller\":\"123534251\",\"customer\":\"648563524\",\"products\":[" +
                "{\"name\":\"milk\",\"code\":\"2364758363546\"}," +
                "{\"name\":\"water\",\"code\":\"3656352437590\"}," +
                // Коды продуктов, отличные от первоначальных
                "{\"name\":\"milk\",\"code\":\"2364758363111\"}," +
                "{\"name\":\"water\",\"code\":\"3656352437111\"}," +
                "{\"name\":\"water\",\"code\":\"3656352437222\"}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = template.postForEntity("/deal", entity, String.class);

        String expectedJson = "{\"status\":\"OK\",\"products\":[" +
                "\"milk:2364758363546\"," +
                "\"water:3656352437590\"," +
                // Коды продуктов изменены на первоначальные
                "\"milk:2364758363546\"," +
                "\"water:3656352437590\"," +
                "\"water:3656352437590\"]}";
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }
}
