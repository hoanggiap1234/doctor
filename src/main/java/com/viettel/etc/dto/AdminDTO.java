package com.viettel.etc.dto;

import com.viettel.etc.utils.FnCommon;
import lombok.Data;

@Data
public class AdminDTO {
    String password;

    String client_id;

    String client_secret;

    String grant_type;

    String username;

    public AdminDTO() {
        this.password = FnCommon.getPropertiesValue("user.keycloak.admin.password");
        this.client_id = FnCommon.getPropertiesValue("user.keycloak.admin.client.id");
        this.client_secret = FnCommon.getPropertiesValue("keycloak.credentials.secret");
        this.grant_type = "password";
        this.username = FnCommon.getPropertiesValue("user.keycloak.admin.username");
    }
    @Override
    public String toString() {
        return "grant_type=" + grant_type + "&" +
                "client_id=" + client_id + "&" +
                "client_secret=" + client_secret + "&" +
                "username=" + username + "&" +
                "password=" + password;
    }
}
