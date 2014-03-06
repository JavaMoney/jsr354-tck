package org.javamoney.tck.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Set;

import javax.money.format.AmountStyle;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class AccessingMonetaryAmountFormatTest {

	@SpecAssertion(section = "4.4.1", id = "441-B1")
	@Test
	public void testLocalesSupported() {
		Locale[] jdkDecimalFormatLocales = DecimalFormat.getAvailableLocales();
		for (Locale jdkDecimalFormatLocale : jdkDecimalFormatLocales) {
			MonetaryAmountFormat amountFormat = MonetaryFormats
					.getAmountFormat(jdkDecimalFormatLocale);
			assertNotNull(amountFormat);
			assertEquals(jdkDecimalFormatLocale, amountFormat.getAmountStyle()
					.getLocale());
		}
	}

	@Test
	@SpecAssertion(section = "4.4.1", id = "441-B2")
	public void testGetAmountFormat() {
		for (Locale locale : DecimalFormat.getAvailableLocales()) {
			assertNotNull(MonetaryFormats.getAmountFormat(AmountStyle
					.of(locale)));
		}
	}

	@Test
	@SpecAssertion(section = "4.4.1", id = "441-B3")
	public void testGetAvailableLocales() {
		Set<Locale> locales = MonetaryFormats.getAvailableLocales();
		for (Locale locale : DecimalFormat.getAvailableLocales()) {
			assertTrue(locales.contains(locale));
		}
	}

	@Test
	@SpecAssertion(section = "4.4.1", id = "441-B3")
	public void testAmountStyleOf() {
		for (Locale locale : DecimalFormat.getAvailableLocales()) {
			assertNotNull(AmountStyle.of(locale));
		}
	}
}
