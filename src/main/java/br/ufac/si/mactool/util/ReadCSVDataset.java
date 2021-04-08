/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufac.si.mactool.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HelenoCampos
 */
public class ReadCSVDataset {

    private List<List<String>> rows;
    private List<String> columns;
    private HashMap<String, HashSet<String>> mergeList;

    public ReadCSVDataset(String filePath) {
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        try ( CSVReader csvReader = new CSVReader(new FileReader(filePath));) {
            String[] values;
            boolean firstRow = true;
            while ((values = csvReader.readNext()) != null) {
                if(firstRow){
                    columns.addAll(Arrays.asList(values));
                    firstRow = false;
                }else{
                    rows.add(Arrays.asList(values));
                }
            }
            processMergeList();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadCSVDataset.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | CsvValidationException ex) {
            Logger.getLogger(ReadCSVDataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the rows
     */
    public List<List<String>> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    /**
     * @return the columns
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    /**
     * @return the mergeList
     */
    public HashMap<String, HashSet<String>> getMergeList() {
        return mergeList;
    }

    public void processMergeList() {
        mergeList = new HashMap();
        for(List<String> row: rows){
            String projectName = row.get(4);
            String mergeSHA = row.get(9);
            if(!this.mergeList.containsKey(projectName)){
                this.mergeList.put(projectName, new HashSet<>());
            }
            this.getMergeList().get(projectName).add(mergeSHA);
        }
    }
}
