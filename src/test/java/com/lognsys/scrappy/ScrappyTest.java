package com.lognsys.scrappy;

import java.util.List;
import java.util.Map;


import org.junit.Assert;
import org.junit.BeforeClass;


/**
 * created by pdoshi on 4/12/16.
 */

public class ScrappyTest {

    private static Map<String, String> mapProperties = null;

   @BeforeClass
   public static void init() {
        mapProperties = Scraper.loadProperties();
    }



    @org.junit.Test
    public void testPageRange() {

        //1. check condition if batch size is greater than pages
        int batchSize1 = 50;
        int pages1 = 33;
        List<Integer[]> listOfPages1 = Scraper.getPageRange(batchSize1, pages1);
        Assert.assertEquals("Page Range Created for batch size greater than pages", 1, listOfPages1.size());

        //2. check if batch size is closer to pages
        int batchSize2 = 33;
        int pages2 = 33;
        List<Integer[]> listOfPages2 = Scraper.getPageRange(batchSize2, pages2 );
        Assert.assertEquals("Page Range Created. BatchSize is same as pages", 1, listOfPages2.size());



        //3. check with application properties value
        int batchSize3 = Integer.parseInt(mapProperties.get(PropertyNames.ZOMATO_BATCHES.toString()));
        int pages3 = Integer.parseInt(mapProperties.get(PropertyNames.ZOMATO_PAGES.toString()));
        List<Integer[]> listOfPages3 = Scraper.getPageRange(batchSize3, pages3 );

        Assert.assertEquals("Page Range Created. BatchSize and Pages from application.properties",8, listOfPages3.size());


        //4. check if batch size is closer to pages
        int batchSize4 = 30;
        int pages4 = 35;
        List<Integer[]> listOfPages4 = Scraper.getPageRange(batchSize4, pages4);
        Assert.assertEquals("Page Range Created. batch size is closer to pages", 2, listOfPages4.size());


        //5. check with lower batch size
        int batchSize5 = 50;
        int pages5 = 300;
        List<Integer[]> listOfPages5 = Scraper.getPageRange(batchSize5, pages5);
        Assert.assertEquals("Page Range Created. batch size is closer to pages", 6, listOfPages5.size());

        //6. check with batch size and page range regular test case
        int batchSize6 = 50;
        int pages6 = 307;
        List<Integer[]> listOfPages6 = Scraper.getPageRange(batchSize6, pages6);
        Assert.assertEquals("Page Range Created. batch size and pages regular test case", 7, listOfPages6.size());

    }


}
