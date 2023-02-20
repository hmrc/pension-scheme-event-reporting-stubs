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
import faker._

// object StubDataGenerator extends App (if you want to test locally)
@Singleton
class StubDataGenerator {

  def generateEvent22SummaryJson(members: Int): JsObject = {
    val arrayOfMembers = for {
      _ <- (1 to members).toList
    } yield {
      Json.obj(
        "membersDetails" -> Json.obj(
          "firstName" -> Name.first_name,
          "lastName" -> Name.last_name,
          "nino" -> s"${new Random().alphanumeric.filter(_.isLetter).take(2).mkString.toUpperCase}${new Random().between(100000, 999999)}C"
        ),
        "chooseTaxYear" -> "2022",
        "totalPensionAmounts" -> BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP)
      )
    }
    Json.obj(
      "data" -> Json.obj(
        "event22" -> Json.obj(
          "members" -> arrayOfMembers
        )
      )
    )
  }
  // See JSON output:
  // println(generateEvent22SummaryJson(3))
}
