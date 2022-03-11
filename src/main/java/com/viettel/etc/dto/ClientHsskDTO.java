package com.viettel.etc.dto;

import com.viettel.etc.utils.FnCommon;
import lombok.Data;

@Data
public class ClientHsskDTO {
    String username;

    String password;

    String grant_type;

    String client_id;

    String client_secret;

    public ClientHsskDTO() {
        this.username = System.getenv(FnCommon.getPropertiesValue("user.hssk.username").replace("${", "").replace("}", ""));
        this.password = System.getenv(FnCommon.getPropertiesValue("user.hssk.password").replace("${", "").replace("}", ""));;
//        this.grant_type = "client_credentials";
//        this.client_id = FnCommon.getPropertiesValue("hssk.client.id");
//        this.client_secret = FnCommon.getPropertiesValue("hssk.client.secret");
    }

    @Override
    public String toString() {
        return "username=" + username + "&"
                + "password=" + password + "&"
                + "grant_type=" + grant_type + "&"
                + "client_id=" + client_id + "&"
                + "client_secret=" + client_secret;
    }
}
