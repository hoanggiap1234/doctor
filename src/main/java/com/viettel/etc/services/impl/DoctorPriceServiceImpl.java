package com.viettel.etc.services.impl;

import com.viettel.etc.dto.DoctorPriceDTO;
import com.viettel.etc.dto.MessageDTO;
import com.viettel.etc.repositories.DoctorPriceRepository;
import com.viettel.etc.repositories.tables.DoctorsHealthfacilitiesRepositoryJPA;
import com.viettel.etc.repositories.tables.DoctorsPricesRepositoryJPA;
import com.viettel.etc.repositories.tables.SysUsersRepositoryJPA;
import com.viettel.etc.repositories.tables.entities.DoctorsPricesEntity;
import com.viettel.etc.services.DoctorPriceService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.utils.*;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 07 14:45:01 ICT 2020
 */
@Service
public class DoctorPriceServiceImpl implements DoctorPriceService {

    private static final Logger LOGGER = Logger.getLogger(DoctorPriceServiceImpl.class);
    @Autowired
    SysUsersRepositoryJPA sysUsersRepositoryJPA;
    @Autowired
    DoctorsHealthfacilitiesRepositoryJPA doctorsHealthfacilitiesRepositoryJPA;
    @Autowired
    private DoctorPriceRepository doctorPriceRepository;
    @Autowired
    private DoctorsPricesRepositoryJPA doctorsPricesRepositoryJPA;
    @Autowired
    private MessageService messageService;

    /**
     * lay danh sach gia kham/ tu van cua bac si theo moc thoi gian
     *
     * @param itemParamsEntity params client
     * @return
     */
    @Override
    public Object getDoctorPrices(DoctorPriceDTO itemParamsEntity, Authentication authentication) throws TeleCareException {
        TelecareUserEntity userEntity = FnCommon.getTelecareUserInfo(authentication);
        if (userEntity.isPatient()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
        }

        ResultSelectEntity dataResult = doctorPriceRepository.getDoctorPrices(itemParamsEntity, userEntity);
        List<DoctorPriceDTO> doctorPriceDTOList = (List<DoctorPriceDTO>) dataResult.getListData();
        for (DoctorPriceDTO element : doctorPriceDTOList) {
            Integer consultantTimeCall = element.getConsultantTimeCall();
            Integer consultantTimeVideoCall = element.getConsultantTimeVideocall();
            if (consultantTimeCall != null) {
                double multiTimeCall = consultantTimeCall / Constants.TIME_CONSULTANT_MIN;
                element.setMultiTimeCall(multiTimeCall);
                element.setConsultantTimeCall((int) Constants.TIME_CONSULTANT_MIN);
                double consultantFeeCall = element.getConsultantFeeCall() != null ? element.getConsultantFeeCall() : 0.0;
                element.setConsultantFeeCall(consultantFeeCall / multiTimeCall);
            }
            if (consultantTimeVideoCall != null) {
                double multiTimeVideoCall = consultantTimeVideoCall / Constants.TIME_CONSULTANT_MIN;
                element.setMultiTimeVideoCall(multiTimeVideoCall);
                element.setConsultantTimeVideocall((int) Constants.TIME_CONSULTANT_MIN);
                double consultantFeeVideoCall = element.getConsultantFeeVideocall() != null ? element.getConsultantFeeVideocall() : 0.0;
                element.setConsultantFeeVideocall(consultantFeeVideoCall / multiTimeVideoCall);
            }
        }
        return dataResult;
    }

