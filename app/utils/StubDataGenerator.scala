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
    val pw = new PrintWriter(new File( s"conf/resources/data/api1832/${eventType}PaginationTestPayloads/$fileName" ))
    pw.write(content)
    pw.close()
  }

  def generateEvent22Or23SummaryJson(eventType: String, numberOfMembers: Int, year: Int): JsObject = {
    val arrayOfMembers = for {
      _ <- (1 to numberOfMembers).toList
    } yield {
      Json.obj(
        "membersDetails" -> Json.obj(
          "firstName" -> Name.first_name,
          "lastName" -> Name.last_name,
          "nino" -> s"AB${new Random().between(100000, 999999)}C"
        ),
        "chooseTaxYear" -> s"${year}",
        "totalPensionAmounts" -> BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP)
      )
    }
    Json.obj(
      "_id" -> "Change this entire line to match the original Mongo document",
      "apiTypes" -> "1830",
      "pstr" -> "87219363YN",
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
      _ <- (1 to numberOfMembers).toList
    } yield {
      Json.obj(
        "membersDetails" -> Json.obj(
          "firstName" -> Name.first_name,
          "lastName" -> Name.last_name,
          "nino" -> s"AB${new Random().between(100000, 999999)}C"
        ),
        //TODO: randomise value of typeOfProtection options
        "typeOfProtection" -> s"${new Random().shuffle(protectionTypes).head}",
        "inputProtectionType" ->  s"${new Random().between(10000000, 99999999)}",
        "AmountCrystallisedAndDate" -> Json.obj(
          "amountCrystallised" -> BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP),
          "crystallisedDate" -> s"${year}-11-0${new Random().between(1, 9)}",
        )
      )
    }
    Json.obj(
      "_id" -> "Change this entire line to match the original Mongo document",
      "apiTypes" -> "1830",
      "pstr" -> "87219363YN",
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
  private val numOfMembers = 100
   private val taxYear = DateHelper.currentYear - 1

  // generate data for event22
  //writeFileToConfResources("event22", generateEvent22Or23SummaryJson("event22", numOfMembers, taxYear).toString(), s"${numOfMembers.toString}Members${taxYear}Payload.json")

  //generate data for event23
//  writeFileToConfResources("event23", generateEvent22Or23SummaryJson("event23", numOfMembers, taxYear).toString(), s"${numOfMembers.toString}Members${taxYear}Payload.json")

  // generate data for event6
  writeFileToConfResources("event6", generateEvent6SummaryJson("event6", numOfMembers, taxYear).toString(), s"${numOfMembers.toString}Members${taxYear}Payload.json")
}
