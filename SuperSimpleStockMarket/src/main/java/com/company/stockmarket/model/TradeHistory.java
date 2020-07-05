package main.java.com.company.stockmarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeHistory {

    private Map<String, List<TradeRecord>> trades;

    public TradeHistory() {
        this.trades = new HashMap<>();
    }

    public Map<String, List<TradeRecord>> getTrades() {
        return trades;
    }

    public void setTrades(Map<String, List<TradeRecord>> trades) {
        this.trades = trades;
    }

    public void addTrade(TradeRecord tradeRecord) {
        String stockSymbol = tradeRecord.getStock().getStockSymbol();

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
}
