/*
 * Copyright (c) 2012, 2020, Werner Keil, Anatole Tresch. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;

import javax.money.MonetaryOperator;
import javax.money.Monetary;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.javamoney.moneta.function.MonetaryOperators;

/**
 * Implementation of TCK Setup class for the Moneta reference implementation.
 */
public final class MonetaTCKSetup implements JSR354TestConfiguration {

    @Override
    public Collection<Class> getAmountClasses() {
            return Arrays
                    .asList(new Class[]{Money.class, FastMoney.class});
    }

    @Override
    public Collection<Class> getCurrencyClasses() {
        try{
            return Arrays
                    .asList(new Class[]{Class.forName("org.javamoney.moneta.spi.JDKCurrencyAdapter")});
        }
        catch(ClassNotFoundException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            throw new RuntimeException("Currency class not loadable: org.javamoney.moneta.spi.JDKCurrencyAdapter");
        }
    }

    @Override
    public Collection<MonetaryOperator> getMonetaryOperators4Test(){
        List<MonetaryOperator> ops = new ArrayList<>();
        ops.add(MonetaryOperators.majorPart());
        ops.add(MonetaryOperators.minorPart());
        ops.add(MonetaryOperators.percent(BigDecimal.ONE));
        ops.add(MonetaryOperators.percent(3.5));
        ops.add(MonetaryOperators.permil(10.3));
        ops.add(MonetaryOperators.permil(BigDecimal.ONE));
        ops.add(MonetaryOperators.permil(10.5, MathContext.DECIMAL32));
        ops.add(MonetaryOperators.reciprocal());
        ops.add(Monetary.getDefaultRounding());
        ops.add(MonetaryConversions.getConversion("EUR"));
        return ops;
    }
}