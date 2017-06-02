package com.lognsys.scrappy.impl;

import com.lognsys.scrappy.PropertyNames;
import com.lognsys.scrappy.Scraper;
import com.lognsys.scrappy.model.Zomato;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 *
 *
 * Created by pdoshi on 2/24/16.
 */
public class ZomatoScraper implements Scraper<Zomato> {


    /**
     * class member variables
     */
    private static int noOfThreads = 0;
    private static int pages = 0;
    private static int batch = 0;
    private static String URL = "";
    private static int errorCount = 0;
    private static int threadCount = 0;


    /**
     * Initialize value from application properties
     */
    public ZomatoScraper() {

        Map<String, String> propertiesMap = Scraper.loadProperties();

        noOfThreads = Integer.parseInt(propertiesMap.get(PropertyNames.ZOMATO_NO_OF_THREADS.name()));
        pages = Integer.parseInt(propertiesMap.get(PropertyNames.ZOMATO_PAGES.name()));
        batch = Integer.parseInt(propertiesMap.get(PropertyNames.ZOMATO_BATCHES.name()));
        URL = propertiesMap.get(PropertyNames.ZOMATO_URL.name());
    }


    /**
     * This main method spawns the threads and start scraping async No. of threads
     * are spawned by size of page range List which is returned by getPageRanged method
     */
    @Override
    public void startScraping() {

        //Init ExecutorService
        ExecutorService exec = Executors.newFixedThreadPool(noOfThreads);

        //Java1.8 Scraper Interface static method to getPageRange
        //Returns List of array pages which determines total number of pages to be spawned.
        List<Integer[]> listOfPageRange = Scraper.getPageRange(batch, pages);

        //List of Futures initialized to store the responses from the Zomoto Pages
        List<Future<Map<Integer, List<Zomato>>>> futures = new ArrayList<Future<Map<Integer, List<Zomato>>>>(10);



        //ListOfPageRange size will invoke total number of threads and each will contain page ranges
        // in batches of 50 eg : 1 - 50, 51 - 60 etc till the end.
        for (Integer[] pageRangeArr : listOfPageRange) {
            Future<Map<Integer, List<Zomato>>> f = exec.submit(new ScraperCallable(pageRangeArr, "Zomato Thread" + (++threadCount)));
            futures.add(f);
        }

        //This will shutdown all the child threads and then main thread.
        exec.shutdown();

        // Waiting for all the threads to finish in 120 seconds before termination
        try {
            exec.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Collect list of Futures
        ListIterator<Future<Map<Integer, List<Zomato>>>> futureIterator = futures.listIterator();

        while (futureIterator.hasNext()) {

            Future<Map<Integer, List<Zomato>>> f = futureIterator.next();


            try {

                Map<Integer, List<Zomato>> fmap = f.get();

                for (Map.Entry<Integer, List<Zomato>> entry : fmap.entrySet()) {

                    List<Zomato> zs = entry.getValue();

                    for (Zomato zomato : zs)
                        System.out.println(zomato.toString());

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

    public void myfunc(List<Zomato> l) {

    }


    /**
     * ScraperCallable implements callable interface and parallelly runs scraping of zomato web pages
     */
    private class ScraperCallable implements Callable {

        private Integer[] pageRange;
        private String threadName;


        public ScraperCallable(Integer[] pageRange, String threadName) {
            this.threadName = threadName;
            this.pageRange = pageRange;
        }


        /**
         * Returns map Map<Integer, List<Zomato>
         *
         * Callable thread runs call method which returns scraped data by each thread from Zomato
         */
        @Override
        public Map<Integer, List<Zomato>> call() throws Exception {

            Map<Integer, List<Zomato>> mapOfZomato = new HashMap<>();

            System.out.println("Starting ZomatoScraper Thread - " + this.threadName);

            //loop through pages and scrape data
            IntStream.rangeClosed(pageRange[0], pageRange[1]).forEach(
                    pageNo -> {
                        List<Zomato> list = scrapeData(pageNo);
                        mapOfZomato.put(pageNo, list);
                    }
            );

            System.out.println("Stoping ZomatoScraper Thread - " + this.threadName);

            return mapOfZomato;
        }

    }

    /**
     *
     * @param pageNo
     * @return
     */
    @Override
    public List<Zomato> scrapeData(int pageNo) {

        List<Zomato> listOfZomatoData = new ArrayList<>();

        try {

            Document doc = Jsoup.connect(URL + pageNo).get();

            doc.select("div.search-card").forEach(element -> {
                Zomato zomato = new Zomato();

                //Restaurant ID
                String resID = element.getElementsByTag("div").attr("data-res_id");
                zomato.setRestID(Long.parseLong(resID));

                //Restaurant Name
                String resName = element.getElementsByClass("result-title").text();
                zomato.setRestName(resName);

                //Restaurant SubZone
                String restZone = element.getElementsByClass("search_result_subzone").text();
                zomato.setSuburbArea(restZone);

                //Restaurant Address
                String restAdd = element.getElementsByClass("search-result-address").text();
                zomato.setAddress(restAdd);

                //Restaurant PhoneNo
                String phoneNo = element.getElementsByTag("a").attr("data-phone-no-str");
                zomato.setPhoneNo(phoneNo);

                //Restaurant Cuisine
                String cuisine = element.after("div.search-page-text.clearfix.row > span")
                        .after("span.col-s-5.col-m-4.ttupper.fontsize5.grey-text > a").before("div.res-cost.clearfix").text().replaceAll(".*.(?=Cuisines)", "").replaceAll("\\s+(Cost for two:).*", "");
                zomato.setCuisine(cuisine);

                //Restaurant Cost
                String cost = element.getElementsByClass("res-cost").text();
                zomato.setCost(cost);

                //Restaurant Timings
                String timing = element.getElementsByClass("res-timing").text();
                zomato.setTiming(timing);

                //Restaurant Votes
                String resVotes = element.getElementsByClass("rating-votes-div-" + resID).text();
                int votes = Integer.parseInt(resVotes.replaceAll("[^\\d]", ""));
                zomato.setVotes(votes);

                //Restaurant Ratings
                String restRatings = element.getElementsByClass("rating-for-" + resID).text();
                zomato.setRatings(Double.parseDouble(restRatings));

                //Reviews
                String reviews = element.getElementsByClass("result-reviews").text();
                zomato.setReviews(reviews);

                listOfZomatoData.add(zomato);

            });

        } catch (IOException iox) {
            iox.getMessage();
           // reportError(String strError);

        }

        return listOfZomatoData;
    }

    /**
     *
     */
    @Override
    public void reportError() {



    }

    /**
     *
     * @param data
     */
    @Override
    public void convertToJSON(List<Zomato> data) {

    }


}
