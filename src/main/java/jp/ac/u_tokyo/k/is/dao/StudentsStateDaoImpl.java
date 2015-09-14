package jp.ac.u_tokyo.k.is.dao;

import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.StudentsState;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StudentsStateDaoImpl implements StudentsStateDao {
	private JdbcTemplate jdbcTemplate;
	private String tableName;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void setTableName(String mode) {
		if (mode.equals("exam")) {
			this.tableName = "students_status_exam";
		} else {
			this.tableName = "students_status";
		}
	}

	public void create(String username, int questionId, String source, boolean passed) {
		String sql = "insert into " + tableName + " (username, question_id, source, passed) values (?, ?, ?, ?)";
		jdbcTemplate.update(sql, username, questionId, source, passed);
	}

	public StudentsState find(String username, int questionId) {
		String sql = "select * from " + tableName + " where username = ? and question_id = ?";
		List<StudentsState> status = jdbcTemplate.query(sql, new StudentsStateMapper(), username, questionId);

		if (status.size() == 1) {
			return status.get(0);
		} else {
			return null;
		}
	}

	public List<StudentsState> findByUsername(String username) {
		String sql = "select * from " + tableName + " where username = ?";
		return jdbcTemplate.query(sql, new StudentsStateMapper(), username);
	}
	
	public int getNumOfPassed(int questionId) {
		String sql = "select * from " + tableName + " where question_id = ? and passed = 1";
		List<StudentsState> status = jdbcTemplate.query(sql, new StudentsStateMapper(), questionId);
		return status.size();
	}

	public void update(String username, int questionId, String source, boolean passed) {
		String sql = "update " + tableName + " set source = ?, passed = ? where username = ? and question_id = ?";

		byte flag = (byte)(passed ? 1 : 0);
		jdbcTemplate.update(sql, source, flag, username, questionId);
	}

	public void delete(String username, int questionId) {
		String sql = "delete from " + tableName + " where username = ? and question_id = ?";
		jdbcTemplate.update(sql, username, questionId);
	}
}
