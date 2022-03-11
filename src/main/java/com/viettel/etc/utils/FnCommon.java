package com.viettel.etc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import com.viettel.etc.dto.AdminDTO;
import com.viettel.etc.dto.ClientHsskDTO;
import com.viettel.etc.dto.MessageDTO;
import com.viettel.etc.dto.ResponseDTO;
import com.viettel.etc.repositories.tables.entities.DoctorsCalendarsEntity;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.MessEntity;
import com.viettel.etc.xlibrary.core.entities.ResponseEntity;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Phuong thuc chung cho toan bo project
 */
public class FnCommon extends FunctionCommon {
	private static final Logger LOGGER = Logger.getLogger(FnCommon.class);
	private static final ResourceBundle RESOURCE_BUNDLE = getResource();

	private static ResourceBundle getResource() {
		try {
			ResourceBundle appConfigRB = ResourceBundle.getBundle(
					Constants.FILE_MESS);
			return appConfigRB;
		} catch (Exception e) {
			LOGGER.error("Loi! getResourceBundle: " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * tra du lieu ve client
	 *
	 * @param errorApp
	 * @param itemObject
	 * @return
	 */
	public static Object responseToClient(ErrorApp errorApp, Object itemObject) {
		ResponseEntity responseEntity = new ResponseEntity();
		if (itemObject != null) {
			responseEntity.setData(itemObject);
		}
		// k trả về messageCode
//		ErrorMsgEntity itemEntity = new ErrorMsgEntity();
		MessEntity itemEntity = new MessEntity();
		itemEntity.setCode(errorApp.getCode());
		itemEntity.setDescription(errorApp.getDescription());
//		itemEntity.setMessageCode(errorApp.toString());
		responseEntity.setMess(itemEntity);
		return responseEntity;
	}

	public static Object responseToClient(MessageDTO messageDTO) {
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setData(null);
		ErrorMsgEntity itemEntity = new ErrorMsgEntity();
		if (Objects.nonNull(messageDTO)) {
			itemEntity.setCode(messageDTO.getCode());
			itemEntity.setDescription(messageDTO.getDescription());
			itemEntity.setMessageCode(messageDTO.getMessageCode());
		}
		responseEntity.setMess(itemEntity);
		return responseEntity;
	}

	public static List<String> stringToList(String str, String split) {
		String[] arr = str.split(split);
		return Arrays.stream(arr).collect(Collectors.toList());
	}

	/**
	 * 1,2,3,4
	 *
	 * @param str
	 * @return
	 */
	public static int[] stringToIntArr(String str) {
		String[] strArr = str.split(",");
		int size = strArr.length;
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
			try {
				arr[i] = Integer.parseInt(strArr[i]);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		return Arrays.stream(arr).distinct().toArray();
	}

	public static Object responseToClient(ErrorApp errorApp) {
		ResponseEntity responseEntity = new ResponseEntity();
// k tra ve messageCode
//		ErrorMsgEntity itemEntity = new ErrorMsgEntity();
		MessEntity itemEntity = new MessEntity();
		itemEntity.setCode(errorApp.getCode());
		itemEntity.setDescription(errorApp.getDescription());
//		itemEntity.setMessageCode(errorApp.toString());

		responseEntity.setMess(itemEntity);
		return responseEntity;
	}

	/**
	 * Convert class to json string
	 *
	 * @param object
	 * @return
	 */
	public static String toStringJson(Object object) throws TeleCareException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			LOGGER.info(e);
			throw new TeleCareException(ErrorApp.ERR_JSON_PARSE);
		}
	}

	/**
	 * Gui request den server keycloak
	 *
	 * @param url
	 * @param token
	 * @param requestBody
	 * @return
	 */
	public static Response doPutRequest(String url, String token, RequestBody requestBody) {
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
					.put(requestBody)
					.build();
			return client.newCall(request).execute();
		} catch (Exception e) {
			LOGGER.error("Has error", e);
		}
		return null;
	}

	public static Response doGetRequest(String url, String token) {
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
					.get()
					.build();
			return client.newCall(request).execute();
		} catch (Exception e) {
			LOGGER.error("Doing GET request got an error", e);
		}
		return null;
	}

