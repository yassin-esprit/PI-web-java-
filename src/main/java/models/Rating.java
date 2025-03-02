package models;

import java.time.LocalDateTime;

public class Rating {
    private int id; // Unique identifier for the rating
    private int eventId; // ID of the event being rated
    private int userId; // ID of the user who submitted the rating
    private int rating; // Rating value (1 to 5 stars)
    private String review; // Optional review text
    private LocalDateTime createdAt; // Timestamp when the rating was created

    // Constructor
    public Rating(int id, int eventId, int userId, int rating, String review, LocalDateTime createdAt) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.rating = rating;
        this.review = review;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}