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

package controllers

import com.google.inject.Inject
import controllers.HipEventReportController.*
import play.api.Logging
import play.api.libs.json.*
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import schemaValidator.HipEpidValidator
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.{APIResponses, JsonUtils, PstrIDs}

import java.time.LocalDate
import javax.inject.Singleton
import scala.annotation.unused

@Singleton()
class HipEventReportController @Inject()(
                                          cc: ControllerComponents,
                                          jsonUtils: JsonUtils,
                                          hipEpidValidator: HipEpidValidator
                                     ) 
  extends BackendController(cc)
    with APIResponses
    with Logging {

  def compileEventReportSummary(@unused pstr: String): Action[AnyContent] = Action {
    implicit request =>
      request.body.asJson match {
        case Some(jsValue) =>
          hipEpidValidator.validateJson(jsValue, hipEpidValidator.api1826RequestSchema) match {
            case errors if errors.isEmpty =>
              Ok(Json.obj("success" -> createCompileEventReportSummarySuccessResponse))
            case errors =>
              logger.error(s"\n\n\n\nHIP #1826 validation errors: \n${errors.mkString("\n")}\n\n\n")
              BadRequest(errors.mkString("\n"))
          }
        case _ =>
          BadRequest(invalidPayload)
      }
  }

  def compileEventOneReport(@unused pstr: String): Action[AnyContent] = Action {
    implicit request =>
      request.body.asJson match {
        case Some(jsValue) =>
          hipEpidValidator.validateJson(jsValue, hipEpidValidator.api1827RequestSchema) match {
            case errors if errors.isEmpty =>
              Ok(Json.obj("successes" -> compileEventOneReportSuccessResponse))
            case errors =>
              logger.error(s"\n\n\n\nHIP #1827 validation errors: \n${errors.mkString("\n")}\n\n\n")
              BadRequest(errors.mkString("\n"))
          }
        case _ =>
          BadRequest(invalidPayload)
      }
  }

  def compileMemberEventReport(@unused pstr: String): Action[AnyContent] = Action {
    implicit request =>
      request.body.asJson match {
        case Some(jsValue) =>
          hipEpidValidator.validateJson(jsValue, hipEpidValidator.api1830RequestSchema) match {
            case errors if errors.isEmpty =>
              Ok(Json.obj("success" -> compileMemberEventReportSuccessResponse))
            case errors =>
              logger.error(s"\n\n\n\nHIP #1830 validation errors: \n${errors.mkString("\n")}\n\n\n")
              BadRequest(errors.mkString("\n"))
          }
        case _ =>
          BadRequest(invalidPayload)
      }
  }

  def getOverview(pstr: String, fromDate: String, toDate: String, reportType: String): Action[AnyContent] = Action {

    val notFoundPSTR = Seq("24000007IN", "24000006IN", "24000002IN", "00000042IN")
    val datePattern: String = "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-](0[1-9]|1[0-9]|2[0-8])))$"

    if (reportType.isEmpty) {
      BadRequest(missingReportTypeResponse)
    }
    else if (fromDate.isEmpty) {
      BadRequest(missingFromDateResponse)
    }
    else if (toDate.isEmpty) {
      BadRequest(missingToDateResponse)
    }
    else if (notFoundPSTR.contains(pstr) || pstr.matches("""^34000[0-9]{3}IN$""")) {
      BadRequest(invalidPstrResponse)
    }
    else if (!fromDate.matches(datePattern)) {
      BadRequest(invalidFromDateResponse)
    }
    else if (!toDate.matches(datePattern)) {
      BadRequest(invalidToDateResponse)
    }
    else if (LocalDate.parse(toDate).isBefore(LocalDate.parse(fromDate))) {
      BadRequest(toDateNotInRangeResponse)
    }
    else if (LocalDate.parse(fromDate).isAfter(LocalDate.now())) {
      BadRequest(fromDateNotInRangeResponse)
    }
    else {
      val jsValue: JsValue =
        jsonUtils
          .readJsonIfFileFound(s"conf/resources/data/getOverview/$pstr.json")
          .getOrElse(defaultOverview(fromDate, toDate))

      Ok(filterOverview(jsValue, fromDate, toDate, reportType))
    }
  }

  def submitEventDeclarationReport(@unused pstr: String): Action[AnyContent] = Action {
    implicit request =>
      request.body.asJson match {
        case Some(jsValue) =>
          hipEpidValidator.validateJson(jsValue, hipEpidValidator.api1828RequestSchema) match {
            case errors if errors.isEmpty =>
              Ok(Json.obj("success" -> submitEventDeclarationReportSuccessResponse))
            case errors =>
              logger.error(s"\n\n\n\nHIP #1828 validation errors: \n${errors.mkString("\n")}\n\n\n")
              BadRequest(errors.mkString("\n"))
          }
        case _ =>
          BadRequest(invalidPayload)
      }
  }

  def submitEvent20ADeclarationReport(@unused pstr: String): Action[AnyContent] = Action {
    implicit request =>
      request.body.asJson match {
        case Some(jsValue) =>
          hipEpidValidator.validateJson(jsValue, hipEpidValidator.api1829RequestSchema) match {
            case errors if errors.isEmpty =>
              Ok(Json.obj("success" -> submitEvent20ADeclarationReportSuccessResponse))
            case errors =>
              logger.error(s"\n\n\n\nHIP #1829 validation errors: \n${errors.mkString("\n")}\n\n\n")
              BadRequest(errors.mkString("\n"))
          }
        case _ =>
          BadRequest(invalidPayload)
      }
  }

  def getERVersions(pstr: String, startDate: String): Action[AnyContent] = Action {

    val notFoundPSTR = Seq("24000007IN", "24000006IN", "24000002IN")
    if (pstr.isEmpty || startDate.isEmpty) {
      Forbidden(invalidRequestResponse)
    } else if (!startDate.matches(datePattern)) {
      BadRequest(invalidStartDateResponse)
    } else if (notFoundPSTR.contains(pstr) || pstr.matches("""^34000[0-9]{3}IN$"""))
      NotFound(invalidPstrResponse)
    else {
      jsonUtils.readJsonIfFileFound(s"conf/resources/data/getVersions/$pstr/$startDate.json") match {
        case Some(jsValue) => 
          Ok(jsValue)
        case None =>
          NotFound(noDataErrorResponse)
      }
    }
  }

  def getER20AVersions(pstr: String, startDate: String): Action[AnyContent] = Action {
    val notFoundPSTR = Seq("24000007IN", "24000006IN", "24000002IN")
    if (pstr.isEmpty || startDate.isEmpty) {
      Forbidden(invalidRequestResponse)
    } else if (!startDate.matches(datePattern)) {
      BadRequest(invalidStartDateResponse)
    } else if (notFoundPSTR.contains(pstr) || pstr.matches("""^34000[0-9]{3}IN$"""))
      NotFound(invalidPstrResponse)
    else {
      val jsValue: JsValue =
        jsonUtils
          .readJsonIfFileFound(s"conf/resources/data/getVersions/$pstr/$startDate.json")
          .getOrElse(Json.parse("[{}]"))

      Ok(jsValue)
    }
  }

  def api1832GET(pstr: String): Action[AnyContent] = Action {
    implicit request =>
      (
        request.headers.get("eventType"),
        request.headers.get("reportVersionNumber"),
        request.headers.get("reportStartDate")
      ) match {
        case (Some(eventType), _, _) if Set("Event2", "Event3", "Event4", "Event5", "Event6", "Event7", "Event8", "Event8A", "Event22", "Event23", "Event24").contains(eventType) =>
          if (pstr == "24000041IN") {
            jsonUtils.readJsonIfFileFound(s"conf/resources/data/api1832/${pstr}_$eventType.json") match {
              case Some(jsValue) =>
                hipEpidValidator.validateJson(Json.obj("success" -> jsValue), hipEpidValidator.api1832ResponseSchema) match {
                  case errors if errors.isEmpty =>
                    Ok(Json.obj("success" -> jsValue))
                  case errors =>
                    logger.error(s"\n\n\n\nHIP #1832 validation errors for $eventType: \n${errors.mkString("\n\n")}\n\n\n")
                    BadRequest(errors.mkString("\n"))
                }
              case None =>
                NotFound(invalidPstrResponse)
            }
          } else {
            UnprocessableEntity(reportNotFoundResponse)
          }
        case (Some(_), Some(_), Some(_)) =>
          UnprocessableEntity(reportNotFoundResponse)
        case (None, _, _) =>
          BadRequest(invalidEventTypeResponse)
        case (_, None, _) =>
          BadRequest(invalidVersionResponse)
        case _ =>
          BadRequest(invalidStartDateResponse)
      }
  }

  def api1833GET(pstr: String): Action[AnyContent] = Action {
    implicit request =>
      
      val notFoundPSTR = Seq("24000007IN", "24000006IN", "24000002IN")
      
      (
        request.headers.get("reportVersionNumber"),
        request.headers.get("reportStartDate")
      ) match {
        case (Some(_), Some(_)) =>
          if (notFoundPSTR.contains(pstr) || pstr.matches(perfTestPstrPattern))
            NotFound(invalidPstrResponse)
          else {
            jsonUtils.readJsonIfFileFound(s"conf/resources/data/api1833/$pstr.json") match {
              case Some(jsValue) =>
                hipEpidValidator.validateJson(Json.obj("success" -> jsValue), hipEpidValidator.api1833ResponseSchema) match {
                  case errors if errors.isEmpty =>
                    Ok(Json.obj("success" -> jsValue))
                  case errors =>
                    logger.error(s"\n\n\n\nHIP #1833 validation errors: \n${errors.mkString("\n")}\n\n\n")
                    BadRequest(errors.mkString("\n"))
                }
              case None =>
                NotFound(invalidPstrResponse)
            }
          }
        case (None, _) =>
          BadRequest(invalidVersionResponse)
        case (_, None) =>
          BadRequest(invalidStartDateResponse)
        case _ =>
          InternalServerError(internalServerErrorResponse)
      }
  }

  def api1834GET(pstr: String): Action[AnyContent] = Action { implicit request =>
    val path = "conf/resources/data/api1834"
    val notFoundPSTR = Seq("24000007IN", "24000006IN", "24000002IN")

    (
      request.headers.get("reportVersionNumber"),
      request.headers.get("reportStartDate")
    ) match {
      case (Some(version), Some(startDate)) =>
        if (notFoundPSTR.contains(pstr) || pstr.matches(perfTestPstrPattern))
          NotFound(invalidPstrResponse)
        else {
          jsonUtils.readJsonIfFileFound(s"$path/$pstr-${startDate.take(4)}-$version.json") match {
            case None =>
              NotFound
            case Some(jsValue) =>
              hipEpidValidator.validateJson(Json.obj("success" -> jsValue), hipEpidValidator.api1834ResponseSchema) match {
                case errors if errors.isEmpty =>
                  Ok(Json.obj("success" -> jsValue))
                case errors =>
                  logger.error(s"\n\n\n\nHIP #1834 validation errors: \n${errors.mkString("\n")}\n\n\n")
                  BadRequest(errors.mkString("\n"))
              }
          }
        }
      case (None, _) =>
        BadRequest(invalidVersionResponse)
      case (_, None) =>
        BadRequest(invalidStartDateResponse)
      case _ =>
        InternalServerError(internalServerErrorResponse)
    }
  }

  def api1831GET(pstr: String): Action[AnyContent] = Action {
    implicit request =>
      (
        request.headers.get("reportVersionNumber"),
        request.headers.get("reportStartDate"),
        request.headers.get("reportFormBundleNumber")
      ) match {
        case (Some(_), Some(_), None) | (None, None, Some(_)) =>
          eventResponseByPstr(pstr, "conf/resources/data/api1831")
        case (None, _, _) =>
          BadRequest(invalidVersionResponse)
        case _ =>
          BadRequest(invalidStartDateResponse)
      }
  }

  private def eventResponseByPstr(pstr: String, path: String): Result =
    pstr match {
      case PstrIDs.INTERNAL_SERVER_ERROR =>
        InternalServerError(serverError)
      case PstrIDs.SERVICE_UNAVAILABLE =>
        ServiceUnavailable(serviceUnavailable)
      case PstrIDs.DUPLICATE_SUBMISSION =>
        Conflict(duplicateSubmission)
      case PstrIDs.INVALID_PAYLOAD =>
        BadRequest(invalidPayload)
      case PstrIDs.REQUEST_NOT_PROCESSED =>
        UnprocessableEntity(unprocessableEntity)
      case value if value.matches("""^34000[0-9]{3}IN$""") =>
        BadRequest(invalidPstrResponse)
      case _ =>
        jsonUtils.readJsonIfFileFound(s"$path/$pstr.json") match {
          case Some(jsValue) =>
            hipEpidValidator.validateJson(Json.obj("success" -> jsValue), hipEpidValidator.api1831ResponseSchema) match {
              case errors if errors.isEmpty =>
                Ok(Json.obj("success" -> jsValue))
              case errors =>
                logger.error(s"\n\n\n\nHIP #1826 validation errors: \n${errors.mkString("\n")}\n\n\n")
                BadRequest(errors.mkString("\n"))
            }
          case None =>
            NotFound(invalidPstrResponse)
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

  private def filterOverview(jsValue: JsValue, startDate: String, endDate: String, @unused reportType: String): JsValue = {
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

object HipEventReportController {
  private val datePattern: String = "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-](0[1-9]|1[0-9]|2[0-8])))$"
  private val perfTestPstrPattern: String = """^34000[0-9]{3}IN$"""

  val invalidPstrResponse: JsObject = Json.obj(
    "code" -> "INVALID_PSTR",
    "reason" -> "Submission has not passed validation. Invalid parameter pstr."
  )

  private val invalidEventTypeResponse: JsObject = Json.obj(
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
  private val reportNotFoundResponse: JsObject = Json.obj(
    "code" -> "REPORT_NOT_FOUND",
    "reason" -> "Report not found"
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
  val invalidRequestResponse: JsObject = Json.obj(
    "code" -> "INVALID_REQUEST",
    "reason" -> "The remote endpoint has indicated that the request is invalid."
  )
  private val internalServerErrorResponse: JsObject = Json.obj(
    "code" -> "SERVER_ERROR",
    "reason" -> "IF is currently experiencing problems that require live service intervention."
  )

  private val noDataErrorResponse: JsObject = Json.obj(
    "code" -> "NO_DATA_FOUND",
    "reason" -> "The remote endpoint has indicated that no scheme report was found for the given period"
  )
}
