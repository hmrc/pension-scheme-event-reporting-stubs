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

package utils

import play.api.libs.json.{JsObject, Json}

import java.time.LocalDate

trait APIResponses {

  val invalidPayload: JsObject = Json.obj(
    "code" -> "INVALID_PAYLOAD",
    "reason" -> "Submission has not passed validation. Invalid PAYLOAD"
  )

  val invalidPSTR: JsObject = Json.obj(
    "code" -> "INVALID_PSTR",
    "reason" -> "Submission has not passed validation. Invalid parameter PSTR"
  )

  val serviceUnavailable: JsObject = Json.obj(
    "code" -> "SERVICE_UNAVAILABLE",
    "reason" -> "Dependent systems are currently not responding."
  )

  val duplicateSubmission: JsObject = Json.obj(
    "code" -> "DUPLICATE_SUBMISSION",
    "reason" -> "The remote endpoint has indicated that duplicate submission"
  )

  val notFound: JsObject = Json.obj(
    "code" -> "NOT_FOUND",
    "reason" -> "Resource not found."
  )

  val invalidCorrelationId: JsObject = Json.obj(
    "code" -> "INVALID_CORRELATIONID",
    "reason" -> "Submission has not passed validation. Invalid header CorrelationId."
  )

  val createCompileEventReportSummarySuccessResponse: JsObject = Json.obj(
    "processingDate" -> LocalDate.now(),
     "formBundleNumber" -> "12345678912"
  )

  val compileEventOneReportSuccessResponse: JsObject = Json.obj(
    "processingDate" -> LocalDate.now(),
    "formBundleNumber" -> "12345678988"
  )

  val submitEventDeclarationReportSuccessResponse: JsObject = Json.obj(
    "processingDate" -> LocalDate.now(),
    "formBundleNumber" -> "12345678933"
  )

}
