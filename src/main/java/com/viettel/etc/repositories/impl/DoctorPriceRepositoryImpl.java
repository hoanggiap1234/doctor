package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.DoctorPriceDTO;
import com.viettel.etc.repositories.DoctorPriceRepository;
import com.viettel.etc.repositories.tables.SysUsersRepositoryJPA;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.utils.TelecareUserEntity;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Autogen class Repository Impl:
 *
 * @author ToolGen
 * @date Mon Sep 07 14:45:01 ICT 2020
 */
@Repository
public class DoctorPriceRepositoryImpl extends CommonDataBaseRepository implements DoctorPriceRepository {
	@Autowired
	SysUsersRepositoryJPA sysUsersRepositoryJPA;

	/**
	 * lay danh sach gia kham, tu van cua bac si theo moc thoi gian
	 *
	 * @param itemParamsEntity: params client truyen len
	 * @return
	 */
	@Override
	public ResultSelectEntity getDoctorPrices(DoctorPriceDTO itemParamsEntity, TelecareUserEntity userEntity) throws TeleCareException {
		if (itemParamsEntity.getPhaseId() == null) {
			return new ResultSelectEntity();
		}
		HashMap<String, Object> hmapParams = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("" +
				"select " +
				"  doctor.doctor_id as doctorId, " +
				"  doctor.avatar, " +
				"  doctor.fullname, " +
				"  doctor.phone_number as phoneNumber, " +
				"  cats_specialist.name AS specialistName, " +
				"  doctors_specialist.specialist_id as specialistId, " +
				"  doctors_healthfacilitie.healthfacilities_code AS healthfacilitiesCode " +
				"from " +
				"  doctors doctor " +
				"inner join ( " +
				"  select " +
				"    healthfacilities_code , doctor_id " +
				"  from " +
				"    doctors_healthfacilities " +
				"  where " +
				"    is_active = 1 " +
				"    and is_delete = 0) doctors_healthfacilitie on " +
				"  doctors_healthfacilitie.doctor_id = doctor.doctor_id " +
				"left join ( " +
				"  select " +
				"    specialist_id, doctor_id " +
				"  from " +
				"    doctors_specialists " +
				"  where " +
				"    is_active = 1 " +
				"    and is_delete = 0) doctors_specialist on " +
				"  doctors_specialist.doctor_id = doctor.doctor_id " +
				"left join ( " +
				"  select " +
				"    name, specialist_id " +
				"  from " +
				"    cats_specialists " +
				"  where " +
				"    is_active = 1 " +
				"    and is_delete = 0) cats_specialist on " +
				"  cats_specialist.specialist_id = doctors_specialist.specialist_id " +
				"where " +
				"  doctor.is_active = 1 " +
				"  and doctor.is_delete = 0 ");
		if (Objects.nonNull(itemParamsEntity.getHealthfacilitiesCode())) {
			sql.append(" and doctors_healthfacilitie.healthfacilities_code = :healthfacilitiesCode ");
			hmapParams.put("healthfacilitiesCode", itemParamsEntity.getHealthfacilitiesCode());
		}
		if (userEntity.isDoctor()) {
			sql.append(" and doctor.doctor_id=:loginUserId ");
			hmapParams.put("loginUserId", userEntity.getTelecareUserId());
		} else if (userEntity.isClinic()) {
			String healthfacilitiesCodes = sysUsersRepositoryJPA.getHealthfacilitiesCodes(userEntity.getKeycloakUserId());
			if (healthfacilitiesCodes != null) {
				sql.append(" and doctors_healthfacilitie.healthfacilities_code in (").append(healthfacilitiesCodes).append(") ");
			} else {
				sql.append(" and 1=2 ");
			}
		}
		if (Objects.nonNull(itemParamsEntity.getSpecialistId())) {
			sql.append("    AND doctors_specialist.specialist_id = :specialistId");
			hmapParams.put("specialistId", itemParamsEntity.getSpecialistId());
		}
		if (!StringUtils.isEmpty(itemParamsEntity.getFullname())) {
			sql.append("    AND doctor.fullname LIKE CONCAT('%', :fullname, '%') ");
			hmapParams.put("fullname", itemParamsEntity.getFullname());
		}

		if (!StringUtils.isEmpty(itemParamsEntity.getPhoneNumber())) {
			sql.append("    AND doctor.phone_number LIKE CONCAT('%', :phoneNumber, '%') ");
			hmapParams.put("phoneNumber", itemParamsEntity.getPhoneNumber());
		}

		sql.append(" ORDER BY cats_specialist.name ");

		Integer start = Constants.START_RECORD_DEFAULT;
		if (itemParamsEntity != null && itemParamsEntity.getStartrecord() != null) {
			start = itemParamsEntity.getStartrecord();
		}
		Integer pageSize = Constants.PAGE_SIZE_DEFAULT;
		if (itemParamsEntity != null && itemParamsEntity.getPagesize() != null) {
			pageSize = itemParamsEntity.getPagesize();
		}
		ResultSelectEntity doctors = getListDataAndCount(sql, hmapParams, start, pageSize, DoctorPriceDTO.class);
		if (!(doctors != null && doctors.getListData().size() > 0)) {
			return doctors;
		}

		List<DoctorPriceDTO> doctorsListData = (List<DoctorPriceDTO>) doctors.getListData();
		Set<Integer> doctorIds = doctorsListData.stream().map(DoctorPriceDTO::getDoctorId).collect(Collectors.toSet());

		StringBuilder sqlPrice = new StringBuilder();
		sqlPrice.append("" +
				"select " +
				"  doctors_price.phase_id as phaseId, " +
				"  doctors_price.medical_examination_fee as medicalExaminationFee, " +
				"  doctors_price.medical_examination_number as medicalExaminationNumber, " +
				"  doctors_price.consultant_fee_videocall as consultantFeeVideocall, " +
				"  doctors_price.consultant_time_videocall as consultantTimeVideocall, " +
				"  doctors_price.consultant_fee_call as consultantFeeCall, " +
				"  doctors_price.consultant_time_call as consultantTimeCall, " +
				"  doctors_price.consultant_fee_chat as consultantFeeChat, " +
				"  doctors_price.doctor_id as doctorId, " +
				"  doctors_price.consultant_chat_number as consultantChatNumber " +
				"from " +
				"  doctors_prices doctors_price " +
				"where is_active=1 and is_delete=0 and " +
				"  doctors_price.phase_id = ").append(itemParamsEntity.getPhaseId()).append(" ");

		Integer[] doctorIdArr = doctorIds.stream().toArray(Integer[]::new);
		String arrString = Arrays.toString(doctorIdArr).replace("[", "").replace("]", "");
		sqlPrice.append(" AND doctors_price.doctor_id IN(").append(arrString).append(") ");

		List<DoctorPriceDTO> priceDTOS = (List<DoctorPriceDTO>) getListData(sqlPrice, new HashMap<>(), null, null, DoctorPriceDTO.class);

		Map<Integer, DoctorPriceDTO> doctorPriceDTOMap = priceDTOS.stream().collect(Collectors.toMap(DoctorPriceDTO::getDoctorId, Function.identity()));

		for (DoctorPriceDTO doctorsListDatum : doctorsListData) {
			if (doctorPriceDTOMap.get(doctorsListDatum.getDoctorId()) == null) {
				continue;
			}
			FnCommon.copyProperties(doctorPriceDTOMap.get(doctorsListDatum.getDoctorId()), doctorsListDatum);
		}

		doctors.setListData(doctorsListData);

		return doctors;

	}

