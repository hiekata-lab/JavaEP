package jp.ac.u_tokyo.k.is.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ac.u_tokyo.k.is.data.Question;

import org.springframework.jdbc.core.RowMapper;

public class QuestionMapper implements RowMapper<Question> {
	public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
		Question question = new Question();
		question.setId(rs.getInt("id"));
		question.setDifficulty(rs.getInt("difficulty"));
		question.setContent(rs.getString("content"));
		question.setArgs(rs.getString("args"));
		question.setAnswers(rs.getString("answers"));
		question.setSource(rs.getString("source"));
		byte b = rs.getByte("object_oriented");
		boolean objectOriented = (b != 0) ? true : false;
		question.setObjectOriented(objectOriented);
		question.setClassName(rs.getString("class_name"));
		b = rs.getByte("shown_in_exam");
		boolean shownInExam = (b != 0) ? true : false;
		question.setShownInExam(shownInExam);
		return question;
	}
}
