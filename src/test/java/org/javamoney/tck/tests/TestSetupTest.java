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
		section = "0",
		id = "EnsureSetup")
	@Test
	public void testTestSetup() {
		assertTrue("TCK Configuration not available.",
				TCKTestSetup.getTestConfiguration() != null);
		assertNotNull(TCKTestSetup.getTestConfiguration());
	}

	@SpecAssertion(
		section = "0",
		id = "EnsurePackageSetup")
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
		section = "0",
		id = "EnsureAdjusterSetup")
	@Test
	public void testOperatorTestSetup() {

		assertNotNull(TCKTestSetup.getTestConfiguration().getOperators());
		assertFalse(TCKTestSetup.getTestConfiguration().getOperators()
				.isEmpty());
	}

	@SpecAssertion(
		section = "0",
		id = "EnsureQuerySetup")
	@Test
	public void testQueryTestSetup() {
		assertNotNull(TCKTestSetup.getTestConfiguration().getQueries());
		assertFalse(TCKTestSetup.getTestConfiguration().getQueries().isEmpty());
	}
}
