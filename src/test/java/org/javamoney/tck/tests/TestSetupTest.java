package org.javamoney.tck.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.javamoney.tck.TCKTestSetup;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class TestSetupTest {

	@SpecAssertion(
		section = "3.1.2",
		id = "EnsureSetup",
		note = "Asserts the basic test setup is working.")
	@Test
	public void testTestSetup() {
		assertTrue("TCK Configuration not available.",
				TCKTestSetup.getTestConfiguration() != null);
		assertNotNull(TCKTestSetup.getTestConfiguration());
	}

	@SpecAssertion(
		section = "3.1.2",
		id = "EnsurePackageSetup",
		note = "Asserts the basic implementation packages are registered.")
	@Test
	public void testExceptionClassesSetup() {
		assertTrue(
				"Implementation Packages not registered.",
				TCKTestSetup.getTestConfiguration().getExceptionClasses() != null);
		assertFalse("Implementation Packages not registered.",
				TCKTestSetup.getTestConfiguration().getExceptionClasses()
						.isEmpty());
	}

	@SpecAssertion(
		section = "3.1.2",
		id = "EnsureAdjusterSetup",
		note = "Asserts adjusters registered are not empty.")
	@Test
	public void testAdjusterTestSetup() {

		assertNotNull(TCKTestSetup.getTestConfiguration().getAdjusters());
		assertFalse(TCKTestSetup.getTestConfiguration().getAdjusters()
				.isEmpty());
	}

	@SpecAssertion(
		section = "3.1.2",
		id = "EnsureQuerySetup",
		note = "Asserts queries returned are not empty.")
	@Test
	public void testQueryTestSetup() {
		assertNotNull(TCKTestSetup.getTestConfiguration().getQueries());
		assertFalse(TCKTestSetup.getTestConfiguration().getQueries().isEmpty());
	}
}
