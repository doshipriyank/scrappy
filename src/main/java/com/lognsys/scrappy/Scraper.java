package com.lognsys.scrappy;


import com.lognsys.scrappy.model.Zomato;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created by pdoshi on 2/24/16.
 */
public interface Scraper<E> {

    /**
     * generic method defined for Scrapers to implement.
     *
     * @param param
     * @return
     */
    public List<E> scrapeData(int param);

    /**
     * reportError method logs error report which include
     * total reocrds
     */
    public void reportError();

    /**
     *
     */
    public void startScraping();

    /**
     *
     * @param data
     */
    public void convertToJSON(List<E> data);


    public static Map<String, String> mapProperties = new HashMap<>();


    /**
     * Returns properties defined in application.properties file. All the properties defined have
     * been maintained in corresponding enums file PropertyName
     *
     * @return Map<String, String> mapProperties
     */
    static Map<String, String> loadProperties() {

        if (mapProperties.isEmpty()) {
            Properties properties = new Properties();

            try (InputStream in = Scraper.class.getClassLoader().getResourceAsStream("application.properties")) {
                System.out.println(in);
                properties.load(in);

                for (PropertyNames p : PropertyNames.values()) {
                    mapProperties.put(p.name(), properties.getProperty(p.name()));
                }

            } catch (FileNotFoundException fe) {
                fe.printStackTrace();

            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        return mapProperties;
    }


    /**
     * Returns List<Integer[]> of PageRange.
     * This method creates arraylist of page range by totalpages/batchsize
     *
     * @param batchSize
     * @param pages
     * @return list of PageRange
     */
    static List<Integer[]> getPageRange(int batchSize, int pages) {

        int remainderPages = 0;

        int arraySize  = pages / batchSize;

        //remainder pages after dividing by batch size and adding it to the count
        //also take care of condition if batch size is greater than pages
        if (pages % batchSize  != 0) {
            remainderPages = pages % batchSize;
            arraySize = arraySize + 1;
        }

        //init List of Integer[]
        List<Integer[]> listOfPages = new ArrayList<>();

        int startPage = 0;
        int endPage = 0;

        for (int i = 0; i < arraySize; i++) {

            //add remainder value at last iteration when remainder ≠ 0 else add batch size
            if (i == arraySize - 1 && remainderPages != 0)
                endPage = endPage + remainderPages;
            else
                endPage = endPage + batchSize;


            //add values to int arrays
            listOfPages.add(new Integer[]{startPage + 1, endPage});

            startPage = endPage;
        }

        return listOfPages;
    }


}
