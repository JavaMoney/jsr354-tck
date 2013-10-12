package org.javamoney.tck.tests;

import static org.junit.Assert.assertTrue;

import javax.money.MonetaryAdjuster;
import javax.money.MonetaryAmount;

import org.javamoney.tck.TCKTestSetup;
import org.javamoney.tck.util.ClassTester;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.junit.Test;

public class MonetaryAmountClassTest {

	@SpecAssertion(
		section = "3.1.1",
		id = "EnsureAmount",
		note = "Asserts at least one MonetaryAmount implementation class is registered for testing.")
	@Test
	public void testSetup() {
		assertTrue("TCK Configuration not available.",
				TCKTestSetup.getTestConfiguration() != null);
		assertTrue(TCKTestSetup.getTestConfiguration().getAmountClasses()
				.size() > 0);
	}

	@Test
	public void testAmountClasses() {
		for (Class type : TCKTestSetup.getTestConfiguration()
				.getAmountClasses()) {
			testAmountClass(type);
		}
	}

	public void testAmountClass(Class type) {
		ClassTester.testImplementsInterface(type, MonetaryAmount.class);
		ClassTester.testComparable(type);
		ClassTester.testImmutableOpt(type);
		ClassTester.testSerializableOpt(type);
		ClassTester.testHasPublicStaticMethodOpt(type, type,
				"from", MonetaryAdjuster.class);

		MonetaryAmount amt = (MonetaryAmount) TCKTestSetup
				.getTestConfiguration().create(type,
						"CHF", Double.valueOf(1.50d));
		ClassTester.testSerializableOpt(amt);
	}

}
