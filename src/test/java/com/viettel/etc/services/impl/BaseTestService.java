package com.viettel.etc.services.impl;

import com.viettel.etc.utils.TelecareUserEntity;
import com.viettel.etc.xlibrary.jwt.JwtRoleEntity;

import java.util.Arrays;

/**
 * Comment
 */
public class BaseTestService {
	public TelecareUserEntity createTestUser(String role) {
		TelecareUserEntity systemEntity = new TelecareUserEntity();
		TelecareUserEntity.JwtAccountEntity resource_access = new TelecareUserEntity.JwtAccountEntity();
		JwtRoleEntity roleEntity = new JwtRoleEntity();
		roleEntity.setRoles(Arrays.asList(role));
		resource_access.setTelecare(roleEntity);
		systemEntity.setResource_access(resource_access);

		return systemEntity;
	}
}
