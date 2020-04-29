package com.market.book.sorting;
import com.market.book.entity.Market;

import java.util.Comparator;

//This class is used as comparator to compare Market elements by price in ascending order
public class AscendingPriceSorter implements Comparator<Market>{
    @Override
    public int compare(Market o1, Market o2) {
        return Integer.compare(o1.getPrice(),o2.getPrice());
    }
}