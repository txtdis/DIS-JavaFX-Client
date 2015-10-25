package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.User;
import ph.txtdis.type.UserType;

@Service
public class UserService {

	private static final String USER = "user";

	@Autowired
	private ReadOnlyService<User> readOnlyService;

	@Autowired
	private SavingService<User> savingService;

	public User find(String username) throws Exception {
		return readOnlyService.module(USER).getOne("/" + username);
	}

	public User findByEmail(String email) throws Exception {
		return readOnlyService.module(USER).getOne("/email?address=" + email);
	}

	public List<User> list() throws Exception {
		return readOnlyService.module(USER).getList();
	}

	public List<User> listByRole(UserType type) throws Exception {
		return readOnlyService.module(USER).getList("/role?name=" + type);
	}

	public User save(User entity) throws Exception {
		return savingService.module(USER).save(entity);
	}
}
