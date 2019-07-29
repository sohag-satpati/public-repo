package com.sohag.gui;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class PropertyTableModel extends DefaultTableModel {
	TableModel model;
	private static final String [] headers = {"Property","Value", "To be used"};
		
	
	@SuppressWarnings("rawtypes")
	Class[] columnTypes = new Class[] {
			Object.class, Object.class, Boolean.class
	};
	
	public void resetTable() {
		super.setRowCount(0);
	}
	
	public PropertyTableModel() {
		super(headers,0);
		this.resetTable();
		this.model = this;
	}
	
	public void setRowsData(Object[][] rowsData) {
		for(Object[] row : rowsData) {
			super.addRow(row);
		}
	}
	
	public Object[][] getTableData(){
		Object[][] data = null;
		int rowCount = super.getRowCount();
		int colCount = super.getColumnCount();
		data = new Object[rowCount][colCount];
		for(int r=0; r<rowCount; r++) {
			for(int c=0; c<colCount; c++) {
				@SuppressWarnings("rawtypes")
				Class ct = columnTypes[c];
				data[r][c] = ct.cast(super.getValueAt(r, c));
			}
		}
		
		return data;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columnIndex) {
		return columnTypes[columnIndex];
	}
	
	public int getColumnCount() {
      return headers.length;
    }


    public String getColumnName(int column) {
      return headers[column];
    }
    
    @Override 
    public boolean isCellEditable(int row, int column)
    {
        return column == 0 ? false : true;
    }
    /*public void setValueAt(Object value, int row, int col) {
    	Class c = columnTypes[col];
    	model.setValueAt(c.cast(value), row, col);
    }*/

	
}
