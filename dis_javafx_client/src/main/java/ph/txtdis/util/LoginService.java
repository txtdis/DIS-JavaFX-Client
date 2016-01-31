package ph.txtdis.util;

import static ph.txtdis.util.SpringUtil.setAuthentication;
import static ph.txtdis.util.SpringUtil.setCredentialsForValidation;
import static ph.txtdis.util.SpringUtil.toGranted;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.User;
import ph.txtdis.service.UserService;

@Component("loginService")
public class LoginService {

	@Autowired
	private UserService service;

	public void validate(String username, String password) throws Exception {
		setCredentialsForValidation(username, password);
		User user = checkVsDatabase(username);
		setAuthentication(user, password, toGranted(user.getRoles()));
	}

	private User checkVsDatabase(String username) throws Exception {
		return service.find(username);
	}
}
