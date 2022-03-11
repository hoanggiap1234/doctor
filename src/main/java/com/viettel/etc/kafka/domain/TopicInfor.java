package com.viettel.etc.kafka.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicInfor {
    @JsonProperty("topicName")
    private String topicName;
    @JsonProperty("configThreadFileName")
    private String configThreadFileName;
    @JsonProperty("modelConfigFileName")
    private String modelConfigFileName;

//    public String getTopicName() {
//        return topicName;
//    }
//
//    public void setTopicName(String topicName) {
//        this.topicName = topicName;
//    }
//
//    public String getConfigThreadFileName() {
//        return configThreadFileName;
//    }
//
//    public void setConfigThreadFileName(String configThreadFileName) {
//        this.configThreadFileName = configThreadFileName;
//    }
//
//    public String getModelConfigFileName() {
//        return modelConfigFileName;
//    }
//
//    public void setModelConfigFileName(String modelConfigFileName) {
//        this.modelConfigFileName = modelConfigFileName;
//    }
//
//    public String getSqlConfigFileName() {
//        return sqlConfigFileName;
//    }
//
//    public void setSqlConfigFileName(String sqlConfigFileName) {
//        this.sqlConfigFileName = sqlConfigFileName;
//    }
}
