package com.viettel.etc.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.etc.repositories.tables.entities.DoctorsEntity;
import com.viettel.etc.repositories.tables.entities.SysUsersEntity;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaService {

	private static Logger log = LoggerFactory.getLogger(KafkaService.class);
	@Autowired
	private Properties kafkaProducerStringProperties;

	public void sendString(String topic, Object key, Object value, Callback callBack) {
		ObjectMapper objectMapper = new ObjectMapper();
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer(kafkaProducerStringProperties);
		try {
			kafkaProducer.send(new ProducerRecord(topic, (key instanceof String) ? key : objectMapper.writeValueAsString(key),
					(value instanceof String) ? value : objectMapper.writeValueAsString(value)), callBack);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.error("error convert object json ", e);
		}
		kafkaProducer.close();
	}

	public void createCinicAccountConfirmed(DoctorsEntity entity) {
		sendString(KafkaConfig.ToppicName.CreateCinicAccountConfirmed.val(), entity.getKeycloakUserId(), entity.getDoctorId(), new Callback() {
			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception e) {
				if (e == null) {
					System.out.println("Success");
				} else {
					e.printStackTrace();
					System.out.println("False");
				}
			}
		});
	}

	public void createSysUser(SysUsersEntity sysUsersEntity) throws JsonProcessingException {
		sendString(KafkaConfig.ToppicName.SaveSysUserToAccount.val(), sysUsersEntity.getKeycloakUserId(), sysUsersEntity, new Callback() {
			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception e) {
				if (e == null) {
					System.out.println("Success");
				} else {
					e.printStackTrace();
					System.out.println("False");
				}
			}
		});
	}

	public void deleteSysUser(SysUsersEntity sysUsersEntity) throws JsonProcessingException {
		sendString(KafkaConfig.ToppicName.DeleteSysUserToAccount.val(), sysUsersEntity.getKeycloakUserId(), sysUsersEntity, new Callback() {
			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception e) {
				if (e == null) {
					System.out.println("Success");
				} else {
					e.printStackTrace();
					System.out.println("False");
				}
			}
		});
	}
}
