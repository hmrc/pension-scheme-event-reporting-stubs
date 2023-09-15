/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils

import play.api.libs.json.{JsObject, Json}
import scala.util.Random
import faker.Name

// Run here to generate fake payload file.
object StubDataGenerator extends App {
  def writeFileToConfResources(eventType: String, content:String, fileName:String):Unit = {
    import java.io._
    // Original:
    // val pw = new PrintWriter(new File( s"conf/resources/data/api1832/${eventType}PaginationTestPayloads/$fileName" ))
    val pw = new PrintWriter(new File( s"conf/resources/data/api1832/$fileName" ))
    pw.write(content)
    pw.close()
  }


  def generateEvent6SummaryCsv(numberOfMembers: Int, year: Int): String = {

    val protectionTypesCsv = Seq("Enhanced lifetime allowance", "Enhanced protection", "Fixed protection",
      "Fixed protection 2014", "Fixed protection 2016", "Individual protection 2014", "Individual protection 2016"
    )

    val headers = "First name," +
      "Last name," +
      "National Insurance number," +
      "Type of protection held for the crystallisation (see LTA certificate)," +
      "Protection type reference," +
      "Total amount crystallised (£)," +
      "Date the benefit was crystallised (XX/XX/XXXX)\n"
    val arrayOfMembers = for {
      num <- (1 to numberOfMembers).toList
    } yield {
      s"${Name.first_name}," +
        s"${Name.last_name}," +
        s"AB${100000 + num}C," +
        s"${new Random().shuffle(protectionTypesCsv).head}," +
        s"${new Random().between(10000000, 99999999)}," +
        s"${BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP)}," +
        s"${year}-11-0${new Random().between(1, 9)}\n"
    }
    val membersListAsString = arrayOfMembers.mkString
    headers + membersListAsString
  }

  def generateEvent23SummaryCsv(numberOfMembers: Int, year: Int): String = {
    val headers = "First name," +
      "Last name," +
      "National Insurance number," +
      "In which tax year was the money purchase pension savings statement issued? (XXXX to XXXX)," +
      "What is the total of the member’s pension input amounts for money purchase arrangements under the scheme for the tax year that the purchase pension savings statement was issued? (£)\n"
    val arrayOfMembers = for {
      num <- (1 to numberOfMembers).toList
    } yield {
      s"${Name.first_name}," +
        s"${Name.last_name}," +
        s"AB${100000 + num}C," +
        s"$year to ${year + 1}," +
        s"${BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP)}\n"
    }
    val membersListAsString = arrayOfMembers.mkString
    headers + membersListAsString
  }

