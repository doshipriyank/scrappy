package com.lognsys.scrappy.impl;

import com.lognsys.scrappy.PropertyNames;
import com.lognsys.scrappy.Scraper;


/**
 * Factory Pattern is used for scraping any website
 * so that it becomes easy to build other scraping class wihtout changing
 * client
 *
 * Created by pdoshi on 4/14/16.
 */
public class ScrapingFactory {

    public static Scraper createScrapingFor(String portal){
        Scraper scraper = null;

        if(portal.equals(PropertyNames.ZOMATO.name()))
            scraper = new ZomatoScraper();


        return scraper;
    }

}
