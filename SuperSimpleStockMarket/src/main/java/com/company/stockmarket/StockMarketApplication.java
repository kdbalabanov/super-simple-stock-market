package main.java.com.company.stockmarket;

import main.java.com.company.stockmarket.service.StockMarketService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main starting point of the Application
 */
public class StockMarketApplication {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        try {
            StockMarketService.getInstance().run();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }

        System.exit(0);
    }
}
