package com.viettel.etc.kafka.multipleconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.viettel.etc.controllers.DoctorPriceController;
import com.viettel.etc.kafka.domain.CreateCinicAccountEntity;
import com.viettel.etc.kafka.domain.TopicInfor;
import com.viettel.etc.kafka.service.KafkaService;
import com.viettel.etc.repositories.tables.DoctorsRepositoryJPA;
import com.viettel.etc.repositories.tables.entities.DoctorsEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CreateCinicAccountConsumer extends ConsumerThread {
	@Autowired
	DoctorsRepositoryJPA doctorsRepositoryJPA;

	@Autowired
	KafkaService kafkaService;

	private static final Logger LOGGER = Logger.getLogger(CreateCinicAccountConsumer.class);
	@Override
	public void setVariable(TopicInfor topicInfor, String bootstrapServer) throws IOException {
		super.setVariable(topicInfor, bootstrapServer);
	}

	@Override
	public void handleRecords(ConsumerRecords<String, String> records) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		for (ConsumerRecord<String, String> record : records) {
			try {
				CreateCinicAccountEntity cinicAccountEntity = objectMapper.readValue(record.value(), CreateCinicAccountEntity.class);
				System.out.println(objectMapper.writeValueAsString(cinicAccountEntity));

				DoctorsEntity doctorsEntity = new DoctorsEntity();
				doctorsEntity.setKeycloakUserId(cinicAccountEntity.getKeyclockUserId());
				doctorsEntity.setGenderId(cinicAccountEntity.getGenderId());
				doctorsEntity.setDoctorCode(cinicAccountEntity.getDoctorCode() + "");
				doctorsEntity.setFullname(cinicAccountEntity.getFullname());
				doctorsEntity.setDoctorType(cinicAccountEntity.getDoctorType());
				doctorsEntity.setPhoneNumber(cinicAccountEntity.getPhoneNumber());
				doctorsRepositoryJPA.save(doctorsEntity);

				kafkaService.createCinicAccountConfirmed(doctorsEntity);

			} catch (JsonProcessingException e) {
				LOGGER.info(e);
				e.printStackTrace();
			}
		}
	}
}
