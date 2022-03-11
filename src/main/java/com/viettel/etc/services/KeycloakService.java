package com.viettel.etc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import com.viettel.etc.dto.DoctorDTO;
import com.viettel.etc.dto.RequestDTO;
import com.viettel.etc.dto.ResponseDTO;
import com.viettel.etc.dto.UserLoginDTO;
import com.viettel.etc.utils.*;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class KeycloakService {
	@Autowired
	private MessageService messageService;
	private static final Logger LOGGER = Logger.getLogger(KeycloakService.class);

	private String adminToken;

	public String getAdminToken() {
		return adminToken;
	}

	public void setAdminToken(String adminToken) {
		this.adminToken = adminToken;
	}


	public String createUserInKeyCloak(DoctorDTO dto) throws TeleCareException {
		if (this.getAdminToken() == null) {
			this.setAdminToken(FnCommon.getAdminToken());
		}

		if (dto.getDoctorId() == null) {
			throw new TeleCareException("Doctor id not null");
		}
		String url = System.getenv("Keycloak") + FnCommon.getPropertiesValue("ws.keycloak.register.user");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(Objects.requireNonNull(this.getAdminToken()));

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode body = objectMapper.createObjectNode();
		body.put("email", dto.getEmail());
		body.put("username", dto.getPhoneNumber());
		body.put("firstName", dto.getFullname());
		body.put("lastName", "");
		body.put("enabled", true);

		ArrayNode credentials = objectMapper.createArrayNode();
		ObjectNode password = objectMapper.createObjectNode();
		password.put("type", "password");
		password.put("value", (dto.getPassword() != null) ? dto.getPassword() : Constants.DOCTOR_PW_DEFAULT);
		credentials.add(password);

		body.put("credentials", credentials);
		String userId = "";

		ObjectNode bodyAttribute = objectMapper.createObjectNode();
		bodyAttribute.put("telecareUserId", dto.getDoctorId());

		ObjectNode attribute = objectMapper.createObjectNode();
		attribute.put("attributes", bodyAttribute);

		HashMap<String, Object> userRole = new HashMap<>();
		// TODO : lấy role id trong db
		userRole.put("id", "170621d7-bad4-4c62-ad17-89c0d85f6d64");
		userRole.put("name", TelecareUserEntity.Role.TELECARE_DOCTOR.val());
		userRole.put("description", "Bác sỹ");
		userRole.put("composite", false);
		userRole.put("clientRole", true);
		userRole.put("containerId", FnCommon.getPropertiesValue("ws.keycloak.client.id"));
		try {
			ResponseEntity<ObjectNode> exchange = restTemplate.exchange(url,
					HttpMethod.POST, new HttpEntity<>(body, headers),
					ObjectNode.class);
			for (String location : Objects.requireNonNull(exchange.getHeaders().get("Location"))) {
				String[] split = location.split("/");
				userId = split[split.length - 1];
			}
			/*Add attribute*/
			restTemplate.exchange(url + "/" + userId,
					HttpMethod.PUT, new HttpEntity<>(attribute, headers),
					ObjectNode.class);
			/*Add role*/

			String addRoleUrl = url + "/" + userId + "/role-mappings/clients/" + FnCommon.getPropertiesValue("ws.keycloak.client.id");
			restTemplate.exchange(addRoleUrl,
					HttpMethod.POST, new HttpEntity<>(Arrays.asList(userRole), headers),
					ObjectNode.class);
		} catch (Exception e) {
			HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
			System.out.println(httpClientErrorException.getMessage());
			if (httpClientErrorException.getStatusCode().value() == 401) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_NOT_EXIST, dto.getLanguage()), ErrorApp.ERR_USER_NOT_EXIST);
			}
			throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_PATIENT_EMAIL_EXIST, dto.getLanguage()), ErrorApp.ERR_DATA_PATIENT_EMAIL_EXIST);
