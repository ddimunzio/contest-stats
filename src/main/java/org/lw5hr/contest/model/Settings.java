package org.lw5hr.contest.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SETTINGS")
public class Settings implements Serializable {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private Long id;

  @Column(name = "settingName")
  private String settingName;

  @Column(name = "settingValue")
  private String settingValue;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSettingName() {
    return settingName;
  }

  public void setSettingName(String settingName) {
    this.settingName = settingName;
  }

  public String getSettingValue() {
    return settingValue;
  }

  public void setSettingValue(String settingValue) {
    this.settingValue = settingValue;
  }
}
