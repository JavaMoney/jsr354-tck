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


import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;
import javax.money.RoundingContext;
import javax.money.spi.RoundingProviderSpi;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Anatole on 26.04.2014.
 */
public class TestRoundingProvider implements RoundingProviderSpi{

    private Map<String,MonetaryOperator> customRoundings = new HashMap<>();

    public TestRoundingProvider(){
        customRoundings.put("NOSCALE", new MonetaryOperator(){
            @Override
            public MonetaryAmount apply(MonetaryAmount value){
                return value.getFactory()
                        .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(0, RoundingMode.HALF_EVEN))
                        .create();
            }
        });
    }

    @Override
    public MonetaryOperator getRounding(RoundingContext context){
        MonetaryOperator customRounding = customRoundings.get(context.getRoundingId());
        if(customRounding != null){
            return customRounding;
        }
        if(context.getCurrencyUnit() == null){
            return null;
        }
        Boolean cashRounding = context.getBoolean("cashRounding", Boolean.FALSE);
        Long timestamp = context.getTimestampMillis();
        if(cashRounding){
            if(timestamp != null){
                return getCashRounding(context.getCurrencyUnit(), timestamp);
            }
            return getCashRounding(context.getCurrencyUnit());
        }else{
            if("XAU".equals(context.getCurrencyUnit().getCurrencyCode())){
                if(timestamp != null){
                    return new MonetaryOperator(){
                        @Override
                        public MonetaryAmount apply(MonetaryAmount value){
                            return value.getFactory().setNumber(
                                    value.getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.UP)).create();
                        }
                    };
                }
                return new MonetaryOperator(){
                    @Override
                    public MonetaryAmount apply(MonetaryAmount value){
                        return value.getFactory()
                                .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(4, RoundingMode.UP))
                                .create();
                    }
                };
            }
        }
        return null;
    }


    private MonetaryOperator getCashRounding(CurrencyUnit currency){
        if("XAU".equals(currency.getCurrencyCode())){
            return new MonetaryOperator(){
                @Override
                public MonetaryAmount apply(MonetaryAmount value){
                    return value.getFactory()
                            .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(1, RoundingMode.DOWN))
                            .create();
                }
            };
        }
        return null;
    }

    private MonetaryOperator getCashRounding(CurrencyUnit currency, long timestamp){
        if("XAU".equals(currency.getCurrencyCode()) && timestamp < 100){
            return new MonetaryOperator(){
                @Override
                public MonetaryAmount apply(MonetaryAmount value){
                    return value.getFactory()
                            .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.DOWN))
                            .create();
                }
            };
        }
        return null;
    }

    private MonetaryOperator getCustomRounding(String customRoundingId){
        return customRoundings.get(customRoundingId);
    }

    @Override
    public Set<String> getRoundingIds(){
        return customRoundings.keySet();
    }
}
