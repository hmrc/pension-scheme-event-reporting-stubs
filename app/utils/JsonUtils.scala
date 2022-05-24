/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package utils

import com.google.inject.Inject
import play.api.Environment
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.NotFoundException

import java.io.{FileInputStream, InputStream}
import scala.io.Source

class JsonUtils @Inject()(environment: Environment) {

  def readJsonFromFile(filePath: String): JsValue = {
    val jsonSchemaFile = environment.getExistingFile(filePath)
    jsonSchemaFile match {
      case Some(schemaFile) =>
        val inputStream = new FileInputStream(schemaFile)
        Json.parse(inputStream)
      case _ =>
        throw new NotFoundException("No Response file found: " + filePath)
    }
  }
}
