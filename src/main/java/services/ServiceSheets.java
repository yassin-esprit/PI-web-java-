package services;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import models.Equipement;
public class ServiceSheets {
    private static final String APPLICATION_NAME = "Equipement Manager";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens_Sheet";
    private static final List<String> SCOPES = Arrays.asList(
            SheetsScopes.SPREADSHEETS,
            SheetsScopes.DRIVE
    );

    private static final NetHttpTransport HTTP_TRANSPORT ;
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HTTP transport", e);
        }
    }
   // private static final String CREDENTIALS_FILE_PATH = "/client_secret_642127544643-1b5hlbj6l7tekphle13csevdf232kqd7.apps.googleusercontent.com.json";

    private static String SPREADSHEET_ID = null; // Will be set after creating the sheet
    private static final String RANGE = "Feuille 1"; // Adjust based on your sheet

    private static Credential getCredentials() throws IOException {
        InputStream in = ServiceSheets.class.getResourceAsStream("/client_secret_642127544643-1b5hlbj6l7tekphle13csevdf232kqd7.apps.googleusercontent.com.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: credentials.json");
        }
        System.out.println("INFO: credentials.json file loaded successfully.");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Create tokens directory
        File tokenFolder = new File(TOKENS_DIRECTORY_PATH);
        if (!tokenFolder.exists()) {
            tokenFolder.mkdirs();
        }

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(tokenFolder))
                .setAccessType("offline")
                .setApprovalPrompt("force") // Force to get a refresh token
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        try {
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (Exception e) {
            System.err.println("ERROR: Authorization failed: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to authorize: " + e.getMessage());
        }
    }
    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public String syncWithGoogleSheets() throws Exception {
        Sheets service = getSheetsService();
        ServiceEquipement serviceEquipement = new ServiceEquipement();
        List<Equipement> equipements = serviceEquipement.recuperer(); // Retrieve equipment list

        System.out.println("INFO: Number of equipment items: " + equipements.size());

        String spreadsheetUrl = null;

        try {
            // Create a new spreadsheet if needed
            if (SPREADSHEET_ID == null) {
                Spreadsheet spreadsheet = new Spreadsheet()
                        .setProperties(new SpreadsheetProperties().setTitle("Equipment Inventory"));
                Spreadsheet createdSheet = service.spreadsheets().create(spreadsheet).execute();
                SPREADSHEET_ID = createdSheet.getSpreadsheetId();
                System.out.println("INFO: Created new spreadsheet with ID: " + SPREADSHEET_ID);
            }

            // Clear existing content
            service.spreadsheets().values()
                    .clear(SPREADSHEET_ID, RANGE, new ClearValuesRequest())
                    .execute();
            System.out.println("INFO: Cleared existing content from sheet");

            // Add header row
            List<List<Object>> values = new ArrayList<>();
            List<Object> headerRow = Arrays.asList(
                    "ID", "Name", "Total Quantity", "Available Quantity", "Description", "Status");
            values.add(headerRow);

            // Add equipment data rows
            for (Equipement equipement : equipements) {
                List<Object> dataRow = Arrays.asList(
                        equipement.getId_eq(),
                        equipement.getNom(),
                        equipement.getQuantite_totale(),
                        equipement.getQuantite_disponible(),
                        equipement.getDescription(),
                        equipement.getStatut() != null ? equipement.getStatut().toString() : ""
                );
                values.add(dataRow);
                System.out.println("INFO: Processing equipment ID: " + equipement.getId_eq());
            }

            // Write all data at once for better performance
            ValueRange body = new ValueRange().setValues(values);
            UpdateValuesResponse result = service.spreadsheets().values()
                    .update(SPREADSHEET_ID, RANGE, body)
                    .setValueInputOption("RAW")
                    .execute();

            System.out.println("INFO: Updated " + result.getUpdatedCells() + " cells");

            // Get spreadsheet URL
            spreadsheetUrl = "https://docs.google.com/spreadsheets/d/" + SPREADSHEET_ID;
            System.out.println("INFO: Spreadsheet URL: " + spreadsheetUrl);

        } catch (Exception e) {
            System.err.println("ERROR: Failed to sync data to Google Sheets: " + e.getMessage());
            e.printStackTrace();
        }

        return spreadsheetUrl; // Return the spreadsheet URL
    }

    /*public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Create a new Google Sheet and return its ID
    public static String createNewSheet(String title) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties().setTitle(title));
        Spreadsheet createdSheet = service.spreadsheets().create(spreadsheet).execute();
        return createdSheet.getSpreadsheetId(); // Return the ID of the newly created sheet
    }

    // Synchronize data from the database to the Google Sheet
    public void syncDatabaseToSheet(List<Equipement> equipements) throws IOException, GeneralSecurityException {
        // Step 1: Create a new sheet if SPREADSHEET_ID is not set
        if (SPREADSHEET_ID == null) {
            SPREADSHEET_ID = createNewSheet("Equipement Data");
            System.out.println("New sheet created with ID: " + SPREADSHEET_ID);
        }

        // Step 2: Clear the existing data in the Google Sheet
        clearSheet(SPREADSHEET_ID, RANGE);

        // Step 3: Write the data from the database to the Google Sheet
        for (Equipement equipement : equipements) {
            List<Object> row = Arrays.asList(
                    equipement.getId_eq(),
                    equipement.getNom(),
                    equipement.getQuantite_totale(),
                    equipement.getQuantite_disponible(),
                    equipement.getDescription(),
                    equipement.getStatut()
            );
            writeSheet(SPREADSHEET_ID, RANGE, row); // Append rows
        }
    }

    // Clear the sheet
    public static void clearSheet(String spreadsheetId, String range) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();
        service.spreadsheets().values()
                .clear(spreadsheetId, range, new ClearValuesRequest())
                .execute();
    }

    // Write data to the sheet
    public static void writeSheet(String spreadsheetId, String range, List<Object> rowData) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();
        ValueRange body = new ValueRange().setValues(Collections.singletonList(rowData));
        service.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }*/
}