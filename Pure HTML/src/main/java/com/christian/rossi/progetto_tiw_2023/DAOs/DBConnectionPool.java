package com.christian.rossi.progetto_tiw_2023.DAOs;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionPool {
    private static volatile DataSource dataSource = null;

    protected static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            synchronized (DBConnectionPool.class) {
                if (dataSource == null) {

                    PoolProperties p = new PoolProperties();
                    p.setDriverClassName("com.mysql.cj.jdbc.Driver");
                    p.setUrl("jdbc:mysql://localhost:3306/progetto_tiw_2023");
                    p.setUsername("root");
                    p.setPassword("Sdp>30@pdtipiw");
                    p.setMaxActive(128);
                    p.setInitialSize(10);

                    dataSource = new DataSource();
                    dataSource.setPoolProperties(p);
                }
            }
        }
        return dataSource.getConnection();
    }
}