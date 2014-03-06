package org.javamoney.tck.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Currency;
import java.util.Locale;

import javax.money.MonetaryAmount;
import javax.money.MonetaryAmounts;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class MonetaryAmountFormatTest {

	@SpecAssertion(section = "4.4.1", id = "441-A1")
	@Test
	public void testNoDepOnAmountImplementation() {
		final Locale defaultLocale = Locale.getDefault();
		MonetaryAmountFormat amountFormat = MonetaryFormats
				.getAmountFormat(defaultLocale);
		final Number[] values = new Number[] { 100, 10000000000000L };// TODO
																		// other
																		// values
																		// and
																		// currencies
		final Currency currency = Currency.getAvailableCurrencies().iterator()
				.next();
		for (Number value : values) {
			MonetaryAmount amount = MonetaryAmounts.getAmountFactory()
					.setCurrency(currency.getCurrencyCode()).setNumber(value)
					.create();
			String formattedAmount = amountFormat.format(amount);
			MonetaryAmount amountMock = TestMonetaryAmountFactory.getAmount(
					value, currency);
			String formattedAmountMock = amountFormat.format(amountMock);
			assertNotNull(formattedAmountMock);
			assertEquals(formattedAmountMock, formattedAmount);
		}
	}
}