    /**
     * create sach gia kham, tu van cua bac si
     *
     * @param dtos           params client
     * @param authentication Authentication client
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void createDoctorPrices(List<DoctorPriceDTO> dtos, int phaseId, Authentication authentication, String language) throws TeleCareException {
        TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
        if (loginUser == null || loginUser.isPatient()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
        } else if (loginUser.isDoctor()) {
            if (dtos.stream().anyMatch(d -> !d.getDoctorId().equals(loginUser.getTelecareUserId()))) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
            }
        } else if (loginUser.isClinic()) {
            List<String> healthFacilityCode = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
            List<Integer> doctorIds = doctorsHealthfacilitiesRepositoryJPA.getDoctorIdsByHealthficility(healthFacilityCode);
            if (dtos.stream().anyMatch(d -> !doctorIds.contains(d.getDoctorId()))) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
            }
        }

        List<Integer> doctorIds = dtos.stream().map(DoctorPriceDTO::getDoctorId).collect(Collectors.toList());
        List<DoctorsPricesEntity> doctorsPrices = doctorsPricesRepositoryJPA.findByDoctorIdInAndPhaseIdAndIsDeleteAndIsActive(doctorIds, phaseId, false, true);
//        Map<Integer, DoctorsPricesEntity> mapPrice = doctorsPrices.stream().collect(Collectors.toMap(DoctorsPricesEntity::getDoctorId, Function.identity()));


        List<DoctorsPricesEntity> doctorsPricesEntities = new ArrayList<>();
        for (DoctorPriceDTO dto : dtos) {
            // check time consultant valid
            if (!Constants.LIST_TIME_CONSULTANT.contains(dto.getConsultantTimeCall())
                    || !Constants.LIST_TIME_CONSULTANT.contains(dto.getConsultantTimeVideocall())) {
                throw new TeleCareException(messageService.getMessage(Constants.TIME_CONSULTANT_INVALID, language), ErrorApp.TIME_CONSULTANT_INVALID);
            }

            //validate params
            Integer examNumber = dto.getMedicalExaminationNumber();
            Integer examFee = dto.getMedicalExaminationFee();
            Integer videoCallTime = dto.getConsultantTimeVideocall();
            Double videoCallFee = dto.getConsultantFeeVideocall();
            Integer callTime = dto.getConsultantTimeCall();
            Double callFee = dto.getConsultantFeeCall();
            Integer chatNumber = dto.getConsultantChatNumber();
            Double chatFee = dto.getConsultantFeeChat();
            if (!(FnCommon.areAllNull(examFee, examNumber) || FnCommon.areAllNotNull(examFee, examNumber))) {
                throw new TeleCareException(messageService.getMessage(Constants.ERROR_EXAM_FEE_INVALID, language));
            } else if (!(FnCommon.areAllNull(videoCallTime, videoCallTime) || FnCommon.areAllNotNull(videoCallTime, videoCallTime))) {
                throw new TeleCareException(messageService.getMessage(Constants.ERROR_VIDEO_CALL_FEE_INVALID, language));
            } else if (!(FnCommon.areAllNull(callTime, callFee) || FnCommon.areAllNotNull(callTime, callFee))) {
                throw new TeleCareException(messageService.getMessage(Constants.ERROR_CALL_FEE_INVALID, language));
            } else if (!(FnCommon.areAllNull(chatNumber, chatFee) || FnCommon.areAllNotNull(chatNumber, chatFee))) {
                throw new TeleCareException(messageService.getMessage(Constants.ERROR_CHAT_FEE_INVALID, language));
            } else if (FnCommon.areAllNull(examFee, videoCallFee, callFee, chatFee)) {
                throw new TeleCareException(messageService.getMessage(Constants.ERROR_ALL_PRICE_ARE_INVALID, language));
            }

            DoctorsPricesEntity pricesEntity;

            //validate price
            int countKeyDuplicated = 0;
            for (DoctorsPricesEntity price : doctorsPrices) {
                if (Objects.equals(price.getMedicalExaminationNumber(), examNumber)) {
                    if (Objects.equals(price.getMedicalExaminationFee(), examFee)) {
                        dto.setMedicalExaminationFee(null);
                        dto.setMedicalExaminationNumber(null);
                        countKeyDuplicated++;
                    } else {
                        MessageDTO message = messageService.getMessage(Constants.ERROR_EXAM_FEE_IS_CONFIGURED, language);
                        String description = message.getDescription().replace("{SO_LUOT}", String.valueOf(examNumber))
                                .replace("{GIA_TIEN}", String.valueOf(price.getMedicalExaminationFee()));
                        message.setDescription(description);
                        throw new TeleCareException(message);
                    }
                }
                if (Objects.equals(price.getConsultantTimeVideocall(), videoCallTime)) {
                    if (Objects.equals(price.getConsultantFeeVideocall(), videoCallFee)) {
                        dto.setConsultantFeeVideocall(null);
                        dto.setConsultantTimeVideocall(null);
                        countKeyDuplicated++;
                    } else {
                        MessageDTO message = messageService.getMessage(Constants.ERROR_VIDEO_CALL_FEE_IS_CONFIGURED, language);
                        String description = message.getDescription().replace("{SO_PHUT}", String.valueOf(videoCallTime))
                                .replace("{GIA_TIEN}", String.valueOf(price.getConsultantFeeVideocall()));
                        message.setDescription(description);
                        throw new TeleCareException(message);
                    }
                }
                if (Objects.equals(price.getConsultantTimeCall(), callTime)) {
                    if (Objects.equals(price.getConsultantFeeCall(), callFee)) {
                        dto.setConsultantFeeCall(null);
                        dto.setConsultantTimeCall(null);
                        countKeyDuplicated++;
                    } else {
                        MessageDTO message = messageService.getMessage(Constants.ERROR_CALL_FEE_IS_CONFIGURED, language);
                        String description = message.getDescription().replace("{SO_PHUT}", String.valueOf(callTime))
                                .replace("{GIA_TIEN}", String.valueOf(price.getConsultantFeeCall()));
                        message.setDescription(description);
                        throw new TeleCareException(message);
                    }
                }
                if (Objects.equals(price.getConsultantChatNumber(), chatNumber)) {
                    if(Objects.equals(price.getConsultantFeeChat(), chatFee)) {
                        dto.setConsultantChatNumber(null);
                        dto.setConsultantFeeChat(null);
                        countKeyDuplicated++;
                    } else {
                        MessageDTO message = messageService.getMessage(Constants.ERROR_CHAT_FEE_IS_CONFIGURED, language);
                        String description = message.getDescription().replace("{SO_LUOT}", String.valueOf(examNumber))
                                .replace("{GIA_TIEN}", String.valueOf(price.getMedicalExaminationFee()));
                        message.setDescription(description);
                        throw new TeleCareException(message);
                    }
                }
            }

            if(countKeyDuplicated >= 4) continue;
            pricesEntity = (DoctorsPricesEntity) FnCommon.convertObjectToObject(dto, DoctorsPricesEntity.class);
            pricesEntity.setPhaseId(phaseId);

            Optional<String> userIdOpt = FnCommon.getUserLogin(authentication);
            Integer userId = null;
            if (userIdOpt.isPresent() && userIdOpt.get().matches("[0-9]+")) {
                userId = Integer.valueOf(userIdOpt.get());
            }

            // set basic info
            if (Objects.isNull(dto.getPriceId())) {
                pricesEntity.setCreateDate(Date.valueOf(LocalDate.now()));
                pricesEntity.setCreateUserId(userId);
            }
            pricesEntity.setUpdateUserId(userId);
            pricesEntity.setUpdateDate(Date.valueOf(LocalDate.now()));

            doctorsPricesEntities.add(pricesEntity);
        }

        doctorsPricesRepositoryJPA.saveAll(doctorsPricesEntities);
    }

    /**
     * Danh sach Doctor Prices
     *
     * @param itemParamsEntity params client
     * @return
     */
    @Override
    public Object getDoctorPricesByPhaseIdAndDoctorId(DoctorPriceDTO itemParamsEntity, Authentication authentication) throws TeleCareException {
        TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
        if (loginUser.isPatient()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
        } else if (loginUser.isDoctor()) {
            if (!loginUser.getTelecareUserId().equals(itemParamsEntity.getDoctorId())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
            }
        } else if (loginUser.isClinic()) {
            List<String> healthfacilitiCode = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
            List<Integer> doctorIds = doctorsHealthfacilitiesRepositoryJPA.getDoctorIdsByHealthficility(healthfacilitiCode);
            if (!doctorIds.contains(itemParamsEntity.getDoctorId())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
            }
        }

        ResultSelectEntity dataResult = doctorPriceRepository.getDoctorPricesByPhaseIdAndDoctorId(itemParamsEntity);
        List<DoctorPriceDTO> doctorPriceDTOList = (List<DoctorPriceDTO>) dataResult.getListData();
        for (DoctorPriceDTO element : doctorPriceDTOList) {
            Integer consultantTimeCall = element.getConsultantTimeCall();
            Integer consultantTimeVideoCall = element.getConsultantTimeVideocall();
            if (consultantTimeCall != null) {
                double multiTimeCall = consultantTimeCall / Constants.TIME_CONSULTANT_MIN;
                element.setMultiTimeCall(multiTimeCall);
                double consultantFeeCall = element.getConsultantFeeCall() != null ? element.getConsultantFeeCall() : 0.0;
                element.setConsultantFeeCall(consultantFeeCall / multiTimeCall);
            }
            if (consultantTimeVideoCall != null) {
                double multiTimeVideoCall = consultantTimeVideoCall / Constants.TIME_CONSULTANT_MIN;
                element.setMultiTimeVideoCall(multiTimeVideoCall);
                double consultantFeeVideoCall = element.getConsultantFeeVideocall() != null ? element.getConsultantFeeVideocall() : 0;
                element.setConsultantFeeVideocall(consultantFeeVideoCall / multiTimeVideoCall);
            }
        }
        return dataResult;
    }