  def generateEvent22SummaryCsv(numberOfMembers: Int, year: Int): String = {
    val headers = "First name," +
      "Last name," +
      "National Insurance number," +
      "In which tax year was the annual allowance exceeded? (XXXX to XXXX)," +
      "What is the total of the member's pension input amounts for all arrangements under the scheme in the tax year that the annual allowance was exceeded? (£)\n"
    val arrayOfMembers = for {
      num <- (1 to numberOfMembers).toList
    } yield {
      s"${Name.first_name}," +
        s"${Name.last_name}," +
        s"AB${100000 + num}C," +
        s"$year to ${year + 1}," +
        s"${BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP)}\n"
    }
    val membersListAsString = arrayOfMembers.mkString
    headers + membersListAsString
  }
//
//
//  def generateEvent2SummaryJson(eventType: String, numberOfMembers: Int): JsObject = {
//    val arrayOfMembers = for {
//      num <- (1 to numberOfMembers).toList
//    } yield {
//      Json.parse(
//        s"""
//          |{
//          |  "deceasedMembersDetails": {
//          |    "firstName": "Hello",
//          |    "lastName": "World",
//          |    "nino": "AB${100000 + num}C"
//          |  },
//          |  "beneficiaryDetails": {
//          |    "firstName": "Bonjour",
//          |    "lastName": "Monde",
//          |    "nino": "AB${200000 + num}C"
//          |  },
//          |  "amountPaid": 10.01,
//          |  "datePaid": "2022-12-12"
//          |}
//          |""".stripMargin)
//    }
//    Json.obj(
//      "_id" -> "Change this entire line to match the original Mongo document",
//      "apiTypes" -> "1830",
//      "pstr" -> "Change this entire line to match the original Mongo document",
//      "data" -> Json.obj(
//        eventType -> Json.obj(
//          "members" -> arrayOfMembers
//        )
//      ),
//      "expireAt" -> "Change this entire line to match the original Mongo document",
//      "lastUpdated" -> "Change this entire line to match the original Mongo document"
//    )
//  }
//
//  def generateEvent3SummaryJson(eventType: String, numberOfMembers: Int): JsObject = {
//    val arrayOfMembers = for {
//      num <- (1 to numberOfMembers).toList
//    } yield {
//      Json.parse(
//        s"""
//          |{
//          |  "membersDetails": {
//          |    "firstName": "Hello",
//          |    "lastName": "World",
//          |    "nino": "AB${100000 + num}C"
//          |  },
//          |  "benefitType": {
//          |    "reasonBenefitTaken": "other",
//          |    "freeText": "Reason"
//          |  },
//          |  "paymentDetails": {
//          |    "amountPaid": 10.01,
//          |    "eventDate": "2022-12-12"
//          |  }
//          |}
//          |""".stripMargin)
//    }
//    Json.obj(
//      "_id" -> "Change this entire line to match the original Mongo document",
//      "apiTypes" -> "1830",
//      "pstr" -> "Change this entire line to match the original Mongo document",
//      "data" -> Json.obj(
//        eventType -> Json.obj(
//          "members" -> arrayOfMembers
//        )
//      ),
//      "expireAt" -> "Change this entire line to match the original Mongo document",
//      "lastUpdated" -> "Change this entire line to match the original Mongo document"
//    )
//  }
//
//  def generateEvent4SummaryJson(eventType: String, numberOfMembers: Int): JsObject = {
//    val arrayOfMembers = for {
//      num <- (1 to numberOfMembers).toList
//    } yield {
//      Json.parse(
//  s"""
//    |{
//    |  "membersDetails": {
//    |    "firstName": "Hello",
//    |    "lastName": "World",
//    |    "nino": "AB${100000 + num}C"
//    |  },
//    |  "paymentDetails": {
//    |    "amountPaid": 10.01,
//    |    "eventDate": "2022-12-12"
//    |  }
//    |}
//    |""".stripMargin)
//    }
//    Json.obj(
//      "_id" -> "Change this entire line to match the original Mongo document",
//      "apiTypes" -> "1830",
//      "pstr" -> "Change this entire line to match the original Mongo document",
//      "data" -> Json.obj(
//        eventType -> Json.obj(
//          "members" -> arrayOfMembers
//        )
//      ),
//      "expireAt" -> "Change this entire line to match the original Mongo document",
//      "lastUpdated" -> "Change this entire line to match the original Mongo document"
//    )
//  }
//
//  def generateEvent5SummaryJson(eventType: String, numberOfMembers: Int): JsObject = {
//    val arrayOfMembers = for {
//      num <- (1 to numberOfMembers).toList
//    } yield {
//      Json.parse(
//        s"""
//          |  {
//          |  "membersDetails": {
//          |    "firstName": "Hello",
//          |    "lastName": "World",
//          |    "nino": "AB${100000 + num}C"
//          |  },
//          |  "paymentDetails": {
//          |    "amountPaid": 10.01,
//          |    "eventDate": "2022-12-12"
//          |  }
//          |}
//          |""".stripMargin)
//    }
//    Json.obj(
//      "_id" -> "Change this entire line to match the original Mongo document",
//      "apiTypes" -> "1830",
//      "pstr" -> "Change this entire line to match the original Mongo document",
//      "data" -> Json.obj(
//        eventType -> Json.obj(
//          "members" -> arrayOfMembers
//        )
//      ),
//      "expireAt" -> "Change this entire line to match the original Mongo document",
//      "lastUpdated" -> "Change this entire line to match the original Mongo document"
//    )
//  }
//
//  def generateEvent7SummaryJson(eventType: String, numberOfMembers: Int): JsObject = {
//    val arrayOfMembers = for {
//      num <- (1 to numberOfMembers).toList
//    } yield {
//      Json.parse(
//        s"""
//          |{
//          |  "membersDetails": {
//          |    "firstName": "Hello",
//          |    "lastName": "World",
//          |    "nino": "AB${100000 + num}C"
//          |  },
//          |  "lumpSumAmount": 10.01,
//          |  "crystallisedAmount": 10.01,
//          |  "paymentDate": {
//          |    "date": "2022-12-12"
//          |  }
//          |}
//          |""".stripMargin)
//    }
//    Json.obj(
//      "_id" -> "Change this entire line to match the original Mongo document",
//      "apiTypes" -> "1830",
//      "pstr" -> "Change this entire line to match the original Mongo document",
//      "data" -> Json.obj(
//        eventType -> Json.obj(
//          "members" -> arrayOfMembers
//        )
//      ),
//      "expireAt" -> "Change this entire line to match the original Mongo document",
//      "lastUpdated" -> "Change this entire line to match the original Mongo document"
//    )
//  }
//
//  def generateEvent8SummaryJson(eventType: String, numberOfMembers: Int): JsObject = {
//    val arrayOfMembers = for {
//      num <- (1 to numberOfMembers).toList
//    } yield {
//      Json.parse(
//        s"""
//          |  {
//          |  "membersDetails": {
//          |    "firstName": "Hello",
//          |    "lastName": "World",
//          |    "nino": "AB${100000 + num}C"
//          |  },
//          |  "typeOfProtection": "primaryProtection",
//          |  "typeOfProtectionReference": "12345678",
//          |  "lumpSumAmountAndDate": {
//          |    "lumpSumAmount": 10.01,
//          |    "lumpSumDate": "2022-12-12"
//          |  }
//          |}
//          |""".stripMargin)
//    }
//    Json.obj(
//      "_id" -> "Change this entire line to match the original Mongo document",
//      "apiTypes" -> "1830",
//      "pstr" -> "Change this entire line to match the original Mongo document",
//      "data" -> Json.obj(
//        eventType -> Json.obj(
//          "members" -> arrayOfMembers
//        )
//      ),
//      "expireAt" -> "Change this entire line to match the original Mongo document",
//      "lastUpdated" -> "Change this entire line to match the original Mongo document"
//    )
//  }
//
//  def generateEvent8ASummaryJson(eventType: String, numberOfMembers: Int): JsObject = {
//    val arrayOfMembers = for {
//      num <- (1 to numberOfMembers).toList
//    } yield {
//      Json.parse(
//        s"""
//          |{
//          |  "membersDetails": {
//          |    "firstName": "Hello",
//          |    "lastName": "World",
//          |    "nino": "AB${100000 + num}C"
//          |  },
//          |  "paymentType": "paymentOfAStandAloneLumpSum",
//          |  "typeOfProtection": "primaryProtection",
//          |  "typeOfProtectionReference": "12345678",
//          |  "lumpSumAmountAndDate": {
//          |    "lumpSumAmount": 10.01,
//          |    "lumpSumDate": "2022-12-12"
//          |  }
//          |}
//          |""".stripMargin)
//    }
//    Json.obj(
//      "_id" -> "Change this entire line to match the original Mongo document",
//      "apiTypes" -> "1830",
//      "pstr" -> "Change this entire line to match the original Mongo document",
//      "data" -> Json.obj(
//        eventType -> Json.obj(
//          "members" -> arrayOfMembers
//        )
//      ),
//      "expireAt" -> "Change this entire line to match the original Mongo document",
//      "lastUpdated" -> "Change this entire line to match the original Mongo document"
//    )
//  }

