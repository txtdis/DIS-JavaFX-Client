package ph.txtdis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.User;
import ph.txtdis.service.UserService;

@Component
public class LoginService {

    @Autowired
    private UserService service;

    @Autowired
    private ServerService server;

    public void validate(String servername, String username, String password)
            throws Exception {
        server.setAddress(servername);
        Spring.setCredentialsForValidation(username, password);
        User user = checkVsDatabase(username);
        Spring.setAuthentication(user, password, Spring.toGranted(user
                .getRoles()));
    }

    private User checkVsDatabase(String username) throws Exception {
        return service.find(username);
    }
}
