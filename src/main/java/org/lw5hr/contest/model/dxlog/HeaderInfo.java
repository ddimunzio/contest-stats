package org.lw5hr.contest.model.dxlog;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "headerinfo")
@IdClass(HeaderInfoPk.class)
public class HeaderInfo implements Serializable {
  @EmbeddedId
  private HeaderInfoPk id;

  @Column(name = "hdrname", columnDefinition = "varchar(30)", updatable = false, insertable = false)
  private String hdrname;

  @Column(name = "hdrvalue", columnDefinition = "varchar(150)", updatable = false, insertable = false)
  private String hdrvalue;

  public HeaderInfoPk getId() {
    return id;
  }

  public void setId(HeaderInfoPk id) {
    this.id = id;
  }

  public String getHdrname() {
    return hdrname;
  }

  public void setHdrname(String hdrname) {
    this.hdrname = hdrname;
  }

  public String getHdrvalue() {
    return hdrvalue;
  }

  public void setHdrvalue(String hdrvalue) {
    this.hdrvalue = hdrvalue;
  }
}

