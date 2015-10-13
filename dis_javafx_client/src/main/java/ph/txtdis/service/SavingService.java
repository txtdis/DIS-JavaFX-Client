package ph.txtdis.service;

import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.util.HttpHeader;
import ph.txtdis.util.ServerService;

@Service("savingService")
public class SavingService<T> {

	@Autowired
	private HttpHeader http;

	@Autowired
	private RestService restService;

	@Autowired
	private ServerService server;

	private String module;

	@SuppressWarnings("unchecked")
	public T save(T entity) throws Exception {
		try {
			return entity == null ? null : (T) restService.postForObject(url(), httpEntity(entity), entity.getClass());
		} catch (ResourceAccessException e) {
			e.printStackTrace();
			throw new NoServerConnectionException(server.name());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED)
				if (e.getResponseBodyAsString().contains("This connection has been closed"))
					throw new StoppedServerException();
				else
					throw new FailedAuthenticationException();
			throw new InvalidException(e.getStatusText());
		}
	}

	private HttpEntity<T> httpEntity(T entity) {
		return new HttpEntity<T>(entity, http.headers());
	}

	private String plural() {
		return English.plural(module);
	}

	private String url() {
		return "https://" + server.address() + ":8443/" + plural();
	}

	protected SavingService<T> module(String module) {
		this.module = module;
		return this;
	}
}
