package com.automator.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.automator.setup.TestSetup;

public class DataLoader extends TestSetup {
	
	private String currentMethodName = "";
	private String currentClassName = "";
	private String currentPackageName = "";

		
	public DataLoader( String currentMethodName , String currentClassName , String currentPackageName ) {
		this.currentMethodName=currentMethodName;
		this.currentClassName=currentClassName;
		this.currentPackageName=currentPackageName;	
	}
	
	private String getUsableFilePath(String pathHighPriority,String pathLowPriority)
	{
		if(PropManager.checkFileExits(pathHighPriority)) {
			return pathHighPriority;
		}
		else if(PropManager.checkFileExits(pathLowPriority)){
			return pathLowPriority;
		}
		return "File does not exist";
	}
	
	public String getDataFilePath()
	{
		String appName=currentPackageName;
		String methodName=currentMethodName;
		String className=currentClassName;	
		String rootDir = PropManager.getAbsolutPath("DataStore");
		String sysDir = System.getProperty("user.dir");
		String commonDataFileSub=PropManager.getProperty("CommonDataFile_Sub");
		String commonDataFileMaster=PropManager.getProperty("CommonDataFile_Master");
		if(rootDir==null ^ rootDir.isEmpty())
		{
			rootDir=sysDir;
		}
		
		String methodFilePath=rootDir+"\\"+appName+"\\"+methodName+".xlsx";
		String classFilePath=rootDir+"\\"+appName+"\\"+className+".xlsx";		
		String commonFileSubPath=rootDir+"\\"+appName+"\\"+commonDataFileSub+".xlsx";
		String commonFileMasterPath=rootDir+"\\"+ commonDataFileMaster+".xlsx";
		
		if(appName.trim().isEmpty())
		{
			methodFilePath=rootDir+"\\"+methodName+".xlsx";
			classFilePath=rootDir+"\\"+className+".xlsx";		
			commonFileSubPath=rootDir+"\\"+commonDataFileSub+".xlsx";
		}
				
		String dataFile=getUsableFilePath(sysDir+"\\"+methodName+".xlsx",methodFilePath);

		if(dataFile.equalsIgnoreCase("File does not exist")) {
			dataFile=getUsableFilePath(sysDir+"\\"+className+".xlsx",classFilePath);
			if(dataFile.equalsIgnoreCase("File does not exist"))
			{
				dataFile=getUsableFilePath(sysDir+"\\"+commonDataFileSub+".xlsx",commonFileSubPath);
				if(dataFile.equalsIgnoreCase("File does not exist"))
				{					
					dataFile=getUsableFilePath(sysDir+"\\"+commonDataFileMaster+".xlsx",commonFileMasterPath);
					if(dataFile.equalsIgnoreCase("File does not exist"))
					{		
						dataFile="Data file does not exist";						
					}					
				}
			}

		}
	
		return dataFile;
		
	}

	private String getMasterDataFilePath()
	{		
		String rootDir = PropManager.getAbsolutPath("DataStore");	
		String sysDir = System.getProperty("user.dir");
		String masterDataSet=PropManager.getProperty("MasterDataFile");
		if(rootDir==null ^ rootDir.isEmpty())
		{
			rootDir=System.getProperty("user.dir");
		}		

		String masterDataFile=getUsableFilePath(sysDir+"\\"+masterDataSet+".xlsx",rootDir+"\\"+masterDataSet+".xlsx");
		if(masterDataFile.equalsIgnoreCase("File does not exist")) {

			masterDataFile="Master Data file does not exist";
		}
		
		return masterDataFile;
	}

	
	
