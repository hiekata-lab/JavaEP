package jp.ac.u_tokyo.k.is.dao;

import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.Question;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class QuestionDaoImpl implements QuestionDao {
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void create(int difficulty, String content, String args, String answers, String source) {
		String sql = "insert into questions (difficulty, content, args, answers, source) values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, difficulty, content, args, answers, source);
	}
	
	public void create(int difficulty, String content, String args, String answers, String source, boolean objectOriented, String className) {
		int flag = 0;
		if (objectOriented) {
			flag = 1;
		}
		String sql = "insert into questions (difficulty, content, args, answers, source, object_oriented, class_name) values (?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, difficulty, content, args, answers, source, flag, className);
	}

	public List<Question> findAll() {
		String sql = "select * from questions";
		List<Question> questions = jdbcTemplate.query(sql, new QuestionMapper());
		return questions;
	}
	
	public List<Question> findExamQuestions() {
		String sql = "select * from questions where shown_in_exam = 1";
		List<Question> questions = jdbcTemplate.query(sql, new QuestionMapper());
		return questions;
	}

	public Question findById(int id) {
		String sql = "select * from questions where id = ?";
		List<Question> questions = jdbcTemplate.query(sql, new QuestionMapper(), id);

		if (questions.size() == 1) {
			return questions.get(0);
		} else {
			return null;
		}
	}
	
	public int count() {
		String sql = "select count(*) from questions";
		return jdbcTemplate.queryForInt(sql);
	}

	public void update(int id, int difficulty, String content, String args, String answers, String source) {
		String sql = "update questions set difficulty = ?, content = ?, args = ?, answers = ?, source = ? where id = ?";
		jdbcTemplate.update(sql, difficulty, content, args, answers, source, id);
	}
	
	public void update(int id, int difficulty, String content, String args, String answers, String source, boolean objectOriented, String className) {
		int flag = 0;
		if (objectOriented) {
			flag = 1;
		}
		String sql = "update questions set difficulty = ?, content = ?, args = ?, answers = ?, source = ?, object_oriented = ?, class_name = ? where id = ?";
		jdbcTemplate.update(sql, difficulty, content, args, answers, source, flag, className, id);
	}
	
	public void update(int id, boolean shownInExam) {
		int flag = 0;
		if (shownInExam) {
			flag = 1;
		}
		String sql = "update questions set shown_in_exam = ? where id = ?";
		jdbcTemplate.update(sql, flag, id);
	}

	public void delete(int id) {
		String sql = "delete from questions where id = ?";
		jdbcTemplate.update(sql, id);
	}

	public void deleteAll(){
		String sql = "delete from questions";
		jdbcTemplate.update(sql);
	}
}
