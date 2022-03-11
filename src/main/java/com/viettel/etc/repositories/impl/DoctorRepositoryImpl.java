package com.viettel.etc.repositories.impl;

import com.google.common.base.Strings;
import com.viettel.etc.dto.*;
import com.viettel.etc.repositories.DoctorRepository;
import com.viettel.etc.repositories.tables.*;
import com.viettel.etc.repositories.tables.entities.CatsAcademicRankEntity;
import com.viettel.etc.repositories.tables.entities.CatsDegreeEntity;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.OrderNumber;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.utils.TelecareUserEntity;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Autogen class Repository Impl: Lop thong tin cua bac si
 *
 * @author ToolGen
 * @date Wed Aug 19 14:21:51 ICT 2020
 */
@Repository
public class DoctorRepositoryImpl extends CommonDataBaseRepository implements DoctorRepository {
    @Autowired
    SysUsersRepositoryJPA sysUsersRepositoryJPA;

    @Autowired
    private DoctorsHealthfacilitiesRepositoryJPA doctorsHealthfacilitiesRepositoryJPA;

    @Autowired
    private CatsAcademicRankRepositoryJPA catsAcademicRankRepositoryJPA;

    @Autowired
    private CatsDegreeRepositoryJPA catsDegreeRepositoryJPA;

    /**
     * @param dto: params client truyen len
     * @return
     */
    @Override
    public ResultSelectEntity getDoctors(DoctorDTO dto, TelecareUserEntity userSystemEntity) throws TeleCareException {
        ResultSelectEntity resultEmptyData = new ResultSelectEntity();
        resultEmptyData.setListData(new ArrayList<>());
        resultEmptyData.setCount(0);

        Integer start = setStartRecord(dto);
        Integer pageSize = setPageSize(dto);

        HashMap<String, Object> hmapParams = new HashMap<>();
        List<Integer> doctorIdsValid = new ArrayList<>();

        //get healthFacilities code of doctor/clinic
        List<String> healthFacilitiesCodesValid = getHealthFacilitiesCodesValid(userSystemEntity);
        if (healthFacilitiesCodesValid == null) return resultEmptyData;

        //filter by healthFacilitiesCode
        String healthfacilitiesCode = dto.getHealthfacilitiesCode();
        if (healthfacilitiesCode != null && !"".equals(healthfacilitiesCode)) {
            if (userSystemEntity != null && !userSystemEntity.isAdmin() && !healthFacilitiesCodesValid.contains(healthfacilitiesCode)) {
                return resultEmptyData; // no satisfied data
            }
            healthFacilitiesCodesValid.clear();
            healthFacilitiesCodesValid.add(healthfacilitiesCode);
        }

        if (healthFacilitiesCodesValid.size() > 0) {
            doctorIdsValid = doctorsHealthfacilitiesRepositoryJPA.getDoctorIdsByHealthficility(healthFacilitiesCodesValid);
            if (doctorIdsValid.size() == 0) {
                return resultEmptyData; // no satisfied data
            }
        } //at this point, if isAdmin/no token, doctorIdsValid.size() can equal 0

        //filter by specialistCode
        if (dto.getSpecialistCode() != null && !"".equals(dto.getSpecialistCode())) {
            List<DoctorIdDTO> doctorIdDTOs = getDoctorBySpecialistCodes(dto, hmapParams);
            if (doctorIdDTOs == null || doctorIdDTOs.size() == 0) {
                return resultEmptyData; // no satisfied data
            }
            List<Integer> doctorIdsBySpecialistCodes = doctorIdDTOs.stream().map(DoctorIdDTO::getDoctorId).collect(Collectors.toList());
            if (doctorIdsValid.size() == 0) {
                doctorIdsValid.addAll(doctorIdsBySpecialistCodes);
            } else {
                doctorIdsValid.retainAll(doctorIdsBySpecialistCodes); //keep duplicate doctorIds from 2 lists
            }
        } //at this point, if isAdmin/no token, doctorIdsValid.size() still can equal 0

        //step 2: get doctorIds
        ResultSelectEntity doctorIdData = getNumberOfResultDoctors(dto, userSystemEntity, start, pageSize, doctorIdsValid);
        List<DoctorIdDTO> doctorIdDTOs = (List<DoctorIdDTO>) doctorIdData.getListData();
        List<Integer> doctorIdList = doctorIdDTOs.stream().map(DoctorIdDTO::getDoctorId).collect(Collectors.toList());

        //step 3: join to get related data, search IN doctorIds
        ResultSelectEntity resultData = getDataResultDoctors(doctorIdList);
        resultData.setCount(doctorIdData.getCount());
        if (resultData == null || resultData.getListData().size() == 0) {
            return resultData;
        }

        //add related data
        List<DoctorDTO> dataResult = (List<DoctorDTO>) resultData.getListData();
//        setDoctorPrice(doctorIdList, dataResult);
        setDoctorSpecialists(doctorIdList, dataResult);
        setDoctorHealthFacilities(doctorIdList, dataResult, dto);

        return resultData;
    }

