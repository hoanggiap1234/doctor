package com.viettel.etc.repositories.tables.entities;import lombok.Data;import lombok.NoArgsConstructor;import org.hibernate.annotations.CreationTimestamp;import javax.persistence.Entity;import javax.persistence.Id;import javax.persistence.IdClass;import javax.persistence.Table;import java.io.Serializable;import java.util.Date;import com.viettel.etc.repositories.tables.entities.OtpEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class OtpEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void OtpEntity (){
		assertThat(OtpEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}