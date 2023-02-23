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

  def writeFileToConfResources(content:String, fileName:String):Unit = {
    import java.io._
    val pw = new PrintWriter(new File( s"conf/resources/data/api1832/event22PaginationTestPayloads/$fileName" ))
    pw.write(content)
    pw.close()
  }

  def generateEvent22SummaryJson(numberOfMembers: Int, year: Int): JsObject = {
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
        "event22" -> Json.obj(
          "members" -> arrayOfMembers
        )
      ),
    "expireAt" -> "Change this entire line to match the original Mongo document",
    "lastUpdated" -> "Change this entire line to match the original Mongo document"
    )
  }

  // Uncomment below to write files.
  //  private val numOfMembers = 100
  //  private val taxYear = DateHelper.currentYear - 1
  //  writeFileToConfResources(generateEvent22SummaryJson(numOfMembers, taxYear).toString(), s"${numOfMembers.toString}Members${taxYear}Payload.json")
}
