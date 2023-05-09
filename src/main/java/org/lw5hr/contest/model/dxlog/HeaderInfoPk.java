package org.lw5hr.contest.model.dxlog;

import java.io.Serializable;
import java.util.Objects;

public class HeaderInfoPk implements Serializable {
  private String hdrname;
  private String hdrvalue;

  public HeaderInfoPk() {}


  public HeaderInfoPk(String hdrname, String hdrvalue) {
    this.hdrname = hdrname;
    this.hdrvalue = hdrvalue;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HeaderInfoPk)) return false;
    HeaderInfoPk that = (HeaderInfoPk) o;
    return Objects.equals(hdrname, that.hdrname) && Objects.equals(hdrvalue, that.hdrvalue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hdrname, hdrvalue);
  }

}
