package student.event.management.infrastructure.repository;

import student.event.management.infrastructure.database.DatabaseHandler;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GenericRepository {

    private DatabaseHandler handler;

    /**
     * execute an sql update
     *
     * @param sql
     * @return
     */
    public boolean execute(String sql) {
        try {
            Connection conn = handler.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error:" + e.getMessage(), "Error occured", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * execute an sql query
     *
     * @param sql
     * @return
     */
    public ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        try {
            Connection conn = handler.getConnection();
            Statement stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error:" + e.getMessage(), "Error occured", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return resultSet;
    }

    public void setHandler(DatabaseHandler handler) {
        this.handler = handler;
    }
}
