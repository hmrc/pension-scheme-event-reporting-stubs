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

import base.SpecBase
import uk.gov.hmrc.http.NotFoundException

import java.io.File

class JsonUtilsSpec extends SpecBase {
  private val jsonUtils = new JsonUtils(environment)

  "readJsonFromFile" must {
    "with return value when file found" in {

      val path = new File("./conf/resources/data/validEventReportSummaryRequest.json").getPath

      val result = jsonUtils.readJsonFromFile(path)
      assert(result.toString().nonEmpty)

    }

    "with throw file found exception " in {
      val path = new File("./conf/notFound/resources/data/validEventReportSummaryRequest.json").getPath
      intercept[NotFoundException] {
        jsonUtils.readJsonFromFile(path)
      }
    }
  }
}
