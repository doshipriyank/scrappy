package com.lognsys.scrappy;


import com.lognsys.scrappy.impl.ScrapingFactory;
import com.lognsys.scrappy.impl.ZomatoScraper;
import com.lognsys.scrappy.model.Zomato;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.lognsys.scrappy.impl.ScrapingFactory.createScrapingFor;

/**
 * @author pdoshi - 02-08-2015
 *
 *         Description: Scrappy analogy is derive from Scooby Doo cartoon. This is a Data scraping
 *         tool to collect data from websites like eg:zomato.com and can be extended to scrape from
 *         other websites. This tool is currently scraping specific to Zomato. Idea is to run this
 *         script as a deamon using JAVA 8 multithreading capabilities.
 *
 *         Using ExecutorSevice to run N threads simultaneously and scrape data and store it in
 *         mongodb database
 *
 *         TODO: Need to scrape data from ajax calls to collection geo location
 */


public class Walk {

    /**
     * instance variables
     */
    private boolean isValidJSON = false;
    private int countRows = 0;


    public boolean validateJSON() {
        return false;
    }

    /**
     *
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        //start time
        long startTime = System.currentTimeMillis();
        Date startDate = new Date();
        System.out.println("ScrappyDoo adventure started  - " + startDate.toString());

        Scraper scraper = ScrapingFactory.createScrapingFor(PropertyNames.ZOMATO.name());
        scraper.startScraping();


        //end time
        long endTime = System.currentTimeMillis();

        float elapsedTime = (endTime - startTime) / 1000F;

        Date endDate = new Date();
        System.out.println("ScrappyDoo adventure started  - " + endDate.toString());

        System.out.println("Total time taken by Scrappy - " + elapsedTime);


    }


}

