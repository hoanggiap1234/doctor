package com.viettel.etc.kafka.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Comment
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCinicAccountEntity implements Serializable {
	private Integer doctorCode;
	private String fullname;
	private Integer genderId;
	private Integer doctorType;
	private String keyclockUserId;
	private String phoneNumber;
}
