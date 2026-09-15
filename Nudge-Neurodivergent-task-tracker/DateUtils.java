import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for date and time operations
 */
public class DateUtils {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
    
    /**
     * Parses a date string in format "yyyy-MM-dd HH:mm"
     */
    public static LocalDateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        return LocalDateTime.parse(dateTimeString, INPUT_FORMATTER);
    }
    
    /**
     * Formats a LocalDateTime for display
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        return dateTime.format(DISPLAY_FORMATTER);
    }
    
    /**
     * Checks if a deadline has passed
     */
    public static boolean isOverdue(LocalDateTime deadline) {
        return deadline.isBefore(LocalDateTime.now());
    }
    
    /**
     * Checks if a deadline is within the next hour
     */
    public static boolean isDueSoon(LocalDateTime deadline) {
        LocalDateTime oneHourFromNow = LocalDateTime.now().plusHours(1);
        return deadline.isBefore(oneHourFromNow) && deadline.isAfter(LocalDateTime.now());
    }
}
