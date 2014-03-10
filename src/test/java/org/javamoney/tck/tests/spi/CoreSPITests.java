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

package org.javamoney.tck.tests.spi;

import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the core SPI implementation.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class CoreSPITests{


    @Test
    public void placeholder(){
        Assert.fail();
    }

    /*
    <group>
			<text>A. Registering Currencies</text>
			<assertion id="451-A1">
				<text>Test registered CurrencyProviderSpi (at least one instance
					required). Test behaviour,
					especially bad case behaviour for invalid
					input.
				</text>
			</assertion>
		</group>
		<group>
			<text>B. Registering Monetary Amount Factories</text>
			<assertion id="451-B1">
				<text>Test registered MonetaryAmountsSpi (at least one instance
					required). Test behaviour,
					especially bad case behaviour for invalid
					input.
				</text>
			</assertion>
		</group>
		<group>
			<text>C. Backing the MonetaryAmounts Singleton</text>
			<assertion id="451-C1">
				<text>Test registered RoundingProviderSpi (at least one instance
					required). Test behaviour,
					especially bad case behaviour for invalid
					input.
				</text>
			</assertion>
		</group>
		<group>
			<text>D. Registering Roundings</text>
			<assertion id="451-D1">
				<text>Test registered RoundingProviderSpi (at least one instance
					required). Test behaviour,
					especially bad case behaviour for invalid
					input.
				</text>
			</assertion>
		</group>
		<group>
			<text>E. Adapting Currency Conversion</text>
		</group>
     */

}
