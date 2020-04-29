package com.market.book.sorting;
import com.market.book.entity.Market;

import java.util.Comparator;

//This class is used as comparator to compare Market elements by price in descending order
public class DescendingPriceSorter implements Comparator<Market>{
    @Override
    public int compare(Market o1, Market o2) {
        return Integer.compare(o2.getPrice(),o1.getPrice());
    }
}
