package com.viettel.etc.kafka.service;

import com.viettel.etc.utils.FnCommon;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Properties;

@Configuration
public class KafkaConfig {

	private String BOOTSTRAP_SERVERS = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
	private String ACK = FnCommon.getPropertiesValue("kafka.acks");
	private String ENABLE_AUTO_COMMIT = FnCommon.getPropertiesValue("kafka.consumer.enable_auto_commit");
	private String TIME_COMMIT_INTERVAL = FnCommon.getPropertiesValue("kafka.consumer.auto_commit_interval_ms");
	private String GROUP_ID = FnCommon.getPropertiesValue("kafka.consumer.group_id");

	@Bean
	public Properties kafkaAdminProperties() {
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
		props.setProperty("acks", ACK);
		return props;
	}

	@Bean
	public Properties kafkaProducerStringProperties() {
		Properties props = new Properties();
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.setProperty(ProducerConfig.ACKS_CONFIG, ACK);
		props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, String.valueOf(16384));
		props.setProperty(ProducerConfig.LINGER_MS_CONFIG, String.valueOf(1));
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		return props;
	}

	@Bean
	public KafkaConsumer<String, String> kafkaConsumerString() {
		Properties props = new Properties();
		System.out.println(BOOTSTRAP_SERVERS);
		props.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
		props.setProperty("acks", ACK);
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("group.id", GROUP_ID);
		props.setProperty("enable.auto.commit", ENABLE_AUTO_COMMIT);
		props.setProperty("auto.commit.interval.ms", TIME_COMMIT_INTERVAL);
		props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		return consumer;
	}

	@Bean
	public ArrayList<NewTopic> listProducerTopics() {
		ArrayList<NewTopic> listTopics = new ArrayList<>();
		listTopics.add(new NewTopic("my-topic", 4, (short) 2));
		return listTopics;
	}

	@Bean
	public ArrayList<String> listConsumerTopics() {
		ArrayList<String> listTopics = new ArrayList<>();
		for (ToppicName t : ToppicName.values()) {
			listTopics.add(t.val());
		}

		return listTopics;
	}

	public enum ToppicName {
		DeleteSysUserToAccount("topic.delete.sys_user"),
		SaveSysUserToAccount("topic.save.sys_user"),
		CreateCinicAccount("toppic.create_cinic_account"),
		CreateCinicAccountConfirmed("toppic.create_cinic_account_confirmed");

		private String name;

		private ToppicName(String name) {
			this.name = name;
		}

		public String val() {
			return this.name;
		}
	}
}
