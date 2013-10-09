package org.javamoney.tck.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Currency;

import javax.money.CurrencyUnit;

import org.javamoney.tck.TCKTestSetup;
import org.javamoney.tck.util.ClassTester;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.junit.Test;

public class CurrencyUnitClassTest {

	@SpecAssertion(
		section = "3.1.1",
		id = "EnsureCurrencyUnit",
		note = "Asserts at least one CurrencyUnit implementation class is registered for testing.")
	@Test
	public void testSetup() {
		assertTrue("TCK Configuration not available.",
				TCKTestSetup.getTestConfiguration() != null);
		assertTrue(TCKTestSetup.getTestConfiguration().getCurrencyClasses()
				.size() > 0);
	}

	@SpecAssertion(
		section = "3.3",
		id = "CUImplementsCU",
		note = "Asserts all CurrencyUnit implementation are serializable.")
	@Test
	public void testCurrencyClassesImplementsCU() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			ClassTester.testImplementsInterface(type, CurrencyUnit.class);
			ClassTester.testHasPublicMethod(type, int.class,
					"getDefaultFractionDigits");
			ClassTester.testHasPublicMethod(type, int.class, "getCashRounding");
			ClassTester.testHasPublicMethod(type, int.class, "getNumericCode");
		}
	}

	@SpecAssertion(
		section = "3.3",
		id = "CUSerializable",
		note = "Asserts all CurrencyUnit implementation are serializable.")
	@Test
	public void testCurrencyClassesSerializable() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			ClassTester.testSerializable(type);
			CurrencyUnit unit = TCKTestSetup.getTestConfiguration().create(
					type,
					"CHF");
			ClassTester.testSerializable(unit);
		}
	}

	@SpecAssertion(
		section = "3.3",
		id = "CUImmutable",
		note = "Asserts all CurrencyUnit implementation are immutable.")
	@Test
	public void testCurrencyClassesImmutable() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			ClassTester.testImmutable(type);
		}
	}

	@SpecAssertion(
		section = "3.3",
		id = "CUComparable",
		note = "Asserts all CurrencyUnit implementation are comparable.")
	@Test
	public void testCurrencyClassesComparable() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			ClassTester.testComparable(type);
		}
	}

	@SpecAssertion(
		section = "3.3",
		id = "CUEqualsHashCode",
		note = "Asserts all CurrencyUnit implementation must implement equals(hashCode.")
	@Test
	public void testCurrencyClassesEqualsHashcode() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			ClassTester.testHasPublicMethod(type, boolean.class, "equals",
					Object.class);
			ClassTester.testHasPublicMethod(type, int.class, "hashCode");
		}
	}

	@SpecAssertion(
		section = "3.3",
		id = "CUISOCodesAvailable",
		note = "Asserts all codes are unique.")
	@Test
	public void testISOCurrencies() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			for (Currency cur : Currency.getAvailableCurrencies()) {
				CurrencyUnit unit = TCKTestSetup.getTestConfiguration()
						.create(type, cur.getCurrencyCode());
				assertEquals(cur.getCurrencyCode(), unit.getCurrencyCode());
				ClassTester.assertValue(cur.getNumericCode(), "getNumericCode",
						unit);
				ClassTester.assertValue(cur.getDefaultFractionDigits(),
						"getDefaultFractionDigits", unit);
				ClassTester.assertValue(-1, "getCashRounding", unit);
			}
		}
	}

	@SpecAssertion(
		section = "3.3",
		id = "CUThreadsafe",
		note = "Asserts all codes are " +
				"thread-safe.")
	@Test
	public void testThreadSafeCurrencyies() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			fail("Test not implemented yet: testThreadSafeCurrencyies");
		}

	}

	@SpecAssertion(
		section = "3.3",
		id = "CUCodesAreUnique",
		note = "Asserts all codes are unique.")
	@Test
	public void testThreadSafeCurrencyiesUniqueCodes() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			fail("Test not implemented yet: testThreadSafeCurrencyiesUniqueCodes");
		}

	}

	@SpecAssertion(
		section = "3.3",
		id = "CUNumericCodesAreUnique",
		note = "Asserts all numeric codes are unique.")
	@Test
	public void testThreadSafeCurrencyiesUniqueNumericCodes() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getCurrencyClasses()) {
			fail("Test not implemented yet: testThreadSafeCurrencyiesUniqueNumericCodes");
		}

	}
}
