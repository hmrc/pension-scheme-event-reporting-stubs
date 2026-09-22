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

class HipEpidValidator {

  private lazy val epidBasePath: String =
    s"file:///${Paths.get("conf/epid/hip").toAbsolutePath}/EPID1822_Pension_Online_Events_openapi_v0.1.2.yaml#/components/schemas".replace(" ", "%20")

  private lazy val api1826RequestPath: String =
    s"$epidBasePath/API1826_request"

  private lazy val api1827RequestPath: String =
    s"$epidBasePath/API1827_request"

  private lazy val api1828RequestPath: String =
    s"$epidBasePath/API1828_request"

  private lazy val api1829RequestPath: String =
    s"$epidBasePath/API1829_request"

  private lazy val api1830RequestPath: String =
    s"$epidBasePath/API1830_request"

  private lazy val api1831RequestPath: String =
    s"$epidBasePath/API1831_request"

  private lazy val api1831ResponsePath: String =
    s"$epidBasePath/API1831_get_RESTAdapter_pension_online_event20a_status_reports_pstr__SuccessResponse"

  private lazy val api1832RequestPath: String =
    s"$epidBasePath/API1832_request"

  private lazy val api1832ResponsePath: String =
    s"$epidBasePath/API1832_get_RESTAdapter_pension_online_member_event_status_reports_pstr__SuccessResponse"

  private lazy val api1833RequestPath: String =
    s"$epidBasePath/API1833_request"

  private lazy val api1833ResponsePath: String =
    s"$epidBasePath/API1833_get_RESTAdapter_pension_online_event1_status_reports_pstr__SuccessResponse"

  private lazy val api1834RequestPath: String =
    s"$epidBasePath/API1834_request"

  private lazy val api1834ResponsePath: String =
    s"$epidBasePath/API1834_get_RESTAdapter_pension_online_event_status_reports_pstr__SuccessResponse"

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

  lazy val api1826RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1826RequestPath), config)

  lazy val api1827RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1827RequestPath), config)

  lazy val api1828RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1828RequestPath), config)

  lazy val api1829RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1829RequestPath), config)

  lazy val api1830RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1830RequestPath), config)

  lazy val api1831RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1831RequestPath), config)

  lazy val api1831ResponseSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1831ResponsePath), config)

  lazy val api1832RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1832RequestPath), config)

  lazy val api1832ResponseSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1832ResponsePath), config)

  lazy val api1833RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1833RequestPath), config)

  lazy val api1833ResponseSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1833ResponsePath), config)

  lazy val api1834RequestSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1834RequestPath), config)

  lazy val api1834ResponseSchema: JsonSchema =
    factory.getSchema(SchemaLocation.of(api1834ResponsePath), config)

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
