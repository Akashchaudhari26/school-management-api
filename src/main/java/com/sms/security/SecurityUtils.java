package com.sms.security;

import com.sms.modules.iam.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

	private SecurityUtils() {
		// prevent instantiation
	}

	public static User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		Object principal = authentication.getPrincipal();

		if (principal instanceof User user) {
			return user;
		}

		return null;
	}

	public static String getCurrentUserId() {
		User user = getCurrentUser();
		return user != null ? user.getId() : null;
	}

	public static String getCurrentRole() {
		User user = getCurrentUser();
		return user != null ? user.getRoleName() : null;
	}

	public static String getTenantId() {
		User user = getCurrentUser();
		return user != null ? user.getTenantId() : null;
	}

	public static boolean hasRole(String role) {
		User user = getCurrentUser();
		return user.getRoleName().equals(role);
	}
}
