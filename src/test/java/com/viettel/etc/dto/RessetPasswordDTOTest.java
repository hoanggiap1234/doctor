package com.viettel.etc.dto;import lombok.Data;import org.hibernate.validator.constraints.Length;import javax.validation.constraints.NotNull;import com.viettel.etc.dto.RessetPasswordDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class RessetPasswordDTOTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void RessetPasswordDTO (){
		assertThat(RessetPasswordDTO.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}