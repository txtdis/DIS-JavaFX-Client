package ph.txtdis.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component("server")
public class Server {

	@Value("#{'${server.location}'.split(',')}")
	private List<String> locations;

	@Value("#{'${server.address}'.split(',')}")
	private List<String> addresses;

	@Value("${server.default}")
	private String server;

	@Value("${server.port}")
	private String port;

	private String location;

	public String address() {
		int index = locations.indexOf(location());
		return addresses.get(index);
	}

	public String location() {
		if (location == null)
			location = server;
		return location;
	}

	public void location(String location) {
		this.location = location;
	}
}
