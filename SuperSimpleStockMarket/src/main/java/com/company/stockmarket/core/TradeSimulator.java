package main.java.com.company.stockmarket.core;

import main.java.com.company.stockmarket.utils.AnalyticsProvider;
import main.java.com.company.stockmarket.utils.TradeType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;


public class TradeSimulator {
    private TradeLedger tradeLedger;
    private double minPrice;
    private double maxPrice;
    private long minNumShares;
    private long maxNumShares;

    public TradeSimulator(TradeLedger tradeLedger, double minPrice, double maxPrice, long minNumShares, long maxNumShares) {
        this.tradeLedger = tradeLedger;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minNumShares = minNumShares;
        this.maxNumShares = maxNumShares;
    }

    public void generateTrades(int numTradesForEachStock) {
        for (Map.Entry<String, Stock> stockEntry : tradeLedger.getRegisteredStocks().entrySet()) {
            for (int i = 0; i < numTradesForEachStock; i++) {
                String stockSymbol = stockEntry.getKey();
                TradeRecord tradeRecord = new TradeRecord(stockSymbol, generateRandomPrice(minPrice, maxPrice), generateRandomNumShares(minNumShares, maxNumShares), TradeType.BUY);
                tradeLedger.addTrade(tradeRecord);
            }
        }
    }

    public BigDecimal generateRandomPrice(double rangeMin, double rangeMax) {
        double generatedDouble = rangeMin + new Random().nextDouble() * (rangeMax - rangeMin);
        return BigDecimal.valueOf(generatedDouble).setScale(AnalyticsProvider.SCALE, RoundingMode.HALF_UP);
    }

    public BigInteger generateRandomNumShares(long rangeMin, long rangeMax) {
        long generatedLong = rangeMin + (long) (new Random().nextDouble() * (rangeMax - rangeMin));
        return BigInteger.valueOf(generatedLong);
    }
}
