package com.viettel.etc.repositories.tables.entities;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Autogen class Entity: Create Entity For Table Name Cats_messages
 *
 * @author ToolGen
 * @date Mon Dec 07 17:22:03 ICT 2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "CATS_MESSAGES")
public class CatsMessagesEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_CODE")
    String messageCode;

    @Column(name = "MESSAGE_VI")
    String messageVi;

    @Column(name = "MESSAGE_EN")
    String messageEn;

    @Column(name = "CODE")
    Integer code;

    @Column(name = "IS_DELETE")
    Boolean isDelete;
}