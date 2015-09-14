package jp.ac.u_tokyo.k.is.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ac.u_tokyo.k.is.data.Authority;

import org.springframework.jdbc.core.RowMapper;

public class AuthorityMapper implements RowMapper<Authority> {
	public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
		Authority authority = new Authority();
		authority.setUsername(rs.getString("username"));
		authority.setAuthority(rs.getString("authority"));
		return authority;
	}
}
