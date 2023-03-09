package com.ruijie.plugins.bugscollect.jdbc;

import java.sql.*;

public class JdbcQuery {

    static {
        try {
            Class.forName(JdbcConstants.JDBC_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取链接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JdbcConstants.JDBC_URL, JdbcConstants.JDBC_USERNAME, JdbcConstants.JDBC_PASSWORD);
    }

    public static Statement getStatement(Connection con) throws SQLException {
        return con.createStatement();
    }

    //释放连接资源
    public static void relase(Connection co, Statement st, ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (co != null) {
            try {
                co.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
