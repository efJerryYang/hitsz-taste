import me.efjerryyang.webserver.dao.MySQLConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MySQLConnectionTest {

    @Test
    public void testGetConnection() throws SQLException {
        MySQLConnection conn = new MySQLConnection();
        Connection connection = conn.getConnection();
        assertNotNull(connection);
    }

}
