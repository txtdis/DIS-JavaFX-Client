package ph.txtdis.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.springframework.stereotype.Component;

@Component
public class ServerService {

	private static StandardPBEStringEncryptor encryptor() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("I'mAdmin4txtDIS@PostgreSQL");
		return encryptor;
	}

	private Properties props, encoded;
	private String address, name;

	private List<String> addresses;

	public ServerService() {
		props = loadProperties(new Properties());
		encoded = loadProperties(new EncryptableProperties(encryptor()));
		createAddressList();
		setDefaultServer();
	}

	public String address() {
		return address;
	}

	public String admin() {
		return encoded.getProperty("default.admin");
	}

	public String database() {
		return encoded.getProperty("default.database");
	}

	public List<String> list() {
		return addresses;
	}

	public String name() {
		return name;
	}

	public void name(String name) {
		this.name = name;
	}

	public String password() {
		return encoded.getProperty("default.password");
	}

	public String setAddress(String name) {
		return address = props.getProperty(name);
	}

	public String version() {
		return props.getProperty("default.version");
	}

	private void createAddressList() {
		// @formatter:off
		addresses = props.stringPropertyNames().stream()
			.filter(p -> !p.contains("default"))
			.sorted((e1, e2) -> e1.compareTo(e2))
			.collect(Collectors.toList());
		// @formatter:on
	}

	private Properties loadProperties(Properties props) {
		try {
			props.load(this.getClass().getResourceAsStream("/config/application.properties"));
			return props;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void setDefaultServer() {
		name = props.getProperty("default.server");
	}
}
