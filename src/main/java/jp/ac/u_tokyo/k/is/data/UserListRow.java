package jp.ac.u_tokyo.k.is.data;

public class UserListRow extends Authority {
	private int score;
	private int examScore;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getExamScore() {
		return examScore;
	}

	public void setExamScore(int examScore) {
		this.examScore = examScore;
	}
}
