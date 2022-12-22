package me.efjerryyang.webserver.dao;

import me.efjerryyang.webserver.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReviewDAO implements DAO<Review> {
    private static final Logger logger = LoggerFactory.getLogger(ReviewDAO.class);
    private final MysqlConnection mysqlConnection;
    private final Connection connection;

    @Autowired
    public ReviewDAO(MysqlConnection mysqlConnection) throws SQLException {
        this.mysqlConnection = mysqlConnection;
        logger.debug("Obtaining connection to database");
        this.connection = mysqlConnection.getConnection();
    }

    @Override
    public Review create(Review review) {
        logger.debug("Creating review with id: {}", review.getReviewId());
        String sql = "INSERT INTO hitsz_taste.reviews (review_id, user_id, rating, comment, create_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, review.getReviewId());
            statement.setObject(2, review.getUserId());
            statement.setObject(3, review.getRating());
            statement.setObject(4, review.getComment());
            statement.setObject(5, review.getCreateAt());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                review.setReviewId(resultSet.getLong(1));
            }
            logger.info("Successfully created review with id {}", review.getReviewId());
            return review;
        } catch (SQLException e) {
            logger.error("Error creating review in database", e);
            return null;
        }
    }

    @Override
    public Review update(Review object) {
        logger.debug("Updating review with id: {}", object.getReviewId());
        String sql = "UPDATE hitsz_taste.reviews SET user_id = ?, rating = ?, comment = ?, create_at = ? WHERE review_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the review in the SQL statement
            logger.debug("Setting review values in SQL statement: user_id = {}, rating = {}, comment = {}, timestamp = {}, review_id = {}", object.getUserId(), object.getRating(), object.getComment(), object.getCreateAt(), object.getReviewId());
            statement.setObject(1, object.getUserId());
            statement.setObject(2, object.getRating());
            statement.setObject(3, object.getComment());
            statement.setObject(4, object.getCreateAt());
            statement.setObject(5, object.getReviewId());
            statement.executeUpdate();
            logger.info("Successfully updated review with id {}", object.getReviewId());
            return object;
        } catch (SQLException e) {
            logger.error("Error updating review in database", e);
            return null;
        }

    }

    @Override
    public Review update(Review objectOld, Review objectNew) {
        logger.debug("Updating review with id: {}", objectOld.getReviewId());
        String sql = "UPDATE hitsz_taste.reviews SET user_id = ?, rating = ?, comment = ?, timestamp = ? WHERE review_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values for the review in the SQL statement
            logger.debug("Setting review values in SQL statement: user_id = {}, rating = {}, comment = {}, timestamp = {}, review_id = {}", objectNew.getUserId(), objectNew.getRating(), objectNew.getComment(), objectNew.getCreateAt(), objectOld.getReviewId());
            statement.setObject(1, objectNew.getUserId());
            statement.setObject(2, objectNew.getRating());
            statement.setObject(3, objectNew.getComment());
            statement.setObject(4, objectNew.getCreateAt());
            statement.setObject(5, objectOld.getReviewId());
            statement.executeUpdate();
            logger.info("Successfully updated review with id {}", objectOld.getReviewId());
            return objectNew;
        } catch (SQLException e) {
            logger.error("Error updating review in database", e);
            return null;
        }
    }

    @Override
    public Review getFromResultSet(ResultSet resultSet) throws SQLException {
        Review review = new Review();
        review.setReviewId(resultSet.getLong("review_id"));
        review.setUserId(resultSet.getLong("user_id"));
        review.setRating(resultSet.getInt("rating"));
        review.setComment(resultSet.getString("comment"));
        review.setCreateAt(resultSet.getTimestamp("create_at"));
        return review;
    }

    @Override
    public List<Review> getAll() {
        logger.debug("Getting all reviews");
        String sql = "SELECT * FROM hitsz_taste.reviews";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()) {
                logger.debug("Adding review with id {} to list", resultSet.getLong("review_id"));
                Review review = getFromResultSet(resultSet);
                logger.debug("Successfully added review with id {} to list", review.getReviewId());
                reviews.add(review);
            }
            logger.info("Successfully retrieved {} reviews", reviews.size());
            return reviews;
        } catch (SQLException e) {
            logger.error("Error retrieving reviews from database", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        logger.debug("Deleting all reviews");
        String sql = "DELETE FROM hitsz_taste.reviews";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            logger.info("Successfully deleted all reviews");
        } catch (SQLException e) {
            logger.error("Error deleting all reviews from database", e);
        }
    }

    @Override
    public void delete(Review object) {
        logger.debug("Deleting review with id: {}", object.getReviewId());
        String sql = "DELETE FROM hitsz_taste.reviews WHERE review_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getReviewId());
            statement.executeUpdate();
            logger.info("Successfully deleted review with id {}", object.getReviewId());
        } catch (SQLException e) {
            logger.error("Error deleting review from database", e);
        }
    }
}
