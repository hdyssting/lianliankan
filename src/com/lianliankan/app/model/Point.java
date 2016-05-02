package com.lianliankan.app.model;

public class Point {

	private int row;
	
	private int col;
	
	public Point() {
		this.row = 0;
		this.col = 0;
	}
	
	public Point(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	
}

