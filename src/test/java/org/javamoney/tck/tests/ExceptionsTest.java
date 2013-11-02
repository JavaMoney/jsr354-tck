package org.javamoney.tck.tests;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Currency;

import javax.money.CurrencyUnit;
import javax.money.MonetaryException;

import org.javamoney.tck.ClassTester;
import org.javamoney.tck.TCKTestSetup;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ExceptionsTest {

	@SpecAssertion(
		section = "4.2.5",
		id = "Exceptions")
	@Test
	public void testExceptionInheritance() {
		for (Class clazz : TCKTestSetup.getTestConfiguration()
				.getExceptionClasses()) {
			assertTrue(MonetaryException.class.isAssignableFrom(clazz));
		}
	}

}
