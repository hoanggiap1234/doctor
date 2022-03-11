package com.viettel.etc.kafka.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

public class FileUtils {

	public static Object getObjectFromJsonFile(String fileName, Class clazz) throws IOException {
		ResourceLoader resourceLoader=new DefaultResourceLoader();
		Resource src=resourceLoader.getResource("classpath:"+ fileName+".json");
		ObjectMapper objectMapper=new ObjectMapper();
		Object o=objectMapper.readValue(src.getInputStream(), clazz);

		return o;
	}

}
