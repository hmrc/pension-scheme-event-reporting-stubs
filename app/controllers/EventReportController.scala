/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import com.google.inject.Inject
import config.AppConfig
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import schemaValidator.SchemaValidator
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.{APIResponses, JsonUtils}

import scala.concurrent.Future

class EventReportController @Inject()(
                                       schemaValidator: SchemaValidator,
                                       appConfig: AppConfig,
                                       jsonUtils: JsonUtils,
                                       cc: ControllerComponents
                                     ) extends BackendController(cc) with APIResponses {

  def compileEventReportSummary(pstr: String): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson match {
        case Some(_) => Future.successful(Ok(createCompileEventReportSummarySuccessResponse))
        case _ => Future.successful(BadRequest(invalidPayload))
      }
  }
}

