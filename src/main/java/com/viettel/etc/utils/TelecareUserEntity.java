package com.viettel.etc.utils;

import com.viettel.etc.xlibrary.jwt.JwtRoleEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comment
 *
 * @author nguyenhungbk96@gmail.com
 */
@Data
@NoArgsConstructor
public class TelecareUserEntity {
	Integer telecareUserId;
	String keycloakUserId;
	String username;
	String password;
	String name;
	String role;
	String baseRole;
	String strJwtToken;
	String orgName;
	Long exp;
	Long iat;
	Long auth_time;
	JwtRoleEntity realm_access;
	JwtAccountEntity resource_access;
	String preferred_username;
	Boolean isVerifyToken;

	private void hasPermisison() throws TeleCareException {
		if (resource_access.getTelecare() == null || !(resource_access.getTelecare() != null || resource_access.getTelecare().getRoles().size() > 0)) {
			throw new TeleCareException(ErrorApp.ERR_USER_PERMISSION);
		}
	}

	public boolean isPatient() throws TeleCareException {
		hasPermisison();
		return resource_access.getTelecare().getRoles().contains("Telecare_Citizen");
	}

	public boolean isDoctor() throws TeleCareException {
		hasPermisison();
		return resource_access.getTelecare().getRoles().contains("Telecare_Doctor");
	}

	public boolean isNursing() throws TeleCareException {
		hasPermisison();
		return resource_access.getTelecare().getRoles().contains("Telecare_Nursing");
	}

	public boolean isClinic() throws TeleCareException {
		hasPermisison();
		return resource_access.getTelecare().getRoles().contains("Telecare_Clinic");
	}

	public boolean isAdmin() throws TeleCareException {
		hasPermisison();
		return resource_access.getTelecare().getRoles().contains("Telecare_Admin");
	}

	public enum Role {
		TELECARE_DOCTOR("Telecare_Doctor"),
		TELECARE_NURSING("Telecare_Nursing"),
		TELECARE_CLINIC("Telecare_Clinic"),
		TELECARE_CITIZEN("Telecare_Citizen");

		private String role;

		private Role(String role) {
			this.role = role;
		}

		public String val() {
			return this.role;
		}
	}

	@Data
	public static class JwtAccountEntity {
		JwtRoleEntity account;
		JwtRoleEntity telecare;
	}
}
