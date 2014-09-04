/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.internal;

import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.spi.MonetaryAmountFactoryProviderSpi;

/**
 * Created by Anatole on 19.04.2014.
 */
public final class TestMonetaryAmountFactoryProvider implements MonetaryAmountFactoryProviderSpi{

    @Override
    public QueryInclusionPolicy getQueryInclusionPolicy(){
        return QueryInclusionPolicy.DIRECT_REFERENCE_ONLY;
    }

    @Override
    public Class getAmountType(){
        return TestAmount.class;
    }

    @Override
    public MonetaryAmountFactory createMonetaryAmountFactory(){
        return new TestMonetaryAmountBuilder();
    }

    @Override
    public MonetaryContext getDefaultMonetaryContext(){
        return TestAmount.MONETARY_CONTEXT;
    }

    @Override
    public MonetaryContext getMaximalMonetaryContext(){
        return TestAmount.MONETARY_CONTEXT;
    }
}