    /**
     * update gia kham, tu van cua bac si
     *
     * @param itemParamsEntity params client
     * @param authentication   Authentication client
     * @return
     */
    @Override
    public Object updateDoctorPrices(DoctorPriceDTO itemParamsEntity, Authentication authentication) throws TeleCareException {
        TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
        if (loginUser.isPatient()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
        } else if (loginUser.isDoctor()) {
            if (!loginUser.getTelecareUserId().equals(itemParamsEntity.getDoctorId())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
            }
        } else if (loginUser.isClinic()) {
            List<String> healthfacilitiCode = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
            List<Integer> doctorIds = doctorsHealthfacilitiesRepositoryJPA.getDoctorIdsByHealthficility(healthfacilitiCode);
            if (!doctorIds.contains(itemParamsEntity.getDoctorId())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
            }
        }

        Predicate<DoctorPriceDTO> isDeleteRecordPredicate = item ->
                Arrays.asList(item.getMedicalExaminationFee(), item.getConsultantFeeVideocall(), item.getConsultantFeeCall(), item.getConsultantFeeChat())
                        .stream().allMatch(val -> Integer.valueOf(0).equals(val));
        LOGGER.debug(MessageFormat.format("update doctor price with itemParamsEntity : {0}", itemParamsEntity));

        // validate input param
        List<Object> paramsWithCheckNull = Arrays.asList(
                itemParamsEntity.getMedicalExaminationFee(), itemParamsEntity.getMedicalExaminationNumber(),
                itemParamsEntity.getConsultantFeeVideocall(), itemParamsEntity.getConsultantTimeVideocall(),
                itemParamsEntity.getConsultantFeeCall(), itemParamsEntity.getConsultantTimeCall(),
                itemParamsEntity.getConsultantChatNumber(), itemParamsEntity.getConsultantFeeChat());
        if (paramsWithCheckNull.stream().anyMatch(Objects::isNull)) {
            throw new TeleCareException(messageService.getMessage(Constants.ERROR_INPUTPARAMS, itemParamsEntity.getLanguage()));
        }

        DoctorsPricesEntity example = new DoctorsPricesEntity();
        example.setPhaseId(itemParamsEntity.getPhaseId());
        example.setDoctorId(itemParamsEntity.getDoctorId());
        List<DoctorsPricesEntity> recordsWillUpdate = doctorsPricesRepositoryJPA.findAll(Example.of(example));

        if (CollectionUtils.isEmpty(recordsWillUpdate)) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, itemParamsEntity.getLanguage()), ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST);
        }

        Optional<String> userIdOpt = FnCommon.getUserLogin(authentication);
        Integer userId = null;
        if (userIdOpt.isPresent() && userIdOpt.get().matches("[0-9]+")) {
            userId = Integer.valueOf(userIdOpt.get());
        }

        boolean isDeleteRecord = isDeleteRecordPredicate.test(itemParamsEntity);
        DoctorsPricesEntity entityOriginUpdate = (DoctorsPricesEntity) FnCommon.convertObjectToObject(itemParamsEntity, DoctorsPricesEntity.class);
        for (DoctorsPricesEntity item : recordsWillUpdate) {
            if (isDeleteRecord) {
                item.setIsDelete(true);
            } else {
                FnCommon.copyProperties(entityOriginUpdate, item);
            }
            item.setUpdateDate(Date.valueOf(LocalDate.now()));
            item.setUpdateUserId(userId);

            doctorsPricesRepositoryJPA.save(item);
        }

        return itemParamsEntity;
    }

}
