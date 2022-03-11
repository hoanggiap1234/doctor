package com.viettel.etc.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileImportDTO {
    private String pathFile;

    private Integer totalImport;

    private Integer totalSuccess;

    private Date completedDate;

    private Integer status;

    private String language;

    private Date importDate;
}
