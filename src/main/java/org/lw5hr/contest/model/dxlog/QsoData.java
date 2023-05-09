package org.lw5hr.contest.model.dxlog;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "qsodata")
public class QsoData implements Serializable {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private int idqso;

  @Column
  private String band;
  @Column
  private int period;
  @Column
  private Double frequency;
  @Column
  private String mode;
  @Column
  private String qsotime;
  @Column
  private String callsign;
  @Column
  private String sent;
  @Column
  private Integer nr;
  @Column
  private String rcvd;
  @Column
  private String recinfo;
  @Column
  private String recinfo2;
  @Column
  private String recinfo3;
  @Column
  private String stn;
  @Column
  private String originstnid;
  @Column
  private int originidqso;
  @Column
  private String xqso;
  @Column
  private String qsotimestamp;
  @Column
  private String qsoguid;
  @Column
  private String operator;
  @Column
  private int recfileindex;
  @Column
  private int recfileposition;

  public int getIdqso() {
    return idqso;
  }

  public void setIdqso(int idqso) {
    this.idqso = idqso;
  }

  public String getBand() {
    return band;
  }

  public void setBand(String band) {
    this.band = band;
  }

  public int getPeriod() {
    return period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public Double getFrequency() {
    return frequency;
  }

  public void setFrequency(Double frequency) {
    this.frequency = frequency;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getQsotime() {
    return qsotime;
  }

  public void setQsotime(String qsotime) {
    this.qsotime = qsotime;
  }

  public String getCallsign() {
    return callsign;
  }

  public void setCallsign(String callsign) {
    this.callsign = callsign;
  }

  public String getSent() {
    return sent;
  }

  public void setSent(String sent) {
    this.sent = sent;
  }

  public Integer getNr() {
    return nr;
  }

  public void setNr(Integer nr) {
    this.nr = nr;
  }

  public String getRcvd() {
    return rcvd;
  }

  public void setRcvd(String rcvd) {
    this.rcvd = rcvd;
  }

  public String getRecinfo() {
    return recinfo;
  }

  public void setRecinfo(String recinfo) {
    this.recinfo = recinfo;
  }

  public String getRecinfo2() {
    return recinfo2;
  }

  public void setRecinfo2(String recinfo2) {
    this.recinfo2 = recinfo2;
  }

  public String getRecinfo3() {
    return recinfo3;
  }

  public void setRecinfo3(String recinfo3) {
    this.recinfo3 = recinfo3;
  }

  public String getStn() {
    return stn;
  }

  public void setStn(String stn) {
    this.stn = stn;
  }

  public String getOriginstnid() {
    return originstnid;
  }

  public void setOriginstnid(String originstnid) {
    this.originstnid = originstnid;
  }

  public int getOriginidqso() {
    return originidqso;
  }

  public void setOriginidqso(int originidqso) {
    this.originidqso = originidqso;
  }

  public String getXqso() {
    return xqso;
  }

  public void setXqso(String xqso) {
    this.xqso = xqso;
  }

  public String getQsotimestamp() {
    return qsotimestamp;
  }

  public void setQsotimestamp(String qsotimestamp) {
    this.qsotimestamp = qsotimestamp;
  }

  public String getQsoguid() {
    return qsoguid;
  }

  public void setQsoguid(String qsoguid) {
    this.qsoguid = qsoguid;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public int getRecfileindex() {
    return recfileindex;
  }

  public void setRecfileindex(int recfileindex) {
    this.recfileindex = recfileindex;
  }

  public int getRecfileposition() {
    return recfileposition;
  }

  public void setRecfileposition(int recfileposition) {
    this.recfileposition = recfileposition;
  }
}
