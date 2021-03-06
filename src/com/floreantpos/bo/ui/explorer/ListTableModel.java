package com.floreantpos.bo.ui.explorer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class ListTableModel<E> extends AbstractTableModel {
	protected String[] columnNames;
	protected List<E> rows;

	protected int pageSize;
	protected int pageOffset;

	// Return values appropriate for the visible table part.
	public int getRowCount() {
		if (rows == null) {
			return 0;
		}
		return Math.min(pageSize, rows.size() - (pageOffset * pageSize));
	}

	protected String formatDouble(double d) {
		NumberFormat f = new DecimalFormat("0.##");
		return f.format(d);
	}

	// Use this method to figure out which page you are on.
	public int getPageOffset() {
		return pageOffset;
	}

	public int getPageCount() {
		if (rows == null) {
			return 1;
		}
		return (int) Math.ceil((double) rows.size() / pageSize);
	}

	// Use this method if you want to know how big the real table is . . . we
	// could also write "getRealValueAt()" if needed.
	public int getRealRowCount() {
		return rows.size();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int s) {
		if (s == pageSize) {
			return;
		}
		int oldPageSize = pageSize;
		pageSize = s;
		pageOffset = (oldPageSize * pageOffset) / pageSize;
		fireTableDataChanged();
		/*
		 * if (pageSize < oldPageSize) { fireTableRowsDeleted(pageSize,
		 * oldPageSize - 1); } else { fireTableRowsInserted(oldPageSize,
		 * pageSize - 1); }
		 */
	}

	// Update the page offset and fire a data changed (all rows).
	public void pageDown() {
		if (pageOffset < getPageCount() - 1) {
			pageOffset++;
			fireTableDataChanged();
		}
	}

	// Update the page offset and fire a data changed (all rows).
	public void pageUp() {
		if (pageOffset > 0) {
			pageOffset--;
			fireTableDataChanged();
		}
	}

	public ListTableModel() {
		super();
	}

	public ListTableModel(String[] names) {
		this.columnNames = names;
	}

	public ListTableModel(String[] names, List<E> rows) {
		columnNames = names;
		this.rows = rows;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		if (rows != null && this.rows != null && (rows.size() < this.rows.size() && (this.getPageOffset() > Math.ceil(rows.size() / this.getPageSize())))) {
			this.pageOffset = (int) (Math.ceil(rows.size() / this.getPageSize()));
		}
		this.rows = rows;
		if (rows != null) {
			this.pageOffset = 0;
		}
		fireTableDataChanged();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public E getRowData(int row) {
		return rows.get(row + (pageOffset * pageSize));
	}

	public void addItem(E data) {
		if (rows == null) {
			rows = new ArrayList();
		}
		int size = rows.size();
		rows.add(data);
		if (pageSize == rows.size() - 1) {
			pageSize = rows.size();
		}
		fireTableRowsInserted(size, size);
	}

	public void deleteItem(int index) {
		rows.remove(index);
		fireTableRowsDeleted(index, index);
	}

	public boolean deleteItem(Object item) {
		return rows.remove(item);
	}

	public void updateItem(int index) {
		fireTableRowsUpdated(index, index);
	}

	public int navigateToItem(E item) {
		int rowI = -1;
		for (int i = 0; i < rows.size(); i++) {
			if (rows.get(i).equals(item)) {
				rowI = i;
				break;
			}
		}
		double offset = (double)rowI / (double)this.getPageSize();
		this.pageOffset = (int) (Math.ceil(offset));
		fireTableDataChanged();
		return rowI - (this.pageOffset * this.getPageSize());
	}

	public void setPageOffset(int offset) {
		if (offset < rows.size() / pageSize) {
			this.pageOffset = offset;
			fireTableDataChanged();
		}
	}
}
