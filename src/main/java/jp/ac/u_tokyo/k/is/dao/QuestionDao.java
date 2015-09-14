package jp.ac.u_tokyo.k.is.dao;

import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.Question;

public interface QuestionDao {
	void setDataSource(DataSource ds);
	void create(int difficulty, String content, String args, String answers, String source);
	void create(int difficulty, String content, String args, String answers, String source, boolean objectOriented, String className);
	List<Question> findAll();
	List<Question> findExamQuestions();
	Question findById(int id);
	int count();
	void update(int id, int difficulty, String content, String args, String answers, String source);
	void update(int id, int difficulty, String content, String args, String answers, String source, boolean objectOriented, String className);
	void update(int id, boolean shownInExam);
	void delete(int id);
	void deleteAll();
}
