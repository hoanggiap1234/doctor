package com.viettel.etc.kafka.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.viettel.etc.kafka.domain.ConsumerModel;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import org.springframework.core.io.ResourceLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class KafkaUtils {

	private static final Logger LOGGER = LogManager.getLogger(KafkaUtils.class);

	public static ConsumerModel getConfigYaml(String fileName) throws IOException {
		ResourceLoader loader = new DefaultResourceLoader();
		Resource rsc = loader.getResource("classpath:"+fileName + ".yml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ConsumerModel consumerModel = mapper.readValue(rsc.getInputStream(), ConsumerModel.class);
		return consumerModel;
	}

	public static Properties createConsumerConfig(String nameFile, String bootstrapServer) throws IOException {
		ConsumerModel consumerModel = KafkaUtils.getConfigYaml(nameFile);
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerModel.getGroupId());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerModel.getEnableAutoCommit());
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerModel.getAutoCommitIntervalMs());
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, consumerModel.getMaxPollIntervalMs());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerModel.getMaxPollRecords());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerModel.getAutoOffsetReset());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerModel.getKeySerializer());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerModel.getValueSerializer());

		props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 9999999);
		props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 6000);
		props.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 9999999);
		props.put(ConsumerConfig.SEND_BUFFER_CONFIG, 9999999);
		return props;
	}

}
