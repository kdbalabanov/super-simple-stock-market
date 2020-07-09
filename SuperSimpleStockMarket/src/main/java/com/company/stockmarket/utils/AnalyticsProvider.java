package main.java.com.company.stockmarket.utils;

import main.java.com.company.stockmarket.core.Stock;
import main.java.com.company.stockmarket.core.TradeRecord;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsProvider {
    public static final int SCALE = 2;

    private AnalyticsProvider() {}

    public static BigDecimal calculateDividendYield(Stock stock, BigDecimal tradePrice){
        BigDecimal dividendYield = BigDecimal.ZERO;
        StockType stockType = stock.getStockType();

        if (tradePrice != null && tradePrice.compareTo(BigDecimal.ZERO) > 0 && stock.getStockType() != null) {
            switch (stockType) {
                case COMMON:
                    BigDecimal lastDividend = stock.getLastDividend();
                    if (lastDividend != null && lastDividend.compareTo(BigDecimal.ZERO) > 0) {
                        dividendYield = lastDividend.divide(tradePrice, SCALE * 3, RoundingMode.HALF_UP);
                    }
                    break;
                case PREFERRED:
                    BigDecimal fixedDividend = stock.getFixedDividend();
                    BigDecimal parValue = stock.getParValue();
                    if (fixedDividend != null && fixedDividend.compareTo(BigDecimal.ZERO) > 0 && parValue != null && parValue.compareTo(BigDecimal.ZERO) > 0) {
                        dividendYield = (fixedDividend.multiply(parValue)).divide(tradePrice, SCALE * 3, RoundingMode.HALF_UP);
                    }
                    break;
                default:
                    return dividendYield;
            }
        } else {

        }

        return dividendYield;
    }

    public static BigDecimal calculatePriceEarningsRatio(Stock stock, BigDecimal tradePrice) {
        BigDecimal priceEarningsRatio = BigDecimal.ZERO;
        BigDecimal lastDividend = stock.getLastDividend();

        if (tradePrice != null && tradePrice.compareTo(BigDecimal.ZERO) > 0 && lastDividend != null && lastDividend.compareTo(BigDecimal.ZERO) > 0) {
            priceEarningsRatio = tradePrice.divide(lastDividend, SCALE * 3, RoundingMode.HALF_UP);
        } else {

        }

        return priceEarningsRatio;
    }

    public static BigDecimal calculateGeometricMean(List<BigDecimal> allPrices) {
        BigDecimal geometricMean = BigDecimal.ZERO;
        BigDecimal allPricesProduct = BigDecimal.ZERO;

        if (allPrices != null && !allPrices.isEmpty()) {
            allPricesProduct = allPrices.stream().reduce(BigDecimal.valueOf(1), BigDecimal::multiply);
            geometricMean = calculateNthRoot(allPrices.size(), allPricesProduct);
        }

        return geometricMean;
    }

    public static BigDecimal calculateNthRoot(int power, BigDecimal allPricesProduct) {
        BigDecimal nthRoot;
        nthRoot = allPricesProduct.divide(allPricesProduct, RoundingMode.HALF_EVEN);
        BigDecimal temp = BigDecimal.ZERO;
        BigDecimal e = BigDecimal.valueOf(0.1);

        do {
            temp = nthRoot;
            nthRoot = nthRoot.add(allPricesProduct.subtract(nthRoot.pow(power)).divide(BigDecimal.valueOf(power).multiply(nthRoot.pow(power - 1)), SCALE, RoundingMode.HALF_EVEN));
        } while (nthRoot.subtract(temp).abs().compareTo(e) > 0);

        return nthRoot.setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculateVolumeWeighthedStockPrice(List<TradeRecord> tradesForStock) {
        BigDecimal volumeWeightedStockPrice = BigDecimal.ZERO;
        BigDecimal numerator = BigDecimal.ZERO;
        BigInteger sumNumShares = BigInteger.ZERO;

        if (tradesForStock != null && !tradesForStock.isEmpty()) {
            List<TradeRecord> filteredTradesForStock = getTradesForLastNMinutes(Instant.now(), 15, tradesForStock);

            if (filteredTradesForStock != null && !filteredTradesForStock.isEmpty()) {
                for (TradeRecord tradeRecord : filteredTradesForStock){
                    if (tradeRecord.getNumShares() != null && tradeRecord.getNumShares().compareTo(BigInteger.ZERO) > 0
                            && tradeRecord.getTradePrice() != null && tradeRecord.getTradePrice().compareTo(BigDecimal.ZERO) > 0) {
                        sumNumShares = sumNumShares.add(tradeRecord.getNumShares());
                        numerator = numerator.add(tradeRecord.getTradePrice().multiply(new BigDecimal(tradeRecord.getNumShares())));
                    }
                }
                volumeWeightedStockPrice = numerator.divide(new BigDecimal(sumNumShares), SCALE, RoundingMode.HALF_UP);
                return volumeWeightedStockPrice;
            }
        }

        return volumeWeightedStockPrice;
    }

    public static List<TradeRecord> getTradesForLastNMinutes(Instant timestamp, int minutes, List<TradeRecord> tradesForStock) {
        return tradesForStock.stream().filter(trade -> trade.getTimestamp().compareTo(timestamp.minus(Duration.ofMinutes(minutes))) > 0).collect(Collectors.toList());
    }
}
