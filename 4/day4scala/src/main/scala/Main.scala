import scala.io.Source

object Main {

  // Add parsing of hex as Option to String
  implicit class HexString(str: String) {
    def hexToLongOption: Option[Int] = {
      try {
        Some(Integer.parseInt(str, 16))
      } catch {
        case e: NumberFormatException => None
      }
    }
  }

  // Base Trait for all domain types
  trait Id

  // ValidId with attributes as strings
  trait ValidId extends Id {
    val byr: String
    val iyr: String
    val eyr: String
    val hgt: String
    val hcl: String
    val ecl: String
    val pid: String
  }

  // Domain types with attributes as strings
  case class Passport(byr: String, iyr: String, eyr: String, hgt: String, hcl: String, ecl: String, pid: String, cid: String) extends ValidId
  case class NorthPoleCredentials(byr: String, iyr: String, eyr: String, hgt: String, hcl: String, ecl: String, pid: String) extends ValidId
  case object Invalid extends Id

  // Domain type with 'proper' types for attributes
  case class ValidatedId(byr: Int, iyr: Int, eyr: Int, hgt: Int, hcl: Int, ecl: String, pid: Int)

  def main(args: Array[String]): Unit = {

    // Merge all related lines (records) into one string and store in a list
    val fileName = "input.txt"
    var records: List[String] = List()
    var recordAcc = ""
    for (line <- Source.fromFile(fileName).getLines()) {
      val strippedLine = line.strip()
      if (strippedLine == "") {
        val strippedRecordAcc = recordAcc.strip()
        records = strippedRecordAcc :: records
        recordAcc = ""
      } else {
        recordAcc = recordAcc + " " + strippedLine
      }
    }

    // First create maps for each record, then create Passport or NPC (ValidId) or Invalid, collect ValidId
    val validDocs: List[ValidId] = records.map(rec => createDocument(createMap(rec))).collect { case valid: ValidId => valid }
    println(s"Answer: ${validDocs.size}")

    // Parse all attribute strings into Options, yield ValidatedIds where all could be parsed
    val validatedIds = for (
      doc <- validDocs;
      byr <- mkBirthYear(doc.byr);
      iyr <- mkIssueYear(doc.iyr);
      eyr <- mkExpirationYear(doc.eyr);
      hgt <- mkHeight(doc.hgt);
      hcl <- mkHairColor(doc.hcl);
      ecl <- mkEyeColor(doc.ecl);
      pid <- mkPassportId(doc.pid)
    ) yield ValidatedId(byr, iyr, eyr, hgt, hcl, ecl, pid)
    println(s"Answer 2: ${validatedIds.length}")
  }

  def mkBirthYear(s: String): Option[Int] = {
    s.toIntOption.filter(year => year >= 1920 && year <= 2002)
  }

  def mkIssueYear(s: String): Option[Int] = {
    s.toIntOption.filter(year => year >= 2010 && year <= 2020)
  }

  def mkExpirationYear(s: String): Option[Int] = {
    s.toIntOption.filter(year => year >= 2020 && year <= 2030)
  }

  def mkHeight(s: String): Option[Int] = {
    s.takeRight(2) match {
      case "cm" => s.stripSuffix("cm").toIntOption.filter(height => height >= 150 && height <= 193)
      case "in" => s.stripSuffix("in").toIntOption.filter(height => height >= 59 && height <= 76)
      case _ => None
    }
  }

  def mkHairColor(s: String): Option[Int] = {
    s.head match {
      case '#' if s.stripPrefix("#").length == 6 => s.stripPrefix("#").hexToLongOption
      case _ => None
    }
  }

  def mkEyeColor(s: String): Option[String] = {
    s match {
      case "amb" => Some("Amber")
      case "blu" => Some("Blue")
      case "brn" => Some("Brown")
      case "gry" => Some("Gray")
      case "grn" => Some("Green")
      case "hzl" => Some("Hazelnut")
      case "oth" => Some("Other")
      case _ => None
    }
  }

  def mkPassportId(s: String): Option[Int] = {
    s.length match {
      case 9 if s.matches("[0-9]*") => s.toIntOption
      case _ => None
    }
  }

  def createMap(record: String): Map[String, String] = {
    val attributeStrings = record.split(" ")
    attributeStrings.map(s => s.split(":").toList match {
      case List(key, value) => key -> value
    }).toMap
  }

  def createDocument(m: Map[String,String]): Id = {
    (m.get("byr"), m.get("iyr"), m.get("eyr"), m.get("hgt"), m.get("hcl"), m.get("ecl"), m.get("pid"), m.get("cid")) match {
      case (Some(byr), Some(iyr), Some(eyr), Some(hgt), Some(hcl), Some(ecl), Some(pid), Some(cid)) => Passport(byr, iyr, eyr, hgt, hcl, ecl, pid, cid)
      case (Some(byr), Some(iyr), Some(eyr), Some(hgt), Some(hcl), Some(ecl), Some(pid), None) => NorthPoleCredentials(byr, iyr, eyr, hgt, hcl, ecl, pid)
      case _ => Invalid
    }
  }
}
