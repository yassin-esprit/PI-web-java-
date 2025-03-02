package services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import models.Reservation_Equipement ;

import models.Equipement  ;


public class Calendar {
    private static final String APPLICATION_NAME = "Carpool Scheduler";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens_Calendar";

    private static final List<String> SCOPES = Arrays.asList(
            CalendarScopes.CALENDAR_EVENTS,
            CalendarScopes.CALENDAR
    );
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_642127544643-mkqscjklodutqsunjfjrphpbou4i3mcq.apps.googleusercontent.com.json";

    public static Credential getCredentials() throws IOException, GeneralSecurityException {
        InputStream in = Calendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            System.err.println("ERROR: credentials.json file not found at path: " + CREDENTIALS_FILE_PATH);
            throw new FileNotFoundException("credentials.json file not found!");
        }

        System.out.println("INFO: credentials.json file loaded successfully.");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static com.google.api.services.calendar.Calendar getCalendarService() throws IOException, GeneralSecurityException {
        return new com.google.api.services.calendar.Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public String syncWithGoogleCalendar() throws Exception {
        com.google.api.services.calendar.Calendar service = getCalendarService();
        ServiceReservation serviceReservation = new ServiceReservation();
        List<Reservation_Equipement> reservations = serviceReservation.recuperer(); // Retrieve reservations

        System.out.println("INFO: Number of reservations: " + reservations.size());

        String lastEventUrl = null; // Variable to store the last event URL

        for (Reservation_Equipement reservation : reservations) {
            try {
                System.out.println("INFO: Processing reservation ID: " + reservation.getId_reservation());
                System.out.println("INFO: Reservation Date: " + reservation.getDateReservation());

                // Convert the reservation date to ISO format
                String isoFormat = convertToIsoFormat(reservation.getDateReservation());
                if (isoFormat == null) {
                    System.err.println("ERROR: Invalid date format for reservation ID: " + reservation.getId_reservation());
                    continue; // Skip this reservation
                }

                // Create DateTime objects for the event
                DateTime startDateTime = new DateTime(isoFormat);
                DateTime endDateTime = new DateTime(startDateTime.getValue() + 60 * 60 * 1000); // 1 hour later

                System.out.println("INFO: Start DateTime: " + startDateTime);
                System.out.println("INFO: End DateTime: " + endDateTime);

                // Create a Google Calendar event
                Event googleEvent = new Event()
                        .setSummary("Reservation: " + reservation.getId_reservation()) // Use reservation ID or name
                        .setDescription("Status: " + reservation.getStatus_reservation()) // Include status
                        .setStart(new EventDateTime()
                                .setDateTime(startDateTime)
                                .setTimeZone("Africa/Tunis"))
                        .setEnd(new EventDateTime()
                                .setDateTime(endDateTime)
                                .setTimeZone("Africa/Tunis"));

                // Insert the reservation into the user's Google Calendar
                Event createdEvent = service.events()
                        .insert("primary", googleEvent)
                        .execute();
                System.out.println("INFO: Reservation created: " + createdEvent.getHtmlLink());

                // Store the event URL
                lastEventUrl = createdEvent.getHtmlLink();

            } catch (Exception e) {
                System.err.println("ERROR: Failed to create event for ID " + reservation.getId_reservation() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        return lastEventUrl; // Return the last event URL
    }

    private String convertToIsoFormat(String date) {
        if (date == null) {
            return null;
        }

        try {
            // Define the expected input format (adjust this to match your input format)
            DateTimeFormatter inputFormatter;

            // Check if the input string contains time information
            if (date.length() == 10) { // Format: yyyy-MM-dd
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(date, inputFormatter);
                // Convert to ZonedDateTime at the start of the day
                ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.of("Africa/Tunis"));
                // Format to ISO 8601 format
                return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            } else if (date.length() == 19) { // Format: yyyy-MM-dd HH:mm:ss
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);
                // Convert to ZonedDateTime with the specified time zone
                ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("Africa/Tunis"));
                // Format to ISO 8601 format
                return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            } else {
                System.err.println("ERROR: Unsupported date format: " + date);
                return null;
            }

        } catch (DateTimeParseException e) {
            System.err.println("ERROR: Failed to parse date: " + date);
            e.printStackTrace();
            return null;
        }
    }
}