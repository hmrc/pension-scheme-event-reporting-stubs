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
import controllers.EventReportController.{InvalidFromDateResponse, InvalidPstrResponse, InvalidToDateResponse, missingFromDateResponse, missingToDateResponse}
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

  def getEROverview(pstr: String, fromDate: String, toDate: String): Action[AnyContent] = Action.async {

    def defaultOverview = {
      Json.arr(
        Json.obj(
          "periodStartDate" -> fromDate,
          "periodEndDate" -> toDate,
          "numberOfVersions" -> 1,
          "submittedVersionAvailable" -> "No",
          "compiledVersionAvailable" -> "Yes"
        )
      )
    }

    val path = "conf/resources/data/getOverview"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN", "00000042IN")
    val erPerfTestPstrPattern: String = """^34000[0-9]{3}IN$"""
    val datePattern: String = "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-](0[1-9]|1[0-9]|2[0-8])))$"


    if(fromDate.isEmpty){
      Future.successful(BadRequest(missingFromDateResponse))
    }
    else if (toDate.isEmpty){
      Future.successful(BadRequest(missingToDateResponse))
    }
    else if (notFoundPSTR.contains(pstr) || pstr.matches(erPerfTestPstrPattern)) {
      Future.successful(NotFound(InvalidPstrResponse))
    }
    else if (!fromDate.matches(datePattern)) {
      Future.successful(BadRequest(InvalidFromDateResponse))
    }
    else if (!toDate.matches(datePattern)) {
      Future.successful(BadRequest(InvalidToDateResponse))
    }
    else {
      val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr.json")
        .getOrElse(defaultOverview)

      Future.successful(Ok(filterOverview(jsValue, fromDate, toDate)))
    }
  }

  def getER20AOverview(pstr: String, fromDate: String, toDate: String): Action[AnyContent] = Action.async {

    def defaultOverview = {
      Json.arr(
        Json.obj(
          "periodStartDate" -> fromDate,
          "periodEndDate" -> toDate,
          "numberOfVersions" -> 1,
          "submittedVersionAvailable" -> "No",
          "compiledVersionAvailable" -> "Yes"
        )
      )
    }

    val path = "conf/resources/data/getOverview"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN", "00000042IN")
    val erPerfTestPstrPattern: String = """^34000[0-9]{3}IN$"""
    val datePattern: String = "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-](0[1-9]|1[0-9]|2[0-8])))$"


    if(fromDate.isEmpty){
      Future.successful(BadRequest(missingFromDateResponse))
    }
    else if (toDate.isEmpty){
      Future.successful(BadRequest(missingToDateResponse))
    }
    else if (notFoundPSTR.contains(pstr) || pstr.matches(erPerfTestPstrPattern)) {
      Future.successful(NotFound(InvalidPstrResponse))
    }
    else if (!fromDate.matches(datePattern)) {
      Future.successful(BadRequest(InvalidFromDateResponse))
    }
    else if (!toDate.matches(datePattern)) {
      Future.successful(BadRequest(InvalidToDateResponse))
    }
    else {
      val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr.json")
        .getOrElse(defaultOverview)

      Future.successful(Ok(filterOverview(jsValue, fromDate, toDate)))
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

  private def filterOverview(jsValue: JsValue, startDate: String, endDate: String): JsValue = {
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
  val NoReportFoundResponse: JsObject = Json.obj(
    "code" -> "NO_REPORT_FOUND",
    "reason" -> "The remote endpoint has indicated No Scheme report was found for the given period."
  )
  val InvalidPstrResponse: JsObject = Json.obj(
    "code" -> "INVALID_PSTR",
    "reason" -> "Submission has not passed validation. Invalid parameter pstr."
  )
  val InvalidFromDateResponse: JsObject = Json.obj(
    "code" -> "INVALID_FROM_DATE",
    "reason" -> "Submission has not passed validation. Invalid query parameter fromDate."
  )
  val InvalidToDateResponse: JsObject = Json.obj(
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
}

