package com.viettel.etc.dto;

import com.viettel.etc.utils.FnCommon;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginDTO {

    String password;

    String client_id;

    String client_secret;

    String grant_type;

    String username;

    public UserLoginDTO(String username, String password) {
        this.password = password;
        this.client_id = FnCommon.getPropertiesValue("user.keycloak.admin.client.id");
        this.client_secret  = FnCommon.getPropertiesValue("keycloak.credentials.secret");
        this.grant_type = "password";
        this.username = username;
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