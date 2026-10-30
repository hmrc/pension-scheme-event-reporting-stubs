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

package utils

import com.google.inject.Inject
import play.api.Environment
import play.api.libs.json.{JsValue, Json}

import java.io.{FileInputStream, InputStream}
import scala.io.Source

class JsonUtils @Inject()(environment: Environment) {

  def readJsonIfFileFound(
                           filePath: String,
                           optionPstr: Option[String] = None
                         ): Option[JsValue] =
    environment.getExistingFile(filePath) match {
      case Some(schemaFile) =>
        val inputStream: FileInputStream =
          new FileInputStream(schemaFile)
        val jsonString: String =
          DateHelper.replacePlaceholderJson(readStreamToString(inputStream))

        optionPstr match {
          case Some(pstr) =>
            Some(Json.parse(jsonString.replace("<PSTR>", pstr)))
          case _ =>
            Some(Json.parse(jsonString))
        }
      case _ =>
        None
    }

  private def readStreamToString(is: InputStream): String =
    try Source.fromInputStream(is).mkString
    finally is.close()
}
