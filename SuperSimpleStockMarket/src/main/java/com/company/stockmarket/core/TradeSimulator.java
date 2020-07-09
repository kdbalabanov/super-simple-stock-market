package main.java.com.company.stockmarket.core;

import main.java.com.company.stockmarket.utils.AnalyticsProvider;
import main.java.com.company.stockmarket.utils.TradeType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;

/**
 * The TradeSimulator generates pre-defined number of trades for all registered stocks
 */
public class TradeSimulator {
    private final TradeLedger TRADE_LEDGER;
    // The minimum used for random price generation
    private final double MIN_PRICE;
    // The maximum used for random price generation
    private final double MAX_PRICE;
    // The minimum used for random number of shares generation
    private final long MIN_NUM_SHARES;
    // The maximum used for random number of shares generation
    private final long MAX_NUM_SHARES;

    public TradeSimulator(TradeLedger tradeLedger, double minPrice, double maxPrice, long minNumShares, long maxNumShares) {
        this.TRADE_LEDGER = tradeLedger;
        this.MIN_PRICE = minPrice;
        this.MAX_PRICE = maxPrice;
        this.MIN_NUM_SHARES = minNumShares;
        this.MAX_NUM_SHARES = maxNumShares;
    }

    /**
     * Generates trades for all stocks
     * Every time a trade is generated, all of the latest information and calculations are printed to the console
     * @param numTradesForEachStock
     */
    public void generateTrades(int numTradesForEachStock) {
        for (Map.Entry<String, Stock> stockEntry : TRADE_LEDGER.getRegisteredStocks().entrySet()) {
            for (int i = 0; i < numTradesForEachStock; i++) {
                String stockSymbol = stockEntry.getKey();
                Stock stock = stockEntry.getValue();

                TradeRecord tradeRecord = new TradeRecord(stockSymbol, generateRandomPrice(MIN_PRICE, MAX_PRICE), generateRandomNumShares(MIN_NUM_SHARES, MAX_NUM_SHARES), TradeType.BUY);
                TRADE_LEDGER.addTrade(tradeRecord);

                printUpdateForStock(stockSymbol, stock, tradeRecord);
            }
        }
    }

    /**
     * Prints Stock related information of interest based on the requirements
     * @param stockSymbol the StockSymbol of the Stock
     * @param stock the Stock
     * @param tradeRecord the TradeRecord
     */
    private void printUpdateForStock(String stockSymbol, Stock stock, TradeRecord tradeRecord) {
        BigDecimal dividendYield = stock.calculateDividendYield(tradeRecord.getTradePrice());
        BigDecimal priceEarningsRatio = stock.calculatePriceEarningsRatio(tradeRecord.getTradePrice());
        BigDecimal volumeWeightedPrice = AnalyticsProvider.calculateVolumeWeighthedStockPrice(TRADE_LEDGER.getTradesForStockSymbol(stockSymbol));
        System.out.printf("%-40s%-15s%-25s%-35s%-45s%-45s%n", tradeRecord.getTimestamp(), "Stock: " + stockSymbol, "Price: " + tradeRecord.getTradePrice(), "Dividend Yield: " + dividendYield,
                "Price Earnings Ratio: " + priceEarningsRatio, "Volume Weighted Price: " + volumeWeightedPrice);
    }

    /**
     * Generates a random price in the range of pre-defined min and max values
     * @param rangeMin the minimum
     * @param rangeMax the maximum
     * @return the random price
     */
    public BigDecimal generateRandomPrice(double rangeMin, double rangeMax) {
        double generatedDouble = rangeMin + new Random().nextDouble() * (rangeMax - rangeMin);
        return BigDecimal.valueOf(generatedDouble).setScale(AnalyticsProvider.SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Generates a random number of shares in the range of pre-defined min and max values
     * @param rangeMin the minimum
     * @param rangeMax the maximum
     * @return the random number of shares
     */
    public BigInteger generateRandomNumShares(long rangeMin, long rangeMax) {
        long generatedLong = rangeMin + (long) (new Random().nextDouble() * (rangeMax - rangeMin));
        return BigInteger.valueOf(generatedLong);
    }
}
