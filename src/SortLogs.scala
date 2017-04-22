import java.io.{BufferedWriter, File, FileWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object SortLogs {

  val destinationOutput = "/Users/<userName>/Desktop/merged-and-sorted-logfile"

  /**
    * @param args
    * For each arg passed in, read contents of file to an array, and append array to mergedLogs.
    */

  def main(args: Array[String]): Unit = {

    val mergedLogs = new ArrayBuffer[String]

    args.foreach(arg => mergedLogs ++= readFileToArray(arg))

    /**
      * Custom comparator recursively called by sortWith()
      */

    val sortedLines = mergedLogs.sortWith(sortByDate)
    outputToFile(destinationOutput, sortedLines)
  }

  /**
    *
    * @param s1
    * @param s2
    * @return
    * Find the timestamp in each line using regex. Parse timestamps to Date Objects. Compare.
    */

  def sortByDate(s1: String, s2: String) = {

    //regular expression for timestamp (e.g. 2016-12-15 17:08:50.129)
    val timeStampRegex = """\b\d{4}-\d{2}-\d{2} \d{1,2}:\d{2}:\d{2}\b""".r
    try {
      val x = timeStampRegex.findFirstIn(s1)
      val y = timeStampRegex.findFirstIn(s2)
      if(stringDateToDateObj(x).compareTo(stringDateToDateObj(y)) > 0)
        s1 < s2
      else
        s1 > s2
    }catch{
      case e: Exception =>
        s1 < s2
    }
  }

  /**
    *
    * @param StringDate Parse string timestamp to formatted date object.
    * @return           Date object.
    */
  def stringDateToDateObj(StringDate: Option[String]): LocalDateTime={
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    val dateObject:LocalDateTime = LocalDateTime.parse(StringDate.toString, formatter);
    dateObject
  }

  /**
    *
    * @param filename Read up lines of file and append to array.
    * @return         Array.
    */
  def readFileToArray(filename: String): ArrayBuffer[String]={
    val arrayBuffer = new ArrayBuffer[String]()
    for (line <- Source.fromFile(filename).getLines())
      arrayBuffer.append(line)

    arrayBuffer
  }

  /**
    *
    * @param destinationPath
    * @param sortedArray
    * Print each element of sorted collection out to file.
    */

  def outputToFile(destinationPath: String, sortedArray: ArrayBuffer[String])={
    val fw: FileWriter = new FileWriter(destinationPath)
    val bw: BufferedWriter = new BufferedWriter(fw)

    for (element <- sortedArray)
      bw.write("\n" + element)

    bw.flush()
    bw.close()
  }
}
