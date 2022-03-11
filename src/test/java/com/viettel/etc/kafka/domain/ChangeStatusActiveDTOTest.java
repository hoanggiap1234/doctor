package com.viettel.etc.kafka.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class ChangeStatusActiveDTOTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ChangeStatusActiveDTO (){
		assertThat(ChangeStatusActiveDTO.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}