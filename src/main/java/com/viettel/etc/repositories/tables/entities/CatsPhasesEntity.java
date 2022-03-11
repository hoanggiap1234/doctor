package com.viettel.etc.repositories.tables.entities;

import com.viettel.etc.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Autogen class Entity: Create Entity For Table Name Cats_phases
 *
 * @author ToolGen
 * @date Mon Sep 14 15:27:31 ICT 2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "CATS_PHASES")
public class CatsPhasesEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PHASE_ID")
	Integer phaseId;

	@Column(name = "HEALTHFACILITIES_CODE")
	String healthfacilitiesCode;

	@Column(name = "CODE")
	String code;

	@Column(name = "NAME")
	String name;

	@Column(name = "FROM_DATE")
	Date fromDate;

	@Column(name = "TO_DATE")
	Date toDate;

	@Column(name = "DESCRIPTION")
	String description;

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
}