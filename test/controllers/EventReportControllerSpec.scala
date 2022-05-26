/*
 * Copyright 2022 HM Revenue & Customs
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

package controllers

import base.SpecBase
import play.api.libs.json.{JsObject, Json}
import play.api.test.Helpers._
import play.api.test._

import java.time.LocalDate

class EventReportControllerSpec extends SpecBase {

  private val fakeRequest = FakeRequest("POST", "/").withHeaders(("CorrelationId", "testId"),
    "Authorization" -> "test Bearer token", ("Environment", "local"))

  private val invalidPayload: JsObject = Json.obj(
    "code" -> "INVALID_PAYLOAD",
    "reason" -> "Submission has not passed validation. Invalid PAYLOAD"
  )
  private val createCompileEventReportSummarySuccessResponse: JsObject = Json.obj(
    "processingDate" -> LocalDate.now(),
    "formBundleNumber" -> "12345678912"
  )

  "compileEventReportSummary" must {

    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/validEventReportRequest.json")
      val postRequest = fakeRequest.withJsonBody(validData)
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.compileEventReportSummary(pstr = "test-pstr")(postRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe createCompileEventReportSummarySuccessResponse
      }
    }

    "return 400 for a bad request" in {
      val postRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.compileEventReportSummary(pstr = "test-pstr")(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidPayload
      }
    }
  }


}

