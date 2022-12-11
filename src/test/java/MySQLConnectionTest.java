import me.efjerryyang.webserver.AppConfig;
import me.efjerryyang.webserver.ApplicationProperties;
import me.efjerryyang.webserver.dao.MySQLConnection;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class MySQLConnectionTest {

    @Autowired
    private ApplicationProperties applicationProperties;

    //    private ApplicationProperties applicationProperties = new ApplicationProperties();
    @Autowired
    private MySQLConnection mySQLConnection;
//    private MySQLConnection mySQLConnection = new MySQLConnection(applicationProperties);
//
//    public MySQLConnectionTest() throws SQLException, IOException {
//    }

    @Test
    public void testGetConnection() throws SQLException, IOException {
        Connection connection = mySQLConnection.getConnection();
        assertNotNull(connection);
    }
}
