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
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

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
        Assert.fail("Not implemenmted.");
    }

    /**
     * Test correct rate evaluation for different provider chains, providers defined by the TCK, with historic rates.<br/>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test @SpecAssertion(id="434-A2", section="4.3.4")
    public void testCorrectRateEvaluationInChainHistoric(){
        Assert.fail("Not implemenmted.");
    }

    /**
     * Test availability of providers defined by the TCK.<br/>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test @SpecAssertion(id="434-A3", section="4.3.4")
    public void testTCKRateChainAvailability(){
        Assert.fail("Not implemenmted.");
    }

}
