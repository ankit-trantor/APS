package controller.dao;

import controller.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

public class LoginDAO {

    public class UserMetadata {

        public static final String TABLE_NAME = "tb_user";
        public static final String ID_USER = "id_user";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password_user";
    }

    private static Connection mConnection;
    private static PreparedStatement mPreparedStatement;

    public LoginDAO() {
        mConnection = Conexao.conectar();
    }

    public User getUserLogged(String username) {
        try {
            String sql = String.format(
                    "SELECT %s, %s, %s, %s FROM %s WHERE %s = ?;",
                    UserMetadata.ID_USER, UserMetadata.USERNAME, UserMetadata.EMAIL,
                    UserMetadata.PASSWORD, UserMetadata.TABLE_NAME, UserMetadata.USERNAME);

            mPreparedStatement = mConnection.prepareStatement(sql);

            mPreparedStatement.setString(1, username);

            ResultSet resultSet = mPreparedStatement.executeQuery();

            User user = null;

            if (resultSet.next()) {
                user = new User();
                user.setIdUser(resultSet.getInt(UserMetadata.ID_USER));
                user.setUsername(resultSet.getString(UserMetadata.USERNAME));
                user.setEmail(resultSet.getString(UserMetadata.EMAIL));
                user.setPassword(resultSet.getString(UserMetadata.PASSWORD));
            }

            return user;

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;

        } finally {
            Conexao.desconectar(mConnection);
        }
    }

    public boolean getUserEmailExist(String email) {
        try {
            String sql = String.format(
                    "SELECT %s, %s, %s, %s FROM %s WHERE %s = ?;",
                    UserMetadata.ID_USER, UserMetadata.USERNAME, UserMetadata.EMAIL,
                    UserMetadata.PASSWORD, UserMetadata.TABLE_NAME, UserMetadata.EMAIL);

            mPreparedStatement = mConnection.prepareStatement(sql);

            mPreparedStatement.setString(1, email);

            ResultSet resultSet = mPreparedStatement.executeQuery();

            return resultSet.next();

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;

        } finally {
            Conexao.desconectar(mConnection);
        }
    }

    public boolean getUserUsernameExist(String username) {
        try {
            String sql = String.format(
                    "SELECT %s, %s, %s, %s FROM %s WHERE %s = ?;",
                    UserMetadata.ID_USER, UserMetadata.USERNAME, UserMetadata.EMAIL,
                    UserMetadata.PASSWORD, UserMetadata.TABLE_NAME, UserMetadata.USERNAME);

            mPreparedStatement = mConnection.prepareStatement(sql);

            mPreparedStatement.setString(1, username);

            ResultSet resultSet = mPreparedStatement.executeQuery();

            return resultSet.next();

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;

        } finally {
            Conexao.desconectar(mConnection);
        }
    }

    public User insertUser(User user) {
        User userResponse = null;
        try {

            String sql = String.format(
                    "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?);",
                    UserMetadata.TABLE_NAME, UserMetadata.USERNAME, UserMetadata.EMAIL,
                    UserMetadata.PASSWORD);

            mPreparedStatement = mConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            mPreparedStatement.setString(1, user.getUsername());
            mPreparedStatement.setString(2, user.getEmail());
            mPreparedStatement.setString(3, user.getPassword());

            if (mPreparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = mPreparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    userResponse = user;
                    userResponse.setIdUser(resultSet.getLong(UserMetadata.ID_USER));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();

            return null;
        }
        return userResponse;
    }
}