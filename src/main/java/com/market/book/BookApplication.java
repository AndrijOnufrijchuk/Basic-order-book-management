package com.market.book;

import com.market.book.entity.Market;
import com.market.book.service.FileService;
import com.market.book.service.MarketService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class BookApplication {

    //@Author Andrij Onufriichuk

    public static void main(String[] args) throws Exception {
        //select input file
        File inputFile = new File("D:/Java projects/book/src/main/resources/test.txt");
        //select output file
        File outputFile = new File("outputFile.txt");
        //get output stream from output file
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        FileService fileProcessing = new FileService();

        //This linked list contains rows of input file
        LinkedList<String> inputFromFileAsListOfStrings = fileProcessing.getInputFromFileAsListOfStrings(inputFile);
        //I've decided to use LinkedList because programme use add/delete more often then display data or get element by index
        //In this list all strings from file will be transformed to Market objects
        List<Market> markets = new LinkedList<>();
        MarketService.performMainOperations(inputFromFileAsListOfStrings, markets, bufferedWriter);
       Object.class.

    }


}
