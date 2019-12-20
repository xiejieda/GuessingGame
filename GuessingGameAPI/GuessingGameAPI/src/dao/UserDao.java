package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBUtils;

import java.sql.Connection;

public class UserDao {
	// ÅÐ¶ÏÓÃ»§ÃûÊÇ·ñ´æÔÚ
	public int getuserid(String username) {
		int id = 0;
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement statement = conn
					.prepareStatement("select * from user_info where username=?");
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return id;
	}
	// ×¢²á
	public Integer insertDataToDB(String username, String password) {
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("insert into user_info(username,password,login_status,game_status,game_grade) values(?,?,0,0,0)");
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			// Ö´ÐÐSQL²éÑ¯Óï¾ä
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return null;
	}
	
	//ÅÐ¶ÏÊÇ·ñÒÑ¾­µÇÂ¼
	public int getloginstatus(String username) {
		int status = 0;
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement statement = conn
					.prepareStatement("select * from user_info where username=?");
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				status = resultSet.getInt("login_status");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return status;
	}
	
	// µÇÂ¼
	public boolean valid(String username, String password) {
		boolean isValid = false;
		Connection connection = DBUtils.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("select * from user_info where username=? and password=?");
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				isValid = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
		return isValid;
	}
	
	//ÐÞ¸ÄµÇÂ¼×´Ì¬
	
	public void changeloginstatus1(int id) {
		Connection connection = DBUtils.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("update user_info set login_status = 1 where id=?");
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
	}
	
	public void changeloginstatus0(int id) {
		Connection connection = DBUtils.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("update user_info set login_status = 0 , game_status = 0 where id=?");
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
	}
	
	//ÐÞ¸ÄÍæ¼ÒÓÎÏ·×´Ì¬
	public Integer changgamestatus(int id,int status){
		Connection connection = DBUtils.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("update user_info set game_status = ?,game_grade = 0 where id = ?");
			preparedStatement.setInt(1, status);
			preparedStatement.setInt(2, id);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Ìí¼Ó³É¼¨
	public Integer addGrade(int id,int grade) {
		Connection connection = DBUtils.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("update user_info set game_grade=? where id=?");
			preparedStatement.setInt(1, grade);
			preparedStatement.setInt(2, id);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
		return null;
	}
}
