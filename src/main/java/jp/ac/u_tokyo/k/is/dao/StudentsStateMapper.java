package jp.ac.u_tokyo.k.is.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ac.u_tokyo.k.is.data.StudentsState;

import org.springframework.jdbc.core.RowMapper;

public class StudentsStateMapper implements RowMapper<StudentsState> {
	public StudentsState mapRow(ResultSet rs, int rowNum) throws SQLException {
		StudentsState state = new StudentsState();
		state.setUsername(rs.getString("username"));
		state.setQuestionId(rs.getInt("question_id"));
		state.setSource(rs.getString("source"));
		byte b = rs.getByte("passed");
		boolean passed = (b != 0) ? true : false;
		state.setPassed(passed);
		return state;
	}
}
