package com.viettel.etc.kafka.multipleconsumer;


import com.viettel.etc.kafka.common.KafkaUtils;
import com.viettel.etc.kafka.domain.ConsumerModel;
import com.viettel.etc.kafka.domain.TopicInfor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@Component
public abstract class ConsumerThread implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(ConsumerThread.class);

	private KafkaConsumer<String, String> consumer;
	private String topic;
	private String configThreadFileName;
	private String configThreadModelFileName;
	private String name;
	private Boolean isUseCustomSql = false;
	private Boolean isUseSequence = false;
	private String bootstrapServer;
	private ConsumerModel consumerModel;
	private String prefixFilePath;
	private TopicInfor topicInfor;

	public void setVariable(TopicInfor topicInfor, String bootstrapServer)
			throws IOException {
		this.topic = topicInfor.getTopicName();
		this.configThreadFileName = topicInfor.getConfigThreadFileName();
		this.configThreadModelFileName = topicInfor.getModelConfigFileName();
		Properties propConsumer = KafkaUtils.createConsumerConfig(configThreadFileName, bootstrapServer);
		ConsumerModel consumerModel = KafkaUtils.getConfigYaml(configThreadFileName);
		this.consumerModel = consumerModel;

		if (bootstrapServer != null && !"".equals(bootstrapServer)) {
			this.bootstrapServer = bootstrapServer;
			propConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		}
		this.consumer = new KafkaConsumer<>(propConsumer);
		this.consumer.subscribe(Arrays.asList(this.topic));
		this.name = topic;
	}

	@Override
	public void run() {
		try {
			Thread.currentThread().setName(name);
			while (true) {
				try {
					ConsumerRecords<String, String> records = consumer.poll(Long.valueOf(consumerModel.getConsumerPollTime()));
					if (records.count() != 0) {
						LOGGER.info("----------------START POOL--------------------");
						handleRecords(records);
					}
					consumer.commitAsync();
				} catch (Exception ex) {
					LOGGER.error("Kafka fail: ", ex);
					ex.printStackTrace();
//                    consumer.subscribe(Arrays.asList(this.topic));
				}
			}
		} finally {
			if (consumer != null) {
				consumer.close();
			}
		}
	}

	private Properties config(String bootstrapServer, String clientId, Integer batchSizeConfig, Long lingerMsConfig) {
		Properties props = new Properties();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
//        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"Transaction_id_1");
		props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSizeConfig);
		props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMsConfig);

		return props;
	}


	abstract public void handleRecords(ConsumerRecords<String, String> records);
}
