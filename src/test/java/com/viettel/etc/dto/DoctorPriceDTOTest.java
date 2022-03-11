package com.viettel.etc.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.viettel.etc.utils.Base64Util;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.viettel.etc.dto.DoctorPriceDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class DoctorPriceDTOTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void DoctorPriceDTO (){
		assertThat(DoctorPriceDTO.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSettersExcluding("avatar")));
	}

}