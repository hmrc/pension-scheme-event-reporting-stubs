/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package schemaValidator

import com.google.inject.Inject
import play.api.Environment
import play.api.libs.json.{JsValue, Json}

class SchemaValidator @Inject()(environment: Environment) {

  def validateJson(jsonSchemaPath: String, data: JsValue): Boolean = {
    val jsonSchemaFile = environment.getExistingFile(jsonSchemaPath)
    jsonSchemaFile match{
      case Some(schemaFile) =>
        val factory = JsonSchemaFactory.byDefault.getJsonSchema(schemaFile.toURI.toString)
        val json = JsonLoader.fromString(Json.stringify(data))
        factory.validate(json).isSuccess
      case _ =>
        throw new RuntimeException("No Schema found")
    }
  }
}

