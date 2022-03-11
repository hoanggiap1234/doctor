package com.viettel.etc.repositories.tables.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "OTP")
@IdClass(OtpIdentify.class)
public class OtpEntity implements Serializable {
    @Id
    @Column(name = "phone")
    String phone;

    @Id
    @Column(name = "confirm_type")
    Integer confirmType;

    @Column(name = "otp")
    String otp;

    @Column(name = "sign_date")
//    @CreationTimestamp
    Date signDate;

    @Column(name = "duration")
    Integer duration;

    @PrePersist
    void onCreate(){
        signDate = new Date();
    }

}
