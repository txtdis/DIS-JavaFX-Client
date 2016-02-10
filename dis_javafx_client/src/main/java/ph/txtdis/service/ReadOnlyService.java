package ph.txtdis.service;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.List;

import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.util.HttpHeader;
import ph.txtdis.util.ServerUtil;
import ph.txtdis.util.TypeMap;

@Scope("prototype")
@Service("readOnlyService")
public class ReadOnlyService<T> {

	@Autowired
	private HttpHeader http;

	@Autowired
	private RestService restService;

	@Autowired
	private ServerUtil serverUtil;

	@Autowired
	private TypeMap response;

	private String module;

	public List<T> getList() throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException,
			InvalidException, RestException {
		return getList("");
	}

	@SuppressWarnings("unchecked")
	public T getOne(String endpoint) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return (T) responseEntity(endpoint, single()).getBody();
	}

	public ReadOnlyService<T> module(String module) {
		this.module = module;
		return this;
	}

	private HttpEntity<T> httpEntity(T entity) {
		return new HttpEntity<T>(entity, http.headers());
	}

	private String plural() {
		return English.plural(single());
	}

	private ResponseEntity<?> responseEntity(String endpoint, String path) throws NoServerConnectionException,
			StoppedServerException, FailedAuthenticationException, InvalidException, RestException {
		try {
			return restService.init().exchange(url() + endpoint, GET, httpEntity(null), response.type(path));
		} catch (ResourceAccessException e) {
			throw new NoServerConnectionException(serverUtil.location());
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			if (e.getStatusCode() == UNAUTHORIZED) {
				if (e.getResponseBodyAsString().contains("This connection has been closed"))
					throw new StoppedServerException();
				else
					throw new FailedAuthenticationException();
			} else
				throw new RestException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidException(e.getMessage());
		}
	}

	private String url() {
		return "https://" + serverUtil.address() + ":" + serverUtil.getPort() + "/" + plural();
	}

	@SuppressWarnings("unchecked")
	protected List<T> getList(String endpoint) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return (List<T>) responseEntity(endpoint, plural()).getBody();
	}

	protected String single() {
		return module;
	}
}