	/**
	 * Reads the data excel and returns data as a 2D array
	 * @return
	 */
	public Object [][] readExcel()
	{
		FileInputStream fsDataExcelStream = null;
		FileInputStream fsMasterDataExcelStream = null;
		try {
			//get data file path
			String dataFile= getDataFilePath();
			//get master data file path
			String masterDataFile=getMasterDataFilePath();
			
			if(!dataFile.equals("Data file does not exist"))
			{			
				//get the data sheet name from config.properties
				String dataSheetName=PropManager.getProperty("DataSheetName");
				//get the setup sheet name from config.properties
				String setupSheetName=PropManager.getProperty("SetupSheetName");
				//default datasheet to "Sheet1" if the same is not set in config
				if(dataSheetName==null ^ dataSheetName.isEmpty())
				{
					dataSheetName="Sheet1";
				}
				//default setupSheetName to "Sheet2" if the same is not set in config
				if(setupSheetName==null ^ setupSheetName.isEmpty())
				{
					setupSheetName="Sheet2";
				}
				
				//load data file in to a file stream
				fsDataExcelStream = new FileInputStream(dataFile);
				//load master data file in to a file stream				
				fsMasterDataExcelStream = new FileInputStream(masterDataFile);
				
				//open data stream as a POI workbook
				XSSFWorkbook wbTestData = new XSSFWorkbook(fsDataExcelStream);
				
				//open master data stream as a POI workbook
				XSSFWorkbook wbMasterTestData = new XSSFWorkbook(fsMasterDataExcelStream);
				
				//Read the setup sheet and obtain the data as a 2D Array
				Object[][] commFieldsArr=readSetupData(wbTestData,setupSheetName, wbMasterTestData);

				//Load the datasheet as POI worksheet
				XSSFSheet shInputDataSheet = wbTestData.getSheet(dataSheetName);
				
				if(null==shInputDataSheet)
				{
					shInputDataSheet = wbTestData.getSheet(currentMethodName);
				}
	
				//get the no of columns and rows
				int totalNoOfCols = shInputDataSheet.getRow(0).getLastCellNum();
				int totalNoOfRows = shInputDataSheet.getPhysicalNumberOfRows();

				//2d Array to hold the data from excel
				Object [][] dataRowsArray=null;

				//checks if there is an enabled key as the first column in the data
				//if enabled keys is present then row will be executed only if the value is set to Yes
				boolean checkEnabled=false;
				if(shInputDataSheet.getRow(0).getCell(0).toString().trim().toLowerCase().equals("enabled"))
				{
					checkEnabled=true;
				}
				
				boolean dataRowAdded=false;
				
				//iterate through each row  in the datasheet
				for (int rowCounter = 1; rowCounter < totalNoOfRows; rowCounter++) 
				{
					boolean pickRow=false;
					if(checkEnabled)
					{
						//if enabled column exists and value is set to Yes the pick the row for execution
						if(shInputDataSheet.getRow(rowCounter).getCell(0).toString().trim().toLowerCase().equalsIgnoreCase("yes"))
						{
							pickRow=true;
						}
					}
					else
					{
						//if enabled column doesnt exist the pick the row any ways
						pickRow=true;
					}
					if(pickRow)
					{
						//read the data from each column of the array and load to another 2d array
						Object [][] dataFieldsArray=new Object [totalNoOfCols][];
						for (int colCounter = 0; colCounter <= totalNoOfCols; colCounter++) 
						{
							//make sure that row header is  not empty
							if (null !=shInputDataSheet.getRow(0).getCell(colCounter)) 
							{
								//read the data if value in the col is not null
								if (null != shInputDataSheet.getRow(rowCounter).getCell(colCounter)) 
								{
									//if the values is a reference data then again parse master data for the reference
									if(shInputDataSheet.getRow(rowCounter).getCell(colCounter).toString().trim().substring(0, 1).equals("."))
									{
										dataFieldsArray=readRefData(shInputDataSheet.getRow(0).getCell(colCounter).toString().trim(),shInputDataSheet.getRow(rowCounter).getCell(colCounter).toString().trim().replaceAll("\\.", ""),wbMasterTestData,dataFieldsArray,"");
									}
									else
									{
										Object [] dataArray=new Object[]{ shInputDataSheet.getRow(0).getCell(colCounter).toString().trim(),shInputDataSheet.getRow(rowCounter).getCell(colCounter).toString().trim()};
										dataFieldsArray[colCounter]=dataArray;
									}
								}
							}
						}
						
						//append the setup data with the datafields read from setup
						dataFieldsArray=mergeArray(commFieldsArr,dataFieldsArray );
						//copy the new row to previously parsed rows
						dataRowsArray=copyArray(dataRowsArray,2,new Object[]{rowCounter,dataFieldsArray}  );
						dataRowAdded=true;
					}
				}
				
				shInputDataSheet=null;
				wbTestData=null;
				wbMasterTestData=null;
				fsDataExcelStream=null;
				fsMasterDataExcelStream=null;
				
				//if atleast one rows is added return the parsed array 
				//else return a blank array
				if(dataRowAdded)
				{
					return dataRowsArray;
				}
				else
				{
					return new Object [0][0];
				}
			}
			else
			{
				return new Object [0][0];
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
			
	}
	
	/**
	 * Read the setup sheet and obtain the data as a 2D Array
	 * @param wbTestData  - POI workbook object
	 * @param setupSheetName - Setup Sheet name
	 * @param fsMasterDataStream - Master Data File Stream
	 * @return 2D array with setup data
	 */
	private Object [][] readSetupData(XSSFWorkbook wbTestData,String setupSheetName,XSSFWorkbook wbMasterTestData)
	{
		
		try {			
						 	
			//Set the POI object for Setup Sheet
			XSSFSheet shSetupDataSheet = wbTestData.getSheet(setupSheetName);		
			//Get number of setup configs
			int totalNoOfRows = shSetupDataSheet.getPhysicalNumberOfRows();
			//Array to hold the setup data
			Object [][] dataFieldsArray=null;			
			boolean dataRowAdded=false;
			//iterate through each of the setup key
			for (int rowCounter = 0; rowCounter < totalNoOfRows; rowCounter++) 
			{	
				//make sure that row key is  not empty
				if (null !=shSetupDataSheet.getRow(rowCounter).getCell(0)) 
				{
					//Process the row only if the key value is not blank
					if(!shSetupDataSheet.getRow(rowCounter).getCell(1).toString().trim().equals(""))
					{
						//get the setup key and value
						String paramKey=shSetupDataSheet.getRow(rowCounter).getCell(0).toString().trim();
						String paramValue=shSetupDataSheet.getRow(rowCounter).getCell(1).toString().trim();
						if(paramValue.substring(0, 1).equals("."))
						{
							//Read refernce data from Master Data excel with Setup Key as the SheetName and values as the Reference Data Key
							dataFieldsArray=readRefData(paramKey,paramValue.replaceAll("\\.", ""),wbMasterTestData,dataFieldsArray,"");
						}
						else
						{						
							//Add the data from setup to datafields array
							Object [] dataArray=new Object[]{ paramKey,paramValue};
							dataFieldsArray=copyArray(dataFieldsArray,dataArray);
						}
						dataRowAdded=true;
					}
				}
			}
			shSetupDataSheet=null;		
			
			//if atleast one rows is added return the parsed array 
			//else return a blank array
			if(dataRowAdded)
			{
				return dataFieldsArray;
			}
			else
			{
				return new Object [0][0];
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
			
	}
	
	/**
	 * Read the reference data from master data excel and retun the whole set as a 2D Array
	 * @param fsMasterDataStream - Master Data File Stream
	 * @param refSheet - Sheet name in Master Data Excel
	 * @param refKey - Row Key in the reference sheet
	 * @param wbTestData - MasterData POI object
	 * @param dataFieldsArray - Current set of datafields array parsed in source sheet
	 * @return the whole reference data set as a 2D Array
	 * @throws IOException
	 */
	private Object [][] readRefData(String refSheet,String refKey,XSSFWorkbook wbTestData,Object [][] dataFieldsArray,String refPrefix) throws IOException
	{
		//If there are multiple references to the same master data from a single source sheet
		//then they are to be differentiated using a calling key
		//for eg:- Address can have sender and reciever
		//Then coulumn name should be Address.Sender & Address.Reciever
		//the calling key will be appended to the data for identification during fetch
		String [] refSheetArray=null;		
		if(refSheet.contains("."))
		{
			//parse the master data sheetname and calling keys 
			refSheetArray=refSheet.split("\\.");
			refSheet=refSheetArray[0];
			refPrefix=refSheetArray[1] + "%callkey%";
		}
		
		//Set the POI sheet for master data
		XSSFSheet shMasterDataSheet = wbTestData.getSheet(refSheet);
		
		//get no of cols and rows for the master data sheet
		int totalNoOfCols = shMasterDataSheet.getRow(0).getLastCellNum();
		int totalNoOfRows = shMasterDataSheet.getPhysicalNumberOfRows();
		
		boolean dataRowAdded=false;
					
		//iterate through each of the rows
		for (int rowCounter = 1; rowCounter < totalNoOfRows; rowCounter++) 
		{
			//if the passed in reference key macthes the value in first column
			if(shMasterDataSheet.getRow(rowCounter).getCell(0).toString().trim().equals(refKey))
			{				
				//read all columns for the reference key matching columns
				for (int colCounter = 0; colCounter <= totalNoOfCols; colCounter++) 
				{
					//make sure that row header is  not empty
					if (null !=shMasterDataSheet.getRow(0).getCell(colCounter)) 
					{
						//if the value is not null
						if (null != shMasterDataSheet.getRow(rowCounter).getCell(colCounter)) 
						{
							//if the values is a reference data then again parse master data for the reference
							if(shMasterDataSheet.getRow(rowCounter).getCell(colCounter).toString().trim().substring(0, 1).equals("."))
							{
								dataFieldsArray=readRefData(shMasterDataSheet.getRow(0).getCell(colCounter).toString().trim(),shMasterDataSheet.getRow(rowCounter).getCell(colCounter).toString().trim().replaceAll("\\.", ""),wbTestData,dataFieldsArray,refPrefix);
							}
							else
							{
								//add the values to the datafields array
								Object [] dataArray=new Object[]{ shMasterDataSheet.getRow(0).getCell(colCounter).toString().trim(),refPrefix + shMasterDataSheet.getRow(rowCounter).getCell(colCounter).toString().trim()};
								dataFieldsArray=copyArray(dataFieldsArray, dataArray);
								dataRowAdded=true;
							}
							
						}
					}
				}
				break;
			}
		}
		shMasterDataSheet=null;
		wbTestData=null;
		
		//if atleast one rows is added return the parsed array 
		//else return a blank array
		if(dataRowAdded)
		{
			return dataFieldsArray;
		}
		else
		{
			return new Object [0][0];
		}	
		
	}
	
	/**
	 * Utility function to copy a 1D array to an existing 2D Array 
	 * @param sourceArray - 2D Array
	 * @param colSize - Size of the 1D array to be copied
	 * @param targetArray - 1D Array to be copied
	 * @return
	 */
	private Object [][] copyArray(Object[][] sourceArray,int colSize,Object[] targetArray )
	{		
		Object [][] localDataArray;
		int currArrSize=0;
		if(sourceArray !=null)
		{
			//create a new local array with size one greater than the current 2D Array
			currArrSize=sourceArray.length;	
			localDataArray=new Object[currArrSize+1][colSize];			
			//copy the current 2d array to the new higher size local array
			java.lang.System.arraycopy(sourceArray,0,localDataArray,0,currArrSize);
		}
		else
		{
			//if source array is null then local array is created with size =1
			localDataArray=new Object[currArrSize+1][colSize];
		}
		//add all indices from the target array to the upper bound of source array 
		for(int colCounter=0;colCounter < colSize; colCounter++)
		{
			localDataArray[currArrSize][colCounter]=targetArray[colCounter];
		}					
		//assign the local array to source and return the source array
		sourceArray=localDataArray;
		return sourceArray;
	}
	
	/**
	 * Utility function to copy a 1D array to an existing 2D Array 
	 * @param sourceArray - 2D Array
	 * @param targetArray - 1D Array to be copied
	 * @return
	 */
	private Object [][] copyArray(Object[][] sourceArray,Object[] targetArray )
	{
		Object [][] localDataArray;
		int currArrSize=0;
		if(sourceArray !=null)
		{
			//create a new local array with size one greater than the current 2D Array
			currArrSize=sourceArray.length;	
			localDataArray=new Object[currArrSize+1][];		
			//copy the current 2d array to the new higher size local array
			java.lang.System.arraycopy(sourceArray,0,localDataArray,0,currArrSize);
		}
		else
		{
			//if source array is null then local array is created with size =1
			localDataArray=new Object[currArrSize+1][];
		}	
		
		//Add the target array to the upper bound of source array
		localDataArray[currArrSize]=targetArray;
		//assign the local array to source and return the source array
		sourceArray=localDataArray;
		return sourceArray;
	}
	
	/**
	 * Merge columns of two 2D Arrays
	 * @param sourceArray - Source 2D Array 
	 * @param targetArray - Target 2D Array
	 * @return
	 */
	private Object [][] mergeArray(Object[][] sourceArray ,Object[][] targetArray )
	{	
		if(targetArray !=null)
		{
			if( targetArray.length >0)
			{
				if(sourceArray !=null)
				{
					if( sourceArray.length >0)
					{
						if(sourceArray[0].length == targetArray[0].length)
						{	
							//merge the columns of the target to source
							int finalArrayLength=sourceArray.length+targetArray.length;
							Object [][] localDataArray=new Object[finalArrayLength][];							
							java.lang.System.arraycopy(sourceArray,0,localDataArray,0,sourceArray.length);
							java.lang.System.arraycopy(targetArray,0,localDataArray,sourceArray.length,targetArray.length);
							sourceArray=localDataArray;							
						}						
					}
					else
					{
						sourceArray=targetArray;
					}
				}
				else
				{
					sourceArray=targetArray;
				}
			}
		}
		return sourceArray;
	}

}
