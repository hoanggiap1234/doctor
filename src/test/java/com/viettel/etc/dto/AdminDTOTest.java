package com.viettel.etc.dto;import com.viettel.etc.utils.FnCommon;import lombok.Data;import com.viettel.etc.dto.AdminDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class AdminDTOTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void AdminDTO (){
		assertThat(AdminDTO.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}