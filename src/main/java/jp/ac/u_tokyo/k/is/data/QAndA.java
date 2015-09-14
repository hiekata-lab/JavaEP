package jp.ac.u_tokyo.k.is.data;

import java.sql.Timestamp;

public class QAndA {
	private int id;
	private int questionId;
	private String username;
	private String source;
	private String console;
	private String questionComment;
	private String responseComment;
	private int responsedFlag;
	private int solvedFlag;
	private String respondant;
	private Timestamp created;
	private Timestamp responsed;
	private Timestamp solved;
	private int likeCount;

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRespondant() {
		return respondant;
	}
	public void setRespondant(String respondant) {
		this.respondant = respondant;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getConsole() {
		return console;
	}
	public void setConsole(String console) {
		this.console = console;
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
	public int getResponsedFlag() {
		return responsedFlag;
	}
	public void setResponsedFlag(int responsedFlag) {
		this.responsedFlag = responsedFlag;
	}
	public int getSolvedFlag() {
		return solvedFlag;
	}
	public void setSolvedFlag(int solvedFlag) {
		this.solvedFlag = solvedFlag;
	}
	public Timestamp getSolved() {
		return solved;
	}
	public void setSolved(Timestamp solved) {
		this.solved = solved;
	}
	public Timestamp getResponsed() {
		return responsed;
	}
	public void setResponsed(Timestamp responsed) {
		this.responsed = responsed;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
}
