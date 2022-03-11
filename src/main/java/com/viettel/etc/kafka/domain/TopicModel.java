package com.viettel.etc.kafka.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TopicModel {
    @JsonProperty("configInfor")
    private List<TopicInfor> configInfor;

}
