package jp.ac.u_tokyo.k.is.dao;

import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.StudentsState;

public interface StudentsStateDao {
	void setDataSource(DataSource dataSource);
	void setTableName(String mode);
	void create(String username, int questionId, String source, String result, String action, boolean passed);
	void createOnlyLog(String username, int questionId, String source, String result, String action, boolean passed);
	StudentsState find(String username, int questionId);
	List<StudentsState> findByUsername(String username);
	List<StudentsState> findExamDataByUsername(String username);
	int getNumOfPassed(int questionId);
	void update(String username, int questionId, String source, String result, String action, boolean passed);
	void updateExam(String username, int questionId, String source, String result, String action, boolean passed);
	void delete(String username, int questionId);
}