    private ResultSelectEntity getDataResultDoctors(List<Integer> doctorIdList) {
        HashMap<String, Object> hmapParams = new HashMap<>();
        hmapParams.put("doctorIdList", doctorIdList);
        StringBuilder sqlJoin = new StringBuilder();
        sqlJoin.append("" +
                "SELECT result.*, " +
                "IF(result.certificationDate IS NULL,null,YEAR(NOW())-YEAR(result.certificationDate)) as experiences " +
                " FROM ( " +
                "  SELECT d.doctor_id AS doctorId " +
                "    ,d.birthday AS birthday " +
                "    ,d.description AS description " +
                "    ,d.certification_date AS certificationDate " +
                "    ,d.phone_number AS phoneNumber " +
                "    ,d.email AS email " +
                "    ,cats_province.province_code AS provinceCode " +
                "    ,cats_province.name AS provinceName " +
                "    ,cats_district.district_code AS districtCode " +
                "    ,cats_district.name AS districtName " +
                "    ,cats_ward.ward_code AS wardCode " +
                "    ,cats_ward.name AS wardName " +
                "    ,d.address AS address " +
                "    ,d.certification_code AS certificationCode " +
                "    ,d.is_medicalexamination AS isMedicalexamination " +
                "    ,d.is_consultant AS isConsultant " +
                "    ,d.is_active AS isActive " +
                "    ,d.fullname AS fullname " +
                "    ,d.avatar AS avatar " +
                "    ,d.gender_id AS genderId " +
                "    ,car.id AS academicRankId " +
                "    ,car.code AS academicRankCode " +
                "    ,car.name AS academicRankName " +
                "    ,cd.id AS degreeId " +
                "    ,cd.code AS degreeCode " +
                "    ,cd.name AS degreeName " +
                "    ,cn.nationCode AS nationCode " +
                "    ,cn.nation_name AS nationName " +
                "    ,ce.ethnicityCode AS ethnicityCode " +
                "    ,ce.name AS ethnicityName " +
                "    ,cre.religionCode AS religionCode " +
                "    ,cre.name AS religionName " +
                "    ,d.summary AS summary " +
                "	 ,doc_com.totalComments " +
                "	 ,doc_com.stars " +
                "	 ,doc_book.totalBooking  " +
                "  FROM (SELECT * FROM doctors WHERE doctor_id in :doctorIdList) d " +
                "  LEFT JOIN ( " +
                "    SELECT Id AS id " +
                "      ,code " +
                "      ,name " +
                "    FROM cats_academic_rank " +
                "    WHERE is_active = 1 " +
                "      AND is_delete = 0 " +
                "    ) car ON car.id = d.academic_rank_id " +
                "  LEFT JOIN ( " +
                "    SELECT nation_code AS nationCode " +
                "      ,nation_name " +
                "    FROM cats_nations " +
                "    WHERE is_active = 1 " +
                "      AND is_delete = 0 " +
                "    ) cn ON cn.nationCode = d.nation_code " +
                "  LEFT JOIN ( " +
                "    SELECT ethnicity_code AS ethnicityCode " +
                "      ,name " +
                "    FROM cats_ethnicities " +
                "    WHERE is_active = 1 " +
                "      AND is_delete = 0 " +
                "    ) ce ON ce.ethnicityCode = d.ethnicity_code " +
                "  LEFT JOIN ( " +
                "    SELECT religion_code AS religionCode " +
                "      ,name " +
                "    FROM cats_religions " +
                "    WHERE is_active = 1 " +
                "      AND is_delete = 0 " +
                "    ) cre ON cre.religionCode = d.religion_code " +
                "  LEFT JOIN ( " +
                "    SELECT degree_id AS id " +
                "      ,code " +
                "      ,name " +
                "    FROM cats_degree " +
                "    WHERE is_active = 1 " +
                "      AND is_delete = 0 " +
                "    ) cd ON cd.id = d.degree_id " +
                "  LEFT JOIN ( " +
                "    SELECT province_code " +
                "      ,name " +
                "    FROM cats_provinces " +
                "    WHERE is_active = 1 " +
                "      AND is_delete = 0 " +
                "    ) cats_province ON cats_province.province_code = d.province_code " +
                "  LEFT JOIN ( " +
                "    SELECT district_code " +
                "      ,name " +
                "    FROM cats_districts " +
                "    WHERE is_active = 1 " +
                "      AND is_delete = 0 " +
                "    ) cats_district ON cats_district.district_code = d.district_code " +
                "  LEFT JOIN ( " +
                "    SELECT ward_code " +
                "      ,name " +
                "    FROM cats_wards " +
                "    WHERE is_active = 1 " +
                "      AND is_delete = 0 " +
                "    ) cats_ward ON cats_ward.ward_code = d.ward_code " +
                "	LEFT JOIN (" +
                "		SELECT COUNT(*) as totalComments" +
                "		,AVG(point_evaluation) AS stars " +
                "		, doctor_id" +
                "		FROM doctors_comments" +
                "		WHERE is_active = 1" +
                "		AND is_delete = 0" +
                "		GROUP BY doctor_id) doc_com" +
                "		ON doc_com.doctor_id = d.doctor_id" +
                "	LEFT JOIN(" +
                "		SELECT COUNT(*) AS totalBooking, doctor_id" +
                "		FROM booking_informations" +
                "		WHERE is_active = 1" +
                "		AND is_delete = 0" +
                "		GROUP BY doctor_id) doc_book" +
                "		ON doc_book.doctor_id = d.doctor_id" +
                "  WHERE d.is_delete = 0 " +
                "    AND d.is_active = 1 " +
                "    AND d.is_active = 1 " +
                "  ) result WHERE 1 = 1 ");

        ResultSelectEntity resultData = getListDataAndCount(sqlJoin, hmapParams, null, null, DoctorDTO.class);
        return resultData;
    }

    private ResultSelectEntity getNumberOfResultDoctors(DoctorDTO dto, TelecareUserEntity userSystemEntity, Integer start, Integer pageSize, List<Integer> doctorIdsValid) throws TeleCareException {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> hmapParams = new HashMap<>();
        sql.append(" SELECT doctor_id AS doctorId FROM doctors WHERE is_delete=0 ");
        filterByDoctorIds(hmapParams, doctorIdsValid, sql);
        filterByBirthYear(dto, hmapParams, sql);
        filterByGender(dto, hmapParams, sql);
        filterByPhoneNumber(dto, hmapParams, sql);
        filterByEmail(dto, hmapParams, sql);
        filterByIsActive(dto, hmapParams, sql);
        filterByFullname(dto, hmapParams, sql);
        filterByProvinceOrDistrict(dto, hmapParams, sql);
        filterByAcademicRank(dto, hmapParams, sql);
        filterByDegree(dto, hmapParams, sql);
        filterByIsMedicalExamOrIsConsultant(dto, userSystemEntity, hmapParams, sql);
        filterByDoctorType(dto, sql, hmapParams);
        sql.append(" GROUP BY doctorId ");
        orderBy(dto, sql);
        ResultSelectEntity doctorIdData = getListDataAndCount(sql, hmapParams, start, pageSize, DoctorIdDTO.class);
        return doctorIdData;
    }

    private void filterByDoctorType(DoctorDTO dto, StringBuilder sql, HashMap<String, Object> hmapParams) {
        if (dto.getDoctorType() != null) {
            sql.append(" and doctor_type = :doctorType ");
            hmapParams.put("doctorType", dto.getDoctorType());
        } else {
            sql.append(" and doctor_type = 1");
        }
    }

    private void orderBy(DoctorDTO dto, StringBuilder sql) {
        if (dto.getOrderBy() != null) {
            if (dto.getOrderBy().equals(OrderNumber.ORDER_BY_NAME_ASC.val()))
                sql.append(" order by fullName asc ");
            if (dto.getOrderBy().equals(OrderNumber.ORDER_BY_SPECIALIST_NAME_ASC.val()))
                sql.append(" order by specialistName asc ");
        }
    }

    private void filterByIsMedicalExamOrIsConsultant(DoctorDTO dto, TelecareUserEntity userSystemEntity, HashMap<String, Object> hmapParams, StringBuilder sql) throws TeleCareException {
        if (userSystemEntity == null || userSystemEntity.isPatient()) {
            sql.append(" and ( is_medicalexamination = 1 or is_Consultant = 1 ) ");
        } else {
            if (dto.getIsMedicalexamination() != null) {
                sql.append(" and is_medicalexamination = :isMedicalexamination ");
                hmapParams.put("isMedicalexamination", dto.getIsMedicalexamination());
            }
            if (dto.getIsConsultant() != null) {
                sql.append(" and is_consultant = :isConsultant ");
                hmapParams.put("isConsultant", dto.getIsConsultant());
            }
        }
    }

    private void filterByDegree(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        //search by degreeCode
        String degreeCodes = dto.getDegreeCodes();
        if (degreeCodes != null && !("".equals(degreeCodes))) {
            degreeCodes = degreeCodes.replaceAll("\\s{2,}", "").trim();
            List<String> degreeCodesList = Arrays.asList(degreeCodes.split(","));
            List<Integer> degreeIdsList = convertDegreeCodesToDegreeIds(degreeCodesList);
            sql.append(" and degree_id in :degreeIdsList ");
            hmapParams.put("degreeIdsList", degreeIdsList);
        }
    }