  def generateEvent22Or23SummaryJson(eventType: String, numberOfMembers: Int, year: Int): JsObject = {
    val arrayOfMembers = for {
      num <- (1 to numberOfMembers).toList
    } yield {
      Json.obj(
        "membersDetails" -> Json.obj(
          "firstName" -> Name.first_name,
          "lastName" -> Name.last_name,
          "nino" -> s"AB${100000 + num}C"
        ),
        "chooseTaxYear" -> s"${year}",
        "totalPensionAmounts" -> BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP)
      )
    }
    Json.obj(
      "_id" -> "Change this entire line to match the original Mongo document",
      "apiTypes" -> "1830",
      "pstr" -> "Change this entire line to match the original Mongo document",
      "data" -> Json.obj(
        eventType -> Json.obj(
          "members" -> arrayOfMembers
        )
      ),
    "expireAt" -> "Change this entire line to match the original Mongo document",
    "lastUpdated" -> "Change this entire line to match the original Mongo document"
    )
  }

  def generateEvent6SummaryJson(eventType: String, numberOfMembers: Int, year: Int): JsObject = {
    val protectionTypes = Seq("enhancedLifetimeAllowance", "enhancedProtection", "fixedProtection",
      "fixedProtection2014", "fixedProtection2016", "individualProtection2014", "individualProtection2016"
    )
    val arrayOfMembers = for {
      num <- (1 to numberOfMembers).toList
    } yield {
      Json.obj(
        "membersDetails" -> Json.obj(
          "firstName" -> Name.first_name,
          "lastName" -> Name.last_name,
          "nino" -> s"AB${100000 + num}C"
        ),
        "typeOfProtection" -> s"${new Random().shuffle(protectionTypes).head}",
        "inputProtectionType" ->  s"${new Random().between(10000000, 99999999)}",
        "AmountCrystallisedAndDate" -> Json.obj(
          "amountCrystallised" -> BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP),
          "crystallisedDate" -> s"$year-11-0${new Random().between(1, 9)}",
        )
      )
    }
    Json.obj(
      "_id" -> "Change this entire line to match the original Mongo document",
      "apiTypes" -> "1830",
      "pstr" -> "Change this entire line to match the original Mongo document",
      "data" -> Json.obj(
        eventType -> Json.obj(
          "members" -> arrayOfMembers
        )
      ),
      "expireAt" -> "Change this entire line to match the original Mongo document",
      "lastUpdated" -> "Change this entire line to match the original Mongo document"
    )
  }

  // Uncomment below to write files.
    private val numOfMembers = 1000
    private val taxYear = DateHelper.currentYear - 1

