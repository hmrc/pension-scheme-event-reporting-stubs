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

import com.google.inject.Inject
import controllers.EventReportController._
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.{APIResponses, JsonUtils}

import java.time.LocalDate
import javax.inject.Singleton
import scala.concurrent.Future

@Singleton()
class EventReportController @Inject()(
                                       cc: ControllerComponents,
                                       jsonUtils: JsonUtils,
                                     ) extends BackendController(cc) with APIResponses {

  def compileEventReportSummary(pstr: String): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson match {
        case Some(_) => Future.successful(Ok(createCompileEventReportSummarySuccessResponse))
        case _ => Future.successful(BadRequest(invalidPayload))
      }
  }

  def compileEventOneReport(pstr: String): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson match {
        case Some(_) => Future.successful(Ok(compileEventOneReportSuccessResponse))
        case _ => Future.successful(BadRequest(invalidPayload))
      }
  }

  def compileMemberEventReport(pstr: String): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson match {
        case Some(_) => Future.successful(Ok(compileMemberEventReportSuccessResponse))
        case _ => Future.successful(BadRequest(invalidPayload))
      }
  }

  def getOverview(pstr: String, fromDate: String, toDate: String, reportType: String): Action[AnyContent] = Action.async {

    val path = "conf/resources/data/getOverview"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN", "00000042IN")
    val erPerfTestPstrPattern: String = """^34000[0-9]{3}IN$"""
    val datePattern: String = "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-](0[1-9]|1[0-9]|2[0-8])))$"

    if (reportType.isEmpty) {
      Future.successful(BadRequest(missingReportTypeResponse))
    }
    else if (fromDate.isEmpty) {
      Future.successful(BadRequest(missingFromDateResponse))
    }
    else if (toDate.isEmpty) {
      Future.successful(BadRequest(missingToDateResponse))
    }
    else if (notFoundPSTR.contains(pstr) || pstr.matches(erPerfTestPstrPattern)) {
      Future.successful(BadRequest(invalidPstrResponse))
    }
    else if (!fromDate.matches(datePattern)) {
      Future.successful(BadRequest(invalidFromDateResponse))
    }
    else if (!toDate.matches(datePattern)) {
      Future.successful(BadRequest(invalidToDateResponse))
    }
    else if (LocalDate.parse(toDate).isBefore(LocalDate.parse(fromDate))) {
      Future.successful(BadRequest(toDateNotInRangeResponse))
    }
    else if (LocalDate.parse(fromDate).isAfter(LocalDate.now())) {
      Future.successful(BadRequest(fromDateNotInRangeResponse))
    }
    else {
      val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr.json")
        .getOrElse(defaultOverview(fromDate, toDate))

      Future.successful(Ok(filterOverview(jsValue, fromDate, toDate, reportType)))
    }
  }

  def submitEventDeclarationReport(pstr: String): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson match {
        case Some(_) => Future.successful(Ok(submitEventDeclarationReportSuccessResponse))
        case _ => Future.successful(BadRequest(invalidPayload))
      }
  }

  def getERVersions(pstr: String, startDate: String): Action[AnyContent] = Action.async {

    val path = "conf/resources/data/getVersions"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN")
    val aftPerfTestPstrPattern: String = """^34000[0-9]{3}IN$"""

    if (startDate.isEmpty) {
      Future.successful(Forbidden(mandatoryStartDateResponse))
    } else if (!startDate.matches(datePattern)) {
      Future.successful(BadRequest(invalidStartDateResponse))
    } else if (notFoundPSTR.contains(pstr) || pstr.matches(aftPerfTestPstrPattern))
      Future.successful(NotFound(invalidPstrResponse))
    else {
      val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr/$startDate.json")
        .getOrElse(defaultVersions(startDate))

      Future.successful(Ok(jsValue))
    }
  }

  def getER20AVersions(pstr: String, startDate: String): Action[AnyContent] = Action.async {
    val path = "conf/resources/data/getVersions"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN")
    val aftPerfTestPstrPattern: String = """^34000[0-9]{3}IN$"""

    if (startDate.isEmpty) {
      Future.successful(Forbidden(mandatoryStartDateResponse))
    } else if (!startDate.matches(datePattern)) {
      Future.successful(BadRequest(invalidStartDateResponse))
    } else if (notFoundPSTR.contains(pstr) || pstr.matches(aftPerfTestPstrPattern))
      Future.successful(NotFound(invalidPstrResponse))
    else {
      val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr/$startDate.json")
        .getOrElse(defaultVersions(startDate))

      Future.successful(Ok(jsValue))
    }
  }

  def api1823GET(pstr: String): Action[AnyContent] = Action.async { implicit request =>
    val path = "conf/resources/data/api1823"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN")

    (request.headers.get("eventType"), request.headers.get("reportVersionNumber"), request.headers.get("reportStartDate")) match {
      case (Some(eventType), Some(version), Some(startDate)) =>
        if (notFoundPSTR.contains(pstr) || pstr.matches(perfTestPstrPattern))
          Future.successful(NotFound(invalidPstrResponse))
        else {
          val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr.json")
            .getOrElse(defaultGetEvent1823(pstr, eventType, version, startDate))
          Future.successful(Ok(jsValue))
        }
      case (None, _, _) => Future.successful(BadRequest(invalidEventTypeResponse))
      case (_, None, _) => Future.successful(BadRequest(invalidVersionResponse))
      case _ => Future.successful(BadRequest(invalidStartDateResponse))
    }


  }

  def api1833GET(pstr: String): Action[AnyContent] = Action.async { implicit request =>
    val path = "conf/resources/data/api1833"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN")

    (request.headers.get("eventType"), request.headers.get("reportVersionNumber"), request.headers.get("reportStartDate")) match {
      case (Some("Event1"), Some(version), Some(startDate)) =>
        if (notFoundPSTR.contains(pstr) || pstr.matches(perfTestPstrPattern))
          Future.successful(NotFound(invalidPstrResponse))
        else {
          val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr.json")
            .getOrElse(defaultGetEvent1833(pstr, version, startDate))
          Future.successful(Ok(jsValue))
        }
      case (None, _, _) => Future.successful(BadRequest(invalidEventTypeResponse))
      case (_, None, _) => Future.successful(BadRequest(invalidVersionResponse))
      case (_, _, None) => Future.successful(BadRequest(invalidStartDateResponse))
      case _ => Future.successful(InternalServerError(internalServerErrorResponse))
    }
  }


  private case class Overview(
                               periodStartDate: LocalDate,
                               periodEndDate: LocalDate,
                               tpssReportPresent: Option[String],
                               numberOfVersions: Option[Int],
                               submittedVersionAvailable: Option[String],
                               compiledVersionAvailable: Option[String]
                             )

  private def filterOverview(jsValue: JsValue, startDate: String, endDate: String, reportType: String): JsValue = {
    implicit val formats: Format[Overview] = Json.format[Overview]
    jsValue.validate[Seq[Overview]] match {
      case JsSuccess(seqOverview, _) =>
        val compareStartDate = LocalDate.parse(startDate)
        val compareEndDate = LocalDate.parse(endDate)

        val filteredSeqOverview = seqOverview.filter(o =>
          (o.periodStartDate.isAfter(compareStartDate) || o.periodStartDate.isEqual(compareStartDate)) &&
            (o.periodEndDate.isBefore(compareEndDate) || o.periodEndDate.isEqual(compareEndDate))
        )
        Json.toJson(filteredSeqOverview)
      case JsError(_) => throw new RuntimeException("Unable to read json")
    }
  }
}

