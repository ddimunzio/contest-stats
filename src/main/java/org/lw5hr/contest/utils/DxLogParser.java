package org.lw5hr.contest.utils;

import org.lw5hr.contest.model.Qso;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class DxLogParser {
   private final String APP = "app";
   private final String CONTEST_NAME = "contestname";
   private final String TIMESTAMP = "timestamp";
   private final String MY_CALL = "mycall";
   private final String BAND = "band";
   private final String RX_FREQ = "rxfreq";
   private final String TX_FREQ = "txfreq";
   private final String OPERATOR = "operator";
   private final String MODE = "mode";
   private final String CALL = "call";
   private final String COUNTRY_PREFIX = "countryprefix";
   private final String WPX_PREFIX = "wpxprefix";
   private final String STATION_PREFIX = "stationprefix";
   private final String CONTINENT = "continent";
   private final String SNT = "snt";
   private final String SNT_NR = "sntnr";
   private final String RCV = "rcv";
   private final String MISC_TEXT = "misctext";
   private final String IS_MULTIPLIER1 = "ismultiplier1";
   private final String IS_MULTIPLIER2 = "ismultiplier2";
   private final String IS_MULTIPLIER3 = "ismultiplier3";
   private final String POINTS = "points";
   private final String RADIO_NR = "radionr";
   private final String RUN1RUN2 = "run1run2";
   private final String IS_ORIGINAL = "IsOriginal";
   private final String NET_BIOS_NAME = "NetBiosName";
   private final String IS_RUN_QSO = "IsRunQSO";
   private final String STATION_NAME = "StationName";
   private final String ID = "ID";
   private final String IS_CLAIMED_QSO = "IsClaimedQso";
    public void parse (final String xml) throws ParserConfigurationException, IOException, SAXException {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      InputSource inputSource = new InputSource(new StringReader(xml));
      Document document = builder.parse(inputSource);

      Element contact = document.getDocumentElement();
      Qso qso = new Qso();
      String app = contact.getElementsByTagName(APP).item(0).getTextContent();
      String contestName = contact.getElementsByTagName(CONTEST_NAME).item(0).getTextContent();
      String timestamp = contact.getElementsByTagName(TIMESTAMP).item(0).getTextContent();
      String myCall = contact.getElementsByTagName(MY_CALL).item(0).getTextContent();
    }
}
