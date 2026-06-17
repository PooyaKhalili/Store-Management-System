package com.storesystem.util;
import com.storesystem.controller.ProductController;
import com.storesystem.model.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    private CsvUtil() {}
    public static List<String[]> readCsvFile(String filePath) throws FileNotFoundException {
        List<String[]> data = new ArrayList<>();
        FileReader fileReader = new FileReader(filePath);
        try(BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String line;
            String []columns;
            int lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null){
                if(line.trim().isEmpty()){
                    continue;
                }
                if(lineNumber == 1){
                    ++lineNumber;
                    continue;
                }
                String[] column = line.split(",");
                data.add(column);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    public static void writeCsvFile(String filePath,List<String[]> data) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
            String[] headers = {"ProductId", "Product Name", "Price", "Stock", "CategoryName"};
            bufferedWriter.write(String.join(",", headers));
            bufferedWriter.newLine();
            for (String[] row : data){
                bufferedWriter.write(String.join(",", row));
                bufferedWriter.newLine();
            }
        }
    }
}