	public static boolean validEmail(String email) {
		final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
		return EMAIL_REGEX.matcher(email).matches();
	}

	public static boolean validPhoneNumber(String phone) {
		final Pattern PHONE_REGX = Pattern.compile("(09|03|08|07|05|\\+84[9|3|8|7])+([0-9]{8})", Pattern.CASE_INSENSITIVE);
		return PHONE_REGX.matcher(phone).matches();
	}

	public static boolean validPassword(String password) {
		final Pattern PHONE_REGX = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[^A-Za-z0-9]).{8,}", Pattern.CASE_INSENSITIVE);
		return PHONE_REGX.matcher(password).matches();
	}

	/**
	 * Upload file
	 *
	 * @param containerFolder
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String uploadFile(String containerFolder, MultipartFile file) {
		try {
			if (file.isEmpty() || Objects.isNull(containerFolder)) {
				return null;
			}
			if (!Files.exists(Paths.get(containerFolder))) {
				Files.createDirectories(Paths.get(containerFolder));
			}

			byte[] bytes = file.getBytes();
			String relativePath = containerFolder + UUID.randomUUID() + "_" + file.getOriginalFilename();
			Path path = Paths.get(relativePath);
			Files.write(path, bytes);

			return ServletUriComponentsBuilder.fromPath(relativePath)
					.toUriString();
		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.error("Loi! uploadFile: " + e.getMessage());
		}

		return null;
	}

	/**
	 * Upload file
	 *
	 * @param containerFolder
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String uploadImage(String containerFolder, MultipartFile file) {
		if (!(file.getSize() / 1024 > Constants.IMAGE_SIZE)) {
			return uploadFile(containerFolder, file);
		}
		try {
			if (file.isEmpty() || Objects.isNull(containerFolder)) {
				return null;
			}
			if (!Files.exists(Paths.get(containerFolder))) {
				Files.createDirectories(Paths.get(containerFolder));
			}
			String relativePath = containerFolder + UUID.randomUUID() + "_" + file.getOriginalFilename();
			ImageResizer.resizeImage(file.getBytes(), relativePath);

			return ServletUriComponentsBuilder.fromPath(relativePath)
					.toUriString();
		} catch (Exception e) {
			LOGGER.info(e);
		}

		return null;
	}

//	/**
//	 * Upload base64 file
//	 *
//	 * @param containerFolder
//	 * @param base64
//	 * @return
//	 * @throws IOException
//	 */
//	public static String uploadFileBase64(String containerFolder, String base64) {
//		try {
//			MultipartFile file = Base64Util.base64ToMultipart(base64);
//
//			return Objects.isNull(file) ? null : FnCommon.uploadFile(containerFolder, file);
//		} catch (Exception e) {
//			LOGGER.error("Loi! uploadFileBase64: " + e.getMessage());
//		}
//
//		return null;
//	}

	public static boolean checkFileExtensionValid(String fileName, String... fileExtensions) {
		Objects.requireNonNull(fileName);
		for (String fileExtension : fileExtensions) {
			if (fileName.toLowerCase().endsWith(fileExtension.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
		if (Objects.isNull(maxFileSizeMb)) {
			maxFileSizeMb = 5;
		}
		Objects.requireNonNull(file);
		long fileSizeMb = file.length / (1024 * 1024);
		return checkFileExtensionValid(fileName, ".JPG", ".PNG", ".TIFF", ".BMP", ".PDF", ".JPEG") && fileSizeMb <= maxFileSizeMb;
	}

//	public static ByteArrayResource convertFileToByte(String filePath) {
//		Path path = Paths.get(filePath);
//		try {
//			return new ByteArrayResource(Files.readAllBytes(path));
//		} catch (IOException ex) {
//			LOGGER.error("Download file error", ex);
//		}
//
//		return null;
//	}

//	/**
//	 * Xoa file
//	 *
//	 * @param path
//	 * @throws IOException
//	 */
//	public static void deleteFile(String path) throws IOException {
//		Path fileToDeletePath = Paths.get(path);
//		if (Files.exists(fileToDeletePath)) {
//			Files.delete(fileToDeletePath);
//		}
//	}

	/**
	 * Nguyen Duy Hung => NDH
	 *
	 * @param name
	 * @return
	 */
	public static String nameToNameCode(String name) {
		if (Objects.isNull(name)) {
			return null;
		}
		name = name.replaceAll("[-+^]*", "");
		name = name.toUpperCase();
		name = name.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
		name = name.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
		name = name.replaceAll("ì|í|ị|ỉ|ĩ/g", "i");
		name = name.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
		name = name.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
		name = name.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
		name = name.replaceAll("đ", "d");
		name = name.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
		name = name.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
		name = name.replaceAll("Ì|Í|Ị|Ỉ|Ĩ/g", "I");
		name = name.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
		name = name.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
		name = name.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
		name = name.replaceAll("Đ", "D");
		name = name.replaceAll("\u0300|\u0301|\u0303|\u0309|\u0323", "");
		name = name.replaceAll("\u02C6|\u0306|\u031B", "");

		String[] ary = name.split(" ");
		StringBuilder nameCode = new StringBuilder();

		for (String s : ary) {
			nameCode.append(s.charAt(0));
		}

		return nameCode.toString();
	}

//	/**
//	 * Convert date date to string date
//	 *
//	 * @param date
//	 * @param isFullDateTime:true: full date time, false: date sort
//	 * @return
//	 */
//	public static String convertDateToString(Date date, Boolean isFullDateTime) {
//		String strDate;
//		if (date == null) {
//			return "";
//		}
//		if (isFullDateTime) {
//			strDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
//		} else {
//			strDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
//		}
//		return strDate;
//	}

//	/**
//	 * Go bo dau tieng viet
//	 *
//	 * @param s
//	 * @return
//	 */
//	public static String removeAccent(String s) {
//		if (s == null) {
//			return "";
//		}
//		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
//		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//		return pattern.matcher(temp).replaceAll("").replace("đ", "d").replace("Đ", "D");
//	}

	/**
	 * doc file properties trong cau hinh thu muc default
	 *
	 * @param key
	 * @return
	 */
	public static String getValueFileMess(String key) {
		String value = RESOURCE_BUNDLE.containsKey(key)
				? RESOURCE_BUNDLE.getString(key)
				: Constants.STR_EMTY;
		if (value.trim().length() <= 0) {
			LOGGER.error("Not value with key:" + key + ", in file properties");
		}
		return value;
	}

	/**
	 * tra ve client thong bao ma loi
	 *
	 * @param etcException
	 * @return
	 */
	public static Object responseToClient(TeleCareException etcException) {
		return responseToClient(etcException.getErrorApp());
	}

	/**
	 * Thuc hien nem loi thong bao ra ngoai
	 *
	 * @param errorApp
	 * @throws TeleCareException
	 */
	public static void throwsErrorApp(ErrorApp errorApp) throws TeleCareException {
		throw new TeleCareException(errorApp);
	}

	// Sau nay chuyen sang Redis
	public static void throwsErrorAppByLang(ErrorApp errorApp, MessageDTO messageDTO) throws TeleCareException {
		errorApp.setDescription(messageDTO.getDescription());
		errorApp.setCode(messageDTO.getCode());
		throw new TeleCareException(errorApp);
	}

	public static TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
		try {
			KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
			String userId = principal.getKeycloakSecurityContext().getToken().getSubject();

			String[] split_string = principal.getKeycloakSecurityContext().getTokenString().split("\\.");
			if (split_string.length <= 0) {
				return null;
			} else {
				String base64EncodedBody = split_string[1];
				org.apache.commons.codec.binary.Base64 base64Url = new Base64(true);
				String body = new String(base64Url.decode(base64EncodedBody));
				TelecareUserEntity authen = (TelecareUserEntity) FunctionCommon.convertJsonToObject(body, TelecareUserEntity.class);
				authen.setStrJwtToken(principal.getKeycloakSecurityContext().getTokenString());
				authen.setKeycloakUserId(userId);

				if (authen != null) {
					authen.setIsVerifyToken(true);
				}

				return authen;
			}
		} catch (Exception e) {
			LOGGER.error("Loi! getUserInfo: " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * lay thong tin nguoi dung
	 *
	 * @param authentication
	 * @return
	 */
	public static Optional<String> getUserLogin(Authentication authentication) {
		String userId = null;
		try {
			KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
			userId = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
		} catch (Exception e) {
			LOGGER.error("Loi! getUserLogin: " + e.getMessage(), e);
		}
		return Optional.ofNullable(userId);
	}

	/**
	 * lay chuoi string token
	 *
	 * @param authentication
	 * @return
	 */
	public static String getStringToken(Authentication authentication) {
		try {
			KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
			String strToken = principal.getKeycloakSecurityContext().getTokenString();
			return strToken;
		} catch (Exception e) {
			LOGGER.error("Loi! getUserLogin: " + e.getMessage(), e);
			return null;
		}
	}

//	/**
//	 * loai bo gio trong date
//	 *
//	 * @param dateCv
//	 * @return
//	 */
//	public static Date dateSort(Date dateCv) {
//		Date date = null;
//		String strDate = dateShow(dateCv, false);
//		try {
//
//			date = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
//		} catch (ParseException ex) {
//			LOGGER.error("==========datnv5: dateSort null", ex);
//		}
//		return date;
//	}

//	/**
//	 * Lấy thông tin attribute của user trong token
//	 *
//	 * @param authentication
//	 * @return
//	 */
//	public static Map<String, Object> getAttribute(Authentication authentication) {
//		try {
//			KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
//			AccessToken token = principal.getKeycloakSecurityContext().getToken();
//			Map<String, Object> map = token.getOtherClaims();
//			return map;
//		} catch (Exception e) {
//			LOGGER.error("Loi! getAttribute: ", e);
//			return null;
//		}
//	}

	public static String generationPasswordApp() {
		return String.valueOf(100000 + new Random().nextInt(900000));
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}

		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public static void copyProperties(Object src, Object target) {
		BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	/**
	 * Lay token admin
	 *
	 * @return
	 */
	public static String getAdminToken() {
		long timeOut = Long.parseLong(getPropertiesValue("ws.timeout"));
		OkHttpClient client = new OkHttpClient();
		AdminDTO adminAccount = new AdminDTO();
		RequestBody body = RequestBody.create(Constants.JSON_TOKEN, adminAccount.toString());
		try {
			client.setConnectTimeout(timeOut, TimeUnit.SECONDS);
			client.setReadTimeout(30, TimeUnit.SECONDS);
			client.setWriteTimeout(30, TimeUnit.SECONDS);
			HttpUrl.Builder httpBuilder = HttpUrl.parse(System.getenv("Keycloak") + FnCommon.getPropertiesValue("ws.keycloak.login")).newBuilder().username(adminAccount.getUsername()).password(adminAccount.getPassword());
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


	/**
	 * lay id thong tin nguoi dung
	 *
	 * @param authentication
	 * @return
	 */
	public static String getUserIdLogin(Authentication authentication) {
		try {
			KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
			String userId = principal.getKeycloakSecurityContext().getToken().getSubject();
			return userId;
		} catch (Exception e) {
			LOGGER.error("Loi! getUserLogin: " + e.getMessage(), e);
			return null;
		}
	}

//	public static String formatTime(String registerTime) {
//		try {
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
//			return LocalTime.parse(registerTime).format(dtf);
//		} catch (Exception e) {
//			LOGGER.error("Loi! formatTime: " + e.getMessage());
//			return null;
//		}
//	}

//	/**
//	 * convert local date to java.util.Date
//	 *
//	 * @param dateToConvert local date convert
//	 * @return java.util.Date convert
//	 */
//	public static Date convertToDate(LocalDate dateToConvert) {
//		return java.util.Date.from(dateToConvert.atStartOfDay()
//				.atZone(ZoneId.systemDefault())
//				.toInstant());
//	}

	/**
	 * convert Timestamp in milliseconds to local date
	 *
	 * @param dateToConvert Timestamp in milliseconds
	 * @return local with Timestamp
	 */
	public static LocalDate convertToLocalDate(Long dateToConvert) {
		return Instant.ofEpochMilli(dateToConvert).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * sanity check a date
	 *
	 * @param time Timestamp in milliseconds
	 * @return true is date, false not date
	 */
	public static boolean isDate(Long time) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTimeInMillis(time);
		try {
			cal.getTime();
			LocalDate localDate = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate();
			return localDate.compareTo(LocalDate.of(1970, 1, 1)) >= 0 && localDate.compareTo(LocalDate.of(2999, 12, 31)) <= 0;
		} catch (Exception e) {
			LOGGER.error("err date:" + e.getMessage(), e);
		}
		return false;
	}

	/**
	 * get ResultSelectEntity with page
	 *
	 * @param startRecord startRecord
	 * @param pageSize    pageSize
	 * @param dataList    dataList
	 * @return ResultSelectEntity with list data and count
	 */
	public static ResultSelectEntity getResultSelectEntity(Integer startRecord, Integer pageSize, List<? extends Object> dataList) {
		ResultSelectEntity resultSelectEntity = new ResultSelectEntity();

		startRecord = Objects.nonNull(startRecord) ? startRecord : Constants.START_RECORD_DEFAULT;
		pageSize = Objects.nonNull(pageSize) ? pageSize : Constants.PAGE_SIZE_DEFAULT;

		int fromIndex = startRecord;
		int toIndex = fromIndex + pageSize;

		resultSelectEntity.setListData(safeSubList(dataList, fromIndex, toIndex));
		resultSelectEntity.setCount(dataList.size());

		return resultSelectEntity;
	}

	/**
	 * get sub list
	 *
	 * @param list      data
	 * @param fromIndex from
	 * @param toIndex   end
	 * @param <T>       list data type
	 * @return sub list from fromIndex to toIndex (exclusive) of the subList
	 */
	public static <T> List<T> safeSubList(List<T> list, int fromIndex, int toIndex) {
		int size = list.size();
		if (fromIndex >= size || toIndex <= 0 || fromIndex >= toIndex) {
			return Collections.emptyList();
		}

		fromIndex = Math.max(0, fromIndex);
		toIndex = Math.min(size, toIndex);

		return list.subList(fromIndex, toIndex);
	}

	/**
	 * format time slot HH:mm-HH:mm
	 *
	 * @param hoursStart  hours start
	 * @param minuteStart minute start
	 * @param hoursEnd    hours end
	 * @param minuteEnd   minute end
	 * @return time slot with format HH:mm-HH:mm
	 */
	public static String formatTimeslot(Integer hoursStart, Integer minuteStart, Integer hoursEnd, Integer minuteEnd) {
		if (Arrays.asList(hoursStart, minuteStart, hoursEnd, minuteEnd).stream().anyMatch(Objects::isNull)) {
			return null;
		}
		LocalTime startBookingTime = LocalTime.of(hoursStart, minuteStart);
		LocalTime endBookingTime = LocalTime.of(hoursEnd, minuteEnd);
		String timeslotFormat = String.join("-", startBookingTime.format(DateTimeFormatter.ofPattern("HH:mm")), endBookingTime.format(DateTimeFormatter.ofPattern("HH:mm")));

		return timeslotFormat;
	}

	/**
	 * format time slot HH:mm
	 *
	 * @param hours  hours
	 * @param minute minute
	 * @return time slot with format HH:mm
	 */
	public static String formatTimeslot(Integer hours, Integer minute) {
		if (Arrays.asList(hours, minute).stream().anyMatch(Objects::isNull)) {
			return null;
		}
		LocalTime bookingTime = LocalTime.of(hours, minute);
		String bookingTimeStr = bookingTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		return bookingTimeStr;
	}

	/**
	 * Kiem tra file da ton tai chua
	 *
	 * @param containerFolder
	 * @return
	 * @throws IOException
	 */
	public static boolean createFolder(String containerFolder) {
		if (!Files.exists(Paths.get(containerFolder))) {
			try {
				Files.createDirectories(Paths.get(containerFolder));
				return true;
			} catch (IOException e) {
				LOGGER.error("Loi! uploadFile: " + e.getMessage(), e);
			}
		}
		return false;
	}

	public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String formattedDateTime = localDateTime.format(formatter);
		return formattedDateTime;
	}

	public static String formatLocalDate(LocalDate localDate, String pattern) {
		return localDate.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static boolean isCurrentDay(LocalDate dateInput) {
		String currentDate = LocalDate.now().toString();
		if (currentDate.equals(dateInput.toString())) {
			return true;
		}
		return false;
	}

	public static LocalTime formatLocalTime(LocalTime time) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(dtf.format(time));
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public static Integer convertDateToDayCode(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static boolean validTemplateFile(MultipartFile file) throws IOException {
		String ext = com.google.common.io.Files.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
		if (!Constants.EXTENSION_FILE.contains(ext)){
			return false;
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		Row header = sheet.getRow(0);
		StringBuilder headerContent = new StringBuilder();
		try {
			for (int i = 0; i < 24; i++){ // Template co 24 cot
				String content = header.getCell(i).getStringCellValue();
				headerContent.append(content.trim().replaceAll(" ", "").toUpperCase());
			}
			String template = Constants.TEMPLATE_FILE_IMPORT_DOCTOR;
			if (!headerContent.toString().equals(template.trim().replaceAll(" ", "").replaceAll(",", "").toUpperCase())){
				return false;
			}
		} catch (Exception e){
			e.printStackTrace();
			LOGGER.info(e);
			return false;
		}
		return true;
	}

	public static boolean areAllNull(Object... objects) {
		return Stream.of(objects).allMatch(Objects::isNull);
	}

	public static boolean areAllNotNull(Object... objects) {
		return Stream.of(objects).allMatch(Objects::nonNull);
	}

	public static String getTokenHSSK() throws TeleCareException {
		long timeOut = Long.parseLong(FunctionCommon.getPropertiesValue("ws.timeout"));
		OkHttpClient client = new OkHttpClient();
		ClientHsskDTO hsskAccount = new ClientHsskDTO();
		RequestBody body = RequestBody.create(Constants.JSON, FnCommon.toStringJson(hsskAccount));
		try{
			client.setConnectTimeout(timeOut, TimeUnit.SECONDS);
			client.setReadTimeout(30, TimeUnit.SECONDS);
			client.setWriteTimeout(30, TimeUnit.SECONDS);
			HttpUrl.Builder httpBuilder = HttpUrl.parse(System.getenv("HSSK_VACCINES_URL") + FnCommon.getPropertiesValue("ws.hssk.login")).newBuilder();
//					FnCommon.getPropertiesValue("ws.hssk.login")).newBuilder().username(hsskAccount.getUsername()).password(hsskAccount.getPassword());
			Request request = new Request.Builder()
					.header("Content-Type", "application/json")
					.url(httpBuilder.build())
					.post(body)
					.build();
			Response response = client.newCall(request).execute();
			if (response != null) {
				ResponseDTO responseDTO = new Gson().fromJson(response.body().string(), ResponseDTO.class);
				if (responseDTO == null || responseDTO.getData() == null || Strings.isNullOrEmpty(responseDTO.getData().getAccess_token())){
					throw new TeleCareException(ErrorApp.REQUEST_FAIL);
				}
				response.body().close();
				return responseDTO.getData().getAccess_token();
			}
		} catch (IOException e) {
			LOGGER.error("Get hssk token has error", e);
			throw new TeleCareException(ErrorApp.REQUEST_FAIL);
		}
		return null;
	}

	// convert object to query string
	public static String toQueryString(Object object){
		// Object --> map
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.convertValue(object, Map.class);
		StringBuilder qs = new StringBuilder();
		qs.append("?");
		for (String key : map.keySet()){
			if (map.get(key) == null || "".equals(map.get(key))){
				continue;
			}
			// key=value&
			qs.append(key);
			qs.append("=");
			qs.append(map.get(key));
			qs.append("&");
		}

		// delete last '&'
		if (qs.length() != 0) {
			qs.deleteCharAt(qs.length() - 1);
		}
		return qs.toString();
	}

	public static Response doPostRequest(String url, String token, RequestBody requestBody) throws TeleCareException {
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
					.post(requestBody)
					.build();
			return client.newCall(request).execute();
		} catch (Exception e) {
			LOGGER.error("Has error", e);
			e.printStackTrace();
			throw new TeleCareException(ErrorApp.REQUEST_FAIL);
		}
	}
}
