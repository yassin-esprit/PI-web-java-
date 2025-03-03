package models;


public class Participation {
    private int evenementId;
    private int userId;

    public Participation(int evenementId, int userId) {
        this.evenementId = evenementId;
        this.userId = userId;
    }

    public int getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "evenementId=" + evenementId +
                ", userId=" + userId +
                '}';
    }
}