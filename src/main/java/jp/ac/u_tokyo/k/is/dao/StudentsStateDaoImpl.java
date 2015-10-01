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

	public void create(String username, int questionId, String source, String result, String action, boolean passed) {
		String sql = "insert into " + tableName + " (username, question_id, source, result, action, passed) values (?, ?, ?, ?, ?, ?)";
		String sql_for_log = "insert into " + tableName + "_log (username, question_id, source, result, action, passed) values (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, username, questionId, source, result, action, passed);
		jdbcTemplate.update(sql_for_log, username, questionId, source, result, action, passed);
	}
	
	public void createOnlyLog(String username, int questionId, String source, String result, String action, boolean passed) {
		String sql_for_log = "insert into " + tableName + "_log (username, question_id, source, result, action, passed) values (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql_for_log, username, questionId, source, result, action, passed);
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

	public void update(String username, int questionId, String source, String result, String action, boolean passed) {
		String sql = "update " + tableName + " set source = ?, result = ?, action = ?, passed = ? where username = ? and question_id = ?";
		String sql_for_log = "insert into " + tableName + "_log (username, question_id, source, result, action, passed) values (?, ?, ?, ?, ?, ?)";
		
		byte flag = (byte)(passed ? 1 : 0);
		jdbcTemplate.update(sql, source, result, action, flag, username, questionId);
		jdbcTemplate.update(sql_for_log, username, questionId, source, result, action, flag);
	}


	public void delete(String username, int questionId) {
		String sql = "delete from " + tableName + " where username = ? and question_id = ?";
		jdbcTemplate.update(sql, username, questionId);
	}
}
