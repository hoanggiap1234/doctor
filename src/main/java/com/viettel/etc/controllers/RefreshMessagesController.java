package com.viettel.etc.controllers;

import com.viettel.etc.services.MessageService;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.utils.TelecareUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshMessagesController {
    @Autowired
    MessageService messageService;

    @PostMapping(value = "/refresh-messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public String refreshMessages(@AuthenticationPrincipal Authentication authentication) throws TeleCareException {
        TelecareUserEntity telecareUserEntity = FnCommon.getTelecareUserInfo(authentication);
        if (telecareUserEntity != null && !telecareUserEntity.isAdmin()) {
            throw new TeleCareException(ErrorApp.ERR_USER_PERMISSION);
        }
        messageService.initializeMessages();
        return "ok";
    }
}
