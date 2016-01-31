package ph.txtdis.service;

import static java.security.KeyStore.getDefaultType;
import static org.apache.http.impl.client.HttpClients.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ph.txtdis.util.Server;

@Service
public class RestService extends RestTemplate {

	@Autowired
	private Server server;

	private boolean init;

	public RestService init() {
		if (!init) {
			setRequestFactory(requestFactory());
			init = true;
		}
		return this;
	}

	private HttpClient httpClient() {
		return custom().setSSLSocketFactory(socketFactory()).build();
	}

	private KeyStore keyStore() {
		try {
			KeyStore k = KeyStore.getInstance(getDefaultType());
			k.load(keyStoreStream(), txtDIS());
			return k;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private FileInputStream keyStoreStream() throws FileNotFoundException {
		File file = new File(server.keystore());
		return new FileInputStream(file);
	}

	private ClientHttpRequestFactory requestFactory() {
		return new HttpComponentsClientHttpRequestFactory(httpClient());
	}

	private SSLConnectionSocketFactory socketFactory() {
		try {
			return new SSLConnectionSocketFactory(
					new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
							.loadKeyMaterial(keyStore(), txtDIS()).build());
		} catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
			return null;
		}
	}

	private char[] txtDIS() {
		return "txtDIS".toCharArray();
	}
}
