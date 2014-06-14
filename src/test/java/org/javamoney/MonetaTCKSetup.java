package org.javamoney;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.internal.JDKCurrencyAdapter;
import org.javamoney.tck.JSR354TestConfiguration;

import javax.money.MonetaryOperator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
            return Arrays
                    .asList(new Class[] { JDKCurrencyAdapter.class});
    }

    @Override
    public Collection<MonetaryOperator> getMonetaryOperators4Test(){
        return Collections.emptyList();
    }

}