package ph.txtdis.util;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class HttpHeader {

    private MultiValueMap<String, String> headers;

    public MultiValueMap<String, String> headers() {
        if (Spring.isAuthenticated())
            return headers;
        return headers = createHeaders();
    }

    private MultiValueMap<String, String> createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader());
        return headers;
    }

    private String authHeader() {
        return "Basic " + new String(encodedAuth());
    }

    private byte[] encodedAuth() {
        return Base64.getEncoder().encode(auth().getBytes());
    }

    private String auth() {
        return Spring.username() + ":" + Spring.password();
    }
}
