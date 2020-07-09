package main.java.com.company.stockmarket.core;

import main.java.com.company.stockmarket.utils.StockType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Child of Stock meant to represent PreferredStock
 */
public class PreferredStock extends Stock {
    private BigDecimal fixedDividend;

    public PreferredStock(String stockSymbol, StockType stockType, BigDecimal lastDividend, BigDecimal fixedDividend, BigDecimal parValue) {
        super(stockSymbol, stockType, lastDividend, parValue);
        this.fixedDividend = fixedDividend;
    }

    /**
     * PreferredStock specific implementation for calculating the dividend yield
     * @param tradePrice the trade price
     * @return the dividend yield
     */
    @Override
    public BigDecimal calculateDividendYield(BigDecimal tradePrice) {
        BigDecimal dividendYield = BigDecimal.ZERO;
        BigDecimal parValue = getParValue();

        if (tradePrice != null && tradePrice.compareTo(BigDecimal.ZERO) > 0 && getStockType() != null) {
            if (fixedDividend != null && fixedDividend.compareTo(BigDecimal.ZERO) > 0 && parValue != null && parValue.compareTo(BigDecimal.ZERO) > 0) {
                dividendYield = (fixedDividend.multiply(parValue)).divide(tradePrice, 6, RoundingMode.HALF_UP);
            }
        } else {

        }

        return dividendYield;
    }

    public BigDecimal getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(BigDecimal fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

}
