package org.javamoney.tck.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Currency;

import javax.money.MonetaryAmount;
import javax.money.MonetaryAmounts;
import javax.money.MonetaryOperator;

import org.javamoney.tck.ClassTester;
import org.javamoney.tck.TCKTestSetup;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Ignore;
import org.junit.Test;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class MonetaryAmountTest {

	@SpecAssertion(
		section = "4.2.2",
		id = "EnsureMonetaryAmount")
	@Test
	public void testEnsureMonetaryAmount() {
		assertTrue("TCK Configuration not available.",
				TCKTestSetup.getTestConfiguration() != null);
		assertTrue(TCKTestSetup.getTestConfiguration().getAmountClasses()
				.size() > 0);
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "CurrencyCode")
	@Test
	public void testCurrencyCode() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			for (Currency jdkCur: Currency.getAvailableCurrencies()) {
				MonetaryAmount amount = MonetaryAmounts
						.getAmountFactory().setCurrency(jdkCur.getCurrencyCode())
						.setNumber(10.15).create();
				assertNotNull(amount);
				assertNotNull(amount.getCurrency());
				assertEquals(jdkCur.getCurrencyCode(), amount.getCurrency().getCurrencyCode());
			}
		}
	}

	// @SpecAssertion(
	// section = "4.2.2",
	// id = "AmountWhole")
	// @Test
	// public void testAmountWhole() {
	// for (Class type : TCKTestSetup.getTestConfiguration()
	// .getAmountClasses()) {
	// double[] nums = new double[] { 0, -0, 1, -1, 0.001, -0.001, 1234,
	// -1234 };
	// long[] wholes = new long[] { 0, 0, 1, -1, 0, 0, 1234, -1234 };
	// for (int i = 0; i < nums.length; i++) {
	// MonetaryAmount amount = (MonetaryAmount) TCKTestSetup
	// .getTestConfiguration()
	// .create(type, "XXX", nums[i]);
	// assertNotNull(amount);
	// assertEquals(wholes[i], amount.getAmountWhole());
	// }
	// }
	// }
	//
	// @SpecAssertion(
	// section = "4.2.2",
	// id = "AmountFractionNumber")
	// @Test
	// public void testAmountFractionNumber() {
	// for (Class type : TCKTestSetup.getTestConfiguration()
	// .getAmountClasses()) {
	// double[] nums = new double[] { 0, -0, 1, -1, 0.001, -0.001, 1234,
	// -1234 };
	// int[] fractionSign = new int[] { 0, 0, 0, 0, 1, -1, 0, 0 };
	// for (int i = 0; i < nums.length; i++) {
	// MonetaryAmount amount = (MonetaryAmount) TCKTestSetup
	// .getTestConfiguration()
	// .create(type, "CHF", nums[i]);
	// assertNotNull(amount);
	// switch (fractionSign[i]) {
	// case 0:
	// assertTrue(
	// "Expected for " + nums[i] + " numerator sign of "
	// + fractionSign[i] + ", but was "
	// + amount.getAmountFractionNumerator(),
	// amount.getAmountFractionNumerator() == 0);
	// break;
	// case 1:
	// assertTrue(
	// "Expected for " + nums[i] + " numerator sign of "
	// + fractionSign[i] + ", but was "
	// + amount.getAmountFractionNumerator(),
	// amount.getAmountFractionNumerator() > 0);
	// break;
	// case -1:
	// assertTrue(
	// "Expected for " + nums[i] + " numerator sign of "
	// + fractionSign[i] + ", but was "
	// + amount.getAmountFractionNumerator(),
	// amount.getAmountFractionNumerator() < 0);
	// break;
	// }
	// }
	// }
	// }
	//
	// @SpecAssertion(
	// section = "4.2.2",
	// id = "AmountFractionDenominator")
	// @Test
	// public void testAmountFractionDenominator() {
	// for (Class type : TCKTestSetup.getTestConfiguration()
	// .getAmountClasses()) {
	// double[] nums = new double[] { 0.00, -0, 1, -1, 0.001, -0.001,
	// 1.12,
	// -1.123456 };
	// for (int i = 0; i < nums.length; i++) {
	// MonetaryAmount amount = (MonetaryAmount) TCKTestSetup
	// .getTestConfiguration()
	// .create(type, "CHF", nums[i]);
	// assertNotNull(amount);
	// assertTrue(
	// "Expected for " + nums[i]
	// + " denominator > 0, but was "
	// + amount.getAmountFractionDenominator(),
	// amount.getAmountFractionDenominator() > 0);
	// }
	// }
	// }
	//
	// @SpecAssertion(
	// section = "4.2.2",
	// id = "NumericRepresentation")
	// @Test
	// public void testNumericRepresentation() {
	// for (Class type : TCKTestSetup.getTestConfiguration()
	// .getAmountClasses()) {
	// // Case 1
	// MonetaryAmount amount = (MonetaryAmount) TCKTestSetup
	// .getTestConfiguration()
	// .create(type, "XXX", 0);
	// assertNotNull(amount);
	// assertEquals(0,
	// amount.getAmountFractionNumerator());
	// assertTrue(amount.getAmountFractionDenominator() > 0);
	// assertEquals(0, amount.getAmountWhole());
	// // Case 2
	// amount = (MonetaryAmount) TCKTestSetup.getTestConfiguration()
	// .create(type, "XXX", -0);
	// assertNotNull(amount);
	// assertEquals(0,
	// amount.getAmountFractionNumerator());
	// assertTrue(amount.getAmountFractionDenominator() > 0);
	// assertEquals(0, amount.getAmountWhole());
	// // Case 3
	// amount = (MonetaryAmount) TCKTestSetup.getTestConfiguration()
	// .create(type, "XXX", -1);
	// assertNotNull(amount);
	// assertEquals(0,
	// amount.getAmountFractionNumerator());
	// assertTrue(amount.getAmountFractionDenominator() > 0);
	// assertEquals(-1, amount.getAmountWhole());
	// // Case 4
	// amount = (MonetaryAmount) TCKTestSetup.getTestConfiguration()
	// .create(type, "XXX", 1);
	// assertNotNull(amount);
	// assertEquals(0,
	// amount.getAmountFractionNumerator());
	// assertTrue(amount.getAmountFractionDenominator() > 0);
	// assertEquals(1, amount.getAmountWhole());
	// // Case 4
	// amount = (MonetaryAmount) TCKTestSetup.getTestConfiguration()
	// .create(type, "XXX", 2.234);
	// assertNotNull(amount);
	// assertTrue(amount.getAmountFractionNumerator() > 0);
	// assertTrue(amount.getAmountFractionDenominator() > 0);
	// assertEquals(2, amount.getAmountWhole());
	// // Case 5
	// amount = (MonetaryAmount) TCKTestSetup.getTestConfiguration()
	// .create(type, "XXX", -2.234);
	// assertNotNull(amount);
	// assertTrue(amount.getAmountFractionNumerator() < 0);
	// assertTrue(amount.getAmountFractionDenominator() > 0);
	// assertEquals(-2, amount.getAmountWhole());
	// }
	// }

	@SpecAssertion(
		section = "4.2.2",
		id = "With")
	@Test
	@Ignore
	public void testWith() {
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "Query")
	@Test
	@Ignore
	public void testQuery() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			MonetaryAmount amount = MonetaryAmounts.getAmountFactory()
					.setCurrency("XXX").setNumber(0).create();
			// amount.query();
			fail("not implemented.");
			// TODO
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "ImplementsEquals")
	@Test
	public void testImplementsEquals() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			MonetaryAmount amount = MonetaryAmounts.getAmountFactory()
					.setCurrency("XXX").setNumber(0).create();
			ClassTester.testHasPublicStaticMethodOpt(type, type,
					"equals", MonetaryOperator.class);
			MonetaryAmount amount2 = MonetaryAmounts.getAmountFactory()
					.setCurrency("XXX").setNumber(0).create();
			assertEquals(amount, amount2);
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "ImplementsHashCode")
	@Test
	public void testImplementsHashCode() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			MonetaryAmount amount = MonetaryAmounts.getAmountFactory()
					.setCurrency("USD").setNumber(0).create();
			ClassTester.testHasPublicStaticMethodOpt(type, type,
					"hashCode", MonetaryOperator.class);
			MonetaryAmount amount2 = MonetaryAmounts.getAmountFactory()
					.setCurrency("USD").setNumber(0).create();
			assertEquals(amount.hashCode(), amount2.hashCode());
		}
	}

	@SpecAssertion(
		section = "4.2.2",
		id = "IsComparable")
	@Test
	public void testImplementComparable() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			ClassTester.testComparable(type);
			MonetaryAmount amount = MonetaryAmounts.getAmountFactory()
					.setCurrency("XXX").setNumber(0).create();
			ClassTester.testHasPublicStaticMethodOpt(type, type,
					"hashCode", MonetaryOperator.class);
			MonetaryAmount amount2 = MonetaryAmounts.getAmountFactory()
					.setCurrency("XXX").setNumber(0).create();
			assertTrue("Comparable failed for: " + type.getName(),
					((Comparable) amount).compareTo(amount2) == 0);
			MonetaryAmount amount3 = MonetaryAmounts.getAmountFactory()
					.setCurrency("CHF").setNumber(1).create();
			assertTrue("Comparable failed for: " + type.getName(),
					((Comparable) amount).compareTo(amount3) > 0);
			assertTrue("Comparable failed for: " + type.getName(),
					((Comparable) amount3).compareTo(amount) < 0);
			MonetaryAmount amount4 = MonetaryAmounts.getAmountFactory()
					.setCurrency("XXX").setNumber(1).create();
			assertTrue("Comparable failed for: " + type.getName(),
					((Comparable) amount3).compareTo(amount4) < 0);
			assertTrue("Comparable failed for: " + type.getName(),
					((Comparable) amount4).compareTo(amount3) > 0);
		}
	}

}
