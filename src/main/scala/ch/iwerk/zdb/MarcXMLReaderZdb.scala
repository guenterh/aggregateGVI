package ch.iwerk.zdb

import java.io.{File, FileInputStream}

import org.marc4j.{MarcXmlReader, MarcXmlWriter}

object MarcXMLReaderZdb extends App {

  val reader = new MarcXmlReader(new FileInputStream(new File("/ext4/gvi/zdb/zdbtitgesamt_924_utf8.mrc-01.xml")))

  var count = 0
  val marcxmlwriter = new MarcXmlWriter(System.out, true)
  while (reader.hasNext) {
    val record = reader.next()
    //marcxmlwriter.write(record)
    count += 1
  }
  println(s"das wars von der ZDB $count")


}
