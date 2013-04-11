package com.renault.tools.synchronization.config.webapp.client.google.docs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.WorksheetQuery;
import com.google.gdata.data.Link;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.renault.tools.synchronization.config.webapp.operation.Operation;
import com.renault.tools.synchronization.config.webapp.operation.OperationCatalog;
import com.renault.tools.synchronization.config.webapp.operation.impl.GoogleAppsOperationFactory;

/**
 * TODO create a separate tool project for Google apps.  
 * 
 * @author dangela
 *
 */
public class GoogleAppsClient {
        
	private GoogleDocumentContext currentDocumentContext;

	private SpreadsheetService service;
		
	private FeedURLFactory factory;

    public void updateSynchroConfigWebappWorksheet(String spreadsheet, String worksheet, OperationCatalog operationCatalog, int jump) throws Exception {
        setCurrentContext(spreadsheet, worksheet);
        
        WorksheetEntry sh = getWorksheet(spreadsheet, worksheet);
        URL url = sh.getListFeedUrl();

        ListFeed listFeed = service.getFeed(url, ListFeed.class);        
        
        int numline = 1;
        for (ListEntry googleRow : listFeed.getEntries()) {

            if (numline <= jump) {
                numline++;
                continue;
            }
            
          String label = googleRow.getCustomElements().getValue("label");
          String configname = googleRow.getCustomElements().getValue("configurationname");
          String filename = googleRow.getCustomElements().getValue("filename");
          String propertyName = googleRow.getCustomElements().getValue("propertyname");
          if (propertyName == null) {
              propertyName = googleRow.getCustomElements().getValue("referencepropertyname");
          }
          if (propertyName == null) {
              propertyName = "NONE";
          }
          
          boolean contains = operationCatalog.containsOperation(label, configname, filename, propertyName);
          if (contains) {
              Operation operation = operationCatalog.removeOperation(label, configname, filename, propertyName);              
              GoogleAppsOperationFactory.buildOperation(operation, this, googleRow).operation();
              GoogleAppsOperationFactory.buildMovementDiary(operation, this).operation();
          }
        }
    }
    
    public ListFeed getListFeed(String spreadsheet, String worksheet) throws Exception {
        setCurrentContext(spreadsheet, worksheet);
        
        WorksheetEntry sh = getWorksheet(spreadsheet, worksheet);
        URL url = sh.getListFeedUrl();

        return service.getFeed(url, ListFeed.class);        
    }
    
    public void importCsvFile(String spreadsheet, String worksheet, String filename, String delimiter) throws Exception {
        setCurrentContext(spreadsheet, worksheet);

        purgeWorksheet(spreadsheet, worksheet);
    
        Pattern delim = Pattern.compile(delimiter);
         
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            int row = 0;
            while (line != null) {   
                // Break up the line by the delimiter and insert the cells
                String[] cells = delim.split(line, -1);  
                for (int col = 0; col < cells.length; col++) {
                    insertCell(spreadsheet, worksheet, row + 1, col + 1, cells[col]);
                }
             
                // Advance the loop
                line = reader.readLine();
                row++;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }        
    }
    
	public GoogleAppsClient() throws Exception {
		factory = FeedURLFactory.getDefault();
		service = new SpreadsheetService("gdata-sample-spreadsheetimport");
	}
	
	/**
	 * Creates a client object for which the provided username and password
	 * produces a valid authentication.
	 *
	 * @param username the Google service user name
	 * @param password the corresponding password for the user name
	 * @throws Exception if error is encountered, such as invalid username and
	 * password pair
	 */
	public GoogleAppsClient(String username, String password) throws Exception {
		this();
		service.setUserCredentials(username, password);
	}
	
