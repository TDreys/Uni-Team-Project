package testing;

import java.sql.SQLException;

import database.DatabaseConnetor;
import database.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

class DatabaseConnetorTest {
	DatabaseConnetor dc = new DatabaseConnetor();

	@Test
	void testGetPlayers() throws SQLException {
		assertEquals(4, dc.getPlayers().size());
	}

	@Test
	void testCheckPlayer() throws SQLException {
		assertEquals(true, dc.checkPlayer(new User("Tom", "1234567", 0, null)));
	}

	@Test
	void testUpdateScore() throws SQLException {
		dc.updateScore("Tom", 100);
		assertEquals(100, dc.getPlayers().get(0).getBestScore());
	}

}
