package jp.ac.u_tokyo.k.is.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import jp.ac.u_tokyo.k.is.dao.QuestionDao;
import jp.ac.u_tokyo.k.is.dao.StudentsStateDao;
import jp.ac.u_tokyo.k.is.dao.UserDao;
import jp.ac.u_tokyo.k.is.data.Question;
import jp.ac.u_tokyo.k.is.data.QuestionListRow;
import jp.ac.u_tokyo.k.is.data.StudentsState;
import jp.ac.u_tokyo.k.is.data.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private Properties javaepProperties;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private StudentsStateDao studentsStateDao;
	
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
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) throws Exception {
		String mode = javaepProperties.getProperty("javaep.mode");
		model.addAttribute("mode", mode);
		return "index";
	}
	
	@RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ResponseBody
	public Collection<QuestionListRow> list() {
		List<Question> questions = questionDao.findAll();
		return createQuestionListRows(questions);
	}
	
	@RequestMapping(value = "/examList", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ResponseBody
	public Collection<QuestionListRow> examList() {
		List<Question> questions;
		if (User.isAdmin(request)){
			questions = questionDao.findAll();
		} else {
			questions = questionDao.findExamQuestions();
		}
		
		return createQuestionListRows(questions);
	}
	
	@RequestMapping(value = "/score", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ResponseBody
	public int[] score() {
		String mode = javaepProperties.getProperty("javaep.mode");
		if (mode.equals("exam")) {
			return userDao.getExamHigherRank(5);
		} else {
			return userDao.getHigherRank(5);
		}
	}
	
	@RequestMapping(value = "/setShownInExam", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ResponseBody
	public String setShownInExam(@RequestParam("questionId") int questionId, @RequestParam("shownInExam") boolean shownInExam) {
		questionDao.update(questionId, shownInExam);
		return "succeeded";
	}
	
	private Collection<QuestionListRow> createQuestionListRows(List<Question> questions) {
		String username = User.getUsername(request);
		List<StudentsState> studentStatus = studentsStateDao.findByUsername(username);
		Map<Integer, QuestionListRow> rowsMap = new HashMap<Integer, QuestionListRow>();
		
		for (Question q : questions) {
			QuestionListRow row = new QuestionListRow();
			row.setId(q.getId());
			row.setScore(0);
			row.setDifficulty(q.getDifficulty());
			row.setContent(q.getContent());
			row.setShownInExam(q.getShownInExam());
			rowsMap.put(q.getId(), row);
		}
		
		for (StudentsState s : studentStatus) {
			if (s.getPassed()) {
				int id = s.getQuestionId();
				QuestionListRow row = rowsMap.get(id);
				if (row != null) {
					row.setScore(row.getDifficulty());
				}
			}
		}
		
		return rowsMap.values();
	}
}
