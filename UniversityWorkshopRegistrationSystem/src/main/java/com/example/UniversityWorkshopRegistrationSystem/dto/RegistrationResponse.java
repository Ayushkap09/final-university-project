package com.example.UniversityWorkshopRegistrationSystem.dto;

import java.time.LocalDateTime;

public class RegistrationResponse {
    private Long id;
    private Long workshopId;
    private String workshopTitle;
    private LocalDateTime workshopStartDatetime;
    private String workshopLocation;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkshopId() { return workshopId; }
    public void setWorkshopId(Long workshopId) { this.workshopId = workshopId; }
    public String getWorkshopTitle() { return workshopTitle; }
    public void setWorkshopTitle(String workshopTitle) { this.workshopTitle = workshopTitle; }
    public LocalDateTime getWorkshopStartDatetime() { return workshopStartDatetime; }
    public void setWorkshopStartDatetime(LocalDateTime workshopStartDatetime) { this.workshopStartDatetime = workshopStartDatetime; }
    public String getWorkshopLocation() { return workshopLocation; }
    public void setWorkshopLocation(String workshopLocation) { this.workshopLocation = workshopLocation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
}
