package com.viettel.etc.utils;

import com.viettel.etc.xlibrary.jwt.JwtRoleEntity;

import java.util.Arrays;

/**
 * Comment
 */
public class TelecareUserEntityDataTest extends TelecareUserEntity {
	public TelecareUserEntityDataTest adminTest() {
		TelecareUserEntity.JwtAccountEntity jwtAccountEntity = new TelecareUserEntity.JwtAccountEntity();
		JwtRoleEntity jwtRoleEntity = new JwtRoleEntity();
		jwtRoleEntity.setRoles(Arrays.asList("Telecare_Admin"));
		jwtAccountEntity.setTelecare(jwtRoleEntity);
		setResource_access(jwtAccountEntity);
		setKeycloakUserId("aaaa");

		getResource_access().getTelecare().setRoles(Arrays.asList("Telecare_Admin"));

		return this;
	}

	public TelecareUserEntityDataTest patientTest() {
		TelecareUserEntity.JwtAccountEntity jwtAccountEntity = new TelecareUserEntity.JwtAccountEntity();
		JwtRoleEntity jwtRoleEntity = new JwtRoleEntity();
		jwtRoleEntity.setRoles(Arrays.asList("Telecare_Citizen"));
		jwtAccountEntity.setTelecare(jwtRoleEntity);
		setResource_access(jwtAccountEntity);
		setKeycloakUserId("aaa");

		getResource_access().getTelecare().setRoles(Arrays.asList("Telecare_Citizen"));

		return this;
	}
}
