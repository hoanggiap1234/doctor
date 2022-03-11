package com.viettel.etc;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RestController;import com.viettel.etc.TestController;
import org.hamcrest.MatcherAssert;
import com.viettel.etc.utils.ErrorApp;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.viettel.etc.utils.TeleCareException;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
class TestControllerTest {

	private MockMvc mvc;
	@InjectMocks 	TestController TestController;	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(TestController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

}