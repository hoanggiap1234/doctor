package com.viettel.etc.utils;

import com.viettel.etc.xlibrary.core.entities.ResponseEntity;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class FnCommonTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void removeAccent() {
		String s = "tét â á ă ";
		MatcherAssert.assertThat(FnCommon.removeAccent(s),
				Matchers.equalTo("tet a a a "));
	}

	@Test
	void responseToClient() {
		String itemObject = "test";
		MatcherAssert.assertThat(FnCommon.responseToClient(ErrorApp.ERROR_INPUTPARAMS, itemObject),
				Matchers.notNullValue());
		MatcherAssert.assertThat(((ResponseEntity) FnCommon.responseToClient(ErrorApp.ERROR_INPUTPARAMS, itemObject)).getMess().getDescription(),
				Matchers.equalTo(ErrorApp.ERROR_INPUTPARAMS.getDescription()));
		MatcherAssert.assertThat(((ResponseEntity) FnCommon.responseToClient(ErrorApp.ERROR_INPUTPARAMS, itemObject)).getData(),
				Matchers.equalTo(itemObject));
	}

	@Test
	void formatLocalDateTime() {
		// 20140101_1010
		LocalDateTime specificDate = LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30);
		MatcherAssert.assertThat(FnCommon.formatLocalDateTime(specificDate, "yyyyMMdd_HHmm"), Matchers.equalTo("20140101_1010"));
	}

	@Test
	void isDate() {
		MatcherAssert.assertThat(FnCommon.isDate((long) 1),
				Matchers.equalTo(true));
	}

	@Test
	void convertToLocalDate() {
	}
}