package org.lw5hr.contest.model;

import org.hibernate.annotations.GenericGenerator;
import org.lw5hr.contest.model.dxlog.QsoData;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import static org.lw5hr.contest.db.DatabaseConstants.DATE_FORMAT;
import static org.lw5hr.contest.db.DatabaseConstants.NO_OPERATOR_SET;

/**
 * Author: Diego Dimunzio - LW5HR   
**/

@Entity
@Table(name = "QSOS")
public class Qso implements Serializable {
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long id;
  @Column(name = "call")
  private String call;
  @Column(name = "date")
  private LocalDate date;
  @Column(name = "time")
  private LocalTime time;
  @Column(name = "band")
  private String band;
  @Column(name = "mode")
  private String mode;
  @Column(name = "CQ")
  private String CQzone;
  @Column(name = "locator")
  private String QTHLocator;
  @Column(name = "state")
  private String state;
  @Column(name = "ITU")
  private String ITUzone;
  @Column(name = "frequeny")
  private String frequency;
  @Column(name = "rRst")
  private String rstReceived;
  @Column(name = "sRst")
  private String rstSent;

  @Column(name = "exchangeTx")
  private String exchangeTx;

  @Column(name = "exchangeRx")
  private String exchangeRx;
  @Column(name = "operator")
  String operator;

  @Column
  private String dxLogId;
  @Transient
  Collection<Qso> workedAlso = new ArrayList<Qso>();

  @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.ALL})
  private Contest contest;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getExchangeTx() {
    return exchangeTx;
  }

  public String getExchangeRx() {
    return exchangeRx;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getQTHLocator() {
    return QTHLocator;
  }

  public void setQTHLocator(String qTHLocator) {
    QTHLocator = qTHLocator;
  }

  public String getITUzone() {
    return ITUzone;
  }

  public void setITUzone(String iTUzone) {
    ITUzone = iTUzone;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public String getRstReceived() {
    return rstReceived;
  }

  public void setRstReceived(String rstReceived) {
    this.rstReceived = rstReceived;
  }

  public String getRstSent() {
    return rstSent;
  }

  public void setRstSent(String rstSent) {
    this.rstSent = rstSent;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public void addWorkedAlso(Qso a) {
    workedAlso.add(a);
  }

  public Collection<Qso> getOworkedAlso() {
    return workedAlso;
  }

  public String getOworkedAlsoAsHTML() {
    StringBuffer buff = new StringBuffer();
    for (Qso qso : workedAlso) {
      buff.append("<b>" + qso.getBand() + "</b> " + qso.getDateAsHTML() + "<br>");
    }
    return buff.toString();
  }

  @Override
  public String toString() {
    return frequency + "," + mode + "," + date + "," + time + "," + call + "," + operator;
  }

  public String getCall() {
    return call;
  }

  public void setCall(String call) {
    this.call = call;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getDateAsHTML() {
    return new SimpleDateFormat("E hh:mm:ss").format(date);
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  public String getBand() {
    return band;
  }

  public void setBand(String band) {
    this.band = band;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getCQzone() {
    return CQzone;
  }

  public void setCQzone(String qzone) {
    CQzone = qzone;
  }

  public String exchangeTx() {
    return exchangeTx;
  }

  public void setExchangeTx(final String exchangeTx) {
    this.exchangeTx = exchangeTx;
  }

  public String exchangeRx() {
    return exchangeRx;
  }

  public void setExchangeRx(final String exchangeRx) {
    this.exchangeRx = exchangeRx;
  }

  public Contest getContest() {
    return contest;
  }

  public void setContest(Contest contest) {
    this.contest = contest;
  }

  public Qso convertQsoDataToQso(final QsoData qsoData) {
    setCall(qsoData.getCallsign());
    setBand(qsoData.getBand());
    setFrequency(qsoData.getFrequency().toString());
    setMode(qsoData.getMode());
    setDxLogId(qsoData.getQsoguid());
    setExchangeTx(qsoData.getNr().toString());
    setExchangeRx(qsoData.getRcvd().split(" ")[0]);
    setOperator(qsoData.getOperator().length() > 0 ? qsoData.getOperator() : NO_OPERATOR_SET);
    /*DxLog date format = 2023-04-26 20:16:01.9999329Z*/
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    LocalDate date = LocalDate.parse(qsoData.getQsotime().substring(0, qsoData.getQsotime().indexOf(".")), formatter);
    LocalTime time = LocalTime.parse(qsoData.getQsotime().substring(0, qsoData.getQsotime().indexOf(".")), formatter);
    setDate(date);
    setTime(time);
    setCQzone(qsoData.getQsoDataCalc().getCqzone());
    return this;
  }

  public String getDxLogId() {
    return dxLogId;
  }

  public void setDxLogId(String dxLogId) {
    this.dxLogId = dxLogId;
  }
}
