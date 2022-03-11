package com.viettel.etc.repositories.tables.entities;import lombok.Data;import lombok.NoArgsConstructor;import org.hibernate.annotations.CreationTimestamp;import org.hibernate.annotations.UpdateTimestamp;import javax.persistence.Column;import javax.persistence.Entity;import javax.persistence.Id;import javax.persistence.Table;import java.io.Serializable;import java.util.Date;import com.viettel.etc.repositories.tables.entities.CatsEthnicitiesEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class CatsEthnicitiesEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void CatsEthnicitiesEntity (){
		assertThat(CatsEthnicitiesEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}