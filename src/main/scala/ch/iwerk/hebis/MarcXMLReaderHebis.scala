package ch.iwerk.hebis

import java.io.{File, FileInputStream}

import org.marc4j.MarcXmlReader

object MarcXMLReaderHebis extends App {

  val reader = new MarcXmlReader(new FileInputStream(new File("/ext4/gvi/hebis/Export_Hebis_2019-04-25T_Part_0062.xml")))

  var count = 0
  while (reader.hasNext) {
    val record = reader.next()
    //System.out.println(record)
    count += 1
  }
  println(s"das wars $count")


}
