package com.viettel.etc.kafka.multipleconsumer;


import com.viettel.etc.kafka.common.FileUtils;
import com.viettel.etc.kafka.domain.TopicInfor;
import com.viettel.etc.kafka.domain.TopicModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MultipleConsumerMain {

	private static final Logger LOGGER = LogManager.getLogger(MultipleConsumerMain.class);
	@Value("${fileConfigTopic}")
	private String fileConfigTopic;
	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServer;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private ApplicationContext context;
	@Value("${importer.numberMinThread}")
	private Integer numberMinThread;
	@Value("${importer.numberMaxThread}")
	private Integer numberMaxThread;

	public void run() {
		taskExecutor.setCorePoolSize(numberMinThread);
		taskExecutor.setMaxPoolSize(numberMaxThread);
		taskExecutor.afterPropertiesSet();
		try {
			TopicModel topicModel = (TopicModel) FileUtils.getObjectFromJsonFile(fileConfigTopic, TopicModel.class);

			for (TopicInfor topicInfor : topicModel.getConfigInfor()) {
				ConsumerThread t;
				try {
					t = getConsumer(topicInfor, bootstrapServer);
					if (t != null) {
						t.setVariable(topicInfor, bootstrapServer);
						taskExecutor.execute(t);
					}

				} catch (IOException e) {
					LOGGER.error("Read file failed: " + e);
				} catch (Exception e) {
					LOGGER.error("Errors: " + e);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Read file failed: " + e);
		}
	}

	public ConsumerThread getConsumer(TopicInfor topicInfor, String bootstrapServer) throws IOException {
		switch (topicInfor.getTopicName()) {
			case "toppic.create_cinic_account":
				return (CreateCinicAccountConsumer) context.getBean("createCinicAccountConsumer");
			case "toppic.change_status_active_doctor":
				return (ChangeStatusActiveConsumer) context.getBean("changeStatusActiveConsumer");
			default:
				return null;
		}

	}
}
