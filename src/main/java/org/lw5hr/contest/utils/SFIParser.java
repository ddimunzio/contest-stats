package org.lw5hr.contest.utils;

import java.net.*;
import java.io.*;

public class SFIParser {
  public void getSfi() throws IOException {
    URL url = new URL("https://services.swpc.noaa.gov/json/solar-cycle/observed-solar-cycle-indices.json");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    InputStream in = new BufferedInputStream(con.getInputStream());
    String response = new String(in.readAllBytes());
    in.close();
  }
}
