package com.viettel.etc.repositories.tables.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Autogen class Entity: Create Entity For Table Name Cats_healthfacilities_calendars
 * 
 * @author ToolGen
 * @date Tue Aug 25 11:22:26 ICT 2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "CATS_HEALTHFACILITIES_CALENDARS")
public class CatsHealthfacilitiesCalendarsEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALENDAR_ID")
    Integer calendarId;

    @Column(name = "HEALTHFACILITIES_CODE")
    String healthfacilitiesCode;

    @Column(name = "DAY_CODE")
    Integer dayCode;

    @Column(name = "AM_FROM")
    String amFrom;

    @Column(name = "AM_TO")
    String amTo;

    @Column(name = "PM_FROM")
    String pmFrom;

    @Column(name = "PM_TO")
    String pmTo;

    @Column(name = "IS_DELETE", insertable = false)
    Integer isDelete;

    @Column(name = "IS_ACTIVE", insertable = false)
    Integer isActive;

    @Column(name = "CREATE_USER_ID")
    Integer createUserId;

    @Column(name = "CREATE_DATE")
    Date createDate;

    @Column(name = "UPDATE_USER_ID")
    Integer updateUserId;

    @Column(name = "UPDATE_DATE")
    Date updateDate;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = new Date();
    }
}