package com.viettel.etc.repositories.tables.entities;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class BookingInformationsEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void BookingInformationsEntity (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalTime>() {
			public LocalTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalTime.class);
		assertThat(BookingInformationsEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}