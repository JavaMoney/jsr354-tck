package org.javamoney.tck.tests.internal;


import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryContext;
import javax.money.MonetaryOperator;
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
    public MonetaryOperator getRounding(CurrencyUnit currency){
        if("XAU".equals(currency.getCurrencyCode())){
            return new MonetaryOperator(){
                @Override
                public <T extends MonetaryAmount> T apply(T value){
                    return (T) value.getFactory()
                            .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(4, RoundingMode.UP))
                            .create();
                }
            };
        }
        return null;
    }

    @Override
    public MonetaryOperator getRounding(CurrencyUnit currency, long timestamp){
        if("XAU".equals(currency.getCurrencyCode()) && timestamp < 100){
            return new MonetaryOperator(){
                @Override
                public <T extends MonetaryAmount> T apply(T value){
                    return (T) value.getFactory()
                            .setNumber(value.getNumber().numberValue(BigDecimal.class).setScale(2, RoundingMode.UP));
                }
            };
        }
        return null;
    }

    @Override
    public MonetaryOperator getCashRounding(CurrencyUnit currency){
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

    @Override
    public MonetaryOperator getCashRounding(CurrencyUnit currency, long timestamp){
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

    @Override
    public MonetaryOperator getCustomRounding(String customRoundingId){
        return customRoundings.get(customRoundingId);
    }

    @Override
    public MonetaryOperator getRounding(MonetaryContext monetaryContext){
        return null;
    }

    @Override
    public Set<String> getCustomRoundingIds(){
        return customRoundings.keySet();
    }
}
