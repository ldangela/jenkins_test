package com.renault.tools.synchronization.config.webapp.client.google.docs;

public class GoogleDocumentContext {
    
    private String spreadsheet;

    private String worksheet;

    public GoogleDocumentContext(String spreadsheet, String worksheet) {
        this.spreadsheet = spreadsheet;
        this.worksheet = worksheet;
    }
    
    public String getSpreadsheet() {
        return spreadsheet;
    }
    
    public String getWorksheet() {
        return worksheet;
    }
    
    public String getDiarySpreadsheet() {
        return "Diary";
    }
    
    public String getDiaryWorksheet() {
        return "Movement";
    }
}
