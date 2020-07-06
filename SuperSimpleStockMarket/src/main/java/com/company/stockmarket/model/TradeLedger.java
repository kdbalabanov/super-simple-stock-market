package main.java.com.company.stockmarket.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeLedger {

    private Map<String, Stock> registeredStocks;
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

    public void addTrade(TradeRecord tradeRecord) {
        String stockSymbol = tradeRecord.getStockSymbol();

        if (trades.containsKey(stockSymbol)) {
            trades.get(stockSymbol).add(tradeRecord);
        } else {
            ArrayList<TradeRecord> tradeRecordsList = new ArrayList<>();
            tradeRecordsList.add(tradeRecord);
            trades.put(stockSymbol, tradeRecordsList);
        }
    }

    public List<TradeRecord> getTradesForStockSymbol(String stockSymbol) {
        if (trades.containsKey(stockSymbol)) {
            return trades.get(stockSymbol);
        }

        return null;
    }

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
