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

package org.javamoney.tck.tests.conversion;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Assert;
import org.junit.Test;

import javax.money.MonetaryException;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class MonetaryConversionsTest{

    // ************************************* A. Test Basic MonetaryConversions Accessors *****************************

    /**
     * Ensure at least one conversion provider is available.<br/>
     * Hint: ignore all TCK test providers, only count up productive providers.
     */
    @Test @SpecAssertion(id="431-A1", section="4.3.1")
    public void testProvidersAvailable(){
        Assert.fail();
    }

    /**
     * Access and test different Currency Conversions for the provider in place.<br/>
     * Test TCK providers, but also test implementation providers. Doing the ladder it
     * is not possible to test the rates quality, just that rates are returned if necessary.
     */
    @Test @SpecAssertion(id="431-A2", section="4.3.1")
    public void testConversionsAreAvailable(){
        Assert.fail();
    }

    /**
     * Test if all providers returns valid meta data.
     * @see javax.money.convert.ProviderContext
     */
    @Test @SpecAssertion(id="431-A3", section="4.3.1")
    public void testProviderMetadata(){
        Assert.fail();
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test @SpecAssertion(id="431-A4", section="4.3.1")
    public void testDefaultProviderChainIsDefined(){
        Assert.fail();
    }

    /**
     * Access and test conversion using the default provider chain.<br/>
     * Hint the exact rate factors returned cannot be tested, just rates that make sense must be returned.
     */
    @Test @SpecAssertion(id="431-A5", section="4.3.1")
    public void testDefaultProviderChain(){
        Assert.fail();
    }

    /**
     * Bad case: Test access of an inexistent provider. Should throw a MonetaryException
     */
    @Test(expected=MonetaryException.class) @SpecAssertion(id="431-A6", section="4.3.1")
    public void testUseInvalidProvider(){
        Assert.fail();
    }

    /**
     * Bad case: Test access of an inexistent provider within a chain of providers (all other providers must be valid).
     * Should throw a MonetaryException
     */
    @Test(expected=MonetaryException.class) @SpecAssertion(id="431-A7", section="4.3.1")
    public void testUseInvalidProviderWithinChain(){
        Assert.fail();
    }

}
