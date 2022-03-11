package com.viettel.etc.kafka.domain;import com.fasterxml.jackson.annotation.JsonIgnoreProperties;import lombok.Data;import lombok.NoArgsConstructor;import java.io.Serializable;import com.viettel.etc.kafka.domain.CreateCinicAccountEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class CreateCinicAccountEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void CreateCinicAccountEntity (){
		assertThat(CreateCinicAccountEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}