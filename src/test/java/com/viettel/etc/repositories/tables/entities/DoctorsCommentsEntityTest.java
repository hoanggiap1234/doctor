package com.viettel.etc.repositories.tables.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class DoctorsCommentsEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void DoctorsCommentsEntity() {
		assertThat(DoctorsCommentsEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}