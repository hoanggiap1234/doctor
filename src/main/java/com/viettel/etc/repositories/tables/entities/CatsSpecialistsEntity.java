package com.viettel.etc.repositories.tables.entities;

import com.viettel.etc.utils.Constants;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Autogen class Entity: Create Entity For Table Name Cats_specialists
 *
 * @author ToolGen
 * @date Wed Aug 19 16:25:41 ICT 2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "CATS_SPECIALISTS")
public class CatsSpecialistsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPECIALIST_ID")
    Integer specialistId;

    @Column(name = "CODE")
    String code;

    @Column(name = "NAME")
    String name;

    @Column(name = "ORDER_NUMBER")
    Integer orderNumber;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "CREATE_USER_ID")
    Integer createUserId;

    @Column(name = "UPDATE_USER_ID")
    Integer updateUserId;

    @Column(name = "IS_DELETE")
    Boolean isDelete = Constants.IS_NOT_DELETE;

    @Column(name = "IS_ACTIVE")
    Boolean isActive = Constants.IS_ACTIVE;

    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    Date updateDate;

    @Column(name = "CREATE_DATE")
    @CreationTimestamp
    Date createDate;
}