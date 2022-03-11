package com.viettel.etc.utils;
import com.viettel.etc.xlibrary.jwt.JwtRoleEntity;
import lombok.Data;
import com.viettel.etc.utils.TelecareUserEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class TelecareUserEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void TelecareUserEntity (){
		assertThat(TelecareUserEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSettersExcluding(new String[]{"admin","clinic","doctor","patient","nursing"})));
		assertThat(TelecareUserEntity.JwtAccountEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
		assertThat(TelecareUserEntity.Role.values().length, Matchers.greaterThanOrEqualTo(1));
	}

}