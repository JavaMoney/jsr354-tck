/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
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
        Assert.fail("Not implemenmted.");
    }

    /**
     * Access and test different Currency Conversions for the provider in place.<br/>
     * Test TCK providers, but also test implementation providers. Doing the ladder it
     * is not possible to test the rates quality, just that rates are returned if necessary.
     */
    @Test @SpecAssertion(id="431-A2", section="4.3.1")
    public void testConversionsAreAvailable(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Test if all providers returns valid meta data.
     * @see javax.money.convert.ProviderContext
     */
    @Test @SpecAssertion(id="431-A3", section="4.3.1")
    public void testProviderMetadata(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test @SpecAssertion(id="431-A4", section="4.3.1")
    public void testDefaultProviderChainIsDefined(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Access and test conversion using the default provider chain.<br/>
     * Hint the exact rate factors returned cannot be tested, just rates that make sense must be returned.
     */
    @Test @SpecAssertion(id="431-A5", section="4.3.1")
    public void testDefaultProviderChain(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Bad case: Test access of an inexistent provider. Should throw a MonetaryException
     */
    @Test(expected=MonetaryException.class) @SpecAssertion(id="431-A6", section="4.3.1")
    public void testUseInvalidProvider(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Bad case: Test access of an inexistent provider within a chain of providers (all other providers must be valid).
     * Should throw a MonetaryException
     */
    @Test(expected=MonetaryException.class) @SpecAssertion(id="431-A7", section="4.3.1")
    public void testUseInvalidProviderWithinChain(){
        Assert.fail("Not implemenmted.");
    }

}
