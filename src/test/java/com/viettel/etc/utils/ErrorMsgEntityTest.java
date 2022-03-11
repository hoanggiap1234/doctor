package com.viettel.etc.utils;import com.viettel.etc.xlibrary.core.entities.MessEntity;import lombok.Data;import com.viettel.etc.utils.ErrorMsgEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ErrorMsgEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ErrorMsgEntity (){
		assertThat(ErrorMsgEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}