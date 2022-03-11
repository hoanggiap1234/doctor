package com.viettel.etc.repositories.tables.entities;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CATS_NATIONS")
public class CatsNationsEntity implements Serializable {

    @Id
    @Column(name = "nation_code")
    private String nationCode;

    @Column(name = "nation_name")
    private String nationName;

    @Column(name = "citizenship")
    private String citizenship;

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
