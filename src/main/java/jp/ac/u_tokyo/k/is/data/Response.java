package jp.ac.u_tokyo.k.is.data;

public class Response {
	private int qandaId;
	private int questionId;
	private String questionContent;
	private String answerCode;
	private String savedCode;
	private String consoleContent;
	private String username;
	private String questionComment;
	private String responseComment;
	private String respondant;
	private int solvedFlag;
	private boolean objectOriented;
	private String className;

	public int getQAndAId() {
		return qandaId;
	}
	public void setQAndAId(int qandaId) {
		this.qandaId = qandaId;
	}
	public String getQuestionContent() {
		return questionContent;
	}
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	public String getAnswerCode() {
		return answerCode;
	}
	public void setAnswerCode(String answerCode) {
		this.answerCode = answerCode;
	}
	public String getSavedCode() {
		return savedCode;
	}
	public void setSavedCode(String savedCode) {
		this.savedCode = savedCode;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getConsoleContent() {
		return consoleContent;
	}
	public void setConsoleContent(String consoleContent) {
		this.consoleContent = consoleContent;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getQuestionComment() {
		return questionComment;
	}
	public void setQuestionComment(String questionComment) {
		this.questionComment = questionComment;
	}
	public String getResponseComment() {
		return responseComment;
	}
	public void setResponseComment(String responseComment) {
		this.responseComment = responseComment;
	}
	public String getRespondant() {
		return respondant;
	}
	public void setRespondant(String respondant) {
		this.respondant = respondant;
	}
	public int getSolvedFlag() {
		return solvedFlag;
	}
	public void setSolvedFlag(int solvedFlag) {
		this.solvedFlag = solvedFlag;
	}
	public boolean isObjectOriented() {
		return objectOriented;
	}
	public void setObjectOriented(boolean objectOriented) {
		this.objectOriented = objectOriented;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
