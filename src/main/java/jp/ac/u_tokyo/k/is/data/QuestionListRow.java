package jp.ac.u_tokyo.k.is.data;

public class QuestionListRow {
	private int id;
	private int score;
	private int difficulty;
	private String content;
	private boolean shownInExam;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean getShownInExam() {
		return shownInExam;
	}
	public void setShownInExam(boolean shownInExam) {
		this.shownInExam = shownInExam;
	}
}
