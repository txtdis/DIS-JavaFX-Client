package ph.txtdis.util;

import static java.util.Base64.getEncoder;
import static ph.txtdis.util.SpringUtil.isAuthenticated;
import static ph.txtdis.util.SpringUtil.password;
import static ph.txtdis.util.SpringUtil.username;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class HttpHeader {

	private MultiValueMap<String, String> headers;

	public MultiValueMap<String, String> headers() {
		if (!isAuthenticated())
			headers = createHeaders();
		return headers;
	}

	private String auth() {
		return username() + ":" + password();
	}

	private String authHeader() {
		return "Basic " + new String(encodedAuth());
	}

	private MultiValueMap<String, String> createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader());
		return headers;
	}

	private byte[] encodedAuth() {
		return getEncoder().encode(auth().getBytes());
	}
}
