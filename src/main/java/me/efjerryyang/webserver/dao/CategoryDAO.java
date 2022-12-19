package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryDAO implements DAO<Category> {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDAO.class);
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    public CategoryDAO(MysqlConnection mysqlConnection) throws SQLException {
        logger.debug("Creating CategoryDAO");
        this.mysqlConnection = mysqlConnection;
        logger.debug("Getting connection to MySQL server");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Category create(Category object) {
        logger.debug("Creating category with name: {} and category_id: {}", object.getName(), object.getCategoryId());
        String sql = "INSERT INTO category (name, category_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, object.getName());
            statement.setObject(2, object.getCategoryId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                object.setCategoryId(resultSet.getLong(1));
            }
            logger.debug("Successfully created category with name: {} and category_id: {}", object.getName(), object.getCategoryId());
            return object;
        } catch (SQLException e) {
            logger.error("Error creating category in database", e);
            return null;
        }
    }

    @Override
    public Category update(Category object) {
        logger.debug("Updating category with name: {} and category_id: {}", object.getName(), object.getCategoryId());
        String sql = "UPDATE hitsz_taste.categories SET name = ? WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getName());
            statement.setObject(2, object.getCategoryId());
            statement.executeUpdate();
            logger.debug("Successfully updated category with name: {} and category_id: {}", object.getName(), object.getCategoryId());
            return object;
        } catch (SQLException e) {
            logger.error("Error updating category in database", e);
            return null;
        }
    }

    @Override
    public Category update(Category objectOld, Category objectNew) {
        logger.debug("Updating category with name: {} and category_id: {} to name: {} and category_id: {}", objectOld.getName(), objectOld.getCategoryId(), objectNew.getName(), objectNew.getCategoryId());
        String sql = "UPDATE hitsz_taste.categories SET name = ?, category_id = ? WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, objectNew.getName());
            statement.setObject(2, objectNew.getCategoryId());
            statement.setObject(3, objectOld.getCategoryId());
            statement.executeUpdate();
            logger.debug("Successfully updated category with name: {} and category_id: {} to name: {} and category_id: {}", objectOld.getName(), objectOld.getCategoryId(), objectNew.getName(), objectNew.getCategoryId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating category in database", e);
            return null;
        }
    }

    @Override
    public Category getFromResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("Getting category from result set");
        Category category = new Category();
        category.setCategoryId(resultSet.getLong("category_id"));
        category.setName(resultSet.getString("name"));
        logger.debug("Successfully got category with name: {} and category_id: {}", category.getName(), category.getCategoryId());
        return category;
    }

    @Override
    public List<Category> getAll() {
        logger.debug("Getting all categories");
        String sql = "SELECT * FROM hitsz_taste.categories";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                categories.add(getFromResultSet(resultSet));
            }
            logger.debug("Successfully got all categories");
            return categories;
        } catch (SQLException e) {
            logger.error("Error getting all categories from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.debug("Deleting all categories");
        String sql = "DELETE FROM hitsz_taste.categories";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.debug("Successfully deleted all categories");
        } catch (SQLException e) {
            logger.error("Error deleting all categories from database", e);
        }
    }

    @Override
    public void delete(Category object) {
        logger.debug("Deleting category with name: {} and category_id: {}", object.getName(), object.getCategoryId());
        String sql = "DELETE FROM hitsz_taste.categories WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getCategoryId());
            statement.executeUpdate();
            logger.debug("Successfully deleted category with name: {} and category_id: {}", object.getName(), object.getCategoryId());
        } catch (SQLException e) {
            logger.error("Error deleting category from database", e);
        }
    }
}
