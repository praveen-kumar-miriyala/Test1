package com.automator.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

	public static List<List<String>> readExcelAsArray(String filePath,String sheetName) throws IOException
	{
		//load excel file in to a file stream
		FileInputStream excelStream = new FileInputStream(filePath);;
		
		//open data stream as a POI workbook
		XSSFWorkbook wbExcelData = new XSSFWorkbook(excelStream);
		
		//Load the datasheet as POI worksheet
		XSSFSheet shDataSheet = wbExcelData.getSheet(sheetName);
		
		//get the no of columns and rows
		int totalNoOfCols = shDataSheet.getRow(0).getLastCellNum();
		int totalNoOfRows = shDataSheet.getPhysicalNumberOfRows();
		

		List<List<String>> lsExcelData=new ArrayList<List<String>>();
		//iterate through each row  in the datasheet
		for (int rowCounter = 0; rowCounter < totalNoOfRows; rowCounter++) 
		{
			List<String> lsRow=new ArrayList<String>();
 			for (int colCounter = 0; colCounter < totalNoOfCols; colCounter++) 
			{ 				
 				lsRow.add(shDataSheet.getRow(rowCounter).getCell(colCounter).toString().trim());
			}	
 			lsExcelData.add(lsRow);
		}

		shDataSheet=null;
		wbExcelData=null;
		excelStream.close();
		
		return lsExcelData;
		
	}
	
}
