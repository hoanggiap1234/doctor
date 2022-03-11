package com.viettel.etc.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.squareup.okhttp.Response;
import com.viettel.etc.dto.*;
import com.viettel.etc.kafka.service.KafkaService;
import com.viettel.etc.repositories.CastWardRepository;
import com.viettel.etc.repositories.DoctorRepository;
import com.viettel.etc.repositories.tables.*;
import com.viettel.etc.repositories.tables.entities.*;
import com.viettel.etc.services.*;
import com.viettel.etc.services.tables.OtpServiceJPA;
import com.viettel.etc.services.tables.SysUsersServiceJPA;
import com.viettel.etc.utils.*;
import com.viettel.etc.xlibrary.core.entities.ExcellDataEntity;
import com.viettel.etc.xlibrary.core.entities.ExcellHeaderEntity;
import com.viettel.etc.xlibrary.core.entities.ExcellSheet;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Autogen class: Lop thong tin cua bac si
 *
 * @author ToolGen
 * @date Wed Aug 19 14:21:51 ICT 2020
 */
@Service
public class DoctorServiceImpl implements DoctorService {

	private static final Logger LOGGER = Logger.getLogger(DoctorServiceImpl.class);
	@Autowired
	CatsHealthfacilitiesRepositoryJPA healthfacilitiesRepositoryJPA;
	@Autowired
	DoctorsRepositoryJPA doctorsRepositoryJPA;
	@Autowired
	CatsSpecialistsRepositoryJPA specialistsRepositoryJPA;
	@Autowired
	DoctorsSpecialistsRepositoryJPA doctorsSpecialistsRepositoryJPA;
	@Autowired
	DoctorsHealthfacilitiesRepositoryJPA doctorsHealthfacilitiesRepositoryJPA;
	@Autowired
	KeycloakService keycloakService;
	@Autowired
	VideoCallService videoCallService;
	@Autowired
	private DoctorRepository doctorRepository;
	@Autowired
	private BookingInformationService bookingInformationService;
	@Autowired
	private CastWardRepository castWardRepository;
	@Autowired
	private OtpServiceJPA otpServiceJPA;
	@Autowired
	private SysUsersServiceJPA sysUserServiceJPA;
	@Autowired
	private SysUsersRepositoryJPA sysUsersRepositoryJPA;
	@Autowired
	private KafkaService kafkaService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private DoctorsCommentsRepositoryJPA doctorsCommentsRepositoryJPA;
	@Autowired
	private ImportHistoryRepositoryJPA importHistoryRepositoryJPA;
	@Autowired
	private CatsAcademicRankRepositoryJPA catsAcademicRankRepositoryJPA;
	@Autowired
	private CatsDegreeRepositoryJPA catsDegreeRepositoryJPA;
	@Autowired
	private CatsNationsRepositoryJPA catsNationsRepositoryJPA;
	@Autowired
	private CatsEthnicitiesRepositoryJPA catsEthnicitiesRepositoryJPA;
	@Autowired
	private CatsReligionsRepositoryJPA catsReligionsRepositoryJPA;
	@Autowired
	private CatsSpecialistsRepositoryJPA catsSpecialistsRepositoryJPA;
	@Autowired
	private CatsHealthfacilitiesRepositoryJPA catsHealthfacilitiesRepositoryJPA;

	/**
	 * @param itemParamsEntity params client
	 * @return
	 */
	@Override
	public Object getDoctors(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException {
        /*
        ==========================================================
        itemParamsEntity: params nguoi dung truyen len
        ==========================================================
        */
        if(authentication==null) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
		}
		TelecareUserEntity userSystemEntity = FnCommon.getTelecareUserInfo(authentication);
		if (userSystemEntity.isPatient()) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
		}

		ResultSelectEntity dataResult = doctorRepository.getDoctors(itemParamsEntity, userSystemEntity);

