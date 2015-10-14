package ph.txtdis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.User;
import ph.txtdis.service.UserService;

@Component("loginService")
public class LoginService {

	@Autowired
	private UserService service;

	public void validate(String username, String password) throws Exception {
		Spring.setCredentialsForValidation(username, password);
		User user = checkVsDatabase(username);
		Spring.setAuthentication(user, password, Spring.toGranted(user.getRoles()));
	}

	private User checkVsDatabase(String username) throws Exception {
		return service.find(username);
	}
}
