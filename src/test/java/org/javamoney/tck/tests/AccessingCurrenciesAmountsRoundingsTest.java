/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2013, Credit Suisse All rights
 * reserved.
 */

package org.javamoney.tck.tests;

import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class AccessingCurrenciesAmountsRoundingsTest{

    @Test
    public void placeholder(){
        Assert.fail();
    }

    /*
    <group>
			<text>A. Accessing Currencies</text>
			<assertion id="427-A1">
				<text>Test if MonetaryCurrencies provides all ISO related entries,
					similar to the JDK.
				</text>
			</assertion>
			<assertion id="427-A2">
				<text>Test if MonetaryCurrencies provides all Locale related
					entries, similar to the JDK.
				</text>
			</assertion>
			<assertion id="427-A3">
				<text>Test if MonetaryCurrencies provides correct check for ISO
					codes.
				</text>
			</assertion>
			<assertion id="427-A4">
				<text>Test if MonetaryCurrencies provides correct check for
					Locales.
				</text>
			</assertion>
			<assertion id="427-A5">
				<text>Test for custom MonetaryCurrencies provided, based on the TCK
					TestProvider.
				</text>
			</assertion>
		</group>
		<group>
			<text>B. Accessing Monetary Amount Factories</text>
			<assertion id="427-B1">
				<text>Ensure the types available, must be at least one type (if one
					has a specified AmountFlavor, 2 are recommended).
				</text>
			</assertion>
			<assertion id="427-B2">
				<text>Ensure amount factories are accessible for all types
					available,
					providing also the
					some test implementations with the
					TCK.
				</text>
			</assertion>
			<assertion id="427-B3">
				<text>Ensure amount factories are accessible for all types
					available,
					providing also the
					some test implementations with the
					TCK,
					and that
					every factory accessed
					is a new instance.
				</text>
			</assertion>
			<assertion id="427-B4">
				<text>Ensure correct query function implementations, providing also
					the
					some test implementations with the TCK.
				</text>
			</assertion>
			<assertion id="427-B5">
				<text>Ensure a default factory is returned. Test javamoney.config
					for
					configuring default value.
				</text>
			</assertion>
		</group>
		<group>
			<text>C. Accessing Roundings</text>
			<assertion id="427-C1">
				<text>Access roundings using all defined currencies, including TCK
					custom currencies.
				</text>
			</assertion>
			<assertion id="427-C2">
				<text>Access roundings using a MonetaryContext. Use different
					MathContext/RoundingMode, as an attribute, when running
					on the JDK.
				</text>
			</assertion>
			<assertion id="427-C3">
				<text>Access custom roundings and ensure TCK custom roundings are
					registered.
				</text>
			</assertion>
			<assertion id="427-C4">
				<text>Test TCK custom roundings.
				</text>
			</assertion>
		</group>
     */
}
