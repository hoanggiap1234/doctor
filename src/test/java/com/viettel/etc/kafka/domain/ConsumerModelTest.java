package com.viettel.etc.kafka.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class ConsumerModelTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ConsumerModel (){
		assertThat(ConsumerModel.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}