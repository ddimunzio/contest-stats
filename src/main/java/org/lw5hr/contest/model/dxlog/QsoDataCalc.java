package org.lw5hr.contest.model.dxlog;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "qsodatacalc")
public class QsoDataCalc implements Serializable {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private int idqso;
  @Column
  private Integer points;
  @Column
  private String mult1;
  @Column
  private String mult2;
  @Column
  private String mult3;
  @Column
  private String dxcc;
  @Column
  private String cqzone;
  @Column
  private String ituzone;
  @Column
  private String invalidqso;
  @Column
  private String doubleqso;
  @Column
  private Integer multkey;


  public int getIdqso() {
    return idqso;
  }

  public void setIdqso(int idqso) {
    this.idqso = idqso;
  }

  public Integer getPoints() {
    return points;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }

  public String getMult1() {
    return mult1;
  }

  public void setMult1(String mult1) {
    this.mult1 = mult1;
  }

  public String getMult2() {
    return mult2;
  }

  public void setMult2(String mult2) {
    this.mult2 = mult2;
  }

  public String getMult3() {
    return mult3;
  }

  public void setMult3(String mult3) {
    this.mult3 = mult3;
  }

  public String getDxcc() {
    return dxcc;
  }

  public void setDxcc(String dxcc) {
    this.dxcc = dxcc;
  }

  public String getCqzone() {
    return cqzone;
  }

  public void setCqzone(String cqzone) {
    this.cqzone = cqzone;
  }

  public String getItuzone() {
    return ituzone;
  }

  public void setItuzone(String ituzone) {
    this.ituzone = ituzone;
  }

  public String getInvalidqso() {
    return invalidqso;
  }

  public void setInvalidqso(String invalidqso) {
    this.invalidqso = invalidqso;
  }

  public String getDoubleqso() {
    return doubleqso;
  }

  public void setDoubleqso(String doubleqso) {
    this.doubleqso = doubleqso;
  }

  public Integer getMultkey() {
    return multkey;
  }

  public void setMultkey(Integer multkey) {
    this.multkey = multkey;
  }
}
