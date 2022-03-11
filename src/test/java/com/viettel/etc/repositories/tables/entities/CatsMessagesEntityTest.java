package com.viettel.etc.repositories.tables.entities;import java.io.Serializable;import javax.persistence.*;import lombok.Data;import lombok.NoArgsConstructor;import com.viettel.etc.repositories.tables.entities.CatsMessagesEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class CatsMessagesEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void CatsMessagesEntity (){
		assertThat(CatsMessagesEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}