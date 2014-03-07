package org.javamoney.tck.tests;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

import javax.money.MonetaryAmount;
import javax.money.MonetaryAmounts;
import javax.money.format.AmountStyle;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class FormattingMonetaryAmountsTest{

    /**
     * Format several amounts, created using the default factory,
     * but
     * also a test instance, provided by the TCK, to ensure no
     * implementation
     * dependencies on the implementation.
     */
    @SpecAssertion(section = "4.4.1", id = "441-A1")
    @Test
    public void testNoDepOnAmountImplementation(){
        final Locale defaultLocale = Locale.getDefault();
        MonetaryAmountFormat amountFormat = MonetaryFormats.getAmountFormat(defaultLocale);
        final Number[] values = new Number[]{100, 10000000000000L};// TODO
        // other
        // values
        // and
        // currencies
        final Currency currency = Currency.getAvailableCurrencies().iterator().next();
        for(Number value : values){
            MonetaryAmount amount =
                    MonetaryAmounts.getAmountFactory().setCurrency(currency.getCurrencyCode()).setNumber(value)
                            .create();
            String formattedAmount = amountFormat.format(amount);
            MonetaryAmount amountMock = TestMonetaryAmountFactory.getAmount(value, currency);
            String formattedAmountMock = amountFormat.format(amountMock);
            assertNotNull(formattedAmountMock);
            assertEquals(formattedAmountMock, formattedAmount);
        }
    }

    /*
    <assertion id="441-A2">
				<text>Print several amounts, created using the default factory, but
					also a test instance, provided by the TCK, to ensure no
					implementation
					dependencies on the implementation.
				</text>
			</assertion>
			<assertion id="441-A3">
				<text>Parse back several amounts, input created using the
					formatting
					from 'Format_formatAmounts'.
				</text>
			</assertion>
			<assertion id="441-A4">
				<text>Get/set different amount styles (especially patterns, group
					sizes, group characters) and compare results with results as from
					RI.
					Also apply patterns without currency invovled.
				</text>
			</assertion>
			<assertion id="441-A5">
				<text>Get/set different monetary contexts and compare results with
					results from parsed amounts.
				</text>
			</assertion>
			<assertion id="441-A6">
				<text>Get/set default currency, try to parse patterns without
					currency information.
				</text>
			</assertion>
     */

    /**
     * AccessingMonetaryAmountFormat using
     * MonetaryFormats.getAmountFormat(Locale locale), all locales
     * available also from java.text.DecimalFormat must be supported.
     */
    @SpecAssertion(section = "4.4.1", id = "441-B1")
    @Test
    public void testLocalesSupported(){
        Locale[] jdkDecimalFormatLocales = DecimalFormat.getAvailableLocales();
        for(Locale jdkDecimalFormatLocale : jdkDecimalFormatLocales){
            MonetaryAmountFormat amountFormat = MonetaryFormats.getAmountFormat(jdkDecimalFormatLocale);
            assertNotNull(amountFormat);
            assertEquals(jdkDecimalFormatLocale, amountFormat.getAmountStyle().getLocale());
        }
    }

    /**
     * AccessingMonetaryAmountFormat using
     * MonetaryFormats.getAmountFormat(AmountStyle style), all locales
     * available also from java.text.DecimalFormat must be supported
     * (using AmountStyle.of(Locale)).
     */
    @Test
    @SpecAssertion(section = "4.4.1", id = "441-B2")
    public void testGetAmountFormat(){
        for(Locale locale : DecimalFormat.getAvailableLocales()){
            assertNotNull(MonetaryFormats.getAmountFormat(AmountStyle.of(locale)));
        }
    }

    /**
     * Test MonetaryFormats.getAvailableLocales, all locales available also from java.text.DecimalFormat must be
     * supported (using AmountStyle.of(Locale)), more locales are possible.
     */
    @Test
    @SpecAssertion(section = "4.4.1", id = "441-B3")
    public void testGetAvailableLocales(){
        Set<Locale> locales = MonetaryFormats.getAvailableLocales();
        for(Locale locale : DecimalFormat.getAvailableLocales()){
            assertTrue(locales.contains(locale));
        }
    }

    /**
     * Test MonetaryFormats.getAvailableLocales, all locales available also from java.text.DecimalFormat must be
     * supported (using AmountStyle.of(Locale)), more locales are possible.
     */
    @Test
    @SpecAssertion(section = "4.4.1", id = "441-B3")
    public void testAmountStyleOf(){
        for(Locale locale : DecimalFormat.getAvailableLocales()){
            assertNotNull(AmountStyle.of(locale));
        }
    }
}
