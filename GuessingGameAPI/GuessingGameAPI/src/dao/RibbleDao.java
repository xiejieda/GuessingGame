package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.DBUtils;

public class RibbleDao {
	//获取谜语id
	public ArrayList<Integer> getribble_id(int table_id){
		ArrayList<Integer> ribble_id = new ArrayList<Integer>();
		Connection conn = DBUtils.getConnection();
		try {
			for (int i = 0; i < 10; i++) {
				PreparedStatement statement = conn.prepareStatement("select * from game_ribble where id=? and table_id=?");
				statement.setInt(1, i+1);
				statement.setInt(2, table_id);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					ribble_id.add(resultSet.getInt("ribble_id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return ribble_id;
	}
	
	
	// 获取谜语提示及答案
	public ArrayList<String> getribble(int id) {
		ArrayList<String> ribble = new ArrayList<String>();
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement statement = conn
					.prepareStatement("select * from ribble_library where id=?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				ribble.add(resultSet.getString("tip"));
				ribble.add(resultSet.getString("ribble"));
				ribble.add(resultSet.getString("answer"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return ribble;
	}
	
	public Integer game_ribble(int ribble_id, int id, int table_id) {
		Connection connection = DBUtils.getConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement("update game_ribble set ribble_id = ? where id=? and table_id=?");
			preparedStatement.setInt(1, ribble_id);
			preparedStatement.setInt(2, id);
			preparedStatement.setInt(3, table_id);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
		return null;
	}

}
