package jp.ac.u_tokyo.k.is.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import jp.ac.u_tokyo.k.is.dao.QAndADao;
import jp.ac.u_tokyo.k.is.dao.QuestionDao;
import jp.ac.u_tokyo.k.is.dao.StudentsStateDao;
import jp.ac.u_tokyo.k.is.dao.UserDao;
import jp.ac.u_tokyo.k.is.data.QAndA;
import jp.ac.u_tokyo.k.is.data.Question;
import jp.ac.u_tokyo.k.is.data.StudentsState;
import jp.ac.u_tokyo.k.is.data.User;
import jp.ac.u_tokyo.k.is.logic.Compiler;
import jp.ac.u_tokyo.k.is.logic.Executor;
import jp.ac.u_tokyo.k.is.logic.GarbageCollector;
import jp.ac.u_tokyo.k.is.logic.Marker;
import jp.ac.u_tokyo.k.is.logic.QuestionGenerator;
import jp.ac.u_tokyo.k.is.logic.Util;

import org.apache.jena.atlas.json.JsonArray;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuestionController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private Properties javaepProperties;

	@Autowired
	private UserDao userDao;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private StudentsStateDao studentsStateDao;

	@Autowired
	private QAndADao qandaDao;

	private static GarbageCollector gc = new GarbageCollector();

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}

	public void setStudentsStateDao(StudentsStateDao studentsStateDao) {
		this.studentsStateDao = studentsStateDao;
		this.studentsStateDao.setTableName(javaepProperties.getProperty("javaep.mode"));
	}

	public void setQandaDao(QAndADao qandaDao) {
		this.qandaDao= qandaDao;
	}

	public void init() throws Exception {
		if (!gc.isAlive()) {
			gc.start();
		}
	}

	public void destroy() throws Exception {
		gc.halt();
	}

	/* start question */

	@RequestMapping(value = "/question", method = RequestMethod.POST)
	public String question(Model model, @RequestParam("questionId") int questionId) throws Exception {
		String mode = javaepProperties.getProperty("javaep.mode");
		model.addAttribute("questionId", questionId);
		model.addAttribute("mode", mode);
		return "question/question";
	}

	@RequestMapping(value = "/question/show", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> show(@RequestParam("questionId") int questionId) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();

		Question q = questionDao.findById(questionId);
		if (q == null) {
			return responseMap;
		}

		responseMap.put("question", q.getContent());
		responseMap.put("difficulty", q.getDifficulty());
		responseMap.put("objectOriented", q.getObjectOriented());
		responseMap.put("className", q.getClassName());
		
		if (User.isAdmin(request)) {
			responseMap.put("source", q.getSource());
			responseMap.put("args", q.getArgs());
		}

		String username = User.getUsername(request);
		StudentsState state = studentsStateDao.find(username, questionId);
		if (state != null) {
			responseMap.put("saved", state.getSource());
			responseMap.put("passed", state.getPassed());
		}

		int numOfPassed = studentsStateDao.getNumOfPassed(questionId);
		responseMap.put("numOfPassed", numOfPassed);

		return responseMap;
	}

	@RequestMapping(value = "/question/execute", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> execute(@RequestParam("questionId") int questionId, @RequestParam("classname") String classname, @RequestParam("args") String args) throws Exception {
		String username = User.getUsername(request);
		String webinfDir = servletContext.getRealPath("/WEB-INF");

		Map<String, Object> responseMap = new HashMap<String, Object>();

		File classesDir = new File(webinfDir, "users");
		File userDir = new File(classesDir.getPath(), username);
		File javaPolicy = new File(webinfDir, "conf/java.policy");

		if (userDir.exists()) {
			Executor executor = new Executor(userDir.getPath(), javaPolicy.getPath());
			boolean success = executor.execute(classname, args);

			responseMap.put("success", success);
			responseMap.put("message", executor.getMessage());
		} else {
			responseMap.put("success", false);
			responseMap.put("message", "Class file's direcotry was not found.");
		}

		return responseMap;
	}

	@RequestMapping(value = "/question/save", method = RequestMethod.POST)
	public String save(@RequestParam("questionId") int questionId, @RequestParam("source") String source) throws Exception {
		String username = User.getUsername(request);
		StudentsState state = studentsStateDao.find(username, questionId);

		if (state == null) {
			studentsStateDao.create(username, questionId, source, false);
		} else {
			studentsStateDao.update(username, questionId, source, state.getPassed());
		}

		return "question/question";
	}

	@RequestMapping(value = "/question/compile", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> compile(@RequestParam("questionId") int questionId, @RequestParam("classname") String classname, @RequestParam("source") String source) throws Exception {
		String username = User.getUsername(request);
		String path = servletContext.getRealPath("/WEB-INF");

		File classesDir = new File(path, "users");
		Util.mkdir(classesDir);

		File userDir = new File(classesDir.getPath(), username);
		Util.mkdir(userDir);

		Compiler compiler = new Compiler(userDir.getPath());
		boolean success = compiler.compile(classname, source);

		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("success", success);
		responseMap.put("message", compiler.getMessage());

		return responseMap;
	}

	@RequestMapping(value = "/question/mark", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> mark(@RequestParam("questionId") int questionId, @RequestParam("classname") String classname, @RequestParam("source") String source) throws Exception {
		String username = User.getUsername(request);
		String webinfDir = servletContext.getRealPath("/WEB-INF");

		Map<String, Object> responseMap = new HashMap<String, Object>();

		Question q = questionDao.findById(questionId);
		if (q == null) {
			responseMap.put("success", false);
			responseMap.put("message", "Question was not found.");
			return responseMap;
		}

		File classesDir = new File(webinfDir, "users");
		Util.mkdir(classesDir);

		File userDir = new File(classesDir.getPath(), username);
		Util.mkdir(userDir);

		File javaPolicy = new File(webinfDir, "conf/java.policy");

		String[] args = Util.parseJsonToArray(q.getArgs());
		String[] answers = Util.parseJsonToArray(q.getAnswers());

		if (args.length != answers.length) {
			responseMap.put("success", false);
			responseMap.put("message", "The number of args is not equal to the number of answers.");
			return responseMap;
		}

		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].trim();
			answers[i] = answers[i].trim();
		}

		Marker marker = new Marker(userDir.getPath());
		boolean success;

		if (q.getObjectOriented()) {
			success = marker.mark(classname, source, args, answers, javaPolicy.getPath(), q.getClassName());
		} else {
			success = marker.mark(classname, source, args, answers, javaPolicy.getPath());
		}

		if (success) {
			StudentsState state = studentsStateDao.find(username, questionId);
			String mode = javaepProperties.getProperty("javaep.mode");

			if (state == null) {
				studentsStateDao.create(username, questionId, source, true);

				if (mode.equals("exam")) {
					userDao.addExamScore(username, q.getDifficulty());
				} else {
					userDao.addScore(username, q.getDifficulty());
				}
			} else {
				studentsStateDao.update(username, questionId, source, true);
				if (!state.getPassed()) {
					if (mode.equals("exam")) {
						userDao.addExamScore(username, q.getDifficulty());
					} else {
						userDao.addScore(username, q.getDifficulty());
					}
				}
			}
		} else {
			StudentsState state = studentsStateDao.find(username, questionId);
			String mode = javaepProperties.getProperty("javaep.mode");

			if (state == null) {
				studentsStateDao.create(username, questionId, source, false);
			} else {
				studentsStateDao.update(username, questionId, source, false);
				if (state.getPassed()) {
					if (mode.equals("exam")) {
						userDao.subExamScore(username, q.getDifficulty());
					} else {
						userDao.subScore(username, q.getDifficulty());
					}
				}
			}
		}

		responseMap.put("success", success);
		responseMap.put("message", marker.getMessage());

		return responseMap;
	}

	/* end question */

	/* start create & edit */

	@RequestMapping(value = "/question/create", method = RequestMethod.GET)
	public String create(Model model) throws Exception {
		model.addAttribute("questionId", questionDao.count() + 1);
		return "question/edit";
	}

	@RequestMapping(value = "/question/edit", method = RequestMethod.GET)
	public String edit(Model model, @RequestParam("questionId") int questionId) throws Exception {
		model.addAttribute("questionId", questionId);
		return "question/edit";
	}

	@RequestMapping(value = "/question/edit/save", method = RequestMethod.POST)
	public String editSave(
			@RequestParam("questionId") int questionId,
			@RequestParam("question") String content,
			@RequestParam("source") String source,
			@RequestParam("difficulty") int difficulty,
			@RequestParam("args") String args,
			@RequestParam("objectOriented") boolean objectOriented,
			@RequestParam("className") String className) throws Exception
	{
		String webinfDir = servletContext.getRealPath("/WEB-INF");
		File tmpDir = new File(webinfDir, "tmp");
		Util.mkdir(tmpDir);
		File javaPolicy = new File(webinfDir, "conf/java.policy");
		String answers = QuestionGenerator.getResult(args, tmpDir.getAbsolutePath(), questionId, source, javaPolicy.getAbsolutePath());

		Question question = questionDao.findById(questionId);
		if (question == null) {
			questionDao.create(difficulty, content, args, answers, source, objectOriented, className);
		} else {
			questionDao.update(questionId, difficulty, content, args, answers, source, objectOriented, className);
		}

		return "question/question";
	}

	/* end crete & edit */

	/* start Q&A */

	/**
	 * 類似エラーコードの回答を取得
	 * @param consoleText
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/question/getSimilarQandas", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getSimilarQandas(String consoleText) throws Exception {
		//1に近いほど似ている
		LevensteinDistance lalgo = new LevensteinDistance();
		List<QAndA> qandas = qandaDao.getAll();

		//System.out.println(consoleText);
		System.out.println("-------------");
		String splitTerm = "Error on line";
		String prefixTerm = "エラー: ";
		String[] errors = consoleText.split(splitTerm);

		Set<String> consoleSet = new HashSet<String>();

		for(String error : errors){
			if(error.length() != 0 ){
				String errorText = splitTerm + error;
				if(errorText.indexOf(prefixTerm) != -1){
					errorText = errorText.split(prefixTerm)[1];

					/*
					errorText = errorText.split("\r\n")[0];
					errorText = errorText.split("\r")[0];
					errorText = errorText.split("\n")[0];
					*/
				}

				consoleSet.add(errorText);

				System.out.println(errorText);
				System.out.println("-------------");
			}
		}

		JsonArray array = new JsonArray();

		for(QAndA qanda : qandas){
			String existingText = qanda.getConsole();

			Set<String> errorSet = new HashSet<String>();

			String[] existingErrors = existingText.split(splitTerm);

			for(String error : existingErrors){
				if(error.length() != 0 ){
					String errorText = splitTerm + error;
					if(errorText.indexOf(prefixTerm) != -1){
						errorText = errorText.split(prefixTerm)[1];

						/*
						errorText = errorText.split("\r\n")[0];
						errorText = errorText.split("\r")[0];
						errorText = errorText.split("\n")[0];
						*/
					}

					errorSet.add(errorText);

				}
			}

			double similarity = 0;
			for(String cError : consoleSet){
				for(String eError : errorSet){
					double sim = lalgo.getDistance(cError, eError);
					if(sim > similarity){
						similarity = sim;
					}
				}
			}

			int id = qanda.getId();

			/*
			str = existingText.split(":");
			String existingErrorContents = "";
			for(int i = 2; i < str.length; i++){
				existingErrorContents += str[i];
			}
			*/
			//double similarity = lalgo.getDistance(errorContents, existingErrorContents);
			//double similarity = lalgo.getDistance(consoleText, existingText);
			String responseComment = qanda.getResponseComment();
			if(responseComment != null && responseComment.length() != 0){
				if(similarity > 0.4){//閾値
					BigDecimal bd = new BigDecimal(similarity);
					JsonObject json = new JsonObject();
					json.put("id", id);
					//小数点第二位を四捨五入
					json.put("value", bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
					json.put("comment", responseComment);
					array.add(json);
				}
			}
		}
		return array.toString();
	}

	@RequestMapping(value = "/question/setQanda", method = RequestMethod.POST)
	@ResponseBody
	public String setQanda(String questionId, String username, String source, String console, String comment) throws Exception {
		qandaDao.create(Integer.valueOf(questionId), username, source, console, comment);
		return "saved.";
	}

	@RequestMapping(value = "/question/getQandaList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<QAndA> getQandaList(String questionId) throws Exception {
		List<QAndA> qandas = qandaDao.findByQuestionAndUsername(Integer.parseInt(questionId), User.getUsername(request));
		return qandas;
	}

	//いいねボタンの追加
	@RequestMapping(value = "/question/setInquiry", method = RequestMethod.POST)
	@ResponseBody
	public void setInquiry(String questionIds) throws Exception {
		String[] str = questionIds.split(",");
		for(String id : str){
			int intId = Integer.parseInt(id);
			QAndA qanda = qandaDao.findById(intId);
			int likeCount = qanda.getLikeCount() + 1;
			qandaDao.updateLikeCount(intId, likeCount);
		}
	}

	/* end Q&A */
}
