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

import javax.money.CurrencyQuery;
import javax.money.CurrencyUnit;
import javax.money.QueryType;
import javax.money.spi.CurrencyProviderSpi;
import java.util.*;

/**
 * Created by Anatole on 19.04.2014.
 */
public final class TestCurrencyProvider implements CurrencyProviderSpi{

    @Override
    public Set<CurrencyUnit> getCurrencies(CurrencyQuery currencyQuery){
        Set<CurrencyUnit> result = new HashSet<>(1);
        for(String cur: currencyQuery.getCurrencyCodes()){
            if(cur.endsWith("_test")){
                result.add(new TestCurrencyUnit(cur));
            }
        }
        for(Locale country: currencyQuery.getCountries()){
            if("test".equals(country.getVariant())){
                result.add(new TestCurrencyUnit(country.toString()));
            }
        }
        return result;
    }

    @Override
    public Set<QueryType> getQueryTypes() {
        return QueryType.DEFAULT_SET;
    }
}
