package main.java.com.company.stockmarket.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The TradeLedger is responsible for recording all trades made for the stocks
 */
public class TradeLedger {
    private static final Logger LOGGER = Logger.getLogger(TradeLedger.class.getName());

    // Stocks need to be registered before trades are executed
    private Map<String, Stock> registeredStocks;
    // Trades for each stock are stored in their own collection
    private Map<String, List<TradeRecord>> trades;

    public TradeLedger() {
        this.trades = new HashMap<>();
        this.registeredStocks = new HashMap<>();
    }

    public Map<String, List<TradeRecord>> getTrades() {
        return trades;
    }

    public void setTrades(Map<String, List<TradeRecord>> trades) {
        this.trades = trades;
    }

    public Map<String, Stock> getRegisteredStocks() {
        return registeredStocks;
    }

    public void setRegisteredStocks(Map<String, Stock> registeredStocks) {
        this.registeredStocks = registeredStocks;
    }

    public void registerStock(Stock stock) {
        if (!registeredStocks.containsKey(stock.getStockSymbol())) {
            registeredStocks.put(stock.getStockSymbol(), stock);
        }
    }

    public Boolean isStockRegistered(String stockSymbol) {
        return registeredStocks.containsKey(stockSymbol);
    }

    /**
     * Adds a TradeRecord - only if Stock is registered
     * @param tradeRecord the TradeRecord to be added
     */
    public void addTrade(TradeRecord tradeRecord) {
        String stockSymbol = tradeRecord.getStockSymbol();

        if (isStockRegistered(stockSymbol)) {
            if (trades.containsKey(stockSymbol)) {
                trades.get(stockSymbol).add(tradeRecord);
            } else {
                ArrayList<TradeRecord> tradeRecordsList = new ArrayList<>();
                tradeRecordsList.add(tradeRecord);
                trades.put(stockSymbol, tradeRecordsList);
            }
        } else {
            LOGGER.log(Level.INFO, "Failed to add trade for Stock " + stockSymbol + " - not registered in TradeLedger");
        }
    }

    /**
     * Returns all TradeRecords for a stock
     * @param stockSymbol StockSymbol of stock
     * @return a collection of TradeRecords
     */
    public List<TradeRecord> getTradesForStockSymbol(String stockSymbol) {
        if (trades.containsKey(stockSymbol)) {
            return trades.get(stockSymbol);
        } else {
            LOGGER.log(Level.INFO, "No trades found for Stock " + stockSymbol);
        }

        return null;
    }

    /**
     * Gets all prices for all recorded trades
     * @return a collection of prices
     */
    public List<BigDecimal> getAllPrices() {
        List<BigDecimal> allPrices = new ArrayList<>();

        for (Map.Entry<String, List<TradeRecord>> tradeRecords : trades.entrySet()) {
            for (TradeRecord tradeRecord : tradeRecords.getValue()) {
                allPrices.add(tradeRecord.getTradePrice());
            }
        }

        return allPrices;
    }

    public void wipeTradeLedger() {
        trades.clear();
        registeredStocks.clear();
    }

}
