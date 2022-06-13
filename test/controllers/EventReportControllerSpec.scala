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

  import EventReportController._

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

  private val compileEventOneReportSuccessResponse: JsObject = Json.obj(
    "processingDate" -> LocalDate.now(),
    "formBundleNumber" -> "12345678988"
  )

  "compileEventReportSummary" must {

    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/validEventReportSummaryRequest.json")
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

  "getEROverview" must {
    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/getOverview/24000015IN.json")
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEROverview(pstr = "24000015IN", fromDate = "2021-04-06", toDate = "2022-04-05")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }

    "return a Bad Request if fromDate is empty" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEROverview(pstr = "24000015IN", fromDate = "", toDate = "2022-04-05")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe missingFromDateResponse
      }
    }

    "return a Bad Request if toDate is empty" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEROverview(pstr = "24000015IN", fromDate = "2022-04-05", toDate = "")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe missingToDateResponse
      }
    }

    "return a Bad Request if fromDate invalid" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEROverview(pstr = "24000015IN", fromDate = "Invalid fromDate", toDate = "2022-04-05")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe InvalidFromDateResponse
      }
    }

    "return a Bad Request if toDate invalid" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEROverview(pstr = "24000015IN", fromDate = "2022-04-05", toDate = "Invalid toDate")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe InvalidToDateResponse
      }
    }

    "return Bad Request if invalid PSTR response" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val invalidPstr = "24000001IN"
        val result = controller.getEROverview(pstr = invalidPstr, fromDate = "2022-04-05", toDate = "2022-04-04")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe InvalidPstrResponse
      }
    }
  }

  "compileEventOneReport" must {

    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/validEventOneReportRequest.json")
      val postRequest = fakeRequest.withJsonBody(validData)
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.compileEventOneReport(pstr = "test-pstr")(postRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe compileEventOneReportSuccessResponse
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

  "getER20AOverview" must {
    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/getOverview/24000015IN.json")
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getER20AOverview(pstr = "24000015IN", fromDate = "2021-04-06", toDate = "2022-04-05")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }

    "must return a Bad Request if fromDate is empty" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getER20AOverview(pstr = "24000015IN", fromDate = "", toDate = "2022-04-05")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe missingFromDateResponse
      }
    }

    "must return a Bad Request if toDate is empty" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getER20AOverview(pstr = "24000015IN", fromDate = "2022-04-05", toDate = "")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe missingToDateResponse
      }
    }

    "must return a Bad Request if fromDate invalid" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getER20AOverview(pstr = "24000015IN", fromDate = "Invalid fromDate", toDate = "2022-04-05")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe InvalidFromDateResponse
      }
    }

    "must return a Bad Request if toDate invalid" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getER20AOverview(pstr = "24000015IN", fromDate = "2022-04-05", toDate = "Invalid toDate")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe InvalidToDateResponse
      }
    }

    "must return Not Found if invalid PSTR response" in {
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val invalidPstr = "24000001IN"
        val result = controller.getER20AOverview(pstr = invalidPstr, fromDate = "2022-04-05", toDate = "2022-04-08")(getRequest)

        status(result) mustBe NOT_FOUND
        contentAsJson(result) mustBe InvalidPstrResponse
      }
    }
  }
}