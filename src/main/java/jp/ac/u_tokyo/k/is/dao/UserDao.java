package jp.ac.u_tokyo.k.is.dao;

import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.User;
import jp.ac.u_tokyo.k.is.data.UserListRow;

public interface UserDao {
	void setDataSource(DataSource ds);
	List<User> findAll();
	List<UserListRow> findAllWithAuthority();
	void addScore(String username, int score);
	void addExamScore(String username, int examScore);
	int[] getHigherRank(int num);
	int[] getExamHigherRank(int num);
	void create(String username, String password);
	boolean checkExistByUsername(String username);
	void updatePassword(String username, String password);
	void deleteAll();
}
