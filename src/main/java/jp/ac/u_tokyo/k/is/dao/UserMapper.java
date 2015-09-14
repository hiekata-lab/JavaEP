package jp.ac.u_tokyo.k.is.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ac.u_tokyo.k.is.data.User;

import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User> {
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setScore(rs.getInt("score"));
		user.setExamScore(rs.getInt("exam_score"));
		byte b = rs.getByte("enabled");
		boolean enabled = (b != 0) ? true : false;
		user.setEnabled(enabled);
		return user;
	}
}
