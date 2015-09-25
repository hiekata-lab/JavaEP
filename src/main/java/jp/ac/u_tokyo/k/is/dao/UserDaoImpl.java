package jp.ac.u_tokyo.k.is.dao;

import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.User;
import jp.ac.u_tokyo.k.is.data.UserListRow;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao {
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<User> findAll() {
		String sql = "select * from users";
		return jdbcTemplate.query(sql, new UserMapper());
	}
	
	public List<UserListRow> findAllWithAuthority() {
		String sql = "select users.username, authorities.authority, users.score, users.exam_score from users "
				+ "inner join authorities on users.username = authorities.username";
		return jdbcTemplate.query(sql, new UserListRowMapper());
	}

	public void addScore(String username, int score) {
		String sql = "update users set score = score + ? where username = ?";
		jdbcTemplate.update(sql, score, username);
	}
	
	public void addExamScore(String username, int examScore) {
		String sql = "update users set exam_score = exam_score + ? where username = ?";
		jdbcTemplate.update(sql, examScore, username);
	}
	
	public void subScore(String username, int score) {
		String sql = "update users set score = score - ? where username = ?";
		jdbcTemplate.update(sql, score, username);
	}
	
	public void subExamScore(String username, int examScore) {
		String sql = "update users set exam_score = exam_score - ? where username = ?";
		jdbcTemplate.update(sql, examScore, username);
	}

	public int[] getHigherRank(int num) {
		String sql = "select * from users order by score desc limit ?";
		List<User> users = jdbcTemplate.query(sql, new UserMapper(), num);

		int[] result = new int[num];
		for (int i = 0; i < num; i++) {
			if (i < users.size()) {
				result[i] = users.get(i).getScore();
			} else {
				result[i] = 0;
			}
		}

		return result;
	}
	
	public int[] getExamHigherRank(int num) {
		String sql = "select * from users order by exam_score desc limit ?";
		List<User> users = jdbcTemplate.query(sql, new UserMapper(), num);

		int[] result = new int[num];
		for (int i = 0; i < num; i++) {
			if (i < users.size()) {
				result[i] = users.get(i).getExamScore();
			} else {
				result[i] = 0;
			}
		}

		return result;
	}

	public void create(String username, String password) {
		String sql = "insert into users (username, password, enabled) values (?, ?, ?)";
		jdbcTemplate.update(sql, username, password, 1);
	}

	public boolean checkExistByUsername(String username){
		String sql = "select * from users where username = ?";
		List<User> users = jdbcTemplate.query(sql, new UserMapper(), username);

		if (users.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void updatePassword(String username, String password){
		String sql = "update users set password = ? where username = ?";
		jdbcTemplate.update(sql, password, username);
	}

	public void deleteAll() {
		String sql = "delete from users";
		jdbcTemplate.update(sql);
	}
}