    private void filterByAcademicRank(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        //search by academicRank
        String academicRankCodes = dto.getAcademicRankCodes();
        if (academicRankCodes != null && !("".equals(academicRankCodes))) {
            academicRankCodes = academicRankCodes.replaceAll("\\s{2,}", "").trim();
            List<String> academicRankCodesList = Arrays.asList(academicRankCodes.split(","));
            List<Integer> academicRankIdsList = convertAcademicRankCodesToAcademicRankIds(academicRankCodesList);
            sql.append(" and academic_rank_id in :academicRankIdsList ");
            hmapParams.put("academicRankIdsList", academicRankIdsList);
        }
    }

    private void filterByProvinceOrDistrict(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        //search by province
        String provinceCodes = dto.getProvinceCodes();
        if (provinceCodes != null && !("".equals(provinceCodes))) {
            provinceCodes = provinceCodes.replaceAll("\\s{2,}", "").trim();
            List<String> provinceCodesList = Arrays.asList(provinceCodes.split(","));
            sql.append(" and (province_code in :provinceCodesList ");
            hmapParams.put("provinceCodesList", provinceCodesList);
        }

        //search by district
        String districtCodes = dto.getDistrictCodes();
        if (districtCodes != null && !("".equals(districtCodes))) {
            districtCodes = districtCodes.replaceAll("\\s{2,}", "").trim();
            List<String> districtCodesList = Arrays.asList(districtCodes.split(","));
            if (provinceCodes != null && !("".equals(provinceCodes))) {
                sql.append(" or");
            } else {
                sql.append(" and (");
            }
            sql.append(" district_code in :districtCodesList) ");
            hmapParams.put("districtCodesList", districtCodesList);
        } else if (provinceCodes != null && !("".equals(provinceCodes))) {
            sql.append(") ");
        }
    }

    private void filterByFullname(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        if (dto.getQueryString() != null && !"".equals(dto.getQueryString())) {
            sql.append(" and fullname like CONCAT('%', :queryString, '%') ");
            hmapParams.put("queryString", dto.getQueryString());
        }
    }

    private void filterByIsActive(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        if (dto.getIsActive() != null) {
            sql.append(" and is_active = :isActive ");
            hmapParams.put("isActive", dto.getIsActive());
        }
    }

    private void filterByEmail(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        if (dto.getEmail() != null && !"".equals(dto.getEmail())) {
            sql.append(" and email = :email ");
            hmapParams.put("email", dto.getEmail());
        }
    }

    private void filterByPhoneNumber(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        if (dto.getPhoneNumber() != null && !"".equals(dto.getPhoneNumber())) {
            sql.append(" and phone_number = :phoneNumber ");
            hmapParams.put("phoneNumber", dto.getPhoneNumber());
        }
    }

    private void filterByGender(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        if (dto.getGenderId() != null) {
            sql.append(" and gender_id = :genderId ");
            hmapParams.put("genderId", dto.getGenderId());
        }
    }

    private void filterByBirthYear(DoctorDTO dto, HashMap<String, Object> hmapParams, StringBuilder sql) {
        if (dto.getYear() != null) {
            sql.append(" and YEAR(birthday) = :year ");
            hmapParams.put("year", dto.getYear());
        }
    }

    private void filterByDoctorIds(HashMap<String, Object> hmapParams, List<Integer> doctorIdsValid, StringBuilder sql) {
        if (doctorIdsValid.size() > 0) {
            sql.append(" and doctor_id in :doctorIdsValid ");
            hmapParams.put("doctorIdsValid", doctorIdsValid);
        }
    }

    private List<DoctorIdDTO> getDoctorBySpecialistCodes(DoctorDTO dto, HashMap<String, Object> hmapParams) {
        StringBuilder sql;
        sql = new StringBuilder();
        sql.append("SELECT doctor_id AS doctorId FROM (SELECT specialist_id, `code` FROM cats_specialists WHERE is_active=1 AND is_delete=0 AND `code`=:specialistCode) cp " +
                " LEFT JOIN (SELECT doctor_id, specialist_id FROM doctors_specialists WHERE is_active=1 AND is_delete=0) ds" +
                " ON cp.specialist_id=ds.specialist_id ");
        hmapParams.put("specialistCode", dto.getSpecialistCode());
        List<DoctorIdDTO> doctorIdDTOs = (List<DoctorIdDTO>) getListData(sql, hmapParams, null, null, DoctorIdDTO.class);
        return doctorIdDTOs;
    }

    private List<String> getHealthFacilitiesCodesValid(TelecareUserEntity userSystemEntity) throws TeleCareException {
        List<String> healthFacilitiesCodesValid = new ArrayList<String>();
        if (userSystemEntity != null) {
            if (userSystemEntity.isDoctor()) {
                healthFacilitiesCodesValid = doctorsHealthfacilitiesRepositoryJPA.getHealthFacilitiesCodesByDoctorId(userSystemEntity.getTelecareUserId());
            } else if (userSystemEntity.isClinic()) {
                String healthfacilitiesCodes = sysUsersRepositoryJPA.getHealthfacilitiesCodes(userSystemEntity.getKeycloakUserId());
                if (healthfacilitiesCodes != null) {
                    healthFacilitiesCodesValid = new ArrayList<>(Arrays.asList(healthfacilitiesCodes.split(",")));
                }
            }
            if ((userSystemEntity.isDoctor() || userSystemEntity.isClinic()) && healthFacilitiesCodesValid.size() == 0) { // no satisfied data
                return null;
            }
        }
        return healthFacilitiesCodesValid;
    }

    private Integer setPageSize(DoctorDTO dto) {
        Integer pageSize = Constants.PAGE_SIZE_DEFAULT;
        if (dto.getPagesize() != null) {
            pageSize = dto.getPagesize();
        }
        return pageSize;
    }

    private Integer setStartRecord(DoctorDTO dto) {
        Integer start = Constants.START_RECORD_DEFAULT;
        if (dto.getStartrecord() != null) {
            start = dto.getStartrecord();
        }
        return start;
    }

    private void setDoctorHealthFacilities(List<Integer> doctorIdList, List<DoctorDTO> dataResult, DoctorDTO dto) {
        List<HealthFacilityDTO> healthFacilityDTOs = getHealthFacilitiesByDoctorIds(doctorIdList, dto);
        Map<Integer, List<HealthFacilityDTO>> hmapDoctorIdHealthFacilities = convertToHmapHealthFacilities(doctorIdList, healthFacilityDTOs);
        HashMap<Integer, List<DoctorPriceDTO>> hmapDoctorPrice = getDoctorPriceV2(doctorIdList);
        dataResult.forEach(data -> {
            List<HealthFacilityDTO> healthFacilityDTOS = hmapDoctorIdHealthFacilities.get(data.getDoctorId());
            if (!hmapDoctorPrice.isEmpty()) {
                //get price data by healthFacility
                List<DoctorPriceDTO> pricesByDoctor = hmapDoctorPrice.get(data.getDoctorId());
                if (pricesByDoctor != null && !pricesByDoctor.isEmpty()) {
                    HashMap<String, Integer> hmapPriceByHealthFacilities = new HashMap<>();
                    pricesByDoctor.forEach(priceByDoctor -> hmapPriceByHealthFacilities.put(priceByDoctor.getHealthfacilitiesCode() == null ? "0" : priceByDoctor.getHealthfacilitiesCode(), priceByDoctor.getMedicalExaminationFee())); //0: default cho tat ca co so y te (phase 0)

                    healthFacilityDTOS.forEach(healthFacilityDTO -> {
                        Integer priceByHealthFacility = hmapPriceByHealthFacilities.get(healthFacilityDTO.getHealthfacilitiesCode());
                        Integer defaultPrice = hmapPriceByHealthFacilities.get("0");
                        healthFacilityDTO.setMedicalExaminationFee(priceByHealthFacility != null ? priceByHealthFacility : defaultPrice);
                    });
                }
            }
            data.setHealthfacilities(healthFacilityDTOS);
        });
    }

