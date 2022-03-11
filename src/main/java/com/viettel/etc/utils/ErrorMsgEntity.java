package com.viettel.etc.utils;

import com.viettel.etc.xlibrary.core.entities.MessEntity;
import lombok.Data;

/**
 * Comment
 *
 * @author nguyenhungbk96@gmail.com
 */
@Data
public class ErrorMsgEntity extends MessEntity {
	private String messageCode;
}
