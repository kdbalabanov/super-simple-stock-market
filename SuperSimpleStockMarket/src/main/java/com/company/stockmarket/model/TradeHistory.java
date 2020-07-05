package main.java.com.company.stockmarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TradeHistory {

    private Map<String, ArrayList<TradeRecord>> trades;

    public TradeHistory() {
        this.trades = new HashMap<>();
    }

    public Map<String, ArrayList<TradeRecord>> getTrades() {
        return trades;
    }

    public void setTrades(Map<String, ArrayList<TradeRecord>> trades) {
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
}
