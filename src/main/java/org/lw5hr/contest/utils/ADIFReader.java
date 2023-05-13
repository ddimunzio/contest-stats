package org.lw5hr.contest.utils;

import org.hibernate.Session;
import org.lw5hr.contest.db.HibernateUtil;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.model.Qso;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ADIFReader {

    private ArrayList<Qso> list = new ArrayList<Qso>();
    private File file;

    public ADIFReader(File file) {
        this.file = file;
    }

    public ADIFReader(String filePath) {
        this.file = new File(filePath);
    }

    public List<Qso> read(final Contest contest) throws Exception {
        ADIFParser parser = new ADIFParser();
        BufferedReader r = new BufferedReader(new FileReader(file));
        String line;
        boolean read = false;
        boolean gotRecord = false;
        StringBuffer record = new StringBuffer();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(contest);

        while ((line = r.readLine()) != null) {
            line = line.trim();
            if (line.toUpperCase().contains("<EOH>")) {
                read = true;
            } else {
                if (read) {
                    record.append(line);
                    if (line.toUpperCase().contains("<EOR>")) {
                        gotRecord = true;
                    }
                }

            }
            if (gotRecord) {
                try {
                    Qso qso = parser.parseLine(record.toString().replaceAll("\n", "\\n").toUpperCase(), contest);
                    if (qso != null) {
                        // qsoPerBand.put(key, value)
                        qso.setContest(contest);
                        list.add(qso);
                        session.save(qso);
                        setOtherBandsIfExist(qso);
                    }
                } catch (Exception e) {
                    System.out.print(record);
                    e.printStackTrace();
                } finally {
                    record = new StringBuffer();
                    gotRecord = false;
                }
            }
        }
        session.getTransaction().commit();
        return list;
    }

    private void setOtherBandsIfExist(Qso q) {

        for (Qso qso : list) {
            if (qso.getBand() != null) {
                if ((qso.getCall().equalsIgnoreCase(q.getCall())) && (!qso.getBand().equalsIgnoreCase(q.getBand()))) {
                    qso.addWorkedAlso(q);
                    q.addWorkedAlso(qso);
                }
            }
        }

    }
}
