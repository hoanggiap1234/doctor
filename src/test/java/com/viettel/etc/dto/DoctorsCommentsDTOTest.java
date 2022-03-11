package com.viettel.etc.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class DoctorsCommentsDTOTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void DoctorsCommentsDTO() {
		assertThat(DoctorsCommentsDTO.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSettersExcluding("patientAvatar")));
	}

}