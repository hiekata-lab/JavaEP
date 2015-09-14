package jp.ac.u_tokyo.k.is.dao;

import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.QAndA;

public interface QAndADao {
	void setDataSource(DataSource ds);
	void create(int questionId, String username, String source, String console, String questionComment);
	void updateResponseComment(int id, String responseComment, String respondant);
	void updateQuestionComment(int id, String questionComment);
	void updateFlag(int id, int solvedFlag);
	void updateLikeCount(int id, int likeCount);
	List<QAndA> getAll();
	QAndA findById(int id);
	List<QAndA> findByQuestionAndUsername(int questionId, String username);
}
