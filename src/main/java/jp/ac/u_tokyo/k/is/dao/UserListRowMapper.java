package jp.ac.u_tokyo.k.is.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ac.u_tokyo.k.is.data.UserListRow;

import org.springframework.jdbc.core.RowMapper;

public class UserListRowMapper implements RowMapper<UserListRow> {
	public UserListRow mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserListRow userListRow = new UserListRow();
		userListRow.setUsername(rs.getString("username"));
		userListRow.setAuthority(rs.getString("authority"));
		userListRow.setScore(rs.getInt("score"));
		userListRow.setExamScore(rs.getInt("exam_score"));
		return userListRow;
	}
}