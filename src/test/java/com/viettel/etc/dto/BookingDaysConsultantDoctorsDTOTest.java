package com.viettel.etc.dto;import com.fasterxml.jackson.annotation.JsonInclude;import com.fasterxml.jackson.annotation.JsonInclude.Include;import lombok.Data;import lombok.NoArgsConstructor;import javax.validation.constraints.NotEmpty;import javax.validation.constraints.NotNull;import java.util.Date;import java.util.List;import com.viettel.etc.dto.BookingDaysConsultantDoctorsDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class BookingDaysConsultantDoctorsDTOTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void BookingDaysConsultantDoctorsDTO (){
		assertThat(BookingDaysConsultantDoctorsDTO.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}