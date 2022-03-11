package com.viettel.etc.kafka.multipleconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.viettel.etc.kafka.domain.ChangeStatusActiveDTO;
import com.viettel.etc.kafka.domain.TopicInfor;
import com.viettel.etc.services.tables.DoctorsServiceJPA;
import com.viettel.etc.utils.TeleCareException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ChangeStatusActiveConsumer extends ConsumerThread {

    @Autowired
    DoctorsServiceJPA doctorServiceJPA;

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(ChangeStatusActiveConsumer.class));

    @Override
    public void setVariable(TopicInfor topicInfor, String bootstrapServer) throws IOException {
        super.setVariable(topicInfor, bootstrapServer);
    }

    @Override
    public void handleRecords(ConsumerRecords<String, String> records) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        for (ConsumerRecord<String, String> record: records){
            try {
                ChangeStatusActiveDTO changeStatusActiveDTO = new ObjectMapper().readValue(record.value(), ChangeStatusActiveDTO.class);
                doctorServiceJPA.changeStatusActive(changeStatusActiveDTO);
            } catch (JsonProcessingException | TeleCareException e) {
                LOGGER.info(e);
                e.printStackTrace();
            }
        }
    }
}
