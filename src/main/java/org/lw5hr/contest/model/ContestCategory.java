package org.lw5hr.contest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Author: Diego Dimunzio - LW5HR   
 */

@Entity
@Table(name = "ContestCategory")
public class ContestCategory implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "categoryName")
  private String categoryName;

  @Column(name = "acronym")
  private String acronym;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Contest> contestList;


  public ContestCategory() {
  }

  public ContestCategory(String categoryName, String acronym) {
    this.categoryName = categoryName;
    this.acronym = acronym;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getAcronym() {
    return acronym;
  }

  public void setAcronym(String acronym) {
    this.acronym = acronym;
  }

  public List<Contest> getContestList() {
    return contestList;
  }

  public void setContestList(List<Contest> contestList) {
    this.contestList = contestList;
  }

  @Override
  public String toString() {
    return categoryName + " (" + acronym + ")";
  }
}
