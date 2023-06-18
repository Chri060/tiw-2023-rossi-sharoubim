package com.christian.rossi.progetto_tiw_2023.DAOs;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO implements AutoCloseable {
    protected Connection connection;

   /* protected AbstractDAO() throws SQLException {
        connection = DBConnectionPool.getConnection();
    }*/

    protected Connection getConnection() throws SQLException{
        if (connection == null || connection.isClosed()) {
            connection = DBConnectionPool.getConnection();
        }
        return connection;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException{
        getConnection();
        connection.setAutoCommit(autoCommit);

    }

    public void rollback() throws SQLException {
        getConnection();
        connection.rollback();
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}