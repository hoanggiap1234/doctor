package com.viettel.etc.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Comment
 *
 * @author nguyenhungbk96@gmail.com
 */
@Data
public class RessetPasswordDTO {
	@NotNull
	private String phoneNumber;
	@NotNull
	@Length(min = 8)
	private String newPassword;
	@NotNull
	@Length(min = 6, max = 6)
	private String otp;
	String language;
}
