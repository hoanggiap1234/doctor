package com.viettel.etc.repositories.tables.entities;


import com.viettel.etc.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cats_healthfacilities_specialists")
public class CatsHealthfacilitiesSpecialistsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "healthfacilities_code")
    private String healthfacilitiesCode;

    @Column(name = "specialist_code")
    private String specialistCode;

    @Column(name = "specialist_id")
    private String specialistId;

    @Column(name = "CREATE_USER_ID")
    private Integer createUserId;

    @Column(name = "UPDATE_USER_ID")
    private Integer updateUserId;

    @Column(name = "IS_DELETE")
    private Boolean isDelete = Constants.IS_NOT_DELETE;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = Constants.IS_ACTIVE;

    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    private Date updateDate;

    @Column(name = "CREATE_DATE")
    @CreationTimestamp
    private Date createDate;
}
