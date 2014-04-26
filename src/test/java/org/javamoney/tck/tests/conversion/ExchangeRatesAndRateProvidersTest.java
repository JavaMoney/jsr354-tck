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

/**
 * Tests for Exchange Rates and Rate Providers.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ExchangeRatesAndRateProvidersTest{

    // *************************** A. Test Basic MonetaryConversions Accessors *********************************

    /**
     * Test access to conversion rates.<br/>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test @SpecAssertion(id="433-A1", section="4.3.3")
    public void testAccessRates(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Ensure additional ConversionContext is passed correctly to SPIs.<br/>
     * Hint: this assertion will require some custom SPIs to be registered and selected for chain inclusion!
     */
    @Test @SpecAssertion(id="433-A2", section="4.3.3")
    public void testPassingOverConversionContextToSPIs(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Bad case: try accessing rates with incosistent/invalid data.<br/>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test @SpecAssertion(id="433-A3", section="4.3.3")
    public void testInvalidUsage(){
        Assert.fail("Not implemenmted.");
    }

}
