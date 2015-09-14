package jp.ac.u_tokyo.k.is.data;

public class Question {
	private int id;
	private int difficulty;
	private String content;
	private String args;
	private String answers;
	private String source;
	private boolean objectOriented;
	private String className;
	private boolean shownInExam;

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	public int getDifficulty() {
		return difficulty;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}

	public void setArgs(String args) {
		this.args = args;
	}
	public String getArgs() {
		return args;
	}

	public void setAnswers(String answer) {
		this.answers = answer;
	}
	public String getAnswers() {
		return answers;
	}

	public void setSource(String source) {
		this.source = source;
	}
	public String getSource() {
		return source;
	}
	
	public void setObjectOriented(boolean objectOriented) {
		this.objectOriented = objectOriented;
	}
	public boolean getObjectOriented() {
		return objectOriented;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassName() {
		return className;
	}
	
	public boolean getShownInExam() {
		return shownInExam;
	}
	public void setShownInExam(boolean shownInExam) {
		this.shownInExam = shownInExam;
	}
}
