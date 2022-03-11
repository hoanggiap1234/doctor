package com.viettel.etc.kafka.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusActiveDTO {

    private Integer id;

    private Integer isActive;
}
