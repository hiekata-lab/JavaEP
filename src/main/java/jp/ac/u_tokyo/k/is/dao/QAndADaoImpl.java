package jp.ac.u_tokyo.k.is.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import jp.ac.u_tokyo.k.is.data.QAndA;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class QAndADaoImpl implements QAndADao {
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	public void create(final int questionId, final String username, final String source, final String console, final String questionComment) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		//updateの作成
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			final Timestamp time = new Timestamp(sdf.parse(sdf.format(date)).getTime());
			PreparedStatementCreator psc = new PreparedStatementCreator(){
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps =
		                connection.prepareStatement("insert into qandas "
		                		+ "(question_id, username, source, console, question_comment, created, responsed, solved) "
		                		+ "values (?, ?, ?, ?, ?, ?, ?, ?)", new String[] {"id"});
		            ps.setInt(1, questionId);
		            ps.setString(2, username);
		            ps.setString(3, source);
		            ps.setString(4, console);
		            ps.setString(5, questionComment);
		            ps.setTimestamp(6, time);
		            ps.setTimestamp(7, time);
		            ps.setTimestamp(8, time);
		            return ps;
		        }
		    };
			jdbcTemplate.update(psc, keyHolder);
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}


	}

	public void updateResponseComment(int id, String responseComment, String respondant){
		QAndA qanda = findById(id);
		String sql = "update qandas set response_comment = ?, responsed_flag = ?, respondant = ? , created = ?, responsed = NOW() "
				+ "where id = ?";
		jdbcTemplate.update(sql, responseComment, 1, respondant, qanda.getCreated(), id);
	}

	public void updateQuestionComment(int id, String questionComment){
		QAndA qanda = findById(id);
		String sql = "update qandas set question_comment = ?, created = ? "
				+ "where id = ?";
		jdbcTemplate.update(sql, questionComment, qanda.getCreated(), id);
	}

	public void updateFlag(int id, int solvedFlag){
		QAndA qanda = findById(id);
		//updateの作成
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Timestamp solved = new Timestamp(sdf.parse(sdf.format(date)).getTime());
			String sql = "update qandas set solved_flag = ?, created = ?, solved = ? "
					+ "where id = ?";
			jdbcTemplate.update(sql, solvedFlag, qanda.getCreated(), solved, id);
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public void updateLikeCount(int id, int likeCount){
		String sql = "update qandas set like_count = ? "
				+ "where id = ?";
		jdbcTemplate.update(sql, likeCount, id);

	}


	public List<QAndA> getAll() {
		String sql = "select * from qandas";
		List<QAndA> qandas = jdbcTemplate.query(sql, new QAndAMapper());
		return qandas;
	}

	public QAndA findById(int id) {
		String sql = "select * from qandas where id = ?";
		List<QAndA> qandas = jdbcTemplate.query(sql, new QAndAMapper(), id);

		if (qandas.size() == 1) {
			return qandas.get(0);
		} else {
			return null;
		}
	}

	public List<QAndA> findByQuestionAndUsername(int questionId, String username) {
		String sql = "select * from qandas where question_id = ? and username = ?";
		List<QAndA> qandas = jdbcTemplate.query(sql, new QAndAMapper(), questionId, username);

		return qandas;
	}

	public void answer(int id, String responseComment) {


	}

}
