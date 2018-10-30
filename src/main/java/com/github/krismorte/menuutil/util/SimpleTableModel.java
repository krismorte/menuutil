/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.util;

import javax.swing.table.AbstractTableModel;

public class SimpleTableModel extends AbstractTableModel {


    private Object[] columns;
    private Object[][] rows;
    private int numberOfRows;

    public SimpleTableModel(Object[] columns, Object[][] rows) {
        this.columns = columns;
        this.rows = rows;
        this.numberOfRows = rows.length;
    }

    @Override
    public Class getColumnClass(int column) throws IllegalStateException {
        try {
            return columns[column].getClass();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return Object.class;
    }

    @Override
    public int getColumnCount() throws IllegalStateException {

        try {
            return columns.length;
        } catch (Exception sex) {
            System.out.println(sex.getMessage());
        }

        return 0;
    }

    @Override
    public String getColumnName(int column) throws IllegalStateException {

        try {
            return columns[column].toString();
        } catch (Exception sex) {
            System.out.println(sex.getMessage());
        }

        return "";
    }

    @Override
    public int getRowCount() throws IllegalStateException {
        return numberOfRows;
    }

    @Override
    public Object getValueAt(int row, int column)
            throws IllegalStateException {
        try {
            return rows[row][column];
        } catch (Exception sex) {
            System.out.println(sex.getMessage());
        }

        return "";
    }

}
