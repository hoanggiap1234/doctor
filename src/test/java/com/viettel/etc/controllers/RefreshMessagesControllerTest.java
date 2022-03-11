package com.viettel.etc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.etc.services.MessageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class RefreshMessagesControllerTest {

	private MockMvc mvc;
	@Mock
    MessageService messageService;
	@InjectMocks
    com.viettel.etc.controllers.RefreshMessagesController RefreshMessagesController;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(RefreshMessagesController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void refreshMessages0() throws Exception {
		Authentication authentication = null;

		//mock method
		MockHttpServletResponse responseActual = mvc.perform(
			post("/refresh-messages")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void refreshMessages0ThrowException1() throws Exception {
		Authentication authentication = null;

		//mock method
		MockHttpServletResponse responseActual = mvc.perform(
			post("/refresh-messages")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

}