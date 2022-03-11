package com.viettel.etc.utils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Comment
 **/
class SendSMSTest {

	@Test
	void isValidPhoneNumber() {
		MatcherAssert.assertThat(SendSMS.isValidPhoneNumber("0976521452"), Matchers.equalTo(true));
	}

	@Test
	void isValidPhoneNumberFalse() {
		MatcherAssert.assertThat(SendSMS.isValidPhoneNumber("1976521452"), Matchers.equalTo(false));
	}

	@Test
	void sendAsyncDev() {
	}
}