package com.viettel.etc.utils;


import com.viettel.etc.dto.MessageDTO;

import java.util.function.Supplier;
/**
 *
 * @author datnv5
 */
public class TeleCareException extends Exception implements Supplier<TeleCareException> {
    private ErrorApp errorApp;
    private Integer codeError;

    public TeleCareException(ErrorApp errorApp) {
        super(errorApp.getDescription());
        this.errorApp = errorApp;
    }

    public TeleCareException(MessageDTO messageDTO) {
        this.errorApp = ErrorApp.ERROR_INPUTPARAMS;
        this.errorApp.setDescription(messageDTO.getDescription());
        this.errorApp.setCode(messageDTO.getCode());
    }

    public TeleCareException(MessageDTO messageDTO, ErrorApp errorApp) {
        this.errorApp = errorApp;
        this.errorApp.setDescription(messageDTO.getDescription());
        this.errorApp.setCode(messageDTO.getCode());
    }
    public TeleCareException(String message) {
        super(message);
    }

    public TeleCareException(Integer codeError, String message) {
        super(message);
        this.codeError = codeError;
    }

    public TeleCareException(ErrorApp errorApp, MessageDTO messageDTO) {
        this.errorApp = errorApp;
        this.errorApp.setDescription(messageDTO.getDescription());
        this.errorApp.setCode(messageDTO.getCode());
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ErrorApp getErrorApp() {
        return errorApp;
    }

    public Integer getCodeError() {
        return codeError;
    }

    @Override
    public TeleCareException get() {
        return this;
    }
}