	/**
	 * Danh sach Doctor Prices
	 *
	 * @param itemParamsEntity: params client truyen len
	 * @return
	 */
	@Override
	public ResultSelectEntity getDoctorPricesByPhaseIdAndDoctorId(DoctorPriceDTO itemParamsEntity) {
		HashMap<String, Object> hmapParams = new HashMap<>();
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT  ");
		sql.append(" doctors_price.price_id as priceId, ");
		sql.append(" doctors_price.doctor_id as doctorId, ");
		sql.append(" doctors_specialist.specialist_id as specialistId, ");
		sql.append(" cats_specialist.name AS specialistName,  ");
		sql.append(" doctors_price.phase_id as phaseId, ");
		sql.append(" doctor.avatar, ");
		sql.append(" doctor.fullname, ");
		sql.append(" doctor.phone_number as phoneNumber, ");
		sql.append(" doctors_price.medical_examination_fee as medicalExaminationFee,  ");
		sql.append(" doctors_price.medical_examination_number AS medicalExaminationNumber, ");
		sql.append(" doctors_price.consultant_fee_videocall AS consultantFeeVideocall, ");
		sql.append(" doctors_price.consultant_time_videocall AS consultantTimeVideocall,  ");
		sql.append(" doctors_price.consultant_fee_call AS consultantFeeCall, ");
		sql.append(" doctors_price.consultant_time_call AS consultantTimeCall, ");
		sql.append(" doctors_price.consultant_fee_chat AS consultantFeeChat, ");
		sql.append(" doctors_price.consultant_chat_number AS consultantChatNumber, ");
		sql.append(" doctors_healthfacilitie.healthfacilities_code AS healthfacilitiesCode ");

		sql.append(" FROM doctors_prices AS doctors_price ");
		sql.append("    JOIN doctors AS doctor ON doctor.doctor_id = doctors_price.doctor_id");
		sql.append("      AND doctor.is_delete = 0 ");
		sql.append("      AND doctor.is_active = 1 ");
		sql.append("    LEFT JOIN doctors_healthfacilities AS doctors_healthfacilitie ON doctors_healthfacilitie.doctor_id = doctors_price.doctor_id");
		sql.append("      AND doctors_healthfacilitie.is_delete = 0 ");
		sql.append("      AND doctors_healthfacilitie.is_active = 1 ");
		sql.append("    LEFT JOIN doctors_specialists AS doctors_specialist ON doctor.doctor_id = doctors_specialist.doctor_id");
		sql.append("      AND doctors_specialist.is_delete = 0 ");
		sql.append("      AND doctors_specialist.is_active = 1 ");
		sql.append("    LEFT JOIN cats_specialists AS cats_specialist ON cats_specialist.specialist_id = doctors_specialist.specialist_id");
		sql.append("      AND cats_specialist.is_delete = 0 ");
		sql.append("      AND cats_specialist.is_active = 1 ");
		sql.append(" WHERE doctors_price.is_delete = 0");
		sql.append("    AND doctors_price.phase_id = :phaseId");
		sql.append("    AND doctors_price.doctor_id = :doctorId");

		if (Objects.nonNull(itemParamsEntity.getHealthfacilitiesCode())) {
			sql.append("    AND doctors_healthfacilitie.healthfacilities_code = :healthfacilitiesCode");
			hmapParams.put("healthfacilitiesCode", itemParamsEntity.getHealthfacilitiesCode());
		}

		sql.append(" GROUP BY doctors_specialist.specialist_id");
		sql.append(" ORDER BY cats_specialist.name ");

		hmapParams.put("phaseId", itemParamsEntity.getPhaseId());
		hmapParams.put("doctorId", itemParamsEntity.getDoctorId());

		ResultSelectEntity resultSelectEntity = getListDataAndCount(sql, hmapParams, null, null, DoctorPriceDTO.class);
		return resultSelectEntity;
	}

}
