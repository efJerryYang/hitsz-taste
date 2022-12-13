package me.efjerryyang.webserver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    T create(T object);

    T update(T object);

    T update(T objectOld, T objectNew);

    T getFromResultSet(ResultSet resultSet) throws SQLException;

    List<T> getAll();

    void deleteAll();

    void delete(T object);

}
