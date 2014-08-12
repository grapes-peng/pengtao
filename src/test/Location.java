package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Location {

	int row;
	int column;
	double maxValue;

	Location(int row, int column, double maxValue) {
		this.row = row;
		this.column = column;
		this.maxValue = maxValue;
	}

	public static Location locateLargest(double[][] a) {

		int row = 0;
		int column = 0;
		double maxValue = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (a[i][j] > maxValue) {
					row = i;
					column = j;
					maxValue = a[i][j];
				}
			}
		}

		return new Location(row, column, maxValue);
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Entry the rows and columns of the array:");// 以空格间隔
		String rowColumn = readUserInput();
		int row = Integer.parseInt(rowColumn.split(" ")[0]);
		int column = Integer.parseInt(rowColumn.split(" ")[1]);
		double array[][] = new double[row][column];

		System.out.println("Entry  the array:");// 每行以空格间隔每个元素,以换行间隔每行
		for (int i = 0; i < row; i++) {
			String rowElments = readUserInput();
			int j = 0;
			for (String s : rowElments.split(" ")) {
				array[i][j] = Double.parseDouble(s);
				// System.out.print(array[i][j] + " ");
				j++;
			}
			j = 0;
		}

		Location locateLargest = locateLargest(array);
		System.out.println("(" + locateLargest.row + " ,"
				+ locateLargest.column + ") : " + locateLargest.maxValue);
	}

	private static String readUserInput() throws IOException {
		InputStreamReader is_reader = new InputStreamReader(System.in);
		return new BufferedReader(is_reader).readLine();
	}

}