package ph.txtdis.dto;

import java.util.List;

import lombok.Data;

@Data
public class User implements Keyed<String> {

	private String username;

	private String password;

	private boolean enabled;

	private List<Authority> roles;

	private String email;

	private Style style;

	@Override
	public String getId() {
		return username;
	}

	public void setId(String id) {
		username = id;
	}

	@Override
	public String toString() {
		return username;
	}
}
