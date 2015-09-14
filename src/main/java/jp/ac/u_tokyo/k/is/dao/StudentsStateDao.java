package jp.ac.u_tokyo.k.is.dao;

import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.StudentsState;

public interface StudentsStateDao {
	void setDataSource(DataSource dataSource);
	void setTableName(String mode);
	void create(String username, int questionId, String source, boolean passed);
	StudentsState find(String username, int questionId);
	List<StudentsState> findByUsername(String username);
	int getNumOfPassed(int questionId);
	void update(String username, int questionId, String source, boolean passed);
	void delete(String username, int questionId);
}
