package org.lw5hr.contest.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "CONTEST_PROPERTIES")
public class ContestProperties implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "event_name")
  private String eventName;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "start_time")
  private LocalTime startTime;
  @Column(name = "end_date")
  private LocalDate endDate;
  @Column(name = "end_time")
  private LocalTime endTime;

  @Column(name = "duration_hours")
  private int durationHours;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Contest> contestList;
  public ContestProperties() {
  }

  public ContestProperties(String eventName, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int durationHours, double multiplier) {
    this.eventName = eventName;
    this.startDate = startDate;
    this.startTime = startTime;
    this.endDate = endDate;
    this.endTime = endTime;
    this.durationHours = durationHours;
      }

  // Getters and Setters

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public int getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(int durationHours) {
    this.durationHours = durationHours;
  }

  public List<Contest> getContestList() {
    return contestList;
  }

  public void setContestList(List<Contest> contestList) {
    this.contestList = contestList;
  }

  @Override
  public String toString() {
    return eventName;
  }
}