		return dataResult;
	}

	/**
	 * @param itemParamsEntity params client
	 * @return
	 */
	@Override
	public Object getDoctorsPublic(DoctorDTO itemParamsEntity) throws TeleCareException {
		ResultSelectEntity dataResult = doctorRepository.getDoctors(itemParamsEntity, null);

		return dataResult;
	}

	@Override
	public Object getDoctorsExcel(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException {
		TelecareUserEntity userSystemEntity = FnCommon.getTelecareUserInfo(authentication);
		if (userSystemEntity.isPatient()) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
		}

		ResultSelectEntity dataResult = doctorRepository.getDoctorsExcel(itemParamsEntity);

		return dataResult;
	}

	@Override
	@Transactional
	public Object updateDoctor(DoctorDTO dto, Authentication authentication) throws TeleCareException, IOException {
		TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
		if (loginUser.isPatient()) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, dto.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
		} else if (loginUser.isDoctor() && !dto.getDoctorId().equals(loginUser.getTelecareUserId())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, dto.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
		} else if (loginUser.isClinic()) {
			List<String> healthfacilitiCode = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
			List<Integer> doctorIds = doctorsHealthfacilitiesRepositoryJPA.getDoctorIdsByHealthficility(healthfacilitiCode);
			if (!doctorIds.contains(dto.getDoctorId())) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, dto.getLanguage()),ErrorApp.ERR_USER_PERMISSION);
			}
		}

		DoctorsEntity doctorsEntity = doctorsRepositoryJPA.findByDoctorId(dto.getDoctorId());
		if (Objects.isNull(doctorsEntity)) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, dto.getLanguage()), ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST);
		}
		String avatar = doctorsEntity.getAvatar();
		if (dto.getAvatar() != null && !Base64Util.toBase64(doctorsEntity.getAvatar())
				.equals(dto.getAvatar())) {
			/* Valid avata file */
			MultipartFile avatarFile = Base64Util.base64ToMultipart(dto.getAvatar());
			if (!FnCommon
					.checkBriefcaseValid(avatarFile.getOriginalFilename(), avatarFile.getBytes(),
							Constants.IMAGE_UPLOAD_MAX_SIZE)) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_IMAGE_FILE_INVALID, dto.getLanguage()), ErrorApp.ERR_IMAGE_FILE_INVALID);
			}
			avatar = FnCommon.uploadImage(Constants.IMAGE_UPLOAD_PATH, avatarFile);
			if (avatar == null) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_IMAGE_FILE_INVALID, dto.getLanguage()), ErrorApp.ERR_IMAGE_FILE_INVALID);
			}
		}

		if (dto.getPositionCode() != null || dto.getDistrictCode() != null || dto.getWardCode() != null) {
			CastWardDto ward = (CastWardDto) castWardRepository
					.getAddress(
							dto.getProvinceCode() == null ? doctorsEntity.getProvinceCode() : dto.getProvinceCode(),
							dto.getDistrictCode() == null ? doctorsEntity.getDistrictCode() : dto.getDistrictCode(),
							dto.getWardCode() == null ? doctorsEntity.getWardCode() : dto.getWardCode());
			if (Objects.isNull(ward) || !ward.getDistrictCode().equals(dto.getDistrictCode()) ||
					!ward.getProvinceCode().equals(dto.getProvinceCode())) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_ADDRESS, dto.getLanguage()), ErrorApp.ERR_ADDRESS);
			}
		}

		if (dto.getPhoneNumber() != null && doctorsRepositoryJPA
				.existsByPhoneNumberAndDoctorIdNot(dto.getPhoneNumber(), dto.getDoctorId())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_PHONE_NUMBER_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST);
		}

		if (dto.getEmail() != null && doctorsRepositoryJPA.existsByEmailAndDoctorIdNot(dto.getEmail(), dto.getDoctorId())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_EMAIL_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_EMAIL_EXIST);
		}

		if (dto.getHealthacilitieCodes() != null && dto.getHealthacilitieCodes().size() > 0) {
			Integer healthfacilities = healthfacilitiesRepositoryJPA
					.countByHealthfacilitiesCodeIn(dto.getHealthacilitieCodes());
			if (dto.getHealthacilitieCodes().size() < healthfacilities) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_HEALTHFACILITIE_NOT_FOUND, dto.getLanguage()), ErrorApp.ERR_HEALTHFACILITIE_NOT_FOUND);
			}
		}

		if (dto.getSpecialistIds() != null && dto.getSpecialistIds().size() > 0) {
			Integer special = specialistsRepositoryJPA.countBySpecialistIdIn(dto.getSpecialistIds());
			if (dto.getSpecialistIds().size() < special) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_SPECIAL_NOT_FOUND, dto.getLanguage()), ErrorApp.ERR_SPECIAL_NOT_FOUND);
			}
		}

		FnCommon.copyProperties(dto, doctorsEntity);
		doctorsEntity.setAvatar(avatar);

		doctorsEntity.setDoctorCode(
				FnCommon.nameToNameCode(doctorsEntity.getFullname() + doctorsEntity.getPhoneNumber()));
		doctorsRepositoryJPA.save(doctorsEntity);

		if (dto.getDoctorId() != null && dto.getSpecialistIds() != null && dto.getSpecialistIds().size() > 0) {
			List<DoctorsSpecialistsEntity> doctorsSpecialists = doctorsSpecialistsRepositoryJPA
					.findByDoctorIdAndIsDelete(dto.getDoctorId(), Constants.IS_NOT_DELETE);
			Iterator<DoctorsSpecialistsEntity> doctorsSpecialistitr = doctorsSpecialists.iterator();
			while (doctorsSpecialistitr.hasNext()) {
				DoctorsSpecialistsEntity ds = doctorsSpecialistitr.next();
				if (dto.getSpecialistIds().contains(ds.getSpecialistId())) {
					dto.getSpecialistIds().remove(ds.getSpecialistId());
					doctorsSpecialistitr.remove();
				} else {
					ds.setIsDelete(Constants.IS_DELETE);
				}
			}
			for (Integer specialistId : dto.getSpecialistIds()) {
				DoctorsSpecialistsEntity doctorsSpecia = new DoctorsSpecialistsEntity();
				doctorsSpecia.setDoctorId(doctorsEntity.getDoctorId());
				doctorsSpecia.setSpecialistId(specialistId);
				doctorsSpecialists.add(doctorsSpecia);
			}
			if (!doctorsSpecialists.isEmpty()) {
				doctorsSpecialistsRepositoryJPA.saveAll(doctorsSpecialists);
			}
		}

		if (dto.getDoctorId() != null && dto.getHealthacilitieCodes() != null && dto.getHealthacilitieCodes().size() > 0) {
			List<DoctorsHealthfacilitiesEntity> doctorsHealthfacilities = doctorsHealthfacilitiesRepositoryJPA
					.findByDoctorIdAndIsDelete(dto.getDoctorId(), Constants.IS_NOT_DELETE);
			Iterator<DoctorsHealthfacilitiesEntity> doctorsHealthfacilitiesitr = doctorsHealthfacilities
					.iterator();
			while (doctorsHealthfacilitiesitr.hasNext()) {
				DoctorsHealthfacilitiesEntity dh = doctorsHealthfacilitiesitr.next();
				if (dto.getHealthacilitieCodes().contains(dh.getHealthfacilitiesCode())) {
					dto.getHealthacilitieCodes().remove(dh.getHealthfacilitiesCode());
					doctorsHealthfacilitiesitr.remove();
				} else {
					dh.setIsDelete(Constants.IS_DELETE);
				}
			}

			for (String healthacilitieCode : dto.getHealthacilitieCodes()) {
				DoctorsHealthfacilitiesEntity dhe = new DoctorsHealthfacilitiesEntity();
				dhe.setDoctorId(doctorsEntity.getDoctorId());
				dhe.setHealthfacilitiesCode(healthacilitieCode);
				doctorsHealthfacilities.add(dhe);
			}

			if (!doctorsHealthfacilities.isEmpty()) {
				doctorsHealthfacilitiesRepositoryJPA.saveAll(doctorsHealthfacilities);
			}
		}

		return dto;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteDoctor(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException, IOException {
		TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
		if (loginUser.isPatient()) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()),ErrorApp.ERR_USER_PERMISSION);
		} else if (loginUser.isDoctor() && !itemParamsEntity.getDoctorId().equals(loginUser.getTelecareUserId())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
		} else if (loginUser.isClinic()) {
			List<String> healthfacilitiCode = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
			List<Integer> doctorIds = doctorsHealthfacilitiesRepositoryJPA.getDoctorIdsByHealthficility(healthfacilitiCode);
			if (!doctorIds.contains(itemParamsEntity.getDoctorId())) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
			}
		}

		Integer doctorId = itemParamsEntity.getDoctorId();
		DoctorsEntity doctorsEntity = doctorsRepositoryJPA.getOne(doctorId);
		if (Objects.isNull(doctorsEntity)) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, itemParamsEntity.getLanguage()), ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST);
		}
		doctorsRepositoryJPA.deleteDoctorById(doctorId);
		doctorsSpecialistsRepositoryJPA.deleteByDoctorId(doctorId);
		doctorsHealthfacilitiesRepositoryJPA.deleteByDoctorId(doctorId);

		// send to topic
		SysUsersEntity sysUserEntity = sysUserServiceJPA.getByIdAndType(doctorId, Constants.DOCTOR_TYPE);
		sysUserEntity.setIsDelete(Constants.IS_DELETE ? 1 : 0);
		kafkaService.deleteSysUser(sysUserEntity);

		// lock user keycloak
		keycloakService.lockUser(sysUserEntity.getKeycloakUserId());
	}

	/**
	 * @param dto
	 * @throws TeleCareException
	 */
	@Override
	public void ressetPassword(RessetPasswordDTO dto) throws TeleCareException {
		if (!FnCommon.validPhoneNumber(dto.getPhoneNumber())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_PHONE_NUMBER_INVALID, dto.getLanguage()), ErrorApp.ERR_PHONE_NUMBER_INVALID);
		}
		if (!FnCommon.validPassword(dto.getNewPassword())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_PSW_INVALID, dto.getLanguage()), ErrorApp.ERR_PASSWORD_INVALID);
		}

		OtpIdentify otpId = new OtpIdentify();
		otpId.setPhone(dto.getPhoneNumber());
		otpId.setConfirmType(OtpIdentify.DOCTOR_REGISTER);
		if (!otpServiceJPA.existsById(otpId)) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_OTP_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_OTP_EXIST);
		}
		OtpEntity otpEntity = otpServiceJPA.getById(otpId);
		if (!otpEntity.getOtp().equals(dto.getOtp())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_OTP_INVALID, dto.getLanguage()), ErrorApp.ERROR_DATA_OTP_INVALID);
		}
		// kiem tra thoi han OTP
		long diff = new Date().getTime() - otpEntity.getSignDate().getTime();
		if (diff > otpEntity.getDuration() * 1000 * 60) {   // diff in minute
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_OTP_EXPIRED, dto.getLanguage()));
		}
		DoctorsEntity doctorsEntity = doctorsRepositoryJPA.findByPhoneNumber(dto.getPhoneNumber());

		if (doctorsEntity == null) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, dto.getLanguage()), ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST);
		}

		keycloakService.resetPassword(doctorsEntity.getKeycloakUserId(), dto.getNewPassword(), dto.getLanguage());
	}

	public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) throws TeleCareException {
		TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
		if (Objects.isNull(loginUser) || loginUser.isPatient() || loginUser.isDoctor()) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
		} else if (loginUser.isAdmin()) {
		} else if (loginUser.isClinic()) {
			List<String> codes = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
			if (!healthacilitieCodes.stream().allMatch(c -> codes.contains(c))) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
			}
		} else {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
		}
	}

	/**
	 * create doctor
	 *
	 * @param dto
	 * @return
	 */
	@Override
	@Transactional(rollbackOn = Exception.class)
	public Object createDoctor(DoctorDTO dto, Authentication authentication) throws TeleCareException, IOException {
		checkPermisisonUpdateDoctor(authentication, dto.getHealthacilitieCodes(), dto.getLanguage());
		if (!FnCommon.validPhoneNumber(dto.getPhoneNumber())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_PHONE_NUMBER_INVALID, dto.getLanguage()), ErrorApp.ERR_PHONE_NUMBER_INVALID);
		}
		if (Objects.nonNull(dto.getEmail()) && !FnCommon.validEmail(dto.getEmail())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_EMAIL_INVALID, dto.getLanguage()), ErrorApp.ERR_EMAIL_INVALID);
		}

		// Khong check OTP, chi quyen admin hoac CSYT moi dc tao bac si
