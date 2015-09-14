package jp.ac.u_tokyo.k.is.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ac.u_tokyo.k.is.data.QAndA;

import org.springframework.jdbc.core.RowMapper;

public class QAndAMapper implements RowMapper<QAndA> {
	public QAndA mapRow(ResultSet rs, int rowNum) throws SQLException {
		QAndA qanda = new QAndA();
		qanda.setId(rs.getInt("id"));
		qanda.setQuestionId(rs.getInt("question_id"));
		qanda.setUsername(rs.getString("username"));
		qanda.setSource(rs.getString("source"));
		qanda.setConsole(rs.getString("console"));
		qanda.setQuestionComment(rs.getString("question_comment"));
		qanda.setResponseComment(rs.getString("response_comment"));
		qanda.setResponsedFlag(rs.getInt("responsed_flag"));
		qanda.setSolvedFlag(rs.getInt("solved_flag"));
		qanda.setRespondant(rs.getString("respondant"));
		qanda.setCreated(rs.getTimestamp("created"));
		qanda.setResponsed(rs.getTimestamp("responsed"));
		qanda.setSolved(rs.getTimestamp("solved"));
		qanda.setLikeCount(rs.getInt("like_count"));
		return qanda;
	}
}