//            LOG.error("Has ERROR", e);
		}
		return userId;
	}

	public void resetPassword(String keyclockUserId, String newPassword, String language) throws TeleCareException {
		if (keyclockUserId == null || newPassword == null) {
			throw new TeleCareException("User id and newPassword not null");
		}

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode body = objectMapper.createObjectNode();
			body.put("temporary", false);
			body.put("type", "password");
			body.put("value", newPassword);

			String url = System.getenv("Keycloak") + FnCommon.getPropertiesValue("ws.keycloak.register.user");
			String ressetPasswordUrl = url + "/" + keyclockUserId + "/reset-password";

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(Objects.requireNonNull(FnCommon.getAdminToken()));

			restTemplate.exchange(ressetPasswordUrl,
					HttpMethod.PUT, new HttpEntity<>(body, headers),
					ObjectNode.class);
		} catch (HttpClientErrorException e) {
			LOGGER.info(e);
			String response = e.getResponseBodyAsString();
			if (response.contains("invalidPasswordHistoryMessage")) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_NEW_PSW_EQUAL_OLD_PSW, language), ErrorApp.ERR_NEW_PASS_EQUAL_OLD_PASS);
			}
			throw new TeleCareException(messageService.getMessage(Constants.CHANGE_PSW_FALSE, language));
		} catch (Exception e) {
			LOGGER.info(e);
			throw new TeleCareException(messageService.getMessage(Constants.CHANGE_PSW_FALSE, language));
		}
	}

	public String loginDoctor(String username, String password) {
		long timeOut = Long.parseLong(FunctionCommon.getPropertiesValue("ocs_timeout"));
		OkHttpClient client = new OkHttpClient();
		UserLoginDTO userAccount = new UserLoginDTO(username, password);
		RequestBody body = RequestBody.create(Constants.JSON_TOKEN, userAccount.toString());
		try {
			client.setConnectTimeout(timeOut, TimeUnit.SECONDS);
			client.setReadTimeout(30, TimeUnit.SECONDS);
			client.setWriteTimeout(30, TimeUnit.SECONDS);
			HttpUrl.Builder httpBuilder = HttpUrl.parse(System.getenv("Keycloak") + FnCommon.getPropertiesValue("ws.keycloak.login")).newBuilder().username(userAccount.getUsername())
					.password(userAccount.getPassword());
			Request request = new Request.Builder()
					.header("Accept", "*/*")
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Accept-Encoding", "gzip, deflate, br")
					.url(httpBuilder.build())
					.post(body)
					.build();
			Response response = client.newCall(request).execute();
			ResponseDTO responseDTO;
			if (response != null) {
				responseDTO = new Gson().fromJson(response.body().string(), ResponseDTO.class);
				return responseDTO.getAccess_token();
			}
		} catch (Exception e) {
			LOGGER.error("Has error", e);
		}
		return null;
	}

	public Object lockUser(String userId) throws TeleCareException, IOException {
		String lockUserUrl = System.getenv("Keycloak") + FnCommon.getPropertiesValue("ws.keycloak.lock.user");
//        String token = FnCommon.getStringToken(authentication);
		// TODO @tungvv2 bao giờ phân quyền thì tính sau
		String token = FnCommon.getAdminToken();
		// lock user => enabled = false
		RequestDTO params = new RequestDTO();
		params.setEnabled(false);
		String url = lockUserUrl.replace("{userId}", userId);
		RequestBody body = RequestBody.create(Constants.JSON, FnCommon.toStringJson(params));
		Response response = FnCommon.doPutRequest(url, token, body);
		ResponseDTO responseDTO = new Gson().fromJson(response.body().string(), ResponseDTO.class);
		if (responseDTO == null) {
			responseDTO = new ResponseDTO();
		}
		responseDTO.setCode(response.code());
		responseDTO.setMessage("Lock user thành công");
		return responseDTO;
	}

	public void deleteUserKeycloak(String userId){
		String lockUserUrl = System.getenv("Keycloak") + FnCommon.getPropertiesValue("ws.keycloak.lock.user");

		String token = FnCommon.getAdminToken();
		String url = lockUserUrl.replace("{userId}", userId);

		long timeOut = Long.parseLong(FunctionCommon.getPropertiesValue("ocs_timeout"));
		OkHttpClient client = new OkHttpClient();
		try {
			client.setConnectTimeout(timeOut, TimeUnit.SECONDS);
			client.setReadTimeout(30, TimeUnit.SECONDS);
			client.setWriteTimeout(30, TimeUnit.SECONDS);
			HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
			Request request = new Request.Builder()
					.header("Accept", "application/json")
					.header("Authorization", "Bearer " + token)
					.url(httpBuilder.build())
					.delete()
					.build();
			client.newCall(request).execute();
		} catch (Exception e) {
			LOGGER.error("Has error", e);
		}
	}
}