//		OtpIdentify otpId = new OtpIdentify();
//		otpId.setPhone(dto.getPhoneNumber());
//		otpId.setConfirmType(OtpIdentify.DOCTOR_REGISTER);
//		if (!otpServiceJPA.existsById(otpId)) {
//			FnCommon.throwsErrorApp(ErrorApp.ERROR_DATA_OTP_EXIST);
//		}
//		OtpEntity otpEntity = otpServiceJPA.getById(otpId);
//		if (!otpEntity.getOtp().equals(dto.getOtp())) {
//			FnCommon.throwsErrorApp(ErrorApp.ERROR_DATA_OTP_INVALID);
//		}
//		long diff = new Date().getTime() - otpEntity.getSignDate().getTime();
//		if (diff > otpEntity.getDuration() * 1000 * 60) {   // diff in minute
//			FnCommon.throwsErrorApp(ErrorApp.ERROR_DATA_OTP_EXPIRED);
//		}
		if (dto.getAvatar() != null) {
			/* Valid avata file */
			MultipartFile avatarFile = Base64Util.base64ToMultipart(dto.getAvatar());
			if (!FnCommon
					.checkBriefcaseValid(avatarFile.getOriginalFilename(), avatarFile.getBytes(),
							Constants.IMAGE_UPLOAD_MAX_SIZE)) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_IMAGE_FILE_INVALID, dto.getLanguage()), ErrorApp.ERR_IMAGE_FILE_INVALID);
			}
			String avatar = FnCommon.uploadImage(Constants.IMAGE_UPLOAD_PATH, avatarFile);
			if (avatar == null) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_IMAGE_FILE_INVALID, dto.getLanguage()), ErrorApp.ERR_IMAGE_FILE_INVALID);
			}
			dto.setAvatar(avatar);
		}

		CastWardDto ward = (CastWardDto) castWardRepository
				.getAddress(dto.getProvinceCode(), dto.getDistrictCode(), dto.getWardCode());
		if (Objects.isNull(ward)) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_LOCATION_INVALID, dto.getLanguage()), ErrorApp.ERROR_DATA_LOCATION_INVALID);
		}

		if (doctorsRepositoryJPA.existsByPhoneNumber(dto.getPhoneNumber())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_PHONE_NUMBER_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST);
		}

		if (Objects.nonNull(dto.getEmail()) && doctorsRepositoryJPA.existsByEmail(dto.getEmail())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_EMAIL_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_EMAIL_EXIST);
		}

		Integer healthfacilities = healthfacilitiesRepositoryJPA
				.countByHealthfacilitiesCodeIn(dto.getHealthacilitieCodes());
		if (dto.getHealthacilitieCodes().size() < healthfacilities) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_HEALTHFACILITIE_NOT_FOUND, dto.getLanguage()), ErrorApp.ERR_HEALTHFACILITIE_NOT_FOUND);
		}

		//tam cho phep null de HSSK tao tai khoan doctor
		if(dto.getSpecialistIds()!=null) {
			Integer special = specialistsRepositoryJPA.countBySpecialistIdIn(dto.getSpecialistIds());
			if (dto.getSpecialistIds().size() < special) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_SPECIAL_NOT_FOUND, dto.getLanguage()), ErrorApp.ERR_SPECIAL_NOT_FOUND);
			}
		}

		Integer genderId = dto.getGenderId();
		if(genderId==null || !genderId.equals(Constants.MALE_GENDER_ID) || !genderId.equals(Constants.FEMALE_GENDER_ID)) {
			dto.setGenderId(Constants.DEFAULT_GENDER_ID);
		}

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon
				.convertObjectToObject(dto, DoctorsEntity.class);
		doctorsEntity.setDoctorCode(
				FnCommon.nameToNameCode(doctorsEntity.getFullname()) + doctorsEntity.getPhoneNumber());
		doctorsEntity.setIsDelete(Constants.IS_NOT_DELETE);

		DoctorsEntity doctorEntitySaved = doctorsRepositoryJPA.save(doctorsEntity);

		String userId = null;
		try {
			dto.setDoctorId(doctorsEntity.getDoctorId());
			userId = keycloakService.createUserInKeyCloak(dto);
		} catch (Exception e) {
			LOGGER.info(e);
			throw new TeleCareException(messageService.getMessage(Constants.GET_KEYCLOCK_FALSE, dto.getLanguage()), ErrorApp.GET_KEYCLOCK_FALSE);
		}
		doctorEntitySaved.setKeycloakUserId(userId);
		DoctorsEntity doctorsEntityUpdate = doctorsRepositoryJPA.save(doctorEntitySaved);

		// send to sys user create to account service
		SysUsersEntity sysUsersEntity = new SysUsersEntity();
		sysUsersEntity.setKeycloakUserId(doctorsEntityUpdate.getKeycloakUserId());
		sysUsersEntity.setId(doctorsEntityUpdate.getDoctorId());
		sysUsersEntity.setType(doctorsEntityUpdate.getDoctorType());
		sysUsersRepositoryJPA.save(sysUsersEntity);
		kafkaService.createSysUser(sysUsersEntity);

		List<DoctorsSpecialistsEntity> doctorsSpecialist = new ArrayList<>();
		//temp change
		if(dto.getSpecialistIds()!=null) {
			for (Integer specialistId : dto.getSpecialistIds()) {
				DoctorsSpecialistsEntity doctorsSpecia = new DoctorsSpecialistsEntity();
				doctorsSpecia.setDoctorId(doctorsEntity.getDoctorId());
				doctorsSpecia.setSpecialistId(specialistId);
				doctorsSpecialist.add(doctorsSpecia);
			}
			doctorsSpecialistsRepositoryJPA.saveAll(doctorsSpecialist);
		}


		List<DoctorsHealthfacilitiesEntity> doctorsHealthfaciliti = new ArrayList<>();
		for (String healthacilitieCode : dto.getHealthacilitieCodes()) {
			DoctorsHealthfacilitiesEntity dhe = new DoctorsHealthfacilitiesEntity();
			dhe.setDoctorId(doctorsEntity.getDoctorId());
			dhe.setHealthfacilitiesCode(healthacilitieCode);
			doctorsHealthfaciliti.add(dhe);
		}
		doctorsHealthfacilitiesRepositoryJPA.saveAll(doctorsHealthfaciliti);

		dto.setAvatar(Base64Util.toBase64(doctorsEntity.getAvatar()));

