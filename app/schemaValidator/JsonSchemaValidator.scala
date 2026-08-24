/*
 * Copyright 2026 HM Revenue & Customs
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

package schemaValidator

import com.networknt.schema.*
import com.networknt.schema.SpecVersion.VersionFlag
import com.networknt.schema.oas.OpenApi30
import play.api.libs.json.JsValue

import java.nio.file.Paths
import scala.compat.java8.FunctionConverters.asJavaConsumer
import scala.jdk.CollectionConverters.CollectionHasAsScala

class JsonSchemaValidator {

  private lazy val schema1832SchemaPath: String =
    s"file:///${Paths.get("conf/resources.schemas").toAbsolutePath}/api-1832-get-member-event-report-response-schema-v1.2.2.json"

  private lazy val config: SchemaValidatorsConfig =
    SchemaValidatorsConfig
      .builder
      .discriminatorKeywordEnabled(false)
      .typeLoose(true)
      .build

  private lazy val factory: JsonSchemaFactory =
    JsonSchemaFactory.getInstance(
      VersionFlag.V202012,
      builder => builder
        .metaSchema(OpenApi30.getInstance())
        .defaultMetaSchemaIri(OpenApi30.getInstance().getIri)
    )

  lazy val api1832ResponseSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(schema1832SchemaPath), config)

  private lazy val executionConfig: ExecutionContext => Unit = {
    (executionContext: ExecutionContext) =>
      executionContext.getExecutionConfig.setDebugEnabled(false)
      executionContext.getExecutionConfig.setFailFast(false)
  }

  def validateJson(data: JsValue, schema: JsonSchema): List[String] =
    schema
      .validate(data.toString, InputFormat.JSON, asJavaConsumer[ExecutionContext](executionConfig))
      .asScala
      .toList
      .map(_.getMessage)
}
