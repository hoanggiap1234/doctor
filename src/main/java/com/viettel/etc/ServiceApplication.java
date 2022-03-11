package com.viettel.etc;

import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.xlibrary.core.constants.RedisUtil;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@SpringBootApplication
@EnableJpaAuditing
//@EnableTransactionManagement
public class ServiceApplication {
    private static final Logger LOGGER = Logger.getLogger(ServiceApplication.class);
    public static void main(String[] args) {
//        //thuc hien doc cau hinh file lay thong tin cau hinh redis
//        String strRedisAddress = FnCommon.getPropertiesValue("redis.server.address").trim();
//        String strIntTimeout = FnCommon.getPropertiesValue("redis.server.timeout");
//        int timeoutRedit = 3000;
//        try {
//            timeoutRedit = Integer.valueOf(strIntTimeout);
//        }catch (Exception ex){
//            LOGGER.error(ex);
//        }
//        if(strRedisAddress.length()>0){
//            RedisUtil redis = new RedisUtil(strRedisAddress, timeoutRedit);
//            redis.setup();
//        }
        SpringApplication.run(ServiceApplication.class, args);
    }
}
