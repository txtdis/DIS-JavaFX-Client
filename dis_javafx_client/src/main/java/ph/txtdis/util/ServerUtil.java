package ph.txtdis.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component("server")
public class ServerUtil {

	@Value("#{'${server.address}'.split(',')}")
	private List<String> addresses;

	@Value("#{'${server.keystore}'.split(',')}")
	private List<String> keystores;

	@Value("#{'${server.location}'.split(',')}")
	private List<String> locations;

	@Value("${server.default}")
	private String server;

	@Value("${server.port}")
	private String port;

	private String location;

	public String address() {
		return addresses.get(index());
	}

	public boolean isOffSite() {
		return location().equals("this.pc");
	}

	public String keystore() {
		return keystores.get(index()) + ".p12";
	}

	public String location() {
		if (location == null)
			location = server;
		return location;
	}

	public void location(String location) {
		this.location = location;
	}

	private int index() {
		return locations.indexOf(location());
	}
}
