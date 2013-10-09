package org.javamoney.tck.tests;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Currency;

import javax.money.CurrencyUnit;
import javax.money.MonetaryException;

import org.javamoney.tck.TCKTestSetup;
import org.javamoney.tck.util.ClassTester;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.junit.Test;

public class ExceptionsTest {

	@SpecAssertion(
		section = "3.2",
		id = "EnsureMonetaryExceptions",
		note = "Asserts all exceptions contained in the implementation package inherit from MonetaryException.")
	@Test
	public void testExceptionInheritance() {
		for (Class clazz : TCKTestSetup.getTestConfiguration()
				.getExceptionClasses()) {
			assertTrue(MonetaryException.class.isAssignableFrom(clazz));
		}
	}

}
