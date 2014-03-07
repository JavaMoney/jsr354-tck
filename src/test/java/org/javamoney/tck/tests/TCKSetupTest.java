package org.javamoney.tck.tests;

import static org.junit.Assert.*;

import org.javamoney.tck.TCKTestSetup;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class TCKSetupTest{

	@SpecAssertion(
		section = "0",
		id = "Setup")
	@Test
	public void testTestSetup() {
		assertTrue("TCK Configuration not available.",
				TCKTestSetup.getTestConfiguration() != null);
		assertNotNull(TCKTestSetup.getTestConfiguration());
	}


}
