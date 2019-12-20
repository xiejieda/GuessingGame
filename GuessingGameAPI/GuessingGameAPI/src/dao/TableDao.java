package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import util.DBUtils;

public class TableDao {
	// 返回大厅信息
	public ArrayList<Integer> getTable_Info1(int id) {
		ArrayList<Integer> info = new ArrayList<Integer>();
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement statement = conn.prepareStatement("select * from table_info where id=?");
			statement.setInt(1,id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				info.add(resultSet.getInt("user_1"));
				info.add(resultSet.getInt("user_2"));
				info.add(resultSet.getInt("user_3"));
				info.add(resultSet.getInt("user_4"));
				info.add(resultSet.getInt("game_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return info;
	}
	public String getTable_Info2(int id) {
		String time = null;
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement statement = conn.prepareStatement("select * from table_info where id=?");
			statement.setInt(1,id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				time = resultSet.getString("last_check");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return time;
	}
	
	//加入房间
	public Integer join_table(int id_table, int user , int id) {
		Connection connection = DBUtils.getConnection();
		PreparedStatement preparedStatement;
		try {
			if (user == 1) {
				preparedStatement = connection.prepareStatement("update table_info set user_1 = ? where id=?");
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, id_table);
				return preparedStatement.executeUpdate();
			}else if (user == 2) {
				preparedStatement = connection.prepareStatement("update table_info set user_2 = ? where id=?");
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, id_table);
				return preparedStatement.executeUpdate();
			}else if (user == 3) {
				preparedStatement = connection.prepareStatement("update table_info set user_3 = ? where id=?");
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, id_table);
				return preparedStatement.executeUpdate();
			}else if (user == 4) {
				preparedStatement = connection.prepareStatement("update table_info set user_4 = ? where id=?");
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, id_table);
				return preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
		return null;
	}
	
	//退出房间
	public Integer leave_table(int id_table, int user) {
		Connection connection = DBUtils.getConnection();
		PreparedStatement preparedStatement;
		try {
			if (user == 1) {
				preparedStatement = connection.prepareStatement("update table_info set user_1 = ? where id=?");
				preparedStatement.setInt(1, 0);
				preparedStatement.setInt(2, id_table);
				return preparedStatement.executeUpdate();
			}else if (user == 2) {
				preparedStatement = connection.prepareStatement("update table_info set user_2 = ? where id=?");
				preparedStatement.setInt(1, 0);
				preparedStatement.setInt(2, id_table);
				return preparedStatement.executeUpdate();
			}else if (user == 3) {
				preparedStatement = connection.prepareStatement("update table_info set user_3 = ? where id=?");
				preparedStatement.setInt(1, 0);
				preparedStatement.setInt(2, id_table);
				return preparedStatement.executeUpdate();
			}else if (user == 4) {
				preparedStatement = connection.prepareStatement("update table_info set user_4 = ? where id=?");
				preparedStatement.setInt(1, 0);
				preparedStatement.setInt(2, id_table);
				return preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
		return null;
	}
	public Integer leave_table(int id) {
		Connection connection = DBUtils.getConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement("update user_info set game_status = 0,game_grade = 0 where id =?");
			preparedStatement.setInt(1, id);
			return preparedStatement.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
		return null;
	}
	
	//房间玩家信息
	public ArrayList<Integer> getTable_userinfo(int id) {
		ArrayList<Integer> info = new ArrayList<Integer>();
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement statement = conn.prepareStatement("select * from table_info where id=?");
			statement.setInt(1,id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				info.add(resultSet.getInt("id"));
				info.add(resultSet.getInt("user_1"));
				info.add(resultSet.getInt("user_2"));
				info.add(resultSet.getInt("user_3"));
				info.add(resultSet.getInt("user_4"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return info;
	}
	
	//玩家就绪状态
	public ArrayList<String> getTable_userstatus(int id) {
		ArrayList<String> info = new ArrayList<String>();
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement statement = conn.prepareStatement("select * from user_info where id=?");
			statement.setInt(1,id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				info.add(resultSet.getInt("id")+"");
				info.add(resultSet.getString("username"));
				info.add(resultSet.getInt("game_status")+"");
				info.add(resultSet.getInt("game_grade")+"");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return info;
	}
	
	//修改游戏正在进行
	public Integer gameStart(int table_id) {
		int last_check=0;
		Connection connection = DBUtils.getConnection();
		PreparedStatement preparedStatement;
		PreparedStatement statement;
		try {
			preparedStatement = connection.prepareStatement("select * from table_info where id=?");
			preparedStatement.setInt(1, table_id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				last_check = resultSet.getInt("last_check");
			}
			if (last_check==0) {
				statement = connection.prepareStatement("update table_info set last_check = 1 where id =?");
				statement.setInt(1, table_id);
				return statement.executeUpdate();
			}else {
				return 1;
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(connection);
		}
		return null;
	}
	//修改游戏停止
		public Integer gameStop(int table_id) {
			Connection connection = DBUtils.getConnection();
			PreparedStatement preparedStatement;
			try {
				preparedStatement = connection.prepareStatement("update table_info set last_check = 0 where id =?");
				preparedStatement.setInt(1, table_id);
				return preparedStatement.executeUpdate();
			}catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtils.closeQuietly(connection);
			}
			return null;
		}
}
