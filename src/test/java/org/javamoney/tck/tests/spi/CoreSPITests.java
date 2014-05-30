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

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the core SPI implementation.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class CoreSPITests{


    // ***************************************** A. Registering Currencies ***********************************

    /**
     * Test registered CurrencyProviderSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test
    @SpecAssertion(id = "451-A1", section = "4.5.1")
    public void testCurrencyProviderSpi(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Test registered MonetaryCurrenciesSingletonSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test
    @SpecAssertion(id = "451-A2", section = "4.5.1")
    public void testMonetaryCurrenciesSingletonSpi(){
        Assert.fail("Not implemenmted.");
    }


    // ***************************************** A. Registering Monetary Amount Factories **************************

    /**
     * Test registered MonetaryAmountsSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test
    @SpecAssertion(id = "451-B1", section = "4.5.1")
    public void testMonetaryAmountFactories(){
        Assert.fail("Not implemenmted.");
    }

    // ************************************ C. Backing the MonetaryAmounts Singleton ******************************

    /**
     * Test registered MonetaryAmountsSingletonSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test
    @SpecAssertion(id = "451-C1", section = "4.5.1")
    public void testMonetaryAmountsSingletonSpi(){
        Assert.fail("Not implemenmted.");
    }

    // ************************************ D. Registering Roundings ******************************

    /**
     * Test registered RoundingProviderSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test
    @SpecAssertion(id = "451-D1", section = "4.5.1")
    public void testRoundingProviderSpi(){
        Assert.fail("Not implemenmted.");
    }

    // ************************************ E. Adapting Currency Conversion ******************************

    /**
     * Test registered ConversionProviderSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test
    @SpecAssertion(id = "451-E1", section = "4.5.1")
    public void testConversionProviderSpi(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Test registered MonetaryConversionsSingletonSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test
    @SpecAssertion(id = "451-E2", section = "4.5.1")
    public void testMonetaryConversionsSingletonSpi(){
        Assert.fail("Not implemenmted.");
    }

}
