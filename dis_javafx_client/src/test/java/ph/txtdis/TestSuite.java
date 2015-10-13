package ph.txtdis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ph.txtdis.service.BookedServiceTest;
import ph.txtdis.service.SoldServiceTest;

//@formatter:off
@RunWith(Suite.class)
@Suite.SuiteClasses({
	BookedServiceTest.class,
	SoldServiceTest.class
})
public class TestSuite {
}
// @formatter:on