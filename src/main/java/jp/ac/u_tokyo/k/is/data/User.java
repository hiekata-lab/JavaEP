package jp.ac.u_tokyo.k.is.data;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.util.WebUtils;

public class User {
	private String username;
	private String password;
	private String passwordSHA256;
	private int score;
	private int examScore;
	private boolean enabled;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordSHA256() {
		return passwordSHA256;
	}
	public void setPasswordSHA256(String passwordSHA256) {
		this.passwordSHA256 = passwordSHA256;
	}

	public int getScore() {
		return examScore;
	}
	public void setScore(int examScore) {
		this.examScore = examScore;
	}
	
	public int getExamScore() {
		return score;
	}
	public void setExamScore(int score) {
		this.score = score;
	}

	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static String getUsername(HttpServletRequest request) {
		SecurityContext context = (SecurityContext)WebUtils.getSessionAttribute(request, "SPRING_SECURITY_CONTEXT");
		String username = context.getAuthentication().getName();
		return username;
	}
	
	public static boolean isAdmin(HttpServletRequest request) {
		SecurityContext context = (SecurityContext)WebUtils.getSessionAttribute(request, "SPRING_SECURITY_CONTEXT");
		Collection<GrantedAuthority> authorities = context.getAuthentication().getAuthorities();
		if (authorities.contains(new GrantedAuthorityImpl("ROLE_ADMIN"))) {
			return true;
		} else {
			return false;
		}
	}
}
