package com.viettel.etc.repositories.tables.entities;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cats_religions")
public class CatsReligionsEntity implements Serializable {
    @Id
    @Column(name = "religion_code")
    private String religionCode;

    @Column(name = "name")
    private String name;

    @Column(name = "other_name")
    private String otherName;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "update_date")
    @UpdateTimestamp
    private Date updateDate;

    @Column(name = "create_date")
    @CreationTimestamp
    private Date createDate;

    @Column(name = "create_user_id")
    private Integer createUserId;

    @Column(name = "update_user_id")
    private Integer updateUserId;

}