    private void setDoctorSpecialists(List<Integer> doctorIdList, List<DoctorDTO> dataResult) {
        List<SpecialistDTO> specialistDTOs = getSpecialistsByDoctorIds(doctorIdList);
        Map<Integer, List<SpecialistDTO>> hmapDoctorIdSpecialists = convertToHmapSpecialists(doctorIdList, specialistDTOs);
        dataResult.forEach(data -> {
            data.setSpecialists(hmapDoctorIdSpecialists.get(data.getDoctorId()));
        });
    }

    private Map<Integer, List<HealthFacilityDTO>> convertToHmapHealthFacilities(List<Integer> doctorIdList, List<HealthFacilityDTO> healthFacilityDTOs) {
        Map<Integer, List<HealthFacilityDTO>> hmapDoctorIdHealthFacilities = doctorIdList.stream().collect(Collectors.toMap(Function.identity(), e -> new ArrayList<>()));
        healthFacilityDTOs.forEach(healthFacilityDTO -> {
            hmapDoctorIdHealthFacilities.get(healthFacilityDTO.getDoctorId()).add(healthFacilityDTO);
        });
        return hmapDoctorIdHealthFacilities;
    }

    private Map<Integer, List<SpecialistDTO>> convertToHmapSpecialists(List<Integer> doctorIdList, List<SpecialistDTO> specialistDTOs) {
        Map<Integer, List<SpecialistDTO>> hmapDoctorIdSpecialists = doctorIdList.stream().collect(Collectors.toMap(Function.identity(), e -> new ArrayList<>()));
        specialistDTOs.forEach(specialistDTO -> {
            hmapDoctorIdSpecialists.get(specialistDTO.getDoctorId()).add(specialistDTO);
        });
        return hmapDoctorIdSpecialists;
    }

    private List<Integer> convertDegreeCodesToDegreeIds(List<String> degreeCodesList) {
        List<CatsDegreeEntity> degreeEntities = catsDegreeRepositoryJPA.findAll();
        Map<String, Integer> mapDegree = degreeEntities.stream().collect(Collectors.toMap(CatsDegreeEntity::getCode, CatsDegreeEntity::getDegreeId));
        List<Integer> academicRankIdsList = degreeCodesList.stream().map(mapDegree::get).collect(Collectors.toList());
        return academicRankIdsList;
    }

    private List<Integer> convertAcademicRankCodesToAcademicRankIds(List<String> academicRankCodesList) {
        List<CatsAcademicRankEntity> academicRankEntities = catsAcademicRankRepositoryJPA.findAll();
        Map<String, Integer> mapAcademicRank = academicRankEntities.stream().collect(Collectors.toMap(CatsAcademicRankEntity::getCode, CatsAcademicRankEntity::getId));
        List<Integer> academicRankIdsList = academicRankCodesList.stream().map(mapAcademicRank::get).collect(Collectors.toList());
        return academicRankIdsList;
    }

    @Override
    public ResultSelectEntity getDoctorsExcel(DoctorDTO itemParamsEntity) {
        HashMap<String, Object> hmapParams = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT " +
                "result.*, " +
                "IF(result.certificationDate IS NULL,null,YEAR(NOW())-YEAR(result.certificationDate)) as experiences " +
                "FROM (" +
                "	select d.doctor_id as doctorId, d.birthday as birthday, " +
                "	d.description as description, d.certification_date as certificationDate, " +
                "	d.phone_number as phoneNumber, d.email as email, " +
                "	cats_province.province_code AS provinceCode, cats_province.name AS provinceName, " +
                "	cats_district.district_code AS districtCode, cats_district.name AS districtName, " +
                "	cats_ward.ward_code AS wardCode, cats_ward.name AS wardName, " +
                "	d.address as address, d.certification_code AS certificationCode, d.is_medicalexamination AS isMedicalexamination, " +
                "	d.is_consultant AS isConsultant, ds.specialist_id AS specialistId, " +
                "	d.is_active as isActive, d.fullname as fullname, " +
                "	d.avatar as avatar, d.gender_id as genderId, car.id as academicRankId, " +
                "	car.code as academicRankCode, car.name as academicRankName, " +
                "	cd.id as degreeId, cd.code as degreeCode, cd.name as degreeName, " +
                "	cs.code as specialistCode, cs.name as specialistName, dh.name as healthFacilitiesName, " +
                "	dh.healthfacilities_code as healthFacilitiesCode, cn.nationCode as nationCode, cn.nation_name as nationName, ce.ethnicityCode as ethnicityCode, ce.name as ethnicityName, cre.religionCode as religionCode, cre.name as religionName, d.summary as summary from doctors d " +
                "left join (" +
                "	select Id as id, code, name from cats_academic_rank where is_active = 1 and is_delete = 0" +
                ") car on car.id = d.academic_rank_id " +

                "left join (" +
                "	select nation_code as nationCode, nation_name from cats_nations where is_active = 1 and is_delete = 0" +
                ") cn on cn.nationCode = d.nation_code " +

                "left join (" +
                "	select ethnicity_code as ethnicityCode, name from cats_ethnicities where is_active = 1 and is_delete = 0" +
                ") ce on ce.ethnicityCode = d.ethnicity_code " +

                "left join (" +
                "	select religion_code as religionCode, name from cats_religions where is_active = 1 and is_delete = 0" +
                ") cre on cre.religionCode = d.religion_code " +

                "left join (" +
                "	select degree_id as id, code, name from cats_degree where is_active = 1 and is_delete = 0" +
                ") cd on cd.id = d.degree_id " +
                "left join (" +
                "	select Id as id, doctor_id, specialist_id from doctors_specialists  where is_active = 1 and is_delete = 0" +
                ") ds on ds.doctor_id = d.doctor_id " +
                "left join (" +
                "	select sd.doctor_id, sd.healthfacilities_code, sh.name from doctors_healthfacilities sd " +
                "	left join (" +
                "	select healthfacilities_code, name from cats_healthfacilities) sh on sh.healthfacilities_code = sd.healthfacilities_code" +
                ") dh on dh.doctor_id = d.doctor_id " +
                "left join (" +
                "	select specialist_id, code, name from cats_specialists where is_active = 1 and is_delete = 0" +
                ") cs on cs.specialist_id = ds.specialist_id" +
                " LEFT JOIN (SELECT province_code, name FROM cats_provinces WHERE is_active = 1 AND is_delete = 0) cats_province ON cats_province.province_code = d.province_code " +
                " LEFT JOIN ( SELECT district_code, name FROM cats_districts WHERE is_active = 1 AND is_delete = 0) cats_district ON cats_district.district_code = d.district_code " +
                " LEFT JOIN (SELECT ward_code, name FROM cats_wards WHERE is_active = 1 AND is_delete = 0) cats_ward ON cats_ward.ward_code = d.ward_code " +
                "  WHERE d.is_delete = 0 AND d.is_active = 1 " +
                " ) result  where 1 = 1 ");
        if (itemParamsEntity != null && itemParamsEntity.getSpecialistCode() != null) {
            sql.append("and result.specialistCode = :specialistCode ");
            hmapParams.put("specialistCode", itemParamsEntity.getSpecialistCode());
        }

