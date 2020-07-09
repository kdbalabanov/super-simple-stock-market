package main.java.com.company.stockmarket.utils;

import main.java.com.company.stockmarket.core.TradeRecord;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The AnalyticsProvider is a stateless helper class meant to provide general analytics functionality
 */
public class AnalyticsProvider {
    private static final Logger LOGGER = Logger.getLogger(AnalyticsProvider.class.getName());
    public static final int SCALE = 2;

    private AnalyticsProvider() {}

    public static BigDecimal calculateGeometricMean(List<BigDecimal> allPrices) {
        BigDecimal geometricMean = BigDecimal.ZERO;
        BigDecimal allPricesProduct = BigDecimal.ZERO;

        if (allPrices != null && !allPrices.isEmpty()) {
            allPricesProduct = allPrices.stream().reduce(BigDecimal.valueOf(1), BigDecimal::multiply);
            geometricMean = calculateNthRoot(allPrices.size(), allPricesProduct);
        } else {
            LOGGER.log(Level.INFO, "Failed to calculate geometric mean - there were no prices for any trades.");
        }

        return geometricMean;
    }

    /**
     * Calculates the nth root of a value
     * For example, let n = 3 and x =8, the nth root is 2
     * @param power the power
     * @param allPricesProduct the product of all prices
     * @return the nth root
     */
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

    /**
     * Calculates the volume weighted stock price for a stock
     * @param tradesForStock the trades for the stock
     * @return the volume weighted stock price
     */
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
        } else {
            LOGGER.log(Level.INFO, "Failed to calculate volume weighted stock price - there were no trades.");
        }

        return volumeWeightedStockPrice;
    }

    /**
     * Get trades for the last N minutes
     * @param timestamp the timestamp
     * @param minutes the minutes
     * @param tradesForStock the trades for the stock
     * @return the trades for the last N minutes
     */
    public static List<TradeRecord> getTradesForLastNMinutes(Instant timestamp, int minutes, List<TradeRecord> tradesForStock) {
        return tradesForStock.stream().filter(trade -> trade.getTimestamp().compareTo(timestamp.minus(Duration.ofMinutes(minutes))) > 0).collect(Collectors.toList());
    }
}
