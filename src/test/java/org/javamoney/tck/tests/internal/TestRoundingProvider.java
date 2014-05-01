package org.javamoney.tck.tests.internal;


import javax.money.*;
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
            public <T extends MonetaryAmount> T apply(T value){
                return (T) value.getFactory()
                        .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(0, RoundingMode.HALF_EVEN))
                        .create();
            }
        });
    }

    @Override
    public MonetaryOperator getRounding(RoundingContext context){
        MonetaryOperator customRounding = customRoundings.get(context.getRoundingId());
        if(customRounding!=null){
            return customRounding;
        }
        if(context.getCurrencyUnit()==null){
            return null;
        }
        Boolean cashRounding = context.getBoolean("cashRounding", Boolean.FALSE);
        Long timestamp = context.getTimestamp();
        if(cashRounding){
            if(timestamp != null){
                return getCashRounding(context.getCurrencyUnit(), timestamp);
            }
            return getCashRounding(context.getCurrencyUnit());
        }
        else{
            if("XAU".equals(context.getCurrencyUnit().getCurrencyCode())){
                if(timestamp != null){
                    return new MonetaryOperator(){
                        @Override
                        public <T extends MonetaryAmount> T apply(T value){
                            return (T) value.getFactory().setNumber(
                                    value.getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.UP));
                        }
                    };
                } return new MonetaryOperator(){
                    @Override
                    public <T extends MonetaryAmount> T apply(T value){
                        return (T) value.getFactory()
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
                public <T extends MonetaryAmount> T apply(T value){
                    return (T) value.getFactory()
                            .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(1, RoundingMode.DOWN));
                }
            };
        }
        return null;
    }

    private MonetaryOperator getCashRounding(CurrencyUnit currency, long timestamp){
        if("XAU".equals(currency.getCurrencyCode()) && timestamp < 100){
            return new MonetaryOperator(){
                @Override
                public <T extends MonetaryAmount> T apply(T value){
                    return (T) value.getFactory()
                            .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.DOWN));
                }
            };
        }
        return null;
    }

    private MonetaryOperator getCustomRounding(String customRoundingId){
        return customRoundings.get(customRoundingId);
    }

    @Override
    public Set<String> getCustomRoundingIds(){
        return customRoundings.keySet();
    }
}
