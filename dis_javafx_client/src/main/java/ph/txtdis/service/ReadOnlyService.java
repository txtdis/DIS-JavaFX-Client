package ph.txtdis.service;

import java.util.List;

import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.util.HttpHeader;
import ph.txtdis.util.Server;
import ph.txtdis.util.TypeMap;

@Service
public class ReadOnlyService<T> {

	@Autowired
	private HttpHeader http;

	@Autowired
	private RestService restService;

	@Autowired
	private Server server;

	@Autowired
	private TypeMap response;

	private String module;

	public List<T> getList() throws Exception {
		return getList("");
	}

	private String plural() {
		return English.plural(single());
	}

	private String single() {
		return module;
	}

	private String url() {
		return "https://" + server.address() + ":" + server.getPort() + "/" + plural();
	}

	@SuppressWarnings("unchecked")
	protected List<T> getList(String endpoint) throws Exception {
		return (List<T>) responseEntity(endpoint, plural()).getBody();
	}

	@SuppressWarnings("unchecked")
	protected T getOne(String endpoint) throws Exception {
		return (T) responseEntity(endpoint, single()).getBody();
	}

	protected HttpEntity<T> httpEntity(T entity) {
		return new HttpEntity<T>(entity, http.headers());
	}

	protected ReadOnlyService<T> module(String module) {
		this.module = module;
		return this;
	}

	protected ResponseEntity<?> responseEntity(String endpoint, String path) throws Exception {
		try {
			return restService.exchange(url() + endpoint, HttpMethod.GET, httpEntity(null), response.type(path));
		} catch (ResourceAccessException e) {
			throw new NoServerConnectionException(server.location());
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				if (e.getResponseBodyAsString().contains("This connection has been closed"))
					throw new StoppedServerException();
				else
					throw new FailedAuthenticationException();
			}
			throw new InvalidException(e.getMessage());
		}
	}
}