object EventReportController {
  private val datePattern: String = "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-](0[1-9]|1[0-9]|2[0-8])))$"
  private val perfTestPstrPattern: String = """^34000[0-9]{3}IN$"""

  val noReportFoundResponse: JsObject = Json.obj(
    "code" -> "NO_REPORT_FOUND",
    "reason" -> "The remote endpoint has indicated No Scheme report was found for the given period."
  )
  val invalidPstrResponse: JsObject = Json.obj(
    "code" -> "INVALID_PSTR",
    "reason" -> "Submission has not passed validation. Invalid parameter pstr."
  )

  val invalidEventTypeResponse: JsObject = Json.obj(
    "code" -> "INVALID_EVENTTYPE",
    "reason" -> "Invalid event type"
  )
  val invalidVersionResponse: JsObject = Json.obj(
    "code" -> "INVALID_VERSIONNUMBER",
    "reason" -> "Invalid version"
  )

  val invalidStartDateResponse: JsObject = Json.obj(
    "code" -> "INVALID_STARTDATE",
    "reason" -> "Invalid start date"
  )
  val invalidFromDateResponse: JsObject = Json.obj(
    "code" -> "INVALID_FROM_DATE",
    "reason" -> "Submission has not passed validation. Invalid query parameter fromDate."
  )
  val invalidToDateResponse: JsObject = Json.obj(
    "code" -> "INVALID_TO_DATE",
    "reason" -> "Submission has not passed validation. Invalid query parameter toDate."
  )
  val missingFromDateResponse: JsObject = Json.obj(
    "code" -> "MISSING_FROM_DATE",
    "reason" -> "Submission has not passed validation. Required query parameter fromDate has not been supplied."
  )
  val missingToDateResponse: JsObject = Json.obj(
    "code" -> "MISSING_TO_DATE",
    "reason" -> "Submission has not passed validation. Required query parameter toDate has not been supplied."
  )
  val toDateNotInRangeResponse: JsObject = Json.obj(
    "code" -> "TO_DATE_NOT_IN_RANGE",
    "reason" -> "The remote endpoint has indicated To Date must be greater than date from."
  )
  val fromDateNotInRangeResponse: JsObject = Json.obj(
    "code" -> "FROM_DATE_NOT_IN_RANGE",
    "reason" -> "The remote endpoint has indicated From Date cannot be in the future."
  )
  val missingReportTypeResponse: JsObject = Json.obj(
    "code" -> "MISSING_REPORT_TYPE",
    "reason" -> "Submission has not passed validation. Required query parameter reportType has not been supplied."
  )
  val mandatoryStartDateResponse: JsObject = Json.obj(
    "code" -> "PERIOD_START_DATE_MANDATORY",
    "reason" -> "The remote endpoint has indicated that Period Start Date must be provided."
  )
  val internalServerErrorResponse: JsObject = Json.obj(
    "code" -> "SERVER_ERROR",
    "reason" -> "IF is currently experiencing problems that require live service intervention."
  )


