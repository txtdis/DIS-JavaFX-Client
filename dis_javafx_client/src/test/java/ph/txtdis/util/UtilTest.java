package ph.txtdis.util;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

	@Test
	public void testAreEqualWhenBothAreNull() {
		Assert.assertTrue(Util.areEqual(null, null));
	}

	@Test
	public void testAreEqualWhenDifferent() {
		Assert.assertFalse(Util.areEqual(BigDecimal.ZERO, BigDecimal.ONE));
	}

	@Test
	public void testAreEqualWhenFirstIsNotNullOtherIs() {
		Assert.assertFalse(Util.areEqual(BigDecimal.ONE, null));
	}

	@Test
	public void testAreEqualWhenFirstIsNullOtherNot() {
		Assert.assertFalse(Util.areEqual(null, BigDecimal.ONE));
	}

	@Test
	public void testAreEqualWhenTheSame() {
		Assert.assertTrue(Util.areEqual(BigDecimal.ONE, BigDecimal.ONE));
	}
}
