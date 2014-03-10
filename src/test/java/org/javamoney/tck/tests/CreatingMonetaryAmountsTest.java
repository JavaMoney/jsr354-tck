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
public class CreatingMonetaryAmountsTest{

    @Test
    public void placeholder(){
        Assert.fail();
    }

  /*
    <group>
    <text>A. Accessing MonetaryAmount Factories</text>
    <assertion id="426-A1">
    <text>Access a MonetaryAmountFactory for each registered type.
    </text>
    </assertion>
    <assertion id="426-A2">
    <text>Checks if getAmountType returns the correct type.</text>
    </assertion>
    <assertion id="426-A3">
    <text>Checks for default and max MonetaryContext.</text>
    </assertion>
    <assertion id="426-A4">
    <text>Checks if capabilities of default MonetaryContext are less
    than Max
    MonetaryContext.
    </text>
    </assertion>
    </group>
    <group>
    <text>B. Testing Creation of Amounts with zero values</text>
    <assertion id="426-B1">
    <text>For each MonetaryAmount Factory: Create zero amounts from a
    factory with currencies.
    </text>
    </assertion>
    <assertion id="426-B2">
    <text>For each MonetaryAmount Factory: Create zero amounts from a
    factory with monetary contexts.
    </text>
    </assertion>
    <assertion id="426-B3">
    <text>For each MonetaryAmount Factory: Bad Case: Create zero amounts
    from a factory with an invalid currency.
    </text>
    </assertion>
    <assertion id="426-B4">
    <text>For each MonetaryAmount Factory: Bad Case: Create zero amounts
    from a factory with an invalid MonetaryContext.
    </text>
    </assertion>
    </group>
    <group>
    <text>C. Testing Creation of Amounts with positive values</text>
    <assertion id="426-C1">
    <text>For each MonetaryAmount Factory: Create positive amounts from
    a factory with currencies.
    </text>
    </assertion>
    <assertion id="426-C2">
    <text>For each MonetaryAmount Factory: Create positive amounts from
    a factory with monetary contexts.
    </text>
    </assertion>
    <assertion id="426-C3">
    <text>For each MonetaryAmount Factory: Bad Case: Create positive
    amounts from a factory with an invalid numeric value (exceeding max
                                                          MonetaryContext).
    </text>
    </assertion>
    <assertion id="426-C4">
    <text>For each MonetaryAmount Factory: Bad Case: Create positive
    amounts from a factory with an invalid currency.
    </text>
    </assertion>
    <assertion id="426-C5">
    <text>For each MonetaryAmount Factory: Bad Case: Create positive
    amounts from a factory with an invalid MonetaryContext.
    </text>
    </assertion>
    </group>
    <group>
    <text>D. Testing Creation of Amounts with negative values</text>
    <assertion id="426-D1">
    <text>For each MonetaryAmount Factory: Create negative amounts from
    a factory with currencies.
    </text>
    </assertion>
    <assertion id="426-D2">
    <text>For each MonetaryAmount Factory: Create negative amounts from
    a factory with monetary contexts.
    </text>
    </assertion>
    <assertion id="426-D3">
    <text>For each MonetaryAmount Factory: Bad Case: Create negative
    amounts from a factory with an invalid numeric value (exceeding max
                                                          MonetaryContext).
    </text>
    </assertion>
    <assertion id="426-D4">
    <text>For each MonetaryAmount Factory: Bad Case: Create negative
    amounts from a factory with an invalid currency.
    </text>
    </assertion>
    <assertion id="426-D5">
    <text>For each MonetaryAmount Factory: Bad Case: Create negative
    amounts from a factory with an invalid MonetaryContext.
    </text>
    </assertion>
    </group>
    */

}
