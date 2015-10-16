package ph.txtdis.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ph.txtdis.App;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class ServerTest {

	@Autowired
	private Server server;

	@Test
	public void testAddress_shouldBeDefault_atStart() {
		Assert.assertEquals("localhost", server.address());
	}

	// @Test
	// public void testGetAddresses() {
	// // fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetLocation() {
	// // fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetLocations() {
	// // fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetPort() {
	// // fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetServer() {
	// // fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testLocation() {
	// // fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testLocationString() {
	// // fail("Not yet implemented");
	// }
}
