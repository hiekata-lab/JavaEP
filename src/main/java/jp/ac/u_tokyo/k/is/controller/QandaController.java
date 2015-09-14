package jp.ac.u_tokyo.k.is.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import jp.ac.u_tokyo.k.is.dao.QAndADao;
import jp.ac.u_tokyo.k.is.dao.QuestionDao;
import jp.ac.u_tokyo.k.is.data.QAndA;
import jp.ac.u_tokyo.k.is.data.Question;
import jp.ac.u_tokyo.k.is.data.Response;
import jp.ac.u_tokyo.k.is.data.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QandaController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private QAndADao qandaDao;
	
	@Autowired
	private QuestionDao questionDao;
	
	public void setQandaDao(QAndADao qandaDao) {
		this.qandaDao = qandaDao;
	}

	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}

	@RequestMapping(value = "/qanda", method = RequestMethod.GET)
	public String qanda(){
		return "qanda/qanda";
	}

	@RequestMapping(value = "/qanda/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<QAndA> list() throws Exception {
		List<QAndA> qandas = qandaDao.getAll();
		return qandas;
	}
	
	@RequestMapping(value = "/qanda/response", method = RequestMethod.GET)
	public String response(Model model, String qandaId){
		model.addAttribute("qandaId", qandaId);
		return "qanda/response";
	}

	@RequestMapping(value = "/qanda/response/get", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response get(String qandaId) throws Exception {
		QAndA qanda = qandaDao.findById(Integer.valueOf(qandaId));

		//問題の取得
		int questionId = qanda.getQuestionId();
		Question question = questionDao.findById(questionId);

		Response resp = new Response();
		resp.setQAndAId(Integer.valueOf(qandaId));//質問番号
		resp.setQuestionId(questionId);//問題番号
		resp.setQuestionContent(question.getContent());//問題内容
		resp.setAnswerCode(question.getSource());//正解コード
		resp.setSavedCode(qanda.getSource());//学生のコード
		resp.setConsoleContent(qanda.getConsole());//コンソール内容
		resp.setQuestionComment(qanda.getQuestionComment());
		resp.setResponseComment(qanda.getResponseComment());
		resp.setUsername(qanda.getUsername());
		resp.setRespondant(qanda.getRespondant());
		resp.setSolvedFlag(qanda.getSolvedFlag());
		
		if (User.isAdmin(request)) {
			resp.setAnswerCode(question.getSource());//正解コード
		}
		
		// for OOP question
		resp.setObjectOriented(question.getObjectOriented());
		resp.setClassName(question.getClassName());

		return resp;
	}

	@RequestMapping(value = "/qanda/response/answer", method = RequestMethod.POST)
	@ResponseBody
	public String answer(String qandaId, String responseComment) throws Exception {
		qandaDao.updateResponseComment(Integer.parseInt(qandaId), responseComment, User.getUsername(request));
		return "saved.";
	}

	@RequestMapping(value = "/qanda/response/comment", method = RequestMethod.POST)
	@ResponseBody
	public String comment(String qandaId, String questionComment) throws Exception {
		qandaDao.updateQuestionComment(Integer.parseInt(qandaId), questionComment);
		return "saved.";
	}

	@RequestMapping(value = "/qanda/response/setFlag", method = RequestMethod.POST)
	@ResponseBody
	public void setFlag(String qandaId, String solvedFlag) throws Exception {
		qandaDao.updateFlag(Integer.parseInt(qandaId), Integer.parseInt(solvedFlag));
	}
}
