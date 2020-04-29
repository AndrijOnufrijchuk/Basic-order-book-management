package com.market.book.service;

import com.market.book.entity.Market;
import com.market.book.sorting.AscendingPriceSorter;
import com.market.book.sorting.DescendingPriceSorter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MarketService {

    //This is the main method of MarketService class.  It takes input from file as list of Strings and "decide" what to do(remove/add element, print best ask/bid,print size at specified price)
    public static void performMainOperations(LinkedList<String> inputFromFileAsListOfStrings, List<Market> markets, BufferedWriter bufferedWriter) throws IOException {
        for (String inputFromFileAsListOfString : inputFromFileAsListOfStrings) {
            String[] arrayWithElementsFromRow = inputFromFileAsListOfString.split(",");

            int counter = 0;
            for (String s : arrayWithElementsFromRow) {
                if (s != null) {
                    counter++;
                }

            }
            if (counter == 4) {

                assert arrayWithElementsFromRow[0] != null;
                if (arrayWithElementsFromRow[0].startsWith("u")) {

                    Market market = new Market(Integer.parseInt(arrayWithElementsFromRow[1]), Integer.parseInt(arrayWithElementsFromRow[2]), arrayWithElementsFromRow[3]);
                    markets.add(market);
                    if (arrayWithElementsFromRow[3].equals("bid")) {
                        MarketService.performTradeBid(markets, market);


                    }

                    if (arrayWithElementsFromRow[3].equals("ask")) {
                        MarketService.performTradeAsk(markets, market);
                    }

                }
            }
            if (counter == 3) {
                if (arrayWithElementsFromRow[0].startsWith("o")) {
                    if (arrayWithElementsFromRow[1].contains("buy")) {
                        MarketService.buyElement(markets, Integer.parseInt(arrayWithElementsFromRow[2]));
                    }
                    if (arrayWithElementsFromRow[1].contains("sell")) {
                        MarketService.sellElement(markets, Integer.parseInt(arrayWithElementsFromRow[2]));

                    }

                }
                if (arrayWithElementsFromRow[0].startsWith("q")) {
                    if (arrayWithElementsFromRow[1].contains("size")) {
                        System.out.println(MarketService.getSizeAtPrice(markets, Integer.parseInt(arrayWithElementsFromRow[2])));
                        bufferedWriter.write(MarketService.getSizeAtPrice(markets, Integer.parseInt(arrayWithElementsFromRow[2])));
                        bufferedWriter.newLine();
                    }
                }
            }
            if (counter == 2) {
                if (arrayWithElementsFromRow[0].startsWith("q")) {
                    if (arrayWithElementsFromRow[1].contains("best_bid")) {


                        System.out.println(MarketService.getBestBid(markets));
                        bufferedWriter.write(MarketService.getBestBid(markets));
                        bufferedWriter.newLine();
                    }
                    if (arrayWithElementsFromRow[1].contains("best_ask")) {


                        System.out.println(MarketService.getBestAsk(markets));
                        bufferedWriter.write(MarketService.getBestAsk(markets));
                        bufferedWriter.newLine();
                    }

                }
            }
        }
        bufferedWriter.close();
    }
    //removes <size> shares out of asks, most cheap ones.
    public static void buyElement(List<Market> markets, int size) {
        int indexOfLowestElement = 0;
        List<Market> askElements = new LinkedList<>();
        for (Market unsortedElement : markets) {
            if (unsortedElement.getStatus().equals("ask")) {
                askElements.add(unsortedElement);

            }
        }


        if (askElements.isEmpty() == false) {
            //set max as first element of list
            int min = askElements.get(0).getPrice();
            for (int i = 0; i < askElements.size(); i++) {
                if (askElements.get(i).getPrice() < min) {
                    min = askElements.get(i).getPrice();
                    indexOfLowestElement = askElements.indexOf(askElements.get(i));
                }
            }

            if (size > 0) {

                //if get_size==size
                if (askElements.get(indexOfLowestElement).getSize() == size) {

                    markets.remove(askElements.get(indexOfLowestElement));
                    askElements.remove(indexOfLowestElement);
                    size = 0;
                }
                //if get_size<size

                if (size > 0 && askElements.get(indexOfLowestElement).getSize() < size) {

                    size -= askElements.get(indexOfLowestElement).getSize();
                    markets.remove(askElements.get(indexOfLowestElement));
                    askElements.remove(askElements.get(indexOfLowestElement));
                    MarketService.sellElement(markets, size);
                    if (!askElements.isEmpty()) {
                        for (int i = 0; i < askElements.size(); i++) {
                            min = askElements.get(0).getPrice();
                            if (askElements.get(i).getPrice() > min) {

                                indexOfLowestElement = askElements.indexOf(askElements.get(i));
                            }
                        }
                    }
                }


                //handle IndexOutOfBoundsException if indexOfHighestElement is alone in list
                if (askElements.size() == 1 && indexOfLowestElement == 1) {
                    indexOfLowestElement = 0;
                }
                //handle bug when index is pointed to [1] element and the lowest element is [0]
                if (size > 0 && askElements.get(indexOfLowestElement).getSize() > size) {
                    askElements.get(indexOfLowestElement).setSize((askElements.get(indexOfLowestElement).getSize()) - size);

                    int a = markets.indexOf(askElements.get(indexOfLowestElement));
//avoid IndexOutOfBoundsException if index is linked to first element

                    if (a == markets.size() - 1) {
                        a--;
                    }
                    if (a == -1) {
                        a = 0;
                    }

                    markets.get(a).setSize(askElements.get(a).getSize());
                    size = 0;

                }

            }
        }

    }
    //removes <size> shares out of bids, most expensive ones
    public static void sellElement(List<Market> markets, int size) {
        int indexOfHighestElement = 0;
        List<Market> bidElements = new LinkedList<>();
        for (Market unsortedElement : markets) {
            if (unsortedElement.getStatus().equals("bid")) {
                bidElements.add(unsortedElement);

            }
        }


        if (bidElements.isEmpty() == false) {
            //set max as first element of list
            int max = bidElements.get(0).getPrice();
            for (int i = 0; i < bidElements.size(); i++) {
                if (bidElements.get(i).getPrice() > max) {
                    max = bidElements.get(i).getPrice();
                    indexOfHighestElement = bidElements.indexOf(bidElements.get(i));
                }
            }

            if (size > 0) {

                //if get_size==size
                if (bidElements.get(indexOfHighestElement).getSize() == size) {

                    markets.remove(bidElements.get(indexOfHighestElement));
                    bidElements.remove(indexOfHighestElement);
                    size = 0;
                }
                //if get_size<size

                if (size > 0 && bidElements.get(indexOfHighestElement).getSize() < size) {

                    size -= bidElements.get(indexOfHighestElement).getSize();
                    markets.remove(bidElements.get(indexOfHighestElement));
                    bidElements.remove(bidElements.get(indexOfHighestElement));
                    MarketService.sellElement(markets, size);
                    if (!bidElements.isEmpty()) {
                        for (int i = 0; i < bidElements.size(); i++) {
                            max = bidElements.get(0).getPrice();
                            if (bidElements.get(i).getPrice() > max) {

                                indexOfHighestElement = bidElements.indexOf(bidElements.get(i));
                            }
                        }
                    }
                }


                //handle IndexOutOfBoundsException if indexOfHighestElement is alone in list
                if (bidElements.size() == 1 && indexOfHighestElement == 1) {
                    indexOfHighestElement = 0;
                }
                //handle bug when index is pointed to [1] element and the lowest element is [0]
                if (size > 0 && bidElements.get(indexOfHighestElement).getSize() > size) {
                    bidElements.get(indexOfHighestElement).setSize((bidElements.get(indexOfHighestElement).getSize()) - size);

                    int a = markets.indexOf(bidElements.get(indexOfHighestElement));
//avoid IndexOutOfBoundsException if index is linked to first element

                    if (a == markets.size() - 1) {
                        a--;
                    }
                    if (a == -1) {
                        a = 0;
                    }

                    markets.get(a).setSize(bidElements.get(a).getSize());
                    size = 0;

                }

            }
        }


    }
    //This method write to console or elements(bid, ask or spread) in our market. Was used to check if program works properly
    public static void soutAllElements(List<Market> markets) {

        for (Market market : markets) {
            System.out.println(market.getPrice() + "," + market.getSize() + "," + market.getStatus());
        }

    }
    //print best bid price and size
    public static String getBestBid(List<Market> records) {

        List<Market> bidOnly = new ArrayList<>();
        for (Market record : records) {

            if (record.getStatus().equals("bid")) {
                bidOnly.add(record);

            }
        }

        int price = 0;
        int size = 0;
        for (Market record : bidOnly) {
            if (record.getPrice() > price) {
                price = record.getPrice();
                size = record.getSize();

            } else if (record.getPrice() == price) {
                size += record.getSize();

            }


        }

        return price + "," + size;
    }
    // print best ask price and size
    public static String getBestAsk(List<Market> records) {

        List<Market> askOnly = new ArrayList<>();
        for (Market record : records) {

            if (record.getStatus().equals("ask")) {
                askOnly.add(record);

            }
        }

        int price = 0;
        int size = 0;
        for (Market record : askOnly) {
            if (record.getPrice() > price) {
                price = record.getPrice();
                size = record.getSize();
            } else if (record.getPrice() == price) {
                size += record.getSize();
            }

        }

        return price + "," + size;
    }

    // print size at specified price (bid, ask or spread).
    public static String getSizeAtPrice(List<Market> markets, int price) {
        int counter = 0;
        List<Market> recordsWithoutPrints = new ArrayList<>();
        for (Market market : markets) {

            if (market.getStatus().equals("ask") || market.getStatus().equals("bid")) {
                recordsWithoutPrints.add(market);
            }

        }
        for (Market record : recordsWithoutPrints) {
            if (record.getPrice() == price) {
                counter += record.getSize();

            }


        }


        return String.valueOf(counter);
    }
    //This method is used to check if there is available ask(buy) that our bid(sell) responds.
    public static void performTradeBid(List<Market> markets, Market newlyAddedElement) {

        boolean end = false;
        if (newlyAddedElement.getSize() <= 0) {
            end = true;
        }
        if (!end) {
            markets.sort(new DescendingPriceSorter());
            for (Market element : markets) {

                if (element.getStatus().equals("ask")) {

                    if (newlyAddedElement.getPrice() <= element.getPrice()) {

                        if (!end && newlyAddedElement.getSize() == element.getSize()) {
                            markets.remove(newlyAddedElement);
                            markets.remove(element);

                            end = true;

                        }
                        if (!end && newlyAddedElement.getSize() < element.getSize()) {

                            markets.remove(newlyAddedElement);
                            element.setSize(element.getSize() - newlyAddedElement.getSize());
                            end = true;

                        }

                        if (!end && newlyAddedElement.getSize() > element.getSize()) {
                            newlyAddedElement.setSize(newlyAddedElement.getSize() - element.getSize());
                            markets.remove(element);
                            if (markets.size() == 0) {
                                markets.add(newlyAddedElement);
                            } else {
                                MarketService.performTradeBid(markets, newlyAddedElement);
                            }
                        }


                    }


                }
            }
        }


    }
//This method is used to check if there is available bid(sell) that our ask(buy) responds.
    public static void performTradeAsk(List<Market> markets, Market newlyAddedElement) {

        boolean end = false;
        if (newlyAddedElement.getSize() <= 0) {
            end = true;
        }
        if (!end) {
            markets.sort(new AscendingPriceSorter());
            for (Market element : markets) {
                if (element.getStatus().equals("bid")) {
                    if (newlyAddedElement.getPrice() >= element.getPrice()) {

                        if (!end && newlyAddedElement.getSize() == element.getSize()) {
                            markets.remove(newlyAddedElement);
                            markets.remove(element);

                            end = true;

                        }
                        if (!end && newlyAddedElement.getSize() < element.getSize()) {
                            markets.remove(newlyAddedElement);
                            element.setSize(element.getSize() - newlyAddedElement.getSize());
                            end = true;

                        }

                        if (!end && newlyAddedElement.getSize() > element.getSize()) {

                            newlyAddedElement.setSize((newlyAddedElement.getSize() - element.getSize()));
                            markets.remove(element);
                            if (markets.size() == 0) {
                                markets.add(newlyAddedElement);
                            } else {
                                MarketService.performTradeAsk(markets, newlyAddedElement);
                            }
                        }


                    }


                }
            }
        }
    }

}
