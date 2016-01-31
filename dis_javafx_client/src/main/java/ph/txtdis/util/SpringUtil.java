package ph.txtdis.util;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ph.txtdis.dto.Authority;
import ph.txtdis.dto.User;
import ph.txtdis.type.UserType;

public class SpringUtil {

	public static UsernamePasswordAuthenticationToken authorize(User user, Authentication authenticate,
			List<GrantedAuthority> roles) {
		return new UsernamePasswordAuthenticationToken(user, authenticate.getCredentials(), roles);
	}

	public static String encode(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	@SuppressWarnings("unchecked")
	public static List<GrantedAuthority> getRoles() {
		return (List<GrantedAuthority>) authentication().getAuthorities();
	}

	public static boolean isAuthenticated() {
		return authentication() == null ? false : authentication().isAuthenticated();
	}

	public static boolean isUser(UserType user) {
		return getRoles().contains(new SimpleGrantedAuthority(user.toString()));
	}

	public static boolean matchPasswords(Authentication authenticate, User user) {
		return new BCryptPasswordEncoder().matches(authenticate.getCredentials().toString(), user.getPassword());
	}

	public static String password() {
		return authentication().getCredentials().toString();
	}

	public static void setAuthentication(User user, String password, List<GrantedAuthority> roles) {
		setAuthentication(authenticate(user, password, roles));
	}

	public static void setCredentialsForValidation(String username, String password) {
		setAuthentication(authenticate(username, password));
	}

	public static void setPassword(String password) {
		setAuthentication(authenticate(password));
	}

	public static List<GrantedAuthority> toGranted(List<Authority> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getAuthority().toString())));
		return authorities;
	}

	public static void updateUser(User user) {
		setAuthentication(authenticate(user, password(), getRoles()));
	}

	public static User user() {
		return (User) authentication().getPrincipal();
	}

	public static String username() {
		return authentication().getName();
	}

	private static UsernamePasswordAuthenticationToken authenticate(String password) {
		return new UsernamePasswordAuthenticationToken(user(), password, getRoles());
	}

	private static UsernamePasswordAuthenticationToken authenticate(String username, String password) {
		return new UsernamePasswordAuthenticationToken(username, password);
	}

	private static UsernamePasswordAuthenticationToken authenticate(User user, String password,
			List<GrantedAuthority> roles) {
		return new UsernamePasswordAuthenticationToken(user, password, roles);
	}

	private static Authentication authentication() {
		return security().getAuthentication();
	}

	private static SecurityContext security() {
		return getContext();
	}

	private static void setAuthentication(UsernamePasswordAuthenticationToken token) {
		security().setAuthentication(token);
	}
}
