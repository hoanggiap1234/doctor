package com.viettel.etc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendSMS {

    private static Logger LOGGER = Logger.getLogger(SendSMS.class.getName());
    private static final org.apache.log4j.Logger LOGGER_4J = org.apache.log4j.Logger.getLogger(SendEmail.class.getName());

    /**
     * The Constant FIRST_NUMBER_84.
     */
    private static final String FIRST_NUMBER_84 = "84";

    /**
     * The Constant FIRST_NUMBER_0.
     */
    private static final String FIRST_NUMBER_0 = "0";

    /**
     * The Constant PHONE_NUMBER_PATTERN.
     */
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^0[0-9]{9}$");

    /**
     * The Constant VALUE_2.
     */
    private static final int VALUE_2 = 2;

    private static final String TOOL_SMS = "/SmsTool.jar";

    /**
     * The Constant MAX_LIMIT_765.
     */
    private static final int MAX_LIMIT_765 = 765;


    /**
     * Checks if is valid phone number.
     *
     * @param phone the phone
     * @return true, if is valid phone number
     */
    private static String buildPhoneNumberBeforeSend(String phone) {
        try {
            String firstNumber = phone.substring(0, VALUE_2);
            String phoneLast = null;

            if (FIRST_NUMBER_84.equals(firstNumber)) {
                phoneLast = phone.substring(VALUE_2, phone.length());
                phone = String.format("%s%s", FIRST_NUMBER_0, phoneLast);
            } else {
                phoneLast = phone.substring(1, phone.length());
            }

            if (isValidPhoneNumber(phone)) {
                LOGGER.info("So phone khong hop le");
            }

            return String.format("%s%s", FIRST_NUMBER_84, phoneLast);

        } catch (Exception e) {
            LOGGER_4J.error("buildPhoneNumberBeforeSend error", e);
        }
        return null;
    }

    public static boolean isValidPhoneNumber(String phone) {
        Matcher m = PHONE_NUMBER_PATTERN.matcher(phone);
        return (m.find() && m.group().equals(phone));
    }


//    public static boolean isNullOrEmpty(String toTest) {
//        return toTest == null;
//    }

    public static void sendAsyncDev(String phone, String content) {
        // whitelist
        String whiteList = "0965385568,0976289888,0986514639,0984086688,0966338800,0975800010,0918447296,0336405816,0975176376,0974328879,0984327789,0325695788,0987830596,0396589443,0387549621,0967824603,0979380353,0386097733,0983520020,0973731179,0989992313,0982915384,0378498748,0986376372,0981651642,0976359945,0982915384,0965423719,0349727674,0386987892,0986506577,0868586888,0989992313,0983140218,0968123568,0983418555,0984468800,0985569851,0969920723,0857248126,0372523078,0773366007,0985569851,0969920723,0857248126,0372523078,0972621298,0346985163,0352691625,0979944169";
        if (!whiteList.contains(phone)) {
            return;
        }

        phone = buildPhoneNumberBeforeSend(phone);
        if (content.length() > MAX_LIMIT_765) {
            LOGGER.info("Noi dung tin nhan qua dai");
//            throw new SendSMSException("Noi dung tin nhan qua dai");
        }

        try {
//            String file = new File(Thread.currentThread().getContextClassLoader().getResource(TOOL_SMS).getFile()).getAbsolutePath();
            String file = new File(TOOL_SMS).getAbsolutePath();
            System.out.println(file);
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", file, phone, content);

            pb.redirectErrorStream(true);
            final Process process = pb.start();

            InputStream stderr = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                LOGGER.info(line);
            }
            process.waitFor();
            LOGGER.info("SEND SMS SUCCESSFULLY");
        } catch (Exception e) {
            LOGGER_4J.error("SEND SMS FAIL", e);
        }
    }
}
