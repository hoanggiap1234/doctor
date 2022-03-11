package com.viettel.etc.repositories.tables.entities;

import com.viettel.etc.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * Autogen class Entity: Create Entity For Table Name Doctors_prices
 *
 * @author ToolGen
 * @date Mon Sep 07 14:45:02 ICT 2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "DOCTORS_PRICES")
public class DoctorsPricesEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRICE_ID")
	Integer priceId;

	@Column(name = "DOCTOR_ID")
	Integer doctorId;

	@Column(name = "PHASE_ID")
	Integer phaseId;

	@Column(name = "MEDICAL_EXAMINATION_FEE")
	Integer medicalExaminationFee;

	@Column(name = "MEDICAL_EXAMINATION_NUMBER")
	Integer medicalExaminationNumber;

	@Column(name = "CONSULTANT_FEE_VIDEOCALL")
	Double consultantFeeVideocall;

	@Column(name = "CONSULTANT_FEE_CALL")
	Double consultantFeeCall;

	@Column(name = "CONSULTANT_FEE_CHAT")
	Double consultantFeeChat;

	@Column(name = "CONSULTANT_TIME_VIDEOCALL")
	Integer consultantTimeVideocall;

	@Column(name = "CONSULTANT_TIME_CALL")
	Integer consultantTimeCall;

	@Column(name = "CONSULTANT_CHAT_NUMBER")
	Integer consultantChatNumber;

	@Column(name = "CREATE_USER_ID")
	Integer createUserId;

	@Column(name = "UPDATE_USER_ID")
	Integer updateUserId;

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

	@PrePersist
	public void prePersist() {
		if (Objects.isNull(isDelete)) {
			isDelete = false;
		}
		if (Objects.isNull(isActive)) {
			isActive = true;
		}
	}
}
