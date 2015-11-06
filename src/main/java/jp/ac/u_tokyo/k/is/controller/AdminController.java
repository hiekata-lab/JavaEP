package jp.ac.u_tokyo.k.is.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ac.u_tokyo.k.is.dao.AuthorityDao;
import jp.ac.u_tokyo.k.is.dao.QuestionDao;
import jp.ac.u_tokyo.k.is.dao.UserDao;
import jp.ac.u_tokyo.k.is.dao.StudentsStateDao;
import jp.ac.u_tokyo.k.is.data.Question;
import jp.ac.u_tokyo.k.is.data.StudentsState;
import jp.ac.u_tokyo.k.is.data.User;
import jp.ac.u_tokyo.k.is.data.UserListRow;
import jp.ac.u_tokyo.k.is.logic.Marker;
import jp.ac.u_tokyo.k.is.logic.QuestionGenerator;
import jp.ac.u_tokyo.k.is.logic.UserGenerator;
import jp.ac.u_tokyo.k.is.logic.Util;

import org.apache.lucene.analysis.reverse.ReverseStringFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private StudentsStateDao studentsStateDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AuthorityDao authorityDao;
	
	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setAuthorityDao(AuthorityDao authorityDao) {
		this.authorityDao = authorityDao;
	}
	
	public void setStuendtStateDao(StudentsStateDao studentsStateDao) {
		this.studentsStateDao = studentsStateDao;
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin() throws Exception {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public String users() throws Exception {
		return "admin/users";
	}

	/**
	 * 問題の登録メソッド
	 * @param dstPath
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/generate", method = RequestMethod.POST)
	@ResponseBody
	public String generate(String dstPath) throws Exception {
		//javaPolicyFileのパスの取得
		String webinfDir = servletContext.getRealPath("/WEB-INF");
		File javaPolicy = new File(webinfDir, "conf/java.policy");

		//QuestionIDとQuestionのマップを取得
		Map<Integer, Question> questionMap = QuestionGenerator.execute(dstPath, javaPolicy.getAbsolutePath());

		for(Entry<Integer, Question> e : questionMap.entrySet()){
			int id = e.getKey();
			Question que = e.getValue();

			//新規登録か更新かの場合分け
			if(questionDao.findById(id) == null){
				questionDao.create(que.getDifficulty(), que.getContent(), que.getArgs(), que.getAnswers(), que.getSource());
			} else {
				questionDao.update(id, que.getDifficulty(), que.getContent(), que.getArgs(), que.getAnswers(), que.getSource());
			}

		}

		return "succeed";
	}

	/**
	 * 問題の登録メソッド
	 * @param dstPath
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/users", method = RequestMethod.POST)
	@ResponseBody
	public void generateUsers(@RequestParam("file") MultipartFile file, HttpServletResponse resp) throws IllegalStateException, IOException {
		List<User> usersList = UserGenerator.execute(file);

		//結果のダウンロード
		File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + file.getOriginalFilename());
		PrintWriter pw = new PrintWriter(new FileWriter(tmpFile));

		//初期化
		//authorityDao.deleteAll();
		//userDao.deleteAll();

		for(User user : usersList){
			userDao.create(user.getUsername(), user.getPasswordSHA256());
			authorityDao.create(user.getUsername(), "ROLE_USER");
			pw.println(user.getUsername()+","+user.getPassword());
		}

		pw.close();


		//ダウンロード用
		Path path = Paths.get(tmpFile.getAbsolutePath());
		byte[] data = Files.readAllBytes(path);

		resp.setContentType("csv");
		resp.setContentLength(data.length);
		resp.setHeader("Content-Disposition","attachment; filename=\"result.csv\"");
		try {
			resp.getOutputStream().write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		tmpFile.delete();
	}

	/**
	 * 登録済みユーザか新規登録かの検索
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/search", method = RequestMethod.POST)
	@ResponseBody
	public String search(String username) throws Exception {
		if(userDao.checkExistByUsername(username)){
			return "exist";
		} else {
			return "new";
		}
	}

	/**
	 * 登録済みユーザか新規登録かの検索
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/set", method = RequestMethod.POST)
	@ResponseBody
	public String set(String username, String password, String authority) throws Exception {
		String passwordSha256 = Util.getSHA256(password);

		//存在する場合
		if(userDao.checkExistByUsername(username)){
			userDao.updatePassword(username, passwordSha256);
		} else {
			userDao.create(username, passwordSha256);
			authorityDao.create(username, authority);
		}

		return "succeed";
	}
	
	@RequestMapping(value = "/admin/showUsers", method = RequestMethod.GET)
	@ResponseBody
	public List<UserListRow> showUsers() throws Exception {
		return userDao.findAllWithAuthority();
	}
	
	@RequestMapping(value = "/admin/exportExamScore", method = RequestMethod.GET)
	public void exportExamScore(HttpServletResponse response) throws Exception {
		List<User> users = userDao.findAll();
		
		File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "exam_score.csv");
		PrintWriter pw = new PrintWriter(new FileWriter(tmpFile));
		
		Date date = new Date();
		pw.println(date.toString());
		
		pw.println("Question ID");
		List<Question> questions = questionDao.findExamQuestions();
		for (Question question : questions) {
			pw.print(question.getId() + ",");
		}
		pw.println();
		
		pw.println("username,exam_score");
		for (User user : users) {
			pw.println(user.getUsername() + "," + user.getExamScore());
		}

		pw.close();

		Path path = Paths.get(tmpFile.getAbsolutePath());
		byte[] data = Files.readAllBytes(path);

		response.setContentType("csv");
		response.setContentLength(data.length);
		response.setHeader("Content-Disposition", "attachment; filename=\"exam_score.csv\"");
		
		try {
			response.getOutputStream().write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		tmpFile.delete();
	}
	
	// TODO : 採点部分のリファクタリング
	/**
	 * 自動採点
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/markAll", method = RequestMethod.POST)
	@ResponseBody
	public String markAll() throws Exception {
		List<UserListRow> users = userDao.findAllWithAuthority();
		Boolean successFlg = true;
		for (UserListRow user : users) {
			if (user.getAuthority().equals("ROLE_ADMIN")) continue;
			List<StudentsState> states = studentsStateDao.findExamDataByUsername(user.getUsername());
			for (StudentsState state : states) {
				
				// Start Mark
				Question q = questionDao.findById(state.getQuestionId());
				if (q == null) {
					successFlg = false;
				}

				String webinfDir = servletContext.getRealPath("/WEB-INF");
				File classesDir = new File(webinfDir, "users");
				Util.mkdir(classesDir);

				File userDir = new File(classesDir.getPath(), user.getUsername());
				Util.mkdir(userDir);

				File javaPolicy = new File(webinfDir, "conf/java.policy");

				String[] args = Util.parseJsonToArray(q.getArgs());
				String[] answers = Util.parseJsonToArray(q.getAnswers());

				if (args.length != answers.length) {
					successFlg = false;
				}

				for (int i = 0; i < args.length; i++) {
					args[i] = args[i].trim();
					answers[i] = answers[i].trim();
				}

				Marker marker = new Marker(userDir.getPath());
				boolean success;
				String classname = "Question" + state.getQuestionId();
				String username = state.getUsername();
				int questionId = state.getQuestionId();
				String source = state.getSource();
				if (q.getObjectOriented()) {
					success = marker.mark(classname, source, args, answers, javaPolicy.getPath(), q.getClassName());
				} else {
					success = marker.mark(classname, source, args, answers, javaPolicy.getPath());
				}

				if (success) {
					studentsStateDao.updateExam(username, questionId, source, marker.getMessage(), "admin", true);
					if (!state.getPassed()) {
						userDao.addExamScore(username, q.getDifficulty());
					}
				} else {
					studentsStateDao.updateExam(username, questionId, source, marker.getMessage(), "admin", false);
				}
				// End Mark
			}
		}
		if (!successFlg) {
			return "fail";
		}
		return "secceed";
	}
	
	/**
	 * 点数計算
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/calcExamScore", method = RequestMethod.POST)
	@ResponseBody
	public String calcExamScore() throws Exception {
		userDao.calcExamScore();
		return "succeed";
	}
	
}
