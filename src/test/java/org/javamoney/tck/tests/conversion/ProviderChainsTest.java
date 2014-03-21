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

/**
 * Tests for conversion provider chains.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ProviderChainsTest{

    // ********************** A. Test Basic MonetaryConversions Accessors *********************************

    /**
     * Test correct rate evaluation for different provider chains, providers defined by the TCK.<br/>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test @SpecAssertion(id="434-A1", section="4.3.4")
    public void testCorrectRateEvaluationInChain(){
        Assert.fail();
    }

    /**
     * Test correct rate evaluation for different provider chains, providers defined by the TCK, with historic rates.<br/>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test @SpecAssertion(id="434-A2", section="4.3.4")
    public void testCorrectRateEvaluationInChainHistoric(){
        Assert.fail();
    }

    /**
     * Test availability of providers defined by the TCK.<br/>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test @SpecAssertion(id="434-A3", section="4.3.4")
    public void testTCKRateChainAvailability(){
        Assert.fail();
    }

}
