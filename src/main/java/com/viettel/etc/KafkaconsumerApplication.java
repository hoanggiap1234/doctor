package com.viettel.etc;

import com.viettel.etc.kafka.multipleconsumer.MultipleConsumerMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaconsumerApplication implements ApplicationRunner {

	@Autowired
	MultipleConsumerMain multipleConsumerMain;


	@Override
	public void run(ApplicationArguments args) throws Exception {
		multipleConsumerMain.run();
	}
}