//   generate data for event22
//   writeFileToConfResources("event22", generateEvent22Or23SummaryJson("event22", numOfMembers, taxYear).toString(), s"${numOfMembers.toString}Members${taxYear}Payload.json")

  // generate csv data for event22
  // writeFileToConfResources("event22", generateEvent22SummaryCsv(numOfMembers, taxYear), s"${numOfMembers.toString}Members${taxYear}Payload.csv")

  // generate json data for event6
  // writeFileToConfResources("event6", generateEvent6SummaryJson("event6", numOfMembers, taxYear).toString(), s"${numOfMembers.toString}Members${taxYear}Payload.json")

  // generate csv data for event6
  // writeFileToConfResources("event6", generateEvent6SummaryCsv(numOfMembers, taxYear), s"${numOfMembers.toString}Members${taxYear}Payload.csv")

  // generate csv data for event23
  // writeFileToConfResources("event23", generateEvent22Or23SummaryCsv(numOfMembers, taxYear), s"${numOfMembers.toString}Members${taxYear}Payload.csv")

  // generate json data for event23
  // writeFileToConfResources("event23", generateEvent22Or23SummaryJson("event23", numOfMembers, taxYear).toString(), s"${numOfMembers.toString}Members${taxYear}Payload.json")


  // generate json data for event2
  // writeFileToConfResources("event2", generateEvent2SummaryJson("event2", numOfMembers).toString(), s"event2_${numOfMembers.toString}Members${taxYear}Payload.json")

  // generate json data for event3
  //writeFileToConfResources("event3", generateEvent3SummaryJson("event3", numOfMembers).toString(), s"event3_${numOfMembers.toString}Members${taxYear}Payload.json")

  // generate json data for event4
  // writeFileToConfResources("event4", generateEvent4SummaryJson("event4", numOfMembers).toString(), s"event4_${numOfMembers.toString}Members${taxYear}Payload.json")

  // generate json data for event5
  // writeFileToConfResources("event5", generateEvent5SummaryJson("event5", numOfMembers).toString(), s"event5_${numOfMembers.toString}Members${taxYear}Payload.json")

  // generate json data for event7
  // writeFileToConfResources("event7", generateEvent7SummaryJson("event7", numOfMembers).toString(), s"event7_${numOfMembers.toString}Members${taxYear}Payload.json")

  // generate json data for event8
  //writeFileToConfResources("event8", generateEvent8SummaryJson("event8", numOfMembers).toString(), s"event8_${numOfMembers.toString}Members${taxYear}Payload.json")


  // generate json data for event8A
  // writeFileToConfResources("event8A", generateEvent8ASummaryJson("event8A", numOfMembers).toString(), s"event8A_${numOfMembers.toString}Members${taxYear}Payload.json")

}