//		videoCallService.registerDevices(dto.getPhoneNumber());

		return dto;
	}

	/**
	 * @param itemParamsEntity params client
	 * @return
	 */
	@Override
	public Object getDoctorInfo(DoctorDTO itemParamsEntity, Integer doctorId)
			throws TeleCareException {
		DoctorDTO dataResult = (DoctorDTO) doctorRepository.getDoctorInfo(itemParamsEntity);
		if (dataResult == null) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, itemParamsEntity.getLanguage()), ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST );
		}
		BookingInformationDTO bookingRequestDTO = new BookingInformationDTO();
		bookingRequestDTO.setDoctorId(itemParamsEntity.getDoctorId());
		BookingInformationDTO bookingInformationDTO = (BookingInformationDTO) bookingInformationService
				.countBookingByBookingGroup(bookingRequestDTO);
		if (bookingInformationDTO != null) {
			dataResult.setBookingOrder(bookingInformationDTO.getBookingOrder());
			dataResult.setBookingAdvisory(bookingInformationDTO.getBookingAdvisory());
		}

		return dataResult;
	}

	@Override
	public Object getDoctorByKeycloakUserId(DoctorDTO itemParamsEntity,
											Authentication authentication) throws TeleCareException {
		String keycloakUserId = FnCommon.getUserIdLogin(authentication);
		if (keycloakUserId == null) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, itemParamsEntity.getLanguage()), ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST);
		}
		itemParamsEntity.setKeycloakUserId(keycloakUserId);
		DoctorDTO doctorEntity = (DoctorDTO) doctorRepository.getDoctorByKeycloakUserId(itemParamsEntity);
		if (doctorEntity == null) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, itemParamsEntity.getLanguage()), ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST);
		}
		List<String> healthFacilityCodes = new ArrayList<>(Arrays.asList(doctorEntity.getHealthfacilitiesCode().split(",")));
		List<CatsHealthfacilitiesEntity> healthfacilitiesEntities = catsHealthfacilitiesRepositoryJPA.findByHealthfacilitiesCodeInAndIsActiveAndIsDelete(healthFacilityCodes, Constants.IS_ACTIVE, Constants.IS_NOT_DELETE);
		List<HealthFacilityDTO> healthFacilityDTOs = healthfacilitiesEntities.stream().map(entity -> {
			HealthFacilityDTO healthFacilityDTO = new HealthFacilityDTO();
			healthFacilityDTO.setHealthfacilitiesCode(entity.getHealthfacilitiesCode());
			healthFacilityDTO.setHealthfacilitiesName(entity.getName());
			healthFacilityDTO.setHealthfacilitiesRoute(entity.getHealthfacilitiesRoute());
			return healthFacilityDTO;
		}).collect(Collectors.toList());
		doctorEntity.setHealthfacilities(healthFacilityDTOs);
		return doctorEntity;
	}


	/**
	 * @param itemParamsEntity params client
	 * @return
	 */
	@Override
	public Object getTopDoctors(DoctorDTO itemParamsEntity) {
		ResultSelectEntity dataResult = doctorRepository.getTopDoctors(itemParamsEntity);
		return dataResult;
	}

	@Override
	public Optional<Path> convertListDataDoctorToExcel(List<DoctorDTO> listDataExport, DoctorDTO dataParams) {
		ExcellSheet sheetExport = new ExcellSheet();
		// set header
		List<ExcellHeaderEntity> listHeader = Arrays.asList("Stt", "Họ và tên", "Ngày tháng năm sinh",
				"Giới tính", "Số điện thoại", "Số GPHN", "Chuyên khoa", "Khám bệnh", "Tư vấn", "Cơ sở y tế").stream()
				.map(headerName -> new ExcellHeaderEntity(headerName, 6000)).collect(Collectors.toList());
		sheetExport.setListHeader(listHeader);

		// set data
		ExcellDataEntity excellDataEntity = new ExcellDataEntity();
		AtomicInteger atomicIntegerSTT = new AtomicInteger();
		List<List<Object>> listDataResultExcel = listDataExport.stream().map(objectDTO -> {
			List<Object> listObject = new ArrayList<>();
			listObject.add(atomicIntegerSTT.incrementAndGet());
			listObject.add(objectDTO.getFullname());
			listObject.add(Objects.isNull(objectDTO.getBirthday()) ? ExcelCommon.STRING_EMPTY :
					FnCommon.formatLocalDate(FnCommon.convertToLocalDate(Long.valueOf(objectDTO.getBirthday().getTime())), Constants.PATTERN_SHORT_DATE));
			listObject.add(Integer.valueOf(1).equals(objectDTO.getGenderId()) ? Constants.MALE_GENDER : Constants.FEMALE_GENDER);
			listObject.add(objectDTO.getPhoneNumber());
			listObject.add(objectDTO.getCertificationCode());
			listObject.add(objectDTO.getSpecialistName());
			String medicalExamination = "";
			if (objectDTO.getIsMedicalexamination() != null) {
				medicalExamination = objectDTO.getIsMedicalexamination() ? Constants.YES_STR : Constants.NO_STR;
			}
			listObject.add(medicalExamination);
			String consultant = "";
			if (objectDTO.getIsConsultant() != null) {
				consultant = objectDTO.getIsConsultant() ? Constants.YES_STR : Constants.NO_STR;
			}
			listObject.add(consultant);
			listObject.add(objectDTO.getHealthfacilitiesName());
			return listObject;
		}).collect(Collectors.toList());

		excellDataEntity.setListData(listDataResultExcel);
		sheetExport.setExcellDataEntity(excellDataEntity);
		sheetExport.setStrSheetName("ThongTinCaNhan");

		FnCommon.createFolder(Constants.EXCEL_EXPORT_PATH);

		String fileName = "DSBacSi_";
		String fileExtension = ".xlsx";
		String currentDate = FnCommon.formatLocalDateTime(LocalDateTime.now(), Constants.PATTERN_DATE_EXCEL_EXPORT);
		String filePath = String.join(ExcelCommon.STRING_EMPTY, Constants.EXCEL_EXPORT_PATH, fileName, currentDate, fileExtension);

		// set title
		StringBuilder title = new StringBuilder();
		title.append("DANH SÁCH BÁC SỸ" + ExcelCommon.NEW_LINE);
		title.append(ExcelCommon.STRING_EMPTY + ExcelCommon.NEW_LINE);
		title.append(MessageFormat.format("Ngày thống kê: {0}{1}", FnCommon.formatLocalDate(LocalDate.now(), Constants.PATTERN_SHORT_DATE), ExcelCommon.NEW_LINE));

		// save excel file
		Optional<Path> pathOpt = ExcelCommon.exportExcel(sheetExport, filePath, title.toString());
		return pathOpt;
	}

	@Override
	public Object getDoctorHomepageWeb(Authentication authentication, String language) throws TeleCareException, IOException {
		TelecareUserEntity telecareUserEntity = FnCommon.getTelecareUserInfo(authentication);
		if(telecareUserEntity==null || !telecareUserEntity.isDoctor()) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
		}
		SysUsersEntity sysUsersEntity = sysUsersRepositoryJPA.findFirstByKeycloakUserId(telecareUserEntity.getKeycloakUserId());
		if(sysUsersEntity==null) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
		}
		Integer doctorId = telecareUserEntity.getTelecareUserId();
		DoctorHomepageWebDTO dataResult = (DoctorHomepageWebDTO) doctorRepository.getPatients(doctorId);
		dataResult.setRates(doctorsCommentsRepositoryJPA.countByDoctorIdAndIsActiveAndIsDelete(doctorId, Constants.IS_ACTIVE, Constants.IS_NOT_DELETE));

		//get data from Communications
		String token = FnCommon.getStringToken(authentication);
		Response response = FnCommon.doGetRequest(System.getenv("Communications") + "/statistic-questions", token);
		CommunicationsResponse communicationsResponse =null;
		if (response != null) {
			communicationsResponse = new Gson().fromJson(response.body().string(), CommunicationsResponse.class);
			FnCommon.copyProperties(communicationsResponse.getData(), dataResult);
		}

		return dataResult;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Object importDoctor(DoctorDTO dto, Authentication authentication) throws TeleCareException, IOException {
		checkPermisisonUpdateDoctor(authentication, dto.getHealthacilitieCodes(), dto.getLanguage());

		if (Objects.nonNull(dto.getEmail()) && !FnCommon.validEmail(dto.getEmail())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_EMAIL_INVALID, dto.getLanguage()), ErrorApp.ERR_EMAIL_INVALID);
		}
		dto.setIsActive(true);
		dto.setDoctorType(1);
		dto.setSpecialistId(String.valueOf(dto.getSpecialistIdInt()));

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon
				.convertObjectToObject(dto, DoctorsEntity.class);
		doctorsEntity.setGenderId(1);
		doctorsEntity.setIsDelete(Constants.IS_NOT_DELETE);

		DoctorsEntity doctorEntitySaved = doctorsRepositoryJPA.save(doctorsEntity);

		String userId = null;
		try {
			dto.setPhoneNumber(dto.getUserName());
			dto.setDoctorId(doctorsEntity.getDoctorId());
			dto.setPassword(Constants.IMPORT_DOCTOR_PW_DEFAULT);
			userId = keycloakService.createUserInKeyCloak(dto);
		} catch (Exception e) {
			LOGGER.info(e);
			throw new TeleCareException(messageService.getMessage(Constants.GET_KEYCLOCK_FALSE, Constants.VIETNAM_CODE), ErrorApp.GET_KEYCLOCK_FALSE);
		}

		try{
			doctorEntitySaved.setKeycloakUserId(userId);
			DoctorsEntity doctorsEntityUpdate = doctorsRepositoryJPA.save(doctorEntitySaved);

			// send to sys user create to account service
			SysUsersEntity sysUsersEntity = new SysUsersEntity();
			sysUsersEntity.setKeycloakUserId(doctorsEntityUpdate.getKeycloakUserId());
			sysUsersEntity.setId(doctorsEntityUpdate.getDoctorId());
			sysUsersEntity.setType(doctorsEntityUpdate.getDoctorType());
			sysUsersRepositoryJPA.save(sysUsersEntity);
			kafkaService.createSysUser(sysUsersEntity);

			Integer specialistId = dto.getSpecialistIdInt();
			DoctorsSpecialistsEntity doctorsSpecia = new DoctorsSpecialistsEntity();
			doctorsSpecia.setDoctorId(doctorsEntity.getDoctorId());
			doctorsSpecia.setSpecialistId(specialistId);
			doctorsSpecialistsRepositoryJPA.save(doctorsSpecia);

			String healthacilitieCode = dto.getHealthfacilitiesCode();
			DoctorsHealthfacilitiesEntity dhe = new DoctorsHealthfacilitiesEntity();
			dhe.setDoctorId(doctorsEntity.getDoctorId());
			dhe.setHealthfacilitiesCode(healthacilitieCode);
			doctorsHealthfacilitiesRepositoryJPA.save(dhe);
		} catch (Exception e){
			e.printStackTrace();
			LOGGER.info(e);
			keycloakService.deleteUserKeycloak(userId);
			throw new TeleCareException(messageService.getMessage(Constants.DOCTOR_SAVE_FAIL, Constants.VIETNAM_CODE));
		}
		return dto;
	}

	@Override
	public Object createDoctorAndVideoCall(DoctorDTO dto, Authentication authentication) throws TeleCareException, IOException {
		checkPermisisonUpdateDoctor(authentication, dto.getHealthacilitieCodes(), dto.getLanguage());
		if (!FnCommon.validPhoneNumber(dto.getPhoneNumber())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_PHONE_NUMBER_INVALID, dto.getLanguage()), ErrorApp.ERR_PHONE_NUMBER_INVALID);
		}
		if (Objects.nonNull(dto.getEmail()) && !FnCommon.validEmail(dto.getEmail())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_EMAIL_INVALID, dto.getLanguage()), ErrorApp.ERR_EMAIL_INVALID);
		}

		if (dto.getAvatar() != null) {
			/* Valid avata file */
			MultipartFile avatarFile = Base64Util.base64ToMultipart(dto.getAvatar());
			if (!FnCommon
					.checkBriefcaseValid(avatarFile.getOriginalFilename(), avatarFile.getBytes(),
							Constants.IMAGE_UPLOAD_MAX_SIZE)) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_IMAGE_FILE_INVALID, dto.getLanguage()), ErrorApp.ERR_IMAGE_FILE_INVALID);
			}
			String avatar = FnCommon.uploadImage(Constants.IMAGE_UPLOAD_PATH, avatarFile);
			if (avatar == null) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_IMAGE_FILE_INVALID, dto.getLanguage()), ErrorApp.ERR_IMAGE_FILE_INVALID);
			}
			dto.setAvatar(avatar);
		}

		CastWardDto ward = (CastWardDto) castWardRepository
				.getAddress(dto.getProvinceCode(), dto.getDistrictCode(), dto.getWardCode());
		if (Objects.isNull(ward)) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_LOCATION_INVALID, dto.getLanguage()), ErrorApp.ERROR_DATA_LOCATION_INVALID);
		}

		if (doctorsRepositoryJPA.existsByPhoneNumber(dto.getPhoneNumber())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_PHONE_NUMBER_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST);
		}

		if (Objects.nonNull(dto.getEmail()) && doctorsRepositoryJPA.existsByEmail(dto.getEmail())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_EMAIL_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_EMAIL_EXIST);
		}

		Integer healthfacilities = healthfacilitiesRepositoryJPA
				.countByHealthfacilitiesCodeIn(dto.getHealthacilitieCodes());
		if (dto.getHealthacilitieCodes().size() < healthfacilities) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_HEALTHFACILITIE_NOT_FOUND, dto.getLanguage()), ErrorApp.ERR_HEALTHFACILITIE_NOT_FOUND);
		}

		//tam cho phep null de HSSK tao tai khoan doctor
		if(dto.getSpecialistIds()!=null) {
			Integer special = specialistsRepositoryJPA.countBySpecialistIdIn(dto.getSpecialistIds());
			if (dto.getSpecialistIds().size() < special) {
				throw new TeleCareException(messageService.getMessage(Constants.ERR_SPECIAL_NOT_FOUND, dto.getLanguage()), ErrorApp.ERR_SPECIAL_NOT_FOUND);
			}
		}

		Integer genderId = dto.getGenderId();
		if(genderId==null || !genderId.equals(Constants.MALE_GENDER_ID) || !genderId.equals(Constants.FEMALE_GENDER_ID)) {
			dto.setGenderId(Constants.DEFAULT_GENDER_ID);
		}

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon
				.convertObjectToObject(dto, DoctorsEntity.class);
		doctorsEntity.setDoctorCode(
				FnCommon.nameToNameCode(doctorsEntity.getFullname()) + doctorsEntity.getPhoneNumber());
		doctorsEntity.setIsDelete(Constants.IS_NOT_DELETE);

		DoctorsEntity doctorEntitySaved = doctorsRepositoryJPA.save(doctorsEntity);

		String userId = null;
		try {
			dto.setDoctorId(doctorsEntity.getDoctorId());
			userId = keycloakService.createUserInKeyCloak(dto);
		} catch (Exception e) {
			LOGGER.info(e);
			throw new TeleCareException(messageService.getMessage(Constants.GET_KEYCLOCK_FALSE, dto.getLanguage()), ErrorApp.GET_KEYCLOCK_FALSE);
		}
		doctorEntitySaved.setKeycloakUserId(userId);
		DoctorsEntity doctorsEntityUpdate = doctorsRepositoryJPA.save(doctorEntitySaved);

		// send to sys user create to account service
		SysUsersEntity sysUsersEntity = new SysUsersEntity();
		sysUsersEntity.setKeycloakUserId(doctorsEntityUpdate.getKeycloakUserId());
		sysUsersEntity.setId(doctorsEntityUpdate.getDoctorId());
		sysUsersEntity.setType(doctorsEntityUpdate.getDoctorType());
		sysUsersRepositoryJPA.save(sysUsersEntity);
		kafkaService.createSysUser(sysUsersEntity);

		List<DoctorsSpecialistsEntity> doctorsSpecialist = new ArrayList<>();
		//temp change
		if(dto.getSpecialistIds()!=null) {
			for (Integer specialistId : dto.getSpecialistIds()) {
				DoctorsSpecialistsEntity doctorsSpecia = new DoctorsSpecialistsEntity();
				doctorsSpecia.setDoctorId(doctorsEntity.getDoctorId());
				doctorsSpecia.setSpecialistId(specialistId);
				doctorsSpecialist.add(doctorsSpecia);
			}
			doctorsSpecialistsRepositoryJPA.saveAll(doctorsSpecialist);
		}


		List<DoctorsHealthfacilitiesEntity> doctorsHealthfaciliti = new ArrayList<>();
		for (String healthacilitieCode : dto.getHealthacilitieCodes()) {
			DoctorsHealthfacilitiesEntity dhe = new DoctorsHealthfacilitiesEntity();
			dhe.setDoctorId(doctorsEntity.getDoctorId());
			dhe.setHealthfacilitiesCode(healthacilitieCode);
			doctorsHealthfaciliti.add(dhe);
		}
		doctorsHealthfacilitiesRepositoryJPA.saveAll(doctorsHealthfaciliti);

		dto.setAvatar(Base64Util.toBase64(doctorsEntity.getAvatar()));

		try {
			videoCallService.registerDevices(dto.getPhoneNumber());
		} catch (Exception e){
			e.printStackTrace();
			LOGGER.info(e);
			keycloakService.deleteUserKeycloak(userId);
			throw new TeleCareException(messageService.getMessage(Constants.REGISTER_VIDEO_CALL_DEVICES_FALSE, dto.getLanguage()));
		}
		return dto;
	}

	@Override
	public Object importDoctorFile(Authentication authentication, FileImportDTO fileImportDTO) throws TeleCareException, IOException, ParseException {
		String language = fileImportDTO.getLanguage();
		TelecareUserEntity userEntity = FnCommon.getTelecareUserInfo(authentication);
		if (userEntity == null || (!userEntity.isAdmin() && !userEntity.isClinic())){
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language));
		}

		Integer sysUserId = null;
		try {
			sysUserId = getSysUserId(authentication, language);
		} catch (Exception e){
			LOGGER.info(e);
			e.printStackTrace();
		}

		File file = new File(fileImportDTO.getPathFile());

		ImportHistoryEntity importHistoryEntity = new ImportHistoryEntity();
		importHistoryEntity.setFileName(file.getName());
		importHistoryEntity.setFileType(Constants.FILE_TYPE_IMPORT_DOCTOR);
		importHistoryEntity.setStatus(Constants.STATUS_IMPORT_DOING);
		importHistoryEntity.setPathFile(fileImportDTO.getPathFile());
		importHistoryEntity.setCreateUserId(sysUserId);
		importHistoryEntity.setImportDate(new Date());
		importHistoryEntity.setIsActive(Constants.IS_ACTIVE);
		importHistoryEntity.setIsDelete(Constants.IS_NOT_DELETE);
		importHistoryEntity.setTotalImport(0);
		importHistoryRepositoryJPA.save(importHistoryEntity);

		Map<String, String> log = new HashMap<>();
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
		workbook.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
		Sheet sheet = workbook.getSheetAt(0);

		int idx = 1;
		int totalImport = 0;
		int totalSuccess = 0;
		while (sheet.getRow(idx).getCell(0) != null && sheet.getRow(idx).getCell(0).getCellType() != CellType.BLANK && !"".equals(sheet.getRow(idx).getCell(0).toString())){
			try{
				Row row = sheet.getRow(idx++);
				DoctorDTO doctorDTO = new DoctorDTO();
				doctorDTO.setFullname(row.getCell(1).getStringCellValue());
				doctorDTO.setBirthday(row.getCell(2) == null ? null : row.getCell(2).getDateCellValue());
				doctorDTO.setGenderId(row.getCell(3).getStringCellValue().equals(Constants.MALE_GENDER) ? Constants.MALE_GENDER_ID : Constants.FEMALE_GENDER_ID);
				doctorDTO.setProvinceCode(row.getCell(4).getStringCellValue());
				doctorDTO.setDistrictCode(row.getCell(5) == null ? null : row.getCell(5).getStringCellValue());
				doctorDTO.setWardCode(row.getCell(6) == null ? null : row.getCell(6).getStringCellValue());
				doctorDTO.setAddress(row.getCell(7) == null ? null : row.getCell(7).getStringCellValue());
				doctorDTO.setPhoneNumber(row.getCell(8).getStringCellValue());
				doctorDTO.setEmail(row.getCell(9) == null ? null : row.getCell(9).getStringCellValue());
				doctorDTO.setCertificationCode(row.getCell(10) == null ? null : row.getCell(10).getStringCellValue());
				doctorDTO.setCertificationDate(row.getCell(11) == null ? null : row.getCell(11).getDateCellValue());
				doctorDTO.setAcademicRankId(row.getCell(12) == null ? null : getAcademicRankIdByName(row.getCell(12).getStringCellValue()));
				doctorDTO.setDegreeId(row.getCell(13) == null ? null : getDegreeIdByName(row.getCell(13).getStringCellValue()));
				doctorDTO.setNationCode(getNationCodeByName(row.getCell(14).getStringCellValue()));
				doctorDTO.setEthnicityCode(row.getCell(15) == null ? null : getEthnicitiesCodeByName(row.getCell(15).getStringCellValue()));
				doctorDTO.setReligionCode(row.getCell(16) == null ? null : getReligionCodeByName(row.getCell(16).getStringCellValue()));
				doctorDTO.setSpecialistIds(Collections.singletonList(getSpecialistIdByName(row.getCell(17).getStringCellValue())));
				doctorDTO.setDescription(row.getCell(18) == null ? null : row.getCell(18).getStringCellValue());
				doctorDTO.setSummary(row.getCell(19) == null ? null : row.getCell(19).getStringCellValue());
				doctorDTO.setHealthacilitieCodes(Collections.singletonList(row.getCell(20).getStringCellValue()));
				doctorDTO.setIsMedicalexamination(row.getCell(21) == null || (Constants.IS_MEDICALEXAMINATION.equals(row.getCell(21).getStringCellValue())));
				doctorDTO.setIsConsultant(row.getCell(22) == null || Constants.IS_CONSULTANT.equals(row.getCell(22).getStringCellValue()));
				doctorDTO.setIsActive(row.getCell(23) == null || Constants.IS_ACTIVE_STRING.equals(row.getCell(23).getStringCellValue()));

				createDoctorImport(doctorDTO, authentication);

				totalSuccess++;
			} catch (Exception e){
				e.printStackTrace();
				LOGGER.info(e);
				log.put("" + idx, "Xay ra loi tai dong thu " + idx);
			}
			totalImport++;
		}
		importHistoryEntity.setTotalImport(totalImport);
		importHistoryEntity.setTotalSucsess(totalSuccess);
		importHistoryEntity.setCompletedDate(new Date());
		importHistoryEntity.setStatus(Constants.STATUS_IMPORT_SUCCESS);
		importHistoryRepositoryJPA.save(importHistoryEntity);

		System.out.println("=================================");
		log.entrySet().forEach(System.out::println);
		System.out.println("=================================");
		FileImportDTO result = new FileImportDTO();
		result.setStatus(Constants.STATUS_IMPORT_SUCCESS);
		result.setTotalImport(importHistoryEntity.getTotalImport());
		result.setTotalSuccess(importHistoryEntity.getTotalSucsess());
		result.setImportDate(importHistoryEntity.getImportDate());
		result.setCompletedDate(importHistoryEntity.getCompletedDate());
		return result;
	}

	@Override
	public Object uploadFileImport(Authentication authentication, MultipartFile file, String language) throws IOException, TeleCareException {
		// kiem tra dinh dang file voi template dinh san
		if (!FnCommon.validTemplateFile(file)){
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_INVALID_FILE_FORMAT, language));
		}

