package jp.ac.u_tokyo.k.is.data;

public class StudentsState {
	private String username;
	private int questionId;
	private String source;
	private boolean passed;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public boolean getPassed() {
		return passed;
	}
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
}
