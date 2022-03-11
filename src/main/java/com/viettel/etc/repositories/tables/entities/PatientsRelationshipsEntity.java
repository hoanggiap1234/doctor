package com.viettel.etc.repositories.tables.entities;

import com.viettel.etc.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static com.viettel.etc.utils.Constants.IS_ACTIVE;
import static com.viettel.etc.utils.Constants.IS_NOT_DELETE;


/**
 * Autogen class Entity: Create Entity For Table Name Patients_relationships
 *
 * @author ToolGen
 * @date Thu Aug 13 22:25:22 ICT 2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "PATIENTS_RELATIONSHIPS")
public class PatientsRelationshipsEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	Integer id;

	@Column(name = "PATIENT_ID")
	Integer patientId;

	@Column(name = "PATIENT_PARENT_ID")
	Integer patientParentId;

	@Column(name = "RELATIONSHIP_ID")
	Integer relationshipId;

	@Column(name = "IS_DELETE")
	Boolean isDelete = IS_NOT_DELETE;

	@Column(name = "IS_ACTIVE")
	Boolean isActive = IS_ACTIVE;

	@Column(name = "CREATE_USER_ID")
	Integer createUserId;

	@Column(name = "CREATE_DATE", insertable = false)
	@CreationTimestamp
	Date createDate;

	@Column(name = "UPDATE_DATE")
	@UpdateTimestamp
	Date updateDate;

	@Column(name = "UPDATE_USER_ID")
	Integer updateUserId;
}