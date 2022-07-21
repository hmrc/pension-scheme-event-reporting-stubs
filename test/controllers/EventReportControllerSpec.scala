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
import play.api.http.Status.FORBIDDEN
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test._

import java.time.LocalDate
import scala.concurrent.Future

class EventReportControllerSpec extends SpecBase {

  import EventReportController._
  import utils.DefaultGetResponse._

  private val fakeRequest = FakeRequest("POST", "/").withHeaders(("CorrelationId", "testId"),
    "Authorization" -> "test Bearer token", ("Environment", "local"))
  private val controller = app.injector.instanceOf[EventReportController]

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

  private val compileMemberEventReportSuccessResponse: JsObject = Json.obj(
    "processingDate" -> LocalDate.now(),
    "formBundleNumber" -> "12345678999"
  )

  private val submitEventDeclarationReportSuccessResponse: JsObject = Json.obj(
    "processingDate" -> LocalDate.now(),
    "formBundleNumber" -> "12345678933"
  )

  private val submitEvent20ADeclarationReportSuccessResponse: JsObject = Json.obj(
    "processingDate" -> LocalDate.now(),
    "formBundleNumber" -> "12345670811"
  )

  "compileEventReportSummary" must {

    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/validEventReportSummaryRequest.json")
      val postRequest = fakeRequest.withJsonBody(validData)
      running() { _ =>
        val result = controller.compileEventReportSummary(pstr = "test-pstr")(postRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe createCompileEventReportSummarySuccessResponse
      }
    }

    "return 400 for a bad request" in {
      val postRequest = fakeRequest
      running() { _ =>
        val result = controller.compileEventReportSummary(pstr = "test-pstr")(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidPayload
      }
    }
  }

  "compileEventOneReport" must {

    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/validEventOneReportRequest.json")
      val postRequest = fakeRequest.withJsonBody(validData)
      running() { _ =>
        val result = controller.compileEventOneReport(pstr = "test-pstr")(postRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe compileEventOneReportSuccessResponse
      }
    }

    "return 400 for a bad request" in {
      val postRequest = fakeRequest
      running() { _ =>
        val result = controller.compileEventReportSummary(pstr = "test-pstr")(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidPayload
      }
    }
  }

  "compileMemberEventReport" must {

    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/validMemberEventReportRequest.json")
      val postRequest = fakeRequest.withJsonBody(validData)
      running() { _ =>
        val result = controller.compileMemberEventReport(pstr = "test-pstr")(postRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe compileMemberEventReportSuccessResponse
      }
    }

    "return 400 for a bad request" in {
      val postRequest = fakeRequest
      running() { _ =>
        val result = controller.compileMemberEventReport(pstr = "test-pstr")(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidPayload
      }
    }
  }


  "getOverview" must {
    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/getOverview/24000015IN.json")
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getOverview(pstr = "24000015IN", fromDate = "2021-04-06", toDate = "2022-04-05", reportType = "ER")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }

    "return a Bad Request if fromDate is empty" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getOverview(pstr = "24000015IN", fromDate = "", toDate = "2022-04-05", reportType = "ER")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe missingFromDateResponse
      }
    }

    "return a Bad Request if toDate is empty" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getOverview(pstr = "24000015IN", fromDate = "2022-04-05", toDate = "", reportType = "ER")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe missingToDateResponse
      }
    }

    "return a Bad Request if fromDate invalid" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getOverview(pstr = "24000015IN", fromDate = "Invalid fromDate", toDate = "2022-04-05", reportType = "ER")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidFromDateResponse
      }
    }

    "return a Bad Request if toDate invalid" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getOverview(pstr = "24000015IN", fromDate = "2022-04-05", toDate = "Invalid toDate", reportType = "ER")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidToDateResponse
      }
    }

    "return Bad Request if invalid PSTR response" in {
      val getRequest = fakeRequest
      running() { _ =>
        val invalidPstr = "24000001IN"
        val result = controller.getOverview(pstr = invalidPstr, fromDate = "2022-04-05", toDate = "2022-04-04", reportType = "ER")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidPstrResponse
      }
    }

    "must return a Bad Request if fromDate not in range" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getOverview(pstr = "24000015IN", fromDate = "2070-04-05", toDate = "2071-04-05", reportType = "ER")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe fromDateNotInRangeResponse
      }
    }

    "must return a Bad Request if toDate not in range" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getOverview(pstr = "24000015IN", fromDate = "2070-04-05", toDate = "2060-04-05", reportType = "ER")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe toDateNotInRangeResponse
      }
    }

    "must return a Bad Request if reportType is missing" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getOverview(pstr = "24000015IN", fromDate = "2070-04-05", toDate = "2060-04-05", reportType = "")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe missingReportTypeResponse
      }
    }
  }

  "api1832GET" must {
    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/api1832/24000015IN.json")
      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "eventType" -> "Event3",
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.api1832GET(pstr = "24000015IN")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }
  }

  "getEvent20A" must {
    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/getEvent20A/24000015IN.json")
      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "24000015IN")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }

    "return 200 for a valid request with reportFormBundleNumber" in {
      val validData = readJsonFromFile(filePath = "/resources/data/getEvent20A/24000015IN.json")
      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportFormBundleNumber" -> "version"
      )
      val getRequest = fakeRequest
      running() { _ =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "24000015IN")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }

    "return BAD_REQUEST for a startDate missing" in {

      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportVersionNumber" -> "version"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "24000015IN")(getRequest)

        status(result) mustBe BAD_REQUEST
      }
    }

    "return BAD_REQUEST for a version missing" in {

      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportStartDate" -> "start"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "24000015IN")(getRequest)

        status(result) mustBe BAD_REQUEST
      }
    }

    "return InternalServerError" in {
      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "21000001AA")(getRequest)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }


    "return ServiceUnavailable" in {
      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "21000002AA")(getRequest)

        status(result) mustBe SERVICE_UNAVAILABLE
      }
    }

    "return DUPLICATE_SUBMISSION" in {
      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "24000009IN")(getRequest)

        status(result) mustBe CONFLICT
      }
    }

    "return INVALID_PAYLOAD" in {
      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "21000004AA")(getRequest)

        status(result) mustBe BAD_REQUEST
      }
    }

    "return REQUEST_NOT_PROCESSED" in {
      val fakeRequest = FakeRequest("POST", "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )
      val getRequest = fakeRequest
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.getEvent20A(pstr = "21000005AA")(getRequest)

        status(result) mustBe UNPROCESSABLE_ENTITY
      }
    }
  }

  "api1833GET" must {
    "return 200 OK for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/api1833/24000015IN.json")

      val fakeRequest = FakeRequest(method = "POST", path = "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "eventType" -> "Event1",
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )

      val getRequest = fakeRequest

      running() { _ =>
        val result = controller.api1833GET(pstr = "24000015IN")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }

    "return 400 BAD REQUEST for a invalid request" in {
      val badRequest = fakeRequest

      running() { _ =>
        val result: Future[Result] = controller.api1833GET(pstr = "test-pstr")(badRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidEventTypeResponse
      }
    }

    "return 404 NOT FOUND for a not found PSTR" in {
      val notFoundPstr = "24000001IN"
      val fakeRequest = FakeRequest(method = "POST", path = "/").withHeaders(
        ("CorrelationId", "testId"),
        "Authorization" -> "test Bearer token",
        ("Environment", "local"),
        "eventType" -> "Event1",
        "reportVersionNumber" -> "version",
        "reportStartDate" -> "start"
      )

      val getRequest = fakeRequest

      running() { _ =>
        val result: Future[Result] = controller.api1833GET(pstr = notFoundPstr)(getRequest)

        status(result) mustBe NOT_FOUND
        contentAsJson(result) mustBe invalidPstrResponse
      }
    }
  }

  "submitEventDeclarationReport" must {

    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/validSubmitEventDeclarationReportRequest.json")
      val postRequest = fakeRequest.withJsonBody(validData)
      running() { _ =>
        val result = controller.submitEventDeclarationReport(pstr = "test-pstr")(postRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe submitEventDeclarationReportSuccessResponse
      }
    }

    "return 400 for a bad request" in {
      val postRequest = fakeRequest
      running() { _ =>
        val result = controller.compileEventReportSummary(pstr = "test-pstr")(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidPayload
      }
    }
  }

  "submitEvent20ADeclarationReport" must {

    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/validSubmitEvent20ADeclarationReportRequest.json")
      val postRequest = fakeRequest.withJsonBody(validData)
      running() { app =>
        val controller = app.injector.instanceOf[EventReportController]
        val result = controller.submitEvent20ADeclarationReport(pstr = "test-pstr")(postRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe submitEvent20ADeclarationReportSuccessResponse
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

  "getERVersions" must {
    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/getVersions/24000015IN/2020-04-01.json")
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getERVersions(pstr = "24000015IN", startDate = "2020-04-01")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }

    "return 200 for a valid request for default value" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getERVersions(pstr = "24000015IN", startDate = "2022-04-01")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe Json.parse(defaultVersions("2022-04-01").toString())
      }
    }

    "must return a FORBIDDEN if startDate is empty" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getERVersions(pstr = "24000015IN", startDate = "")(getRequest)

        status(result) mustBe FORBIDDEN
        contentAsJson(result) mustBe mandatoryStartDateResponse
      }
    }

    "must return a Bad Request if startDate invalid" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getERVersions(pstr = "24000015IN", startDate = "Invalid fromDate")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidStartDateResponse
      }
    }

    "must return Not Found if invalid PSTR response" in {
      val getRequest = fakeRequest
      running() { _ =>
        val invalidPstr = "24000001IN"
        val result = controller.getERVersions(pstr = invalidPstr, startDate = "2022-04-05")(getRequest)

        status(result) mustBe NOT_FOUND
        contentAsJson(result) mustBe invalidPstrResponse
      }
    }
  }

  "getER20AVersions" must {
    "return 200 for a valid request" in {
      val validData = readJsonFromFile(filePath = "/resources/data/getVersions/24000015IN/2021-04-01.json")
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getER20AVersions(pstr = "24000015IN", startDate = "2021-04-01")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe validData
      }
    }

    "return 200 for a valid request for default value" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getER20AVersions(pstr = "24000015IN", startDate = "2022-04-01")(getRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe Json.parse(defaultVersions("2022-04-01").toString())
      }
    }

    "must return a FORBIDDEN if startDate is empty" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getER20AVersions(pstr = "24000015IN", startDate = "")(getRequest)

        status(result) mustBe FORBIDDEN
        contentAsJson(result) mustBe mandatoryStartDateResponse
      }
    }

    "must return a Bad Request if startDate invalid" in {
      val getRequest = fakeRequest
      running() { _ =>
        val result = controller.getER20AVersions(pstr = "24000015IN", startDate = "Invalid fromDate")(getRequest)

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe invalidStartDateResponse
      }
    }

    "must return Not Found if invalid PSTR response" in {
      val getRequest = fakeRequest
      running() { _ =>
        val invalidPstr = "24000001IN"
        val result = controller.getER20AVersions(pstr = invalidPstr, startDate = "2022-04-05")(getRequest)

        status(result) mustBe NOT_FOUND
        contentAsJson(result) mustBe invalidPstrResponse
      }
    }
  }
}