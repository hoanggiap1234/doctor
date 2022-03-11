package com.viettel.etc.repositories.tables.entities;import com.viettel.etc.utils.Constants;import java.io.Serializable;import java.util.Date;import javax.persistence.Column;import javax.persistence.Entity;import javax.persistence.GeneratedValue;import javax.persistence.GenerationType;import javax.persistence.Id;import javax.persistence.Table;import lombok.Data;import lombok.NoArgsConstructor;import org.hibernate.annotations.CreationTimestamp;import org.hibernate.annotations.UpdateTimestamp;import com.viettel.etc.repositories.tables.entities.DoctorsEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class DoctorsEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void DoctorsEntity (){
		assertThat(DoctorsEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}