  def defaultVersions(startDate: String): JsArray =
    Json.arr(
      Json.obj(
        "reportFormBundleNumber" -> "123456789012",
        "reportVersion" -> 1,
        "reportStatus" -> "Compiled",
        "compilationOrSubmissionDate" -> s"${startDate}T09:30:47Z",
        "reportSubmitterDetails" -> Json.obj(
          "reportSubmittedBy" -> "PSP",
          "orgOrPartnershipDetails" -> Json.obj(
            "orgOrPartnershipName" -> "ABC Limited"
          )
        ),
        "psaDetails" -> Json.obj(
          "psaOrgOrPartnershipDetails" -> Json.obj(
            "orgOrPartnershipName" -> "XYZ Limited"
          )
        )
      )
    )

  private def defaultGetEvent1823(pstr: String, eventType: String, version: String, startDate: String): JsValue = Json.parse(
    s"""
       |{
       |  "success": {
       |    "headerDetails": {
       |      "processingDate": "2023-12-15T12:30:46Z"
       |    },
       |    "schemeDetails": {
       |      "pSTR": "$pstr",
       |      "schemeName": "Abc Ltd"
       |    },
       |    "eventReportDetails": {
       |      "fbNumber": "123456789012",
       |      "reportStartDate": "$startDate",
       |      "reportEndDate": "2024-04-05",
       |      "reportStatus": "Compiled",
       |      "reportVersion": "$version",
       |      "reportSubmittedDateAndTime": "2023-12-13T12:12:12Z"
       |    },
       |    "eventDetails": [
       |      {
       |        "memberDetails": {
       |          "eventType": "$eventType",
       |          "amendedVersion": "001",
       |          "memberStatus": "New",
       |          "title": "0001",
       |          "firstName": "John",
       |          "middleName": "S",
       |          "lastName": "Smith",
       |          "ninoRef": "AS123456A"
       |        },
       |        "paymentDetails": {
       |          "amountPaidBenefitLumpsum": 100.34,
       |          "eventDateOrTaxYear": "2023-04-13"
       |        },
       |        "personReceivedThePayment": {
       |          "title": "0001",
       |          "firstName": "Andrew",
       |          "middleName": "D",
       |          "lastName": "Collin",
       |          "ninoRef": "AS123456A"
       |        }
       |      }
       |    ]
       |  }
       |}
       |
       |""".stripMargin)

  private def defaultGetEvent1833(pstr: String, version: String, startDate: String): JsValue = Json.parse(
    s"""
       | {
       |   "processingDate": "2023-12-15T12:30:46Z",
       |   "schemeDetails": {
       |     "pSTR": $pstr,
       |     "schemeName": "Abc Ltd"
       |   },
       |   "er1Details": {
       |     "reportStartDate": $startDate,
       |     "reportEndDate": "2022-04-05",
       |     "reportVersionNumber": $version,
       |     "reportSubmittedDateAndTime": "2023-12-13T12:12:12Z"
       |   },
       |   "schemeMasterTrustDetails": {
       |     "startDate": "2021-06-08"
       |   },
       |   "erDeclarationDetails": {
       |     "submittedBy": "PSP",
       |     "submittedID": "20000001",
       |     "submittedName": "ABCDEFGHIJKLMNOPQRSTUV",
       |     "pspDeclaration": {
       |     "authorisedPSAID": "A4045157",
       |     "pspDeclaration1": "Selected",
       |     "pspDeclaration2": "Selected"
       |   }
       |   }
       |
       |}""".stripMargin)
}

