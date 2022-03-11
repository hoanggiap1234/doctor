package com.viettel.etc.dto;import com.fasterxml.jackson.annotation.JsonInclude;import lombok.AllArgsConstructor;import lombok.Data;import lombok.NoArgsConstructor;import com.viettel.etc.dto.MessageDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class MessageDTOTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void MessageDTO (){
		assertThat(MessageDTO.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}