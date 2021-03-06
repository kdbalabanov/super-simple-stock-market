package test.java.com.company.stockmarket.utils;

import main.java.com.company.stockmarket.core.*;
import main.java.com.company.stockmarket.utils.AnalyticsProvider;
import main.java.com.company.stockmarket.utils.StockType;
import main.java.com.company.stockmarket.utils.TradeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class AnalyticsProviderTest {
    private Stock commonStock;
    private final String commonStockSymbol = "CommonStockSymbol";
    private final BigDecimal commonStockLastDividend = BigDecimal.valueOf(5);
    private final BigDecimal commonStockParValue = BigDecimal.valueOf(75);

    private Stock preferredStock;
    private final String preferredStockSymbol = "PreferredStockSymbol";
    private final BigDecimal preferredStockLastDividend = BigDecimal.valueOf(8);
    private final BigDecimal preferredStockFixedDividend = BigDecimal.valueOf(1.02);
    private final BigDecimal preferredStockParValue = BigDecimal.valueOf(150);

    private TradeLedger tradeLedger;

    @Before
    public void setup() {
        commonStock = new CommonStock(commonStockSymbol, StockType.COMMON, commonStockLastDividend, commonStockParValue);
        preferredStock = new PreferredStock(preferredStockSymbol, StockType.PREFERRED, preferredStockLastDividend, preferredStockFixedDividend, preferredStockParValue);
        tradeLedger = new TradeLedger();
        tradeLedger.registerStock(commonStock);
        tradeLedger.registerStock(preferredStock);
    }

    @After
    public void cleanUp() {
        commonStock = null;
        preferredStock = null;
        tradeLedger.wipeTradeLedger();
        tradeLedger = null;
    }

    @Test
    public void testCalculateGeometricMean() {
        double product = 1 * 2 * 3 * 4 * 5 * 6 * 7;
        BigDecimal expectedResult = AnalyticsProvider.calculateNthRoot(7, BigDecimal.valueOf(product));
        tradeLedger.addTrade(new TradeRecord(commonStockSymbol, BigDecimal.valueOf(1), BigInteger.valueOf(100), TradeType.BUY));
        tradeLedger.addTrade(new TradeRecord(commonStockSymbol, BigDecimal.valueOf(2), BigInteger.valueOf(100), TradeType.BUY));
        tradeLedger.addTrade(new TradeRecord(commonStockSymbol, BigDecimal.valueOf(3), BigInteger.valueOf(100), TradeType.BUY));
        tradeLedger.addTrade(new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(4), BigInteger.valueOf(55), TradeType.SELL));
        tradeLedger.addTrade(new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(5), BigInteger.valueOf(55), TradeType.SELL));
        tradeLedger.addTrade(new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(6), BigInteger.valueOf(55), TradeType.SELL));
        tradeLedger.addTrade(new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(7), BigInteger.valueOf(55), TradeType.SELL));
        assertEquals(expectedResult, AnalyticsProvider.calculateGeometricMean(tradeLedger.getAllPrices()));
    }

    @Test
    public void testCalculateNthRoot() {
        BigDecimal expectedResultA = BigDecimal.valueOf(Math.pow(8, 1.00/3.00)).setScale(2, RoundingMode.HALF_EVEN);
        assertEquals(expectedResultA, AnalyticsProvider.calculateNthRoot(3, BigDecimal.valueOf(8)));

        BigDecimal expectedResultB =  BigDecimal.valueOf(Math.pow(248832, 1.00/5.00)).setScale(2, RoundingMode.HALF_EVEN);
        assertEquals(expectedResultB, AnalyticsProvider.calculateNthRoot(5, BigDecimal.valueOf(248832)));

        BigDecimal expectedResultC = BigDecimal.valueOf(Math.pow(15, 1.00/3.00)).setScale(2, RoundingMode.HALF_EVEN);
        assertEquals(expectedResultC, AnalyticsProvider.calculateNthRoot(3, BigDecimal.valueOf(15)));

        BigDecimal expectedResultD = BigDecimal.valueOf(Math.pow(1124134553, 1.00/7.00)).setScale(2, RoundingMode.HALF_EVEN);
        assertEquals(expectedResultD, AnalyticsProvider.calculateNthRoot(7, BigDecimal.valueOf(1124134553)));
    }

    @Test
    public void testCalculateVolumeWeighthedStockPrice() {
        BigDecimal expectedResultA = BigDecimal.valueOf((double)(1*100 + 2*100 + 3*100) / (100 + 100 + 100)).setScale(AnalyticsProvider.SCALE, RoundingMode.HALF_UP);
        tradeLedger.addTrade(new TradeRecord(commonStockSymbol, BigDecimal.valueOf(1), BigInteger.valueOf(100), TradeType.BUY));
        tradeLedger.addTrade(new TradeRecord(commonStockSymbol, BigDecimal.valueOf(2), BigInteger.valueOf(100), TradeType.BUY));
        tradeLedger.addTrade(new TradeRecord(commonStockSymbol, BigDecimal.valueOf(3), BigInteger.valueOf(100), TradeType.BUY));
        assertEquals(expectedResultA, AnalyticsProvider.calculateVolumeWeighthedStockPrice(tradeLedger.getTradesForStockSymbol(commonStockSymbol)));

        BigDecimal expectedResultB = BigDecimal.valueOf((double)(433*5534 + 54*3343 + 644*1132 + 71*3222) / (5534 + 3343 + 1132 + 3222)).setScale(AnalyticsProvider.SCALE, RoundingMode.HALF_UP);
        tradeLedger.addTrade(new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(433), BigInteger.valueOf(5534), TradeType.SELL));
        tradeLedger.addTrade(new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(54), BigInteger.valueOf(3343), TradeType.SELL));
        tradeLedger.addTrade(new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(644), BigInteger.valueOf(1132), TradeType.SELL));
        tradeLedger.addTrade(new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(71), BigInteger.valueOf(3222), TradeType.SELL));
        assertEquals(expectedResultB, AnalyticsProvider.calculateVolumeWeighthedStockPrice(tradeLedger.getTradesForStockSymbol(preferredStockSymbol)));
    }

    @Test
    public void testGetTradesForLastNMinutes() {
        Instant timestamp = Instant.now();

        TradeRecord tradeRecordA = new TradeRecord(commonStockSymbol, BigDecimal.valueOf(1), BigInteger.valueOf(100), TradeType.BUY);
        tradeRecordA.setTimestamp(timestamp.minus(Duration.ofMinutes(5)));
        tradeLedger.addTrade(tradeRecordA);

        TradeRecord tradeRecordB = new TradeRecord(commonStockSymbol, BigDecimal.valueOf(2), BigInteger.valueOf(100), TradeType.BUY);
        tradeRecordB.setTimestamp(timestamp.minus(Duration.ofMinutes(10)));
        tradeLedger.addTrade(tradeRecordB);

        TradeRecord tradeRecordC = new TradeRecord(commonStockSymbol, BigDecimal.valueOf(3), BigInteger.valueOf(100), TradeType.BUY);
        tradeRecordC.setTimestamp(timestamp.minus(Duration.ofMinutes(20)));
        tradeLedger.addTrade(tradeRecordC);

        assertEquals(1, AnalyticsProvider.getTradesForLastNMinutes(timestamp, 7, tradeLedger.getTradesForStockSymbol(commonStockSymbol)).size());
        assertEquals(2, AnalyticsProvider.getTradesForLastNMinutes(timestamp, 11, tradeLedger.getTradesForStockSymbol(commonStockSymbol)).size());
        assertEquals(3, AnalyticsProvider.getTradesForLastNMinutes(timestamp, 21, tradeLedger.getTradesForStockSymbol(commonStockSymbol)).size());
    }

}
