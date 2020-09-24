package ch.iwerk

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.{BufferedOutputStream, ByteArrayInputStream, ByteArrayOutputStream, File, FileInputStream}

import helper.{TemplateCreator, TemplateTransformer}
import org.apache.flink.api.common.io.{DefaultInputSplitAssigner, RichInputFormat}
import org.apache.flink.api.common.io.statistics.BaseStatistics
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.io.{InputSplit, InputSplitAssigner}
import org.marc4j.{MarcStreamReader, MarcXmlWriter}
import org.marc4j.marc.Record
//import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.scala._
import org.marc4j.MarcXmlReader
import scala.xml._

/**
 * Skeleton for a Flink Job.
 *
 * For a full example of a Flink Job, see the WordCountJob.scala file in the
 * same package/directory or have a look at the website.
 *
 * You can also generate a .jar file that you can submit on your Flink
 * cluster. Just type
 * {{{
 *   sbt clean assembly
 * }}}
 * in the projects root directory. You will find the jar in
 * target/scala-2.11/Flink\ Project-assembly-0.1-SNAPSHOT.jar
 *
 */
object Job {


  def main(args: Array[String]): Unit = {
    // set up the execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //env.createInput()

    //val textfile = env.readTextFile("")


    class FileSplit extends InputSplit {

      private val TRANSFORMERIMPL = "net.sf.saxon.TransformerFactoryImpl"
      private val removens = "removeallns.xslt"

      private lazy val removeallns = {
        new TemplateCreator(TRANSFORMERIMPL, removens).createTransformerFromResource
      }


      override def getSplitNumber: Int = 1

      var reader:MarcXmlReader = _
      private var hn = false



      def hasNext = {
        hn = reader.hasNext
        hn
      }

      def openFile = {

        reader = new MarcXmlReader(new FileInputStream(new File("/ext4/gvi/hebis/Export_Hebis_2019-04-25T_Part_0062.xml")))

      }

      def nextRecord:Option[String] = {
        if (hn) {
          val record = reader.next()

          val ba = new ByteArrayOutputStream()
          val bos = new BufferedOutputStream(ba)

          val xmlw = new MarcXmlWriter(bos, true)

          xmlw.write(record)
          xmlw.close()
          bos.flush()
          val xml = ba.toString()
          val purexml = new TemplateTransformer(xml).transform(removeallns)

          //ba.flush()



          val elems = XML.loadString(purexml)

          /*
          val singler = (elems \\ "record")

          val elem: Elem = XML.loadString(singler.toString())

           */
          val p = new scala.xml.PrettyPrinter(0, 0,true)
          val sb = new StringBuilder
          p.format(elems, sb)

          Option(sb.toString())
        } else
          Option.empty

      }


    }
    //https://github.com/mushketyk/flink-examples/blob/master/src/main/java/com/example/flink/StanfordTweetsDataSetInputFormat.java

    //implicit val typeInfo: TypeInformation[String] = TypeInformation.of(classOf[(String,FileSplit)])

    class MarcData  extends RichInputFormat[String,FileSplit] {

      val myFileSplit = new FileSplit

      override def configure(parameters: Configuration): Unit = {

      }

      override def getStatistics(cachedStatistics: BaseStatistics): BaseStatistics = new BaseStatistics {
        override def getTotalInputSize: Long = BaseStatistics.SIZE_UNKNOWN

        override def getNumberOfRecords: Long = BaseStatistics.NUM_RECORDS_UNKNOWN

        override def getAverageRecordWidth: Float = BaseStatistics.AVG_RECORD_BYTES_UNKNOWN
      }

      override def createInputSplits(minNumSplits: Int): Array[FileSplit] = Array(myFileSplit )

      override def getInputSplitAssigner(inputSplits: Array[FileSplit]): InputSplitAssigner = {
        new DefaultInputSplitAssigner(inputSplits.asInstanceOf[Array[InputSplit]])
      }

      override def open(split: FileSplit): Unit = myFileSplit.openFile

      override def reachedEnd(): Boolean = !myFileSplit.hasNext

      override def nextRecord(reuse: String): String =
        myFileSplit.nextRecord.get

      override def close(): Unit = {}
    }


    val source: DataSet[String] =  env.createInput(new MarcData())
      .map((record: String) => {
        //val ba = new ByteArrayOutputStream()
        //val xmlw = new MarcXmlWriter(ba, true)

        record
    }
    )
      println(source.count())

    //println(s"number of records processed $count")


    //env.execute("jetzt geht es los")



    /**
     * Here, you can start creating your execution plan for Flink.
     *
     * Start with getting some data from the environment, like
     * env.readTextFile(textPath);
     *
     * then, transform the resulting DataSet[String] using operations
     * like:
     *   .filter()
     *   .flatMap()
     *   .join()
     *   .group()
     *
     * and many more.
     * Have a look at the programming guide:
     *
     * http://flink.apache.org/docs/latest/programming_guide.html
     *
     * and the examples
     *
     * http://flink.apache.org/docs/latest/examples.html
     *
     */


    // execute program
    //env.execute("Flink Scala API Skeleton")
  }
}