	/**
	 * Gets the SpreadsheetEntry for the first spreadsheet with that name
	 * retrieved in the feed.
	 *
	 * @param spreadsheet the name of the spreadsheet
	 * @return the first SpreadsheetEntry in the returned feed, so latest
	 * spreadsheet with the specified name
	 * @throws Exception if error is encountered, such as no spreadsheets with the
	 * name
	 */
	public SpreadsheetEntry getSpreadsheet(String spreadsheet) throws Exception {

	    SpreadsheetQuery spreadsheetQuery = new SpreadsheetQuery(factory.getSpreadsheetsFeedUrl());
	    spreadsheetQuery.setTitleQuery(spreadsheet);
	    SpreadsheetFeed spreadsheetFeed = service.query(spreadsheetQuery, SpreadsheetFeed.class);
	    List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
	    if (spreadsheets.isEmpty()) {
	    	throw new Exception("No spreadsheets with that name");
	    }
	
	    return spreadsheets.get(0);
	}
	
	/**
	 * Get the WorksheetEntry for the worksheet in the spreadsheet with the
	 * specified name.
	 *
	 * @param spreadsheet the name of the spreadsheet
	 * @param worksheet the name of the worksheet in the spreadsheet
	 * @return worksheet with the specified name in the spreadsheet with the
	 * specified name
	 * @throws Exception if error is encountered, such as no spreadsheets with the
	 * name, or no worksheet wiht the name in the spreadsheet
	 */
	public WorksheetEntry getWorksheet(String spreadsheet, String worksheet) throws Exception {
        setCurrentContext(spreadsheet, worksheet);

		SpreadsheetEntry spreadsheetEntry = getSpreadsheet(spreadsheet);
	
		WorksheetQuery worksheetQuery = new WorksheetQuery(spreadsheetEntry.getWorksheetFeedUrl());
	
		worksheetQuery.setTitleQuery(worksheet);
		WorksheetFeed worksheetFeed = service.query(worksheetQuery, WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		if (worksheets.isEmpty()) {
			throw new Exception("No worksheets with that name in spreadhsheet " + spreadsheetEntry.getTitle().getPlainText());
		}
	
		return worksheets.get(0);
	}
	
	/**
	 * Clears all the cell entries in the worksheet.
	 *
	 * @param spreadsheet the name of the spreadsheet
	 * @param worksheet the name of the worksheet
	 * @throws Exception if error is encountered, such as bad permissions
	 */
	public void purgeWorksheet(String spreadsheet, String worksheet) throws Exception {
        setCurrentContext(spreadsheet, worksheet);

		WorksheetEntry worksheetEntry = getWorksheet(spreadsheet, worksheet);
		
		CellFeed cellFeed = service.getFeed(worksheetEntry.getCellFeedUrl(), CellFeed.class);
		
		List<CellEntry> cells = cellFeed.getEntries();
		for (CellEntry cell : cells) {
			Link editLink = cell.getEditLink();
			service.delete(new URL(editLink.getHref()), cell.getEtag());
		}
	}

	/**
	 * Inserts a cell entry in the worksheet.
	 *
	 * @param spreadsheet the name of the spreadsheet
	 * @param worksheet the name of the worksheet
	 * @param row the index of the row
	 * @param column the index of the column
	 * @param input the input string for the cell
	 * @throws Exception if error is encountered, such as bad permissions
	 */
	public void insertCell(String spreadsheet, String worksheet, int row, int column, String input) throws Exception {
        setCurrentContext(spreadsheet, worksheet);
		URL cellFeedUrl = getWorksheet(spreadsheet, worksheet).getCellFeedUrl();
		CellEntry newEntry = new CellEntry(row, column, input);
		service.insert(cellFeedUrl, newEntry);
	}
	
    public void insertRow(String spreadsheet, String worksheet, ListEntry row) throws Exception {
        setCurrentContext(spreadsheet, worksheet);
        URL listFeedUrl = getWorksheet(spreadsheet, worksheet).getListFeedUrl();
        service.insert(listFeedUrl, row);
    }

    private void setCurrentContext(String spreadsheet, String worksheet) {
        currentDocumentContext = new GoogleDocumentContext(spreadsheet, worksheet);
    }
    
    public GoogleDocumentContext getCurrentDocumentContext() {
        return currentDocumentContext;
    }
}