//		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//		Sheet sheet = workbook.getSheetAt(0);
//		while (sheet.getRow(idx).getCell(0) != null && sheet.getRow(idx).getCell(0).getCellType() != CellType.BLANK && !sheet.getRow(idx).getCell(0).toString().equals("")){
//			Row row = sheet.getRow(idx++);
//			if (row.getCell(1) == null || row.getCell(1).getCellType() == CellType.BLANK 				// Ho va ten
//					|| row.getCell(3) == null || row.getCell(3).getCellType() == CellType.BLANK 		// Gioi tinh
//					|| row.getCell(4) == null || row.getCell(4).getCellType() == CellType.BLANK			// Ma tinh/TP
//					|| row.getCell(8) == null || row.getCell(8).getCellType() == CellType.BLANK			// So dien thoai
//					|| row.getCell(14) == null || row.getCell(14).getCellType() == CellType.BLANK		// Quoc tich
//					|| row.getCell(17) == null || row.getCell(17).getCellType() == CellType.BLANK 		// Chuyen khoa
//					|| row.getCell(20) == null || row.getCell(20).getCellType() == CellType.BLANK)		// Ma CSYT
//			{
//				throw new TeleCareException(messageService.getMessage(Constants.ERROR_INVALID_COLUMN_DATA, language));
//			}
//		}

		String pathFile = FnCommon.uploadFile(Constants.IMAGE_UPLOAD_PATH, file);
		FileImportDTO fileImportDTO = new FileImportDTO();
		fileImportDTO.setPathFile(pathFile);
		return fileImportDTO;
	}

	@Transactional(rollbackOn = Exception.class)
	public Object createDoctorImport(DoctorDTO dto, Authentication authentication) throws TeleCareException, JsonProcessingException {
		checkPermisisonUpdateDoctor(authentication, dto.getHealthacilitieCodes(), dto.getLanguage());
		if (!FnCommon.validPhoneNumber(dto.getPhoneNumber())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_PHONE_NUMBER_INVALID, dto.getLanguage()), ErrorApp.ERR_PHONE_NUMBER_INVALID);
		}
		if (Objects.nonNull(dto.getEmail()) && !FnCommon.validEmail(dto.getEmail())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_EMAIL_INVALID, dto.getLanguage()), ErrorApp.ERR_EMAIL_INVALID);
		}

		if (doctorsRepositoryJPA.existsByPhoneNumber(dto.getPhoneNumber())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_PHONE_NUMBER_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST);
		}

		if (Objects.nonNull(dto.getEmail()) && doctorsRepositoryJPA.existsByEmail(dto.getEmail())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_EMAIL_EXIST, dto.getLanguage()), ErrorApp.ERROR_DATA_EMAIL_EXIST);
		}

		Integer healthfacilities = healthfacilitiesRepositoryJPA
				.countByHealthfacilitiesCodeIn(dto.getHealthacilitieCodes());
		if (dto.getHealthacilitieCodes().size() < healthfacilities) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_HEALTHFACILITIE_NOT_FOUND, dto.getLanguage()), ErrorApp.ERR_HEALTHFACILITIE_NOT_FOUND);
		}

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon
				.convertObjectToObject(dto, DoctorsEntity.class);
		doctorsEntity.setDoctorCode(
				FnCommon.nameToNameCode(doctorsEntity.getFullname()) + doctorsEntity.getPhoneNumber());
		doctorsEntity.setIsDelete(Constants.IS_NOT_DELETE);
		doctorsEntity.setDoctorType(Constants.DOCTOR_TYPE);
		doctorsEntity.setCreateUserId(getSysUserId(authentication, Constants.VIETNAM_CODE));
		DoctorsEntity doctorEntitySaved = doctorsRepositoryJPA.save(doctorsEntity);

		String userId = null;
		try {
			dto.setDoctorId(doctorsEntity.getDoctorId());
			userId = keycloakService.createUserInKeyCloak(dto);
		} catch (Exception e) {
			LOGGER.info(e);
			throw new TeleCareException(messageService.getMessage(Constants.GET_KEYCLOCK_FALSE, dto.getLanguage()), ErrorApp.GET_KEYCLOCK_FALSE);
		}
		doctorEntitySaved.setKeycloakUserId(userId);
		DoctorsEntity doctorsEntityUpdate = doctorsRepositoryJPA.save(doctorEntitySaved);

		// send to sys user create to account service
		SysUsersEntity sysUsersEntity = new SysUsersEntity();
		sysUsersEntity.setKeycloakUserId(doctorsEntityUpdate.getKeycloakUserId());
		sysUsersEntity.setId(doctorsEntityUpdate.getDoctorId());
		sysUsersEntity.setType(doctorsEntityUpdate.getDoctorType());
		sysUsersRepositoryJPA.save(sysUsersEntity);
		kafkaService.createSysUser(sysUsersEntity);

		List<DoctorsSpecialistsEntity> doctorsSpecialist = new ArrayList<>();
		//temp change
		if(dto.getSpecialistIds()!=null) {
			for (Integer specialistId : dto.getSpecialistIds()) {
				DoctorsSpecialistsEntity doctorsSpecia = new DoctorsSpecialistsEntity();
				doctorsSpecia.setDoctorId(doctorsEntity.getDoctorId());
				doctorsSpecia.setSpecialistId(specialistId);
				doctorsSpecialist.add(doctorsSpecia);
			}
			doctorsSpecialistsRepositoryJPA.saveAll(doctorsSpecialist);
		}

		List<DoctorsHealthfacilitiesEntity> doctorsHealthfaciliti = new ArrayList<>();
		for (String healthacilitieCode : dto.getHealthacilitieCodes()) {
			DoctorsHealthfacilitiesEntity dhe = new DoctorsHealthfacilitiesEntity();
			dhe.setDoctorId(doctorsEntity.getDoctorId());
			dhe.setHealthfacilitiesCode(healthacilitieCode);
			doctorsHealthfaciliti.add(dhe);
		}
		doctorsHealthfacilitiesRepositoryJPA.saveAll(doctorsHealthfaciliti);

		return dto;
	}


	public Integer getSysUserId(Authentication authentication, String language) throws TeleCareException {
		SysUsersEntity currentUser = sysUsersRepositoryJPA
				.findFirstByKeycloakUserId(FnCommon.getUserIdLogin(authentication));
		if (currentUser == null) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language));
		}
		return currentUser.getUserId();
	}

	private Integer getAcademicRankIdByName(String name){
		return Strings.isNullOrEmpty(name) ? null : catsAcademicRankRepositoryJPA.findByName(name).getId();
	}
	private Integer getDegreeIdByName(String name){
		return  Strings.isNullOrEmpty(name) ? null : catsDegreeRepositoryJPA.findByName(name).getDegreeId();
	}
	private String getNationCodeByName(String name){
		return  Strings.isNullOrEmpty(name) ? null : catsNationsRepositoryJPA.findByNationName(name).getNationCode();
	}
	private String getEthnicitiesCodeByName(String name){
		return  Strings.isNullOrEmpty(name) ? null : catsEthnicitiesRepositoryJPA.findByName(name).getEthnicityCode();
	}
	private String getReligionCodeByName(String name){
		return  Strings.isNullOrEmpty(name) ? null : catsReligionsRepositoryJPA.findByName(name).getReligionCode();
	}
	private Integer getSpecialistIdByName(String name){
		return  Strings.isNullOrEmpty(name) ? null : catsSpecialistsRepositoryJPA.findByNameAndIsActiveAndIsDelete(name, Constants.IS_ACTIVE, Constants.IS_NOT_DELETE).getSpecialistId();
	}
}
