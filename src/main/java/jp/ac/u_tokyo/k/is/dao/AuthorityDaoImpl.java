package jp.ac.u_tokyo.k.is.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthorityDaoImpl implements AuthorityDao {
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void create(String username, String authority) {
		String sql = "insert into authorities (username, authority) values (?, ?)";
		jdbcTemplate.update(sql, username, authority);

	}

	public void deleteAll() {
		String sql = "delete from authorities";
		jdbcTemplate.update(sql);

	}
}
