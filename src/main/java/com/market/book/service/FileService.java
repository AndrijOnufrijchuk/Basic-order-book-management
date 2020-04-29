package com.market.book.service;

import java.io.*;
import java.util.LinkedList;

public class FileService {


    //This method takes file and return List of Strings from that file
    public LinkedList<String> getInputFromFileAsListOfStrings(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        LinkedList<String> listOfInputStrings = new LinkedList<>();
        String s = bufferedReader.readLine();

        while (s != null) {
            listOfInputStrings.add(s);
            s = bufferedReader.readLine();
        }

        return listOfInputStrings;
    }


}
