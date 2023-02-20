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

// TODO: Change back to class once happy with implementation, object extending App for now so you can run it.
object StubDataGenerator extends App {

  // Makes as many random members as you like and turns into the JSON required for the FE display.
  def generateEvent22PaginationJson(members: Int): List[JsObject] = {
    for {
      _ <- (1 to members).toList
    } yield {
      Json.obj(
        "firstName" -> Name.first_name,
        "lastName" -> Name.last_name,
        "nino" -> s"AB${new Random().between(100000, 999999)}C",
        "taxYear" -> "2022-23",
        "amount" -> BigDecimal(new Random().between(1: Float, 1000: Float)).setScale(2, BigDecimal.RoundingMode.HALF_UP)
      )
    }
  }
  // TODO: remove println, exists now to see JSON output.
  println(generateEvent22PaginationJson(3))
}
