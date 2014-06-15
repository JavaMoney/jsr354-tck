package org.javamoney;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.function.MonetaryFunctions;
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
        ops.add(MonetaryFunctions.majorPart());
        ops.add(MonetaryFunctions.minorPart());
        ops.add(MonetaryFunctions.percent(BigDecimal.ONE));
        ops.add(MonetaryFunctions.percent(3.5));
        ops.add(MonetaryFunctions.permil(10.3));
        ops.add(MonetaryFunctions.permil(BigDecimal.ONE));
        ops.add(MonetaryFunctions.permil(10.5, MathContext.DECIMAL32));
        ops.add(MonetaryFunctions.reciprocal());
        ops.add(MonetaryRoundings.getRounding());
        ops.add(MonetaryConversions.getConversion("EUR"));
        return ops;
    }

}