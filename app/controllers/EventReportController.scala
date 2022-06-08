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

import java.time.LocalDate
import javax.inject.Singleton

@Singleton()
class EventReportController @Inject()(
                                       cc: ControllerComponents
                                     ) extends BackendController(cc) with APIResponses {

  def compileEventReportSummary(pstr: String): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson match {
        case Some(_) => Future.successful(Ok(createCompileEventReportSummarySuccessResponse))
        case _ => Future.successful(BadRequest(invalidPayload))
      }
  }

  def getEROverview(pstr: String, startDate: String, endDate: String): Action[AnyContent] = Action.async {

    def defaultOverview = {
      Json.arr(
        Json.obj(
          "periodStartDate" -> startDate,
          "periodEndDate" -> endDate,
          "numberOfVersions" -> 1,
          "submittedVersionAvailable" -> "No",
          "compiledVersionAvailable" -> "Yes"
        )
      )
    }

    val path = "conf/resources/data/getOverview"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN", "00000042IN")
    val erPerfTestPstrPattern: String = """^34000[0-9]{3}IN$"""

    if (notFoundPSTR.contains(pstr) || pstr.matches(erPerfTestPstrPattern))
      Future.successful(NotFound(NotFoundResponse))
    else {
      val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr.json")
        .getOrElse(defaultOverview)

      Future.successful(Ok(filterOverview(jsValue, startDate, endDate)))
    }
  }

  def getER20AOverview(pstr: String, startDate: String, endDate: String): Action[AnyContent] = Action.async {

    def defaultOverview = {
      Json.arr(
        Json.obj(
          "periodStartDate" -> startDate,
          "periodEndDate" -> endDate,
          "numberOfVersions" -> 1,
          "submittedVersionAvailable" -> "No",
          "compiledVersionAvailable" -> "Yes"
        )
      )
    }

    val path = "conf/resources/data/getOverview"
    val notFoundPSTR = Seq("24000001IN", "24000007IN", "24000006IN", "24000002IN", "00000042IN")
    val erPerfTestPstrPattern: String = """^34000[0-9]{3}IN$"""

    if (notFoundPSTR.contains(pstr) || pstr.matches(erPerfTestPstrPattern))
      Future.successful(NotFound(NotFoundResponse))
    else {
      val jsValue = jsonUtils.readJsonIfFileFound(s"$path/$pstr.json")
        .getOrElse(defaultOverview)

      Future.successful(Ok(filterOverview(jsValue, startDate, endDate)))
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

