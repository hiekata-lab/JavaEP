package jp.ac.u_tokyo.k.is.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.ac.u_tokyo.k.is.data.User;

import org.springframework.web.multipart.MultipartFile;

public class UserGenerator {
	public static List<User> execute(MultipartFile file) throws IllegalStateException, IOException {
		File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + file.getOriginalFilename());
		file.transferTo(tmpFile);

		BufferedReader br = new BufferedReader(new FileReader(tmpFile));
		String line;

		List<User> usersList = new ArrayList<User>();

		while ((line = br.readLine())!= null) {
			User user = new User();
			user.setUsername(line);
			
			String password = "!" + line;
			user.setPassword(password);
			user.setPasswordSHA256(Util.getSHA256(password));

			usersList.add(user);
		}

		br.close();
		tmpFile.delete();

		return usersList;
	}
}
