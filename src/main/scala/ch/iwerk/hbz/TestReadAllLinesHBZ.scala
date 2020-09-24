package ch.iwerk.hbz

import java.io.{BufferedWriter, File, FileWriter}

import scala.io.Source

object TestReadAllLinesHBZ extends App {

  val source = Source.fromFile("/ext4/gvi/hbz/20190125.gvi.f.mrc")

  val file = new File("/ext4/gvi/hbz/20190125.gvi.f.n.eol.mrc")


  val bw = new BufferedWriter(new FileWriter(file))


  source.getLines().foreach(line =>
    //println(_)

    try {
      bw.write(line)
    } catch {
      case ex: Exception => println(ex)
      case all => println(all)
    }
  )
  bw.close()
}
