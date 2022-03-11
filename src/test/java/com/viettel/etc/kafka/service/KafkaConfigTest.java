package com.viettel.etc.kafka.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class KafkaConfigTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void KafkaConfig (){
		assertThat(KafkaConfig.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
		assertThat(KafkaConfig.ToppicName.values().length, Matchers.greaterThanOrEqualTo(1));
	}

}