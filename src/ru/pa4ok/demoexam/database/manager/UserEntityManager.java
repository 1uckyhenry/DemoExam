package ru.pa4ok.demoexam.database.manager;

import ru.pa4ok.demoexam.database.entity.UserEntity;
import ru.pa4ok.demoexam.util.BaseManager;
import ru.pa4ok.demoexam.util.MysqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserEntityManager extends BaseManager
{
    public UserEntityManager(MysqlDatabase database) {
        super(database);
    }

    public UserEntity addUser(UserEntity userEntity) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "INSERT INTO users(login, PASSWORD) values(?,?)";
            PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            s.setString(1, userEntity.getLogin());
            s.setString(2, userEntity.getPassword());
            s.executeUpdate();

            ResultSet resultSet = s.getGeneratedKeys();
            if(resultSet.next()) {
                userEntity.setId(resultSet.getInt(1));
                return userEntity;
            }

            throw new SQLException("Entity not added");
        }
    }

    public UserEntity getUserById(int id) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM users WHERE id=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);

            ResultSet resultSet = s.executeQuery();
            if(resultSet.next()) {
                return new UserEntity(
                    resultSet.getInt("id"),
                    resultSet.getString("login"),
                    resultSet.getString("PASSWORD")
                );
            }

            return null;
        }
    }

    public List<UserEntity> getAllUsers() throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "SELECT * FROM users";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<UserEntity> users = new ArrayList<>();
            while(resultSet.next()) {
                users.add(new UserEntity(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("PASSWORD")
                ));
            }

            return users;
        }
    }

    public int update(UserEntity userEntity) throws SQLException
    {
        try(Connection c = database.getConnection())
        {
            String sql = "UPDATE users SET login=?, PASSWORD=? WHERE id=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, userEntity.getLogin());
            s.setString(2, userEntity.getPassword());
            s.setInt(3, userEntity.getId());

            return s.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException
    {
       try(Connection c = database.getConnection())
       {
           String sql = "DELETE FROM users WHERE id=?";
           PreparedStatement s = c.prepareStatement(sql);
           s.setInt(1, id);

           return s.executeUpdate();
       }
    }
}
