package com.viettel.etc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;

@Service
public class VideoCallService {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	private static final Logger LOGGER = Logger.getLogger(VideoCallService.class);

	@Value("${videocall.remote_url}")
	private String remoteUrl;
	@Value("${videocall.hashkey}")
	private String hashkey;
	@Value("${videocall.account_id}")
	private String accountId;

	public String getHashing(String data) throws SignatureException {
		String result;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(hashkey.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			result = new String(Base64.getEncoder().encode(rawHmac));
		} catch (Exception e) {
			LOGGER.info(e);
			throw new SignatureException(e);
		}
		return result.trim();
	}

	public void registerDevices(String phone) throws TeleCareException {
		try {
			if (phone == null || deviceExits(phone)) {
				return;
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode sip = objectMapper.createObjectNode();
			sip.put("password", Constants.DEFAULT_PW);

			ObjectNode data = objectMapper.createObjectNode();
			data.put("name", phone);
			data.put("sip", sip);

			ObjectNode body = objectMapper.createObjectNode();
			body.put("data", data);
			body.put("hashing", getHashing(phone));

			ResponseEntity<ObjectNode> exchange;

			RestTemplate restTemplate = restTemplate();
			exchange = restTemplate.exchange(remoteUrl + "v2/accounts/" + accountId + "/createDeviceAndCallflow",
					HttpMethod.POST, new HttpEntity<>(body, headers),
					ObjectNode.class);

			if (!((exchange.getStatusCode() == HttpStatus.OK || exchange.getStatusCode() == HttpStatus.CREATED)
					&& exchange != null && !exchange.getBody().isNull() && !exchange.getBody().get("id").isNull())) {
				FnCommon.throwsErrorApp(ErrorApp.REGISTER_VIDEO_CALL_DEVICES_FALSE);
			}
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			FnCommon.throwsErrorApp(ErrorApp.REGISTER_VIDEO_CALL_DEVICES_FALSE);
		}
	}

	private RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();

		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
				socketFactoryRegistry);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
				.setConnectionManager(connectionManager).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		RestTemplate restTemplate = new RestTemplate(requestFactory);

		return restTemplate;
	}

	private boolean deviceExits(String phone) throws TeleCareException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode sip = objectMapper.createObjectNode();

			ObjectNode data = objectMapper.createObjectNode();
			data.put("name", phone);
			data.put("sip", sip);

			ObjectNode body = objectMapper.createObjectNode();
			body.put("data", data);
			body.put("hashing", getHashing(phone));

			RestTemplate restTemplate = restTemplate();
			ResponseEntity<ObjectNode> exchange = restTemplate.exchange(remoteUrl + "v2/accounts/" + accountId + "/devices",
					HttpMethod.POST, new HttpEntity<>(body, headers),
					ObjectNode.class);

			if ((exchange.getStatusCode() == HttpStatus.OK || exchange.getStatusCode() == HttpStatus.CREATED) &&
					exchange != null && !exchange.getBody().isNull() && !exchange.getBody().get("id").isNull()) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			FnCommon.throwsErrorApp(ErrorApp.REGISTER_VIDEO_CALL_DEVICES_FALSE);
		}
		return false;
	}
}