        if (itemParamsEntity != null && itemParamsEntity.getHealthfacilitiesCode() != null) {
            sql.append("and healthfacilitiesCode = :healthfacilitiesCode ");
            hmapParams.put("healthfacilitiesCode", itemParamsEntity.getHealthfacilitiesCode());
        }

        if (itemParamsEntity.getYear() != null) {
            sql.append("and YEAR(result.birthday) = :year ");
            hmapParams.put("year", itemParamsEntity.getYear());
        }

        if (itemParamsEntity.getGenderId() != null) {
            sql.append("and result.genderId = :genderId ");
            hmapParams.put("genderId", itemParamsEntity.getGenderId());
        }

        if (itemParamsEntity.getPhoneNumber() != null) {
            sql.append("and result.phoneNumber = :phoneNumber ");
            hmapParams.put("phoneNumber", itemParamsEntity.getPhoneNumber());
        }

        if (itemParamsEntity.getEmail() != null) {
            sql.append("and result.email = :email ");
            hmapParams.put("email", itemParamsEntity.getEmail());
        }

        if (itemParamsEntity.getIsActive() != null) {
            sql.append("and result.isActive = :isActive ");
            hmapParams.put("isActive", itemParamsEntity.getIsActive());
        }

        if (itemParamsEntity != null && itemParamsEntity.getQueryString() != null) {
            sql.append("and result.fullname like CONCAT('%', :queryString, '%') ");
            hmapParams.put("queryString", itemParamsEntity.getQueryString());
        }

        if (itemParamsEntity != null && itemParamsEntity.getIsMedicalexamination() != null) {
            sql.append(" and result.isMedicalexamination = :isMedicalexamination ");
            hmapParams.put("isMedicalexamination", itemParamsEntity.getIsMedicalexamination());
        }

        if (itemParamsEntity != null && itemParamsEntity.getIsConsultant() != null) {
            sql.append(" and result.isConsultant = :isConsultant ");
            hmapParams.put("isConsultant", itemParamsEntity.getIsConsultant());
        }
        sql.append("GROUP BY result.doctorId order by fullName");
        Integer start = Constants.START_RECORD_DEFAULT;
        if (itemParamsEntity != null && itemParamsEntity.getStartrecord() != null) {
            start = itemParamsEntity.getStartrecord();
        }
        Integer pageSize = Constants.PAGE_SIZE_DEFAULT;
        if (itemParamsEntity != null && itemParamsEntity.getPagesize() != null) {
            pageSize = itemParamsEntity.getPagesize();
        }
        ResultSelectEntity resultData = getListDataAndCount(sql, hmapParams, start, pageSize, DoctorDTO.class);

        List<Integer> doctorIds = resultData.getListData().stream()
                .map(l -> ((DoctorDTO) l).getDoctorId())
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, Integer> doctorPrice = getDoctorPrice(doctorIds);

        for (Object listDatum : resultData.getListData()) {
            DoctorDTO data = (DoctorDTO) listDatum;
            data.setMedicalExaminationFee(doctorPrice.get(data.getDoctorId()));
        }

