package ph.txtdis.service;

import java.io.File;
import java.io.FileInputStream;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService extends RestTemplate {

    public RestService() {
        super();
        setRequestFactory(requestFactory());
    }

    private ClientHttpRequestFactory requestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    private HttpClient httpClient() {
        return HttpClients.custom().setSSLSocketFactory(socketFactory()).build();
    }

    private SSLConnectionSocketFactory socketFactory() {
        try {
            return new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy()).loadKeyMaterial(keyStore(), "txtDIS".toCharArray()).build());
        } catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
    }

    private KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(new File("keystore.jks")), "txtDIS".toCharArray());
            return keyStore;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
