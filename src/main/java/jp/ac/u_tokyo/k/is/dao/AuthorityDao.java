package jp.ac.u_tokyo.k.is.dao;

import javax.sql.DataSource;

public interface AuthorityDao {
	void setDataSource(DataSource ds);
	void create(String username, String authority);
//	void update(String username, String authority);
//	void delete(String username);
	void deleteAll();
}
