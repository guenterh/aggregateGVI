package ch.iwerk.dnb

import java.io.FileInputStream

import org.marc4j.{MarcStreamReader, MarcXmlWriter}

object TestMarc4J extends App {


  val in = new FileInputStream("/ext4/gvi/dnb/DNB-Title-utf8-marc21-1.mrc")

  val marcreader = new MarcStreamReader(in)


  val marcxmlwriter = new MarcXmlWriter(System.out, true)

  var count = 0
  while (marcreader.hasNext) {

    try {

      val record = marcreader.next()
      //marcxmlwriter.write(record)
    } catch {
      case ex: Throwable =>
        println(ex)
        marcreader.next()
    }

    //marcxmlwriter.write(record)

    count += 1

    //println(record)
  }

  println(s"das wars, anzahl saetze $count")

}