        return resultData;
    }

    /**
     * getPrice
     *
     * @param doctorIds
     * @return
     */
    private Map<Integer, Integer> getDoctorPrice(List<Integer> doctorIds) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isDelete", Constants.IS_NOT_DELETE);
        hashMap.put("isActive", Constants.IS_ACTIVE);

        StringBuilder sqlDoctorPrice = new StringBuilder();
        String arrString = Arrays.toString(doctorIds.toArray()).replace("[", "").replace("]", "");
        sqlDoctorPrice
                .append("SELECT " +
                        "dp.doctor_id as doctorId, " +
                        "dp.medical_examination_fee as medicalExaminationFee " +
                        "FROM cats_phases cp " +
                        "INNER JOIN (" +
                        "	SELECT is_delete, is_active, doctor_id, medical_examination_fee, phase_id FROM doctors_prices ")
                .append(!arrString.isEmpty() ? "WHERE doctor_id IN (" + arrString + ") AND is_delete=:isDelete AND is_active=:isActive " : "")
                .append(") dp ON dp.phase_id=cp.phase_id " +
                        "WHERE cp.is_active =:isActive AND cp.is_delete =:isDelete AND cp.from_date <= CURRENT_DATE() AND CURRENT_DATE() <= cp.to_date ORDER BY cp.phase_id");
        List<DoctorPriceDTO> doctorPriceDTOS = (List<DoctorPriceDTO>) getListData(sqlDoctorPrice, hashMap, 0, Integer.MAX_VALUE, DoctorPriceDTO.class);

        StringBuilder sqlDefaultPrice = new StringBuilder();
        sqlDefaultPrice.append("" +
                "select " +
                "  medical_examination_fee as medicalExaminationFee, doctor_id as doctorId " +
                "from " +
                "  doctors_prices " +
                "where " +
                "  phase_id = 0 " +
                "  and is_active = :isActive " +
                "  and is_delete = :isDelete " +
                "  and doctor_id in (" + arrString + ") group by doctor_id");
        List<DoctorPriceDTO> defaultPrice = (List<DoctorPriceDTO>) getListData(sqlDefaultPrice, hashMap, 0, Integer.MAX_VALUE, DoctorPriceDTO.class);

        Map<Integer, Integer> result = new HashMap<>();
        for (DoctorPriceDTO doctorPriceDTO : doctorPriceDTOS) {
            if (doctorPriceDTO.getDoctorId() != null && doctorPriceDTO.getMedicalExaminationFee() != null) {
                result.put(doctorPriceDTO.getDoctorId(), doctorPriceDTO.getMedicalExaminationFee());
            }
        }

        if (defaultPrice != null) {
            Map<Integer, Integer> mapDefaultPrice = defaultPrice.stream().collect(Collectors.toMap(DoctorPriceDTO::getDoctorId, DoctorPriceDTO::getMedicalExaminationFee));

            for (Integer doctorId : doctorIds) {
                if (!result.containsKey(doctorId)) {
                    result.put(doctorId, mapDefaultPrice.get(doctorId));
                }
            }
        }

        return result;
    }

    private HashMap<Integer, List<DoctorPriceDTO>> getDoctorPriceV2(List<Integer> doctorIds) {
        HashMap<String, Object> hmapParams = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " +
                " (SELECT doctor_id AS doctorId, medical_examination_fee as medicalExaminationFee, phase_id AS phaseId FROM doctors_prices WHERE medical_examination_number=1 AND is_active=1 AND is_delete=0 AND doctor_id IN :doctorIds) dp ");
        sql.append(" LEFT JOIN (SELECT phase_id, healthfacilities_code AS healthfacilitiesCode FROM cats_phases WHERE from_date <= CURRENT_DATE() AND CURRENT_DATE() <= to_date AND is_active = 1 AND is_delete = 0) p ON dp.phaseId = p.phase_id ");
        sql.append(" WHERE healthfacilitiesCode IS NOT NULL OR phaseId =0");
        hmapParams.put("doctorIds", doctorIds);
        List<DoctorPriceDTO> prices = (List<DoctorPriceDTO>) getListData(sql, hmapParams, null, null, DoctorPriceDTO.class);
        Set<DoctorPriceDTO> setPrices = new HashSet<>(prices);
        HashMap<Integer, List<DoctorPriceDTO>> hmapDoctorPrices = new HashMap<>();
        setPrices.forEach(price -> {
            Integer doctorId = price.getDoctorId();
            List<DoctorPriceDTO> listPriceByDoctor = hmapDoctorPrices.containsKey(doctorId) ? hmapDoctorPrices.get(doctorId) : new ArrayList<>();
            listPriceByDoctor.add(price);
            hmapDoctorPrices.put(doctorId, listPriceByDoctor);
        });
        return hmapDoctorPrices;
    }

    public HashMap<Integer, Integer> getExaminationFee(Integer doctorId, String healthFacilityCode) {
        List<DoctorPriceDTO> priceData = getDoctorPrice(doctorId, healthFacilityCode);
        HashMap<Integer, Integer> hmapPrice = new HashMap<>();
        priceData.forEach(price -> hmapPrice.put(price.getMedicalExaminationNumber(), price.getMedicalExaminationFee()));
        return hmapPrice;
    }

    public List<DoctorPriceDTO> getDoctorPrice(Integer doctorId, String healthfacilitiesCode) {
        HashMap<String, Object> hmapParams = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dp.*, cp.healthfacilities_code as healthfacilitiesCode FROM " +
                " (SELECT price_id as priceId, phase_id AS phaseId," +
                " medical_examination_fee AS medicalExaminationFee, medical_examination_number AS medicalExaminationNumber, " +
                " consultant_fee_videocall AS consultantFeeVideocall,consultant_time_videocall AS consultantTimeVideocall, " +
                " consultant_fee_call AS consultantFeeCall, consultant_time_call AS consultantTimeCall," +
                " consultant_fee_chat AS consultantFeeChat, consultant_chat_number AS consultantChatNumber " +
                " FROM doctors_prices WHERE is_active=1 AND is_delete=0  ");
        if (doctorId != null) {
            sql.append(" AND doctor_id=:doctorId ");
            hmapParams.put("doctorId", doctorId);
        }
        sql.append(") dp");
        sql.append(" LEFT JOIN (SELECT phase_id, healthfacilities_code FROM cats_phases WHERE is_active=1 AND is_delete=0 AND from_date<= CURDATE() AND CURDATE()<=to_date ");
        if (!Strings.isNullOrEmpty(healthfacilitiesCode)) {
            sql.append(" and healthfacilities_code =:healthFacilityCode ");
            hmapParams.put("healthFacilityCode", healthfacilitiesCode);
        }
        sql.append(") cp ON cp.phase_id=dp.phaseId");
        sql.append(" WHERE cp.phase_id!=0 OR dp.phaseId=0 ORDER BY dp.phaseId DESC ");

        List<DoctorPriceDTO> listData = (List<DoctorPriceDTO>) getListData(sql, hmapParams, null, null, DoctorPriceDTO.class);
        List<DoctorPriceDTO> defaultPrice = listData.stream().filter(price -> price.getPhaseId() == 0).collect(Collectors.toList());
        List<DoctorPriceDTO> phasePrice = listData.stream().filter(price -> price.getPhaseId() != 0).collect(Collectors.toList());
        listData.clear();
        listData = phasePrice.size() > 0 ? phasePrice : defaultPrice;
        return listData;
    }

    /**
     * @param itemParamsEntity: params client truyen len
     * @return
     */
    @Override
    public Object getDoctorInfo(DoctorDTO itemParamsEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
                "result.*, " +
                "IF(result.certificationDate IS NULL,null,YEAR(NOW())-YEAR(result.certificationDate)) as experiences " +
                "FROM (" +
                "   select d.doctor_id as doctorId, d.fullname as fullName, d.birthday as birthday, d.gender_id as genderId, d.nation_code as nationCode, d.ethnicity_code as ethnicityCode, d.religion_code as religionCode, d.summary as summary, cre.name as religionName, ce.name as ethnicityName, cn.nation_name as nationName,   " +
                "   d.phone_number as phoneNumber, d.email as email, d.is_active as isActive, " +
                "   cats_province.province_code AS provinceCode, cats_province.name AS provinceName, " +
                "   cats_district.district_code AS districtCode, cats_district.name AS districtName, " +
                "   cats_ward.ward_code AS wardCode, cats_ward.name AS wardName, " +
                "   d.address as address, d.certification_code AS certificationCode, d.is_medicalexamination AS isMedicalexamination, " +
                "   d.is_consultant AS isConsultant, ds.specialist_id AS specialistId, " +
                "   d.certification_date as certificationDate, d.description as description, " +
                "   d.avatar as avatar, d.position_code as positionCode, " +
                "   car.id as academicRankId, car.code as academicRankCode, " +
                "   car.name as academicRankName, cd.id as degreeId, " +
                "   cd.code as degreeCode, cd.name as degreeName, " +
                "   cs.name as specialistName, ch.name as healthFacilitiesName, ch.healthfacilities_code as healthFacilitiesCode, " +
                "   cs.code as specialistCode, " +
                "   doc_com.totalComments, " +
                "   doc_com.stars " +
                "from (select * from doctors where doctor_id = :doctorId) d " +
                "left join (" +
                "   select healthfacilities_code, doctor_id from doctors_healthfacilities" +
                ") dh on dh.doctor_id = d.doctor_id " +
                "left join (" +
                "   select healthfacilities_code, name from cats_healthfacilities" +
                ") ch on ch.healthfacilities_code = dh.healthfacilities_code " +
                "left join (" +
                "   select Id as id, code, name from cats_academic_rank where is_active = 1 and is_delete = 0" +
                ") car on car.id = d.academic_rank_id " +
                "left join (" +
                "   select degree_id as id, code, name from cats_degree where is_active = 1 and is_delete = 0" +
                ") cd on cd.id = d.degree_id " +
                "left join (" +
                "   select Id as id, doctor_id, specialist_id from doctors_specialists  where is_active = 1 and is_delete = 0" +
                ") ds on ds.doctor_id = d.doctor_id " +

                "left join (" +
                "	select nation_code as nationCode, nation_name from cats_nations where is_active = 1 and is_delete = 0" +
                ") cn on cn.nationCode = d.nation_code " +

                "left join (" +
                "	select ethnicity_code as ethnicityCode, name from cats_ethnicities where is_active = 1 and is_delete = 0" +
                ") ce on ce.ethnicityCode = d.ethnicity_code " +

                "left join (" +
                "	select religion_code as religionCode, name from cats_religions where is_active = 1 and is_delete = 0" +
                ") cre on cre.religionCode = d.religion_code " +

                "left join (" +
                "   select specialist_id, code, name from cats_specialists where is_active = 1 and is_delete = 0" +
                ") cs on cs.specialist_id = ds.specialist_id " +
                " LEFT JOIN ( SELECT province_code, name FROM cats_provinces WHERE is_active = 1 AND is_delete = 0) cats_province ON cats_province.province_code = d.province_code " +
                " LEFT JOIN ( SELECT district_code, name FROM cats_districts WHERE is_active = 1 AND is_delete = 0) cats_district ON cats_district.district_code = d.district_code " +
                " LEFT JOIN ( SELECT ward_code, name FROM cats_wards WHERE is_active = 1 AND is_delete = 0) cats_ward ON cats_ward.ward_code = d.ward_code " +
                " LEFT JOIN(" +
                "  SELECT COUNT(*) as totalComments " +
                "	,AVG(point_evaluation) AS stars " +
                "   , doctor_id " +
                "   FROM doctors_comments " +
                "   WHERE is_active = 1 " +
                "   AND is_delete = 0 " +
                "   GROUP BY doctor_id) doc_com " +
                "   ON doc_com.doctor_id = d.doctor_id    " +
                " ) result GROUP BY result.doctorId");
        HashMap<String, Object> hmapParams = new HashMap<>();
        hmapParams.put("doctorId", itemParamsEntity.getDoctorId());

        DoctorDTO doctorDTO = (DoctorDTO) getFirstData(sql, hmapParams, DoctorDTO.class);
        if (doctorDTO != null) {
//            Map<Integer, Integer> mapPrice = getDoctorPrice(Arrays.asList(itemParamsEntity.getDoctorId()));
//            doctorDTO.setMedicalExaminationFee(mapPrice.get(itemParamsEntity.getDoctorId()));
            doctorDTO.setSpecialists(getSpecialistsByDoctorId(itemParamsEntity.getDoctorId()));
            List<HealthFacilityDTO> healthFacilityDTOS = getHealthFacilitiesByDoctorId(itemParamsEntity.getDoctorId(), itemParamsEntity);
            healthFacilityDTOS.forEach(healthFacility -> {
                HashMap<Integer, Integer> hmapPrice = getExaminationFee(itemParamsEntity.getDoctorId(), healthFacility.getHealthfacilitiesCode());
                healthFacility.setMedicalExaminationFee(hmapPrice.get(1));
            });
            doctorDTO.setHealthfacilities(healthFacilityDTOS);
        }
        return doctorDTO;
    }

    /**
     * @param itemParamsEntity: params client truyen len
     * @return
     */
    @Override
    public Object getDoctorByKeycloakUserId(DoctorDTO itemParamsEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT result.doctorId, result.fullName, result.phoneNumber, result.description, result.keycloakUserId, result.avatar, result.positionCode, result.academicRankId, result.academicRankCode, result.academicRankName, result.degreeId, result.degreeCode, result.degreeName, IF(result.certificationDate IS NULL,null,YEAR(NOW())-YEAR(result.certificationDate)) as experiences, GROUP_CONCAT(distinct specialist_id) as specialistCode, GROUP_CONCAT(distinct specialistName) as specialistName, GROUP_CONCAT(distinct result.healthfacilities_code) as healthFacilitiesCode FROM (select d.doctor_id as doctorId, d.fullname as fullName, d.phone_number as phoneNumber, d.certification_date as certificationDate, d.description as description, d.avatar as avatar, d.keycloak_user_id as keycloakUserId, d.position_code as positionCode, car.id as academicRankId, car.code as academicRankCode, car.name as academicRankName, cd.id as degreeId, cd.code as degreeCode, cd.name as degreeName, cs.name as specialistName, dh.healthfacilities_code, cs.specialist_id from (select * from doctors where keycloak_user_id = :keycloakUserId) d left join (select healthfacilities_code, doctor_id from doctors_healthfacilities where is_active=1 and is_delete=0) dh on dh.doctor_id = d.doctor_id left join (select Id as id, code, name from cats_academic_rank where is_active = 1 and is_delete = 0) car on car.id = d.academic_rank_id left join (select degree_id as id, code, name from cats_degree where is_active = 1 and is_delete = 0) cd on cd.id = d.degree_id left join (select Id as id, doctor_id, specialist_id from doctors_specialists  where is_active = 1 and is_delete = 0) ds on ds.doctor_id = d.doctor_id left join (select specialist_id, name from cats_specialists where is_active = 1 and is_delete = 0) cs on cs.specialist_id = ds.specialist_id ) result GROUP BY result.doctorId");
        HashMap<String, Object> hmapParams = new HashMap<>();
        hmapParams.put("keycloakUserId", itemParamsEntity.getKeycloakUserId());
        return getFirstData(sql, hmapParams, DoctorDTO.class);

    }

    @Override
    public DoctorHomepageWebDTO getPatients(Integer doctorId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(bi.patient_id) AS patients FROM (" +
                " SELECT DISTINCT patient_id FROM booking_informations WHERE is_active=1 AND is_delete=0 AND doctor_id=:doctorId) bi" +
                " INNER JOIN (SELECT patient_id FROM patients WHERE is_active=1 AND is_delete=0) p" +
                " ON bi.patient_id=p.patient_id");
        HashMap<String, Object> hmapParams = new HashMap<>();
        hmapParams.put("doctorId", doctorId);
        return (DoctorHomepageWebDTO) getFirstData(sql, hmapParams, DoctorHomepageWebDTO.class);
    }


    /**
     * @param itemParamsEntity: params client truyen len
     * @return
     */
    @Override
    public ResultSelectEntity getTopDoctors(DoctorDTO itemParamsEntity) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> hmapParams = new HashMap<>();

        sql.append(" SELECT d.doctor_id AS doctorId, d.avatar, d.doctor_code AS doctorCode, fullname, bi.totalBooking, dc.average, dc.totalComments, car.academicRankCode, car.academicRankName, ");
        sql.append(" cd.degreeCode, cd.degreeName, cp.* ");
        sql.append(" FROM ( ");
        sql.append(" SELECT doctor_id, fullname, avatar, doctor_code, academic_rank_id, degree_id, province_code ");
        sql.append(" FROM doctors WHERE is_active = 1 and is_delete = 0 AND doctor_id > 0) d ");
        sql.append(" LEFT JOIN ( ");
        sql.append(" SELECT doctor_id, COUNT(booking_id) AS totalBooking ");
        sql.append(" FROM booking_informations where is_active = 1 and is_delete = 0 ");
        sql.append(" GROUP BY doctor_id ");
        sql.append(" ORDER BY totalBooking  DESC) bi ON d.doctor_id = bi.doctor_id ");
        sql.append(" LEFT JOIN (select doctor_id, AVG(point_evaluation) as average, COUNT(comments) AS totalComments from doctors_comments where is_active = 1 and is_delete = 0 group by doctor_id) dc on d.doctor_id = dc.doctor_id ");
        sql.append(" LEFT JOIN (SELECT id, CODE AS academicRankCode, NAME AS academicRankName FROM cats_academic_rank WHERE is_active = 1 AND is_delete = 0) car ON car.id = d.academic_rank_id ");
        sql.append(" LEFT JOIN (SELECT degree_id, CODE AS degreeCode, NAME AS degreeName FROM cats_degree WHERE is_active = 1 AND is_delete = 0) cd ON cd.degree_id = d.degree_id ");
        sql.append(" LEFT JOIN (SELECT province_code AS provinceCode, NAME AS provinceName FROM cats_provinces WHERE is_active = 1 AND is_delete = 0) cp ON cp.provinceCode = d.province_code ");
        sql.append(" LIMIT :limit ");

        if (itemParamsEntity.getLimit() == null) {
            hmapParams.put("limit", Constants.LIMIT_DEFAULT);
        } else {
            hmapParams.put("limit", itemParamsEntity.getLimit());
        }

        Integer start = null;
        if (itemParamsEntity != null && itemParamsEntity.getStartrecord() != null) {
            start = itemParamsEntity.getStartrecord();
        }
        Integer pageSize = null;
        if (itemParamsEntity != null && itemParamsEntity.getPagesize() != null) {
            pageSize = itemParamsEntity.getPagesize();
        }
        ResultSelectEntity resultData = getListDataAndCount(sql, hmapParams, start, pageSize, DoctorDTO.class);
        for (Object element : resultData.getListData()) {
            DoctorDTO data = (DoctorDTO) element;
            data.setSpecialists(getSpecialistsByDoctorId(data.getDoctorId()));
            data.setHealthfacilities(getHealthFacilitiesByDoctorId(data.getDoctorId(), itemParamsEntity));
        }
        return resultData;
    }


    public List<SpecialistDTO> getSpecialistsByDoctorId(Integer doctorId) {
        // get specialists
        StringBuilder sqlSpecialists = new StringBuilder();
        HashMap<String, Object> hmapParamsSpecialists = new HashMap<>();
        sqlSpecialists.append("select cs.* from doctors_specialists ds join (select specialist_id as specialistId , name as specialistName, code as specialistCode from cats_specialists ");
        sqlSpecialists.append("where is_active = 1 and is_delete = 0) cs on cs.specialistId = ds.specialist_id ");
        sqlSpecialists.append("where ds.doctor_id = :doctorId ");
        sqlSpecialists.append("and is_active = 1 and is_delete = 0 group by cs.specialistId order by cs.specialistId");
        hmapParamsSpecialists.put("doctorId", doctorId);
        List<SpecialistDTO> specialists = (List<SpecialistDTO>) getListData(sqlSpecialists, hmapParamsSpecialists, null, null, SpecialistDTO.class);
        return specialists;
    }

    public List<SpecialistDTO> getSpecialistsByDoctorIds(List<Integer> doctorIds) {
        // get specialists
        StringBuilder sqlSpecialists = new StringBuilder();
        HashMap<String, Object> hmapParamsSpecialists = new HashMap<>();
        sqlSpecialists.append(" SELECT ds.doctor_id as doctorId, ds.specialist_id as specialistId, cs.name as specialistName, cs.code as specialistCode" +
                " FROM (SELECT doctor_id, specialist_id FROM doctors_specialists WHERE is_active=1 AND is_delete=0 AND doctor_id IN :doctorIds) ds " +
                " INNER JOIN (SELECT specialist_id, `code`, `name` FROM cats_specialists WHERE is_active=1 AND is_delete=0) cs ON ds.specialist_id = cs.specialist_id ");
        hmapParamsSpecialists.put("doctorIds", doctorIds);
        List<SpecialistDTO> specialists = (List<SpecialistDTO>) getListData(sqlSpecialists, hmapParamsSpecialists, null, null, SpecialistDTO.class);
        return specialists;
    }

    public List<HealthFacilityDTO> getHealthFacilitiesByDoctorId(Integer doctorId, DoctorDTO dto) {
        // get specialists
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlHealthfacilities = new StringBuilder();
        HashMap<String, Object> hmapParams = new HashMap<>();

        sqlHealthfacilities.append("select healthfacilities_code as healthfacilitiesCode , name as healthfacilitiesName from cats_healthfacilities where is_active = 1 and is_delete = 0 ");
        if (dto != null) {
            if (dto.getAllowBooking() != null) {
                sqlHealthfacilities.append("and allow_booking =:allowBooking ");
                hmapParams.put("allowBooking", dto.getAllowBooking());
            }
            if (dto.getAllowFilter() != null) {
                sqlHealthfacilities.append("and allow_filter =:allowFilter ");
                hmapParams.put("allowFilter", dto.getAllowFilter());
            }
            if (dto.getAllowSearch() != null) {
                sqlHealthfacilities.append("and allow_search =:allowSearch ");
                hmapParams.put("allowSearch", dto.getAllowSearch());
            }
        }

        sql.append("select ch.* from doctors_healthfacilities dh ");
        sql.append("join (");
        sql.append(sqlHealthfacilities);
        sql.append(") ch on ch.healthfacilitiesCode = dh.healthfacilities_code ");
        sql.append("where dh.doctor_id = :doctorId and is_active = 1 and is_delete = 0 ");
        sql.append("group by ch.healthfacilitiesCode order by ch.healthfacilitiesCode");
        hmapParams.put("doctorId", doctorId);
        List<HealthFacilityDTO> healthFacilities = (List<HealthFacilityDTO>) getListData(sql, hmapParams, null, null, HealthFacilityDTO.class);
        return healthFacilities;
    }

    public List<HealthFacilityDTO> getHealthFacilitiesByDoctorIds(List<Integer> doctorIds, DoctorDTO dto) {
        // get specialists
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlHealthfacilities = new StringBuilder();
        HashMap<String, Object> hmapParams = new HashMap<>();
        sqlHealthfacilities.append("SELECT healthfacilities_code, name FROM cats_healthfacilities WHERE is_active=1 AND is_delete=0 ");
        if (dto != null) {
            if (dto.getAllowBooking() != null) {
                sqlHealthfacilities.append("AND allow_booking =:allowBooking ");
                hmapParams.put("allowBooking", dto.getAllowBooking());
            }
            if (dto.getAllowFilter() != null) {
                sqlHealthfacilities.append("AND allow_filter =:allowFilter ");
                hmapParams.put("allowFilter", dto.getAllowFilter());
            }
            if (dto.getAllowSearch() != null) {
                sqlHealthfacilities.append("AND allow_search =:allowSearch ");
                hmapParams.put("allowSearch", dto.getAllowSearch());
            }
        }

        sql.append(" SELECT dh.doctor_id as doctorId, dh.healthfacilities_code as healthfacilitiesCode, ch.name as healthfacilitiesName " +
                " FROM (SELECT doctor_id, healthfacilities_code FROM doctors_healthfacilities WHERE is_active=1 AND is_delete=0 AND doctor_id IN :doctorIds) dh " +
                " INNER JOIN (");
        sql.append(sqlHealthfacilities);
        sql.append(") ch ON dh.healthfacilities_code=ch.healthfacilities_code ");
        hmapParams.put("doctorIds", doctorIds);
        List<HealthFacilityDTO> healthFacilities = (List<HealthFacilityDTO>) getListData(sql, hmapParams, null, null, HealthFacilityDTO.class);
        return healthFacilities;
    }
}
