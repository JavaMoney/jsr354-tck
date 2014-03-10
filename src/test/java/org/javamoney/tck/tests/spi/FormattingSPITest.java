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
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class FormattingSPITest{

    @Test
    public void placeholder(){
        Assert.fail();
    }

    /*
    <group>
			<text>A. Prodivding Monetary Amount Format Symbols</text>
			<assertion id="452-A1">
				<text>Test registered AmountFormatSymbolsSpi (one is required).
					Test
					behaviour (every locale in DecimalFormatSybols must be
					supported),
					especially bad case behaviour for
					invalid
					input.
				</text>
			</assertion>
		</group>
		<group>
			<text>B. Prodivding Amount Styles</text>
			<assertion id="452-B1">
				<text>Test registered AmountStyleProviderSpi (one is required).
					Test
					behaviour (every locale in DecimalFormatSybols must be
					supported),
					especially bad case behaviour for
					invalid
					input.
				</text>
			</assertion>
		</group>
		<group>
			<text>C. Prodivding Amount Formats</text>
			<assertion id="452-C1">
				<text>Test registered MonetaryAmountFormatProviderSpi (one is
					required),
					especially bad case behaviour for
					invalid
					input.
				</text>
			</assertion>
		</group>
     */

}
