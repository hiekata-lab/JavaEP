package jp.ac.u_tokyo.k.is.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import jp.ac.u_tokyo.k.is.dao.AuthorityDao;
import jp.ac.u_tokyo.k.is.dao.QuestionDao;
import jp.ac.u_tokyo.k.is.dao.UserDao;
import jp.ac.u_tokyo.k.is.logic.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SettingController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private QuestionDao questionDao;

	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}

	@Autowired
	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	private AuthorityDao authorityDao;

	public void setAuthorityDao(AuthorityDao authorityDao) {
		this.authorityDao= authorityDao;
	}

	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String user() throws Exception {
		return "setting/setting";
	}
	
	@RequestMapping(value = "/setting/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	public String updatePassword(String username, String password) throws Exception {
		String passwordSha256 = Util.getSHA256(password);
		
		try {
			userDao.updatePassword(username, passwordSha256);
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed.";
		}
		
		return "Succeeded.";
	}
}
