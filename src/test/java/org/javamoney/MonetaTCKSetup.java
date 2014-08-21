/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.function.MonetaryFunctions;
import org.javamoney.moneta.function.MonetaryUtil;
import org.javamoney.tck.JSR354TestConfiguration;

import javax.money.MonetaryOperator;
import javax.money.MonetaryRoundings;
import javax.money.convert.MonetaryConversions;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

/**
 * Created by Anatole on 14.06.2014.
 */
public final class MonetaTCKSetup implements JSR354TestConfiguration{

    @Override
    public Collection<Class> getAmountClasses() {
            return Arrays
                    .asList(new Class[]{FastMoney.class,FastMoney.class});
    }

    @Override
    public Collection<Class> getCurrencyClasses() {
        try{
            return Arrays
                    .asList(new Class[] { Class.forName("org.javamoney.moneta.internal.JDKCurrencyAdapter")});
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException("Currency class not lodable: org.javamoney.moneta.internal.JDKCurrencyAdapter");
        }
    }

    @Override
    public Collection<MonetaryOperator> getMonetaryOperators4Test(){
        List<MonetaryOperator> ops = new ArrayList<>();
        ops.add(MonetaryUtil.majorPart());
        ops.add(MonetaryUtil.minorPart());
        ops.add(MonetaryUtil.percent(BigDecimal.ONE));
        ops.add(MonetaryUtil.percent(3.5));
        ops.add(MonetaryUtil.permil(10.3));
        ops.add(MonetaryUtil.permil(BigDecimal.ONE));
        ops.add(MonetaryUtil.permil(10.5, MathContext.DECIMAL32));
        ops.add(MonetaryUtil.reciprocal());
        ops.add(MonetaryRoundings.getDefaultRounding());
        ops.add(MonetaryConversions.getConversion("EUR"));
        return ops;
    }

}