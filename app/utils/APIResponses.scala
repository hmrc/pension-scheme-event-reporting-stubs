/*
 * Copyright 2022 HM Revenue & Customs
 *
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
}
