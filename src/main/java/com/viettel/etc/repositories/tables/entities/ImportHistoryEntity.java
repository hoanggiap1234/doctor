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
@Table(name = "IMPORT_HISTORY")
public class ImportHistoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "path_file")
    private String pathFile;

    @Column(name = "file_type") // 0-patient, 1-doctor
    private Integer fileType;

    @Column(name = "status")
    private Integer status; // 0-Dang thuc hien, 1-Thanh cong, 2-That bai

    @Column(name = "total_import")
    private Integer totalImport; // Tong so ban ghi import

    @Column(name = "total_success")
    private Integer totalSucsess; // Tong so ban ghi thanh cong

    @Column(name = "import_date")
    private Date importDate;

    @Column(name = "completed_date")
    private Date completedDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    private Date updateDate;

    @Column(name = "CREATE_DATE")
    @CreationTimestamp
    private Date createDate;

    @Column(name = "create_user_id")
    private Integer createUserId;

    @Column(name = "update_user_id")
    private Integer updateUserId;
}
