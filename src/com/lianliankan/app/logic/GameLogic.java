package com.lianliankan.app.logic;

import java.util.ArrayList;
import java.util.List;

import com.lianliankan.app.model.Point;

public class GameLogic {

	private int mapRow = 0;
	
	private int mapCol = 0;
	
	private int leftNum = 0;
	
	private int typeNum = 0;
	
	private int[][] map = null;
	
	public GameLogic() {
		this.mapRow = 8;
		this.mapCol = 10;
		this.leftNum = 80;
		this.typeNum = 13;
		this.map = new int[8][10];
	}
	
	public GameLogic(final int row, final int col, final int num) {
		this.mapRow = row;
		this.mapCol = col;
		this.leftNum = row * col;
		this.typeNum = num;
		this.map = new int[row][col];
	}
	
	public int getLeftNum() {
		return leftNum;
	}
	
	public void init() {
		int[][] staticMap = 
			{
					{3,1,4,3,7,3,7,7,3,8},
					{4,9,11,10,10,12,9,11,2,1},
					{5,4,3,2,3,10,4,10,2,8},
					{9,1,13,9,11,6,5,10,5,5},
					{11,3,13,9,11,6,5,10,5,5},
					{12,6,8,11,4,4,7,8,10,6},
					{7,7,7,7,3,9,2,1,13,11},
					{5,10,10,6,13,11,3,3,10,6}
			};
		
		for (int i = 0; i < mapRow; i++) {
			for (int j = 0; j < mapCol; j++) {
				this.map[i][j] = staticMap[i][j];
			}
		}
		
		this.leftNum = mapRow * mapCol;
	}
	
	public int[][] getMap() {
		return map;
	}
	
	public int getElementType(final int row, final int col) {
		return map[row][col];
	}
	
	protected boolean linkRow(final Point selectedOne, final Point selectedTwo) {
		int startY = Math.min(selectedOne.getCol(), selectedTwo.getCol());
		int stopY = Math.max(selectedOne.getCol(), selectedTwo.getCol());
		
		if ((selectedOne.getCol() == selectedTwo.getCol()) || (selectedOne.getRow() != selectedTwo.getRow())) {
			return false;
		}
		
		if ((stopY - startY) > 1) {
			for (int i = startY + 1; i < stopY; i++) {
				if (map[selectedOne.getRow()][i] != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	protected boolean linkCol(final Point selectedOne, final Point selectedTwo) {
		int startX = Math.min(selectedOne.getRow(), selectedTwo.getRow());
		int stopX = Math.max(selectedOne.getRow(), selectedTwo.getRow());
		
		if ((selectedOne.getRow() == selectedTwo.getRow()) || (selectedOne.getCol() != selectedTwo.getCol())) {
			return false;
		}
		
		if ((stopX - startX) > 1) {
			for (int i = startX + 1; i < stopX; i++) {
				if (map[i][selectedOne.getCol()] != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	protected List<Point> lineOne(final Point selectedOne, final Point selectedTwo) {
		
		boolean isConnected = false;
		List<Point> connPath = null;
		
		isConnected = linkRow(selectedOne, selectedTwo);
		if (! isConnected) {
			isConnected = linkCol(selectedOne, selectedTwo);
		}
		
		if (isConnected) {
			connPath = new ArrayList<Point>();
			connPath.add(selectedOne);
			connPath.add(selectedTwo);
		}
		
		return connPath;
	}
	
	protected List<Point> lineTwo(final Point selectedOne, final Point selectedTwo) {
		
		List<Point> connPath = null;
		
		int crossOneRow = selectedOne.getRow();
		int crossOneCol = selectedTwo.getCol();
		int crossTwoRow = selectedTwo.getRow();
		int crossTwoCol = selectedOne.getCol();
		Point crossOne = new Point(crossOneRow, crossOneCol);
		Point crossTwo = new Point(crossTwoRow, crossTwoCol);
		
		if (map[crossOneRow][crossOneCol] == 0) {
			if (linkRow(selectedOne, crossOne) && linkCol(selectedTwo, selectedOne)) {
				
				connPath = new ArrayList<Point>();
				connPath.add(selectedOne);
				connPath.add(crossOne);
				connPath.add(selectedTwo);
				return connPath;
			}
		}
		
		if ((map[crossTwoRow][crossTwoCol] == 0)) {
			if (linkRow(selectedTwo, crossTwo) && linkCol(selectedOne, crossTwo)) {
				
				connPath = new ArrayList<Point>();
				connPath.add(selectedOne);
				connPath.add(crossTwo);
				connPath.add(selectedTwo);
				return connPath;
			}
		}
		
		return connPath;
		
		
	}
	
	protected List<Point> lineThree(final Point selectedOne, final Point selectedTwo) {
		List<Point> connPath = null;
		
		for (int col = 0; col < mapCol; col++) {
			Point connPointOne = new Point(selectedOne.getRow(), col);
			Point connPointTwo = new Point(selectedTwo.getRow(), col);
			if (map[connPointOne.getRow()][connPointOne.getCol()] == 0 && map[connPointTwo.getRow()][connPointTwo.getCol()]
					== 0) {
				if (linkCol(connPointOne, connPointTwo)) {
					if (linkRow(selectedOne, connPointOne) && linkRow(selectedTwo, connPointTwo)) {
						connPath = new ArrayList<Point>();
						connPath.add(selectedOne);
						connPath.add(connPointOne);
						connPath.add(connPointTwo);
						connPath.add(selectedTwo);
						return connPath;
					}
				}
			}
		}
		
		for (int row = 0; row < mapRow; row++) {
			Point connPointOne = new Point(row, selectedOne.getCol());
			Point connPointTwo = new Point(row, selectedTwo.getCol());
			if (map[connPointOne.getRow()][connPointOne.getCol()] == 0 && 
					map[connPointTwo.getRow()][connPointTwo.getCol()] == 0) {
				if (linkRow(connPointOne, connPointTwo)) {
					if (linkCol(selectedOne, connPointOne) && linkCol(selectedTwo, connPointTwo)) {
						connPath = new ArrayList<Point>();
						connPath.add(selectedOne);
						connPath.add(connPointOne);
						connPath.add(connPointTwo);
						connPath.add(selectedTwo);
						return connPath;
					}
				}
			}
		}
		
		return connPath;
	}
	
	protected List<Point> getConnPath(final Point selectedOne, final Point selectedTwo) {
		List<Point> connPath = null;
		
		if (map[selectedOne.getRow()][selectedOne.getCol()] != map[selectedTwo.getRow()][selectedTwo.getCol()]) {
			return connPath;
		}
		
		if ((selectedOne.getRow() == selectedTwo.getRow()) && (selectedOne.getCol() == selectedTwo.getCol())) {
			return connPath;
		}
		
		connPath = lineOne(selectedOne, selectedTwo);
		if (connPath == null || connPath.size() == 0) {
			connPath = lineTwo(selectedOne, selectedTwo);
		}
		if (connPath == null || connPath.size() == 0) {
			connPath = lineThree(selectedOne, selectedTwo);
		}
		
		return connPath;
	}
	
	public List<Point> kill(final Point selectedOne, final Point selectedTwo) {
		List<Point> connPath = null;
		
		connPath = getConnPath(selectedOne, selectedTwo);
		
		if (connPath != null && connPath.size() > 0) {
			map[selectedOne.getRow()][selectedOne.getCol()] = 0;
			map[selectedTwo.getRow()][selectedTwo.getCol()] = 0;
			leftNum = leftNum - 2;
		}
		return connPath;
	}
	
	
}
