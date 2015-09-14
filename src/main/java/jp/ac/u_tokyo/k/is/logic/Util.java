package jp.ac.u_tokyo.k.is.logic;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class Util {
	// 文字列から SHA256 のハッシュ値を取得
	public static String getSHA256(String target) {
		MessageDigest md = null;
		StringBuffer buf = new StringBuffer();
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(target.getBytes());
			byte[] digest = md.digest();

			for (int i = 0; i < digest.length; i++) {
				buf.append(String.format("%02x", digest[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return buf.toString();
	}
	
	public static void mkdir(File dir) {
		if (!dir.exists()) {
			dir.mkdir();
		}
	}
	
	public static File[] listFiles(String directory, String extension) {
		File file = new File(directory);
		return file.listFiles(getFileExtensionFilter(extension));
	}
	
	public static FilenameFilter getFileExtensionFilter(String extension) {
		final String ext = extension;
		return new FilenameFilter() {
			public boolean accept(File file, String name) {
				boolean ret = name.endsWith(ext);
				return ret;
			}
		};
	}
	
	public static String[] parseJsonToArray(String jsonString) throws IOException {
		List<String> list = new ArrayList<String>();

		try {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createJsonParser(jsonString);

			if (parser.nextToken() == JsonToken.START_ARRAY) {
				while (parser.nextToken() != JsonToken.END_ARRAY) {
					if (parser.getCurrentToken() == JsonToken.VALUE_STRING) {
						list.add(parser.getText());
					}
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		}

		return list.toArray(new String[list.size()]);
	}
}
