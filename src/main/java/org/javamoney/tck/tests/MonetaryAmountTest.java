package org.javamoney.tck.tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import javax.money.MonetaryAdjuster;
import javax.money.MonetaryAmount;

import org.javamoney.tck.TCKTestSetup;
import org.javamoney.tck.util.ClassTester;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.junit.Test;

public class MonetaryAmountTest {

	@SpecAssertion(
		section = "4.2.2",
		id = "EnsureMonetaryAmount",
		note = "Ensure at least one javax.money.MonetaryAmount implementation is available.")
	@Test
	public void testEnsureMonetaryAmount() {
		assertTrue("TCK Configuration not available.",
				TCKTestSetup.getTestConfiguration() != null);
		assertTrue(TCKTestSetup.getTestConfiguration().getAmountClasses()
				.size() > 0);
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "CurrencyCode",
		note = "Ensure getCurrencyCode returns correct results.")
	@Test
	public void testCurrencyCode() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			for (String code : new String[] { "CHF", "hsgd", "374347&*%*ç" }) {
				MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
						.create(type, code, 10.15);
				assertNotNull(amount);
				assertNotNull(amount.getCurrency());
				assertEquals(code, amount.getCurrency().getCurrencyCode());
			}
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "AmountWhole",
		note = "Ensure getAmountWhole returns correct results.")
	@Test
	public void testAmountWhole() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			double[] nums = new double[] { 0, -0, 1, -1, 0.001, -0.001, 1234,
					-1234 };
			long[] wholes = new long[] { 0, 0, 1, -1, 0, 0, 1234, -1234 };
			for (int i = 0; i < nums.length; i++) {
				MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
						.create(type, "XXX", nums[i]);
				assertNotNull(amount);
				assertEquals(wholes[i], amount.getAmountWhole());
			}
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "AmountFractionNumber",
		note = "Ensure getAmountFractionNumber returns correct results.")
	@Test
	public void testAmountFractionNumber() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			double[] nums = new double[] { 0, -0, 1, -1, 0.001, -0.001, 1234,
					-1234 };
			long[] fractionNums = new long[] { 0, 0, 0, 0, 1, -1, 0, 0 };
			for (int i = 0; i < nums.length; i++) {
				MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
						.create(type, "CHF", nums[i]);
				assertNotNull(amount);
				assertEquals(fractionNums[i],
						amount.getAmountFractionNumerator());
			}
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "AmountFractionDenominator",
		note = "Ensure getAmountFractionDenominator returns correct results.")
	@Test
	public void testAmountFractionDenominator() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			double[] nums = new double[] { 0, -0, 1, -1, 0.001, -0.001, 1.12,
					-1.123456 };
			long[] fractionDenoms = new long[] { 100, 100, 100, 100, 1000,
					1000, 100, 1000000 };
			for (int i = 0; i < nums.length; i++) {
				MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
						.create(type, "CHF", nums[i]);
				assertNotNull(amount);
				assertEquals(fractionDenoms[i],
						amount.getAmountFractionNumerator());
			}
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "NumericRepresentation",
		note = "The portable numeric representation constraints must be followed.")
	@Test
	public void testNumericRepresentation() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			// Case 1
			MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 0);
			assertNotNull(amount);
			assertEquals(0,
					amount.getAmountFractionNumerator());
			assertTrue(amount.getAmountFractionDenominator() > 0);
			assertEquals(0, amount.getAmountWhole());
			// Case 2
			amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", -0);
			assertNotNull(amount);
			assertEquals(0,
					amount.getAmountFractionNumerator());
			assertTrue(amount.getAmountFractionDenominator() > 0);
			assertEquals(0, amount.getAmountWhole());
			// Case 3
			amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", -1);
			assertNotNull(amount);
			assertEquals(0,
					amount.getAmountFractionNumerator());
			assertTrue(amount.getAmountFractionDenominator() > 0);
			assertEquals(-1, amount.getAmountWhole());
			// Case 4
			amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 1);
			assertNotNull(amount);
			assertEquals(0,
					amount.getAmountFractionNumerator());
			assertTrue(amount.getAmountFractionDenominator() > 0);
			assertEquals(1, amount.getAmountWhole());
			// Case 4
			amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 2.234);
			assertNotNull(amount);
			assertTrue(amount.getAmountFractionNumerator() > 0);
			assertTrue(amount.getAmountFractionDenominator() > 0);
			assertEquals(2, amount.getAmountWhole());
			// Case 5
			amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", -2.234);
			assertNotNull(amount);
			assertTrue(amount.getAmountFractionNumerator() < 0);
			assertTrue(amount.getAmountFractionDenominator() > 0);
			assertEquals(-2, amount.getAmountWhole());
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "With",
		note = "Ensure with(MonetaryAdjuster) can be called and produces valuable results.")
	@Test
	public void testWith() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 0);
			// amount.with();
			// TODO
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "Query",
		note = "Ensure query(MonetaryAdjuster) can be called and produces valuable results.")
	@Test
	public void testQuery() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 0);
			// amount.query();
			// TODO
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "ImplementsEquals",
		note = "Implementations of MonetaryAmount must implement equals, considering number, currency and implementation type.")
	@Test
	public void testImplementsEquals() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 0);
			ClassTester.testHasPublicStaticMethodOpt(type, type,
					"equals", MonetaryAdjuster.class);
			MonetaryAmount amount2 = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 0);
			assertEquals(amount, amount2);
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "ImplementsHashCode",
		note = "Implementations of MonetaryAmount must implement hashCode, considering number, currency and implementation type.")
	@Test
	public void testImplementsHashCode() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
					.create(type, "TST", 0);
			ClassTester.testHasPublicStaticMethodOpt(type, type,
					"hashCode", MonetaryAdjuster.class);
			MonetaryAmount amount2 = TCKTestSetup.getTestConfiguration()
					.create(type, "TST", 0);
			assertEquals(amount.hashCode(), amount2.hashCode());
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "ImplementComparable",
		note = "Implementations of MonetaryAmount must be Comparable.")
	@Test
	public void testImplementComparable() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			ClassTester.testComparable(type);
			MonetaryAmount amount = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 0);
			ClassTester.testHasPublicStaticMethodOpt(type, type,
					"hashCode", MonetaryAdjuster.class);
			MonetaryAmount amount2 = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 0);
			assertTrue(((Comparable) amount).compareTo(amount2) == 0);
			MonetaryAmount amount3 = TCKTestSetup.getTestConfiguration()
					.create(type, "CHF", 1);
			assertTrue(((Comparable) amount).compareTo(amount3) > 0);
			assertTrue(((Comparable) amount3).compareTo(amount) < 0);
			MonetaryAmount amount4 = TCKTestSetup.getTestConfiguration()
					.create(type, "XXX", 1);
			assertTrue(((Comparable) amount3).compareTo(amount4) < 0);
			assertTrue(((Comparable) amount4).compareTo(amount3) > 0);
		}
	}

}
