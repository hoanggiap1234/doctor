package com.viettel.etc.utils;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

public class Base64Util implements MultipartFile {

	private static final Logger LOGGER = Logger.getLogger(String.valueOf(Base64Util.class));
	private final byte[] imgContent;

	private final String header;

	public Base64Util(byte[] imgContent, String header) {
		this.imgContent = imgContent;
		this.header = header.split(";")[0];
	}

	public static boolean isBase64String(String base64value) {
		String[] stra = base64value.split(",");
		ArrayList<String> strList = (ArrayList<String>) Arrays.stream(stra)
				.collect(Collectors.toList());
		if (strList.size() != 2) {
			return false;
		}
		return org.apache.commons.codec.binary.Base64.isBase64(strList.get(1));
	}

	public static String toBase64(String filePath) {
		if (filePath == null) {
			return "";
		}
		StringBuilder base64File = new StringBuilder();
		File file = new File(filePath);
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a file from file system
			byte fileData[] = new byte[(int) file.length()];
			imageInFile.read(fileData);
			base64File.append("data:");
			base64File.append(Files.probeContentType(file.toPath()));
			base64File.append(";base64,");
			base64File.append(Base64.getEncoder().encodeToString(fileData));
		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the file " + ioe);
		}
		return base64File.toString();
	}

	public static MultipartFile base64ToMultipart(String base64) {
		try {
			String[] baseStrs = base64.split(",");

			BASE64Decoder decoder = new BASE64Decoder();
//            byte[] b = new byte[0];
			byte[] b = decoder.decodeBuffer(baseStrs[1]);

			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			return new Base64Util(b, baseStrs[0]);
		} catch (IOException e) {
			LOGGER.info(e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getName() {
		return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
	}

	@Override
	public String getOriginalFilename() {
		return System.currentTimeMillis() + (int) Math.random() * 10000 + "." + header
				.split("/")[1];
	}

	@Override
	public String getContentType() {
		return header.split(":")[1];
	}

	@Override
	public boolean isEmpty() {
		return imgContent == null || imgContent.length == 0;
	}

	@Override
	public long getSize() {
		return imgContent.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return imgContent;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(imgContent);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
//        new FileOutputStream(dest).write(imgContent);
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(dest);
			stream.write(imgContent);
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}
}

