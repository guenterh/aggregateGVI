package ch.iwerk.hbz;

import org.marc4j.MarcStreamReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;

import java.io.*;
import java.nio.Buffer;

public class ReadAllLines {

    public static void main(String[] args) {
        BufferedReader reader;
        File file = new File("/ext4/gvi/hbz/20190125.gvi.f.xml");




        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            reader = new BufferedReader(new FileReader("/ext4/gvi/hbz/20190125.gvi.f.mrc"));
            String line = reader.readLine();
            //File fileout = new File("/ext4/gvi/hbz/20190125.gvi.f.xml");


            //BufferedWriter bw = new BufferedWriter(new FileWriter(file))

            //StringWriter wr = new StringWriter();

            //MarcXmlWriter marcxmlwriter = new MarcXmlWriter(new FileOutputStream(file) ,true);
            //MarcXmlWriter marcxmlwriter = new MarcXmlWriter(System.out ,true);
            ByteArrayOutputStream ba  = new ByteArrayOutputStream();

            MarcXmlWriter marcxmlwriter = new MarcXmlWriter(ba ,true);
            //MarcXmlWriter marcxmlwriter = new MarcXmlWriter(new DataOutputStream(wr) ,false);
            while (line != null) {

                try {


                    MarcStreamReader mr =  new MarcStreamReader(new ByteArrayInputStream(line.getBytes()),"UTF-8");
                    Record r =  mr.next();
                    //System.out.println(r);

                    //marcxmlwriter.setConverter();
                    marcxmlwriter.write(r );
                    ba.flush();
                    String s = ba.toString();

                    System.out.println(ba.toString());
                    ba.reset();
                    line = reader.readLine();


                    //"n1 bal"

                } catch (Throwable th) {
                    //System.out.println(line);

                    System.out.println(th);
                }

            }
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

}
