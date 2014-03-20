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

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class AccessingCurrenciesAmountsRoundingsTest{

    // ****************** A. Accessing Currencies *******************


    /**
     * Test if MonetaryCurrencies provides all ISO related entries,
     similar to the JDK.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-A1")
    public void testAllISOCurrenciesAvailable(){
        Assert.fail();
    }

    /**
     * Test if MonetaryCurrencies provides all Locale related
     entries, similar to the JDK.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-A2")
    public void testAllLocaleCurrenciesAvailable(){
        Assert.fail();
    }

    /**
     * Test if MonetaryCurrencies provides  correct instance with ISO
     codes.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-A3")
    public void testCorrectISOCodes(){
        Assert.fail();
    }

    /**
     * Test if MonetaryCurrencies provides correct instance with
     Locales.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-A4")
    public void testCorrectLocales(){
        Assert.fail();
    }

    /**
     * Test for custom MonetaryCurrencies provided, based on the TCK
     TestProvider.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-A5")
    public void testCustomCurrencies(){
        Assert.fail();
    }

    // ********************************* B. Accessing Monetary Amount Factories ***********************

    /**
     * Ensure the types available, must be at least one type (if one
     has a specified AmountFlavor, 2 are recommended).
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-B1")
    public void testAmountTypesDefined(){
        Assert.fail();
    }

    /**
     * Ensure amount factories are accessible for all types
     available,
     providing also the
     some test implementations with the
     TCK.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-B2")
    public void testAmountTypesProvided(){
        Assert.fail();
    }

    /**
     * Ensure amount factories are accessible for all types
     available,
     providing also the
     some test implementations with the
     TCK,
     and that
     every factory accessed
     is a new instance.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-B3")
    public void testAmountTypesInstantiatable(){
        Assert.fail();
    }

    /**
     * Ensure correct query function implementations, providing also
     the some test implementations with the TCK.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-B4")
    public void testAmountQueryType(){
        Assert.fail();
    }

    /**
     * Ensure a default factory is returned. Test javamoney.config
     for  configuring default value.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-B5")
    public void testAmountDefaultType(){
        Assert.fail();
    }

    // ********************************* C. Accessing Roundings *****************************

    /**
     * Access roundings using all defined currencies, including TCK
     custom currencies.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-C1")
    public void testAccessRoundingsForCustomCurrencies(){
        Assert.fail();
    }

    /**
     * Access roundings using a MonetaryContext. Use different
     MathContext/RoundingMode, as an attribute, when running
     on the JDK.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-C2")
    public void testAccessRoundingsWithMonetaryContext(){
        Assert.fail();
    }

    /**
     * Access custom roundings and ensure TCK custom roundings are
     registered.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-C3")
    public void testAccessCustomRoundings(){
        Assert.fail();
    }

    /**
     * Test TCK custom roundings.
     */
    @Test @SpecAssertion(section="4.2.7", id = "427-C4")
    public void testCustomRoundings(){
        Assert.fail();
    }

}
