package com.viettel.etc.repositories.tables.entities;

import com.viettel.etc.utils.Constants;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Autogen class Entity: Create Entity For Table Name Doctors
 *
 * @author ToolGen
 * @date Wed Aug 19 14:21:51 ICT 2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "DOCTORS")
public class DoctorsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOCTOR_ID")
    Integer doctorId;

    @Column(name = "HIS_ID")
    String hisId;

    @Column(name = "KEYCLOAK_USER_ID")
    String keycloakUserId;

    @Column(name = "QR_CODE")
    String qrCode;

    @Column(name = "DOCTOR_CODE")
    String doctorCode;

    @Column(name = "FULLNAME")
    String fullname;

    @Column(name = "BIRTHDAY")
    Date birthday;

    @Column(name = "GENDER_ID")
    Integer genderId;

    @Column(name = "PROVINCE_CODE")
    String provinceCode;

    @Column(name = "DISTRICT_CODE")
    String districtCode;

    @Column(name = "ADDRESS")
    String address;

    @Column(name = "PHONE_NUMBER")
    String phoneNumber;

    @Column(name = "EMAIL")
    String email;

    @Column(name = "CERTIFICATION_CODE")
    String certificationCode;

    @Column(name = "CERTIFICATION_DATE")
    Date certificationDate;

    @Column(name = "POSITION_CODE")
    String positionCode;

    @Column(name = "ACADEMIC_RANK_ID")
    Integer academicRankId;

    @Column(name = "DEGREE_ID")
    Integer degreeId;

    @Column(name = "NATION_CODE")
    String nationCode;

    @Column(name = "ETHNICITY_CODE")
    String ethnicityCode;

    @Column(name = "RELIGION_CODE")
    String religionCode;

    @Column(name = "AVATAR")
    String avatar;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "SUMMARY")
    String summary;

    @Column(name = "IS_SYNC")
    Boolean isSync = Constants.IS_NOT_SYNC;

    @Column(name = "IS_MEDICALEXAMINATION")
    Boolean isMedicalexamination;

    @Column(name = "IS_CONSULTANT")
    Boolean isConsultant;

    @Column(name = "ALLOW_FILTER")
    Boolean allowFilter;

    @Column(name = "ALLOW_SEARCH")
    Boolean allowSearch;

    @Column(name = "ALLOW_BOOKING")
    Boolean allowBooking;

    @Column(name = "CREATE_USER_ID")
    Integer createUserId;

    @Column(name = "UPDATE_USER_ID")
    Integer updateUserId;

    @Column(name = "HISID")
    String hisid;

    @Column(name = "DOCTOR_TYPE")
    Integer doctorType;

    @Column(name = "WARD_CODE")
    String wardCode;

    @Column(name = "IS_DELETE")
    Boolean isDelete = Constants.IS_NOT_DELETE;

    @Column(name = "IS_ACTIVE")
    Boolean isActive = Constants.IS_ACTIVE;

    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    Date updateDate;

    @Column(name = "CREATE_DATE")
    @CreationTimestamp
    Date createDate;
}