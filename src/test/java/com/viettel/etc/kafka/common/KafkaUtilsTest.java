package com.viettel.etc.kafka.common;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class KafkaUtilsTest {

    KafkaUtils kafkaUtils = new KafkaUtils();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getConfigYaml() {
    }

    @Test
    void createConsumerConfig() throws IOException {
        String nameFile = "configTopics/change_status_active_doctor_consummer";
        String bootstrapServer = "localhost:9093";
        MatcherAssert.assertThat(kafkaUtils.createConsumerConfig(nameFile,bootstrapServer).getProperty(ConsumerConfig.GROUP_ID_CONFIG), Matchers.equalTo("doctor"));
    }
}