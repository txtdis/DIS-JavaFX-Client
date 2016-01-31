package ph.txtdis.exception;

import org.springframework.web.client.HttpStatusCodeException;

import static org.apache.commons.lang3.StringUtils.substringBetween;

public class RestException extends Exception {

	private static final long serialVersionUID = 174954871159198865L;

	public RestException(HttpStatusCodeException e) {
		super(substringBetween(//
				e.getResponseBodyAsString(), //
				"\"message\":\"", //
				"\",\"path\""));
	}
}
