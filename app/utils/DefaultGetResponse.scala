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

import play.api.libs.json.{JsArray, JsValue, Json}

object DefaultGetResponse {

  def defaultVersions(startDate: String): JsArray =
    Json.arr(
      Json.obj(
        "reportFormBundleNumber" -> "123456789012",
        "reportVersion" -> 1,
        "reportStatus" -> "Compiled",
        "compilationOrSubmissionDate" -> s"${startDate}T09:30:47Z",
        "reportSubmitterDetails" -> Json.obj(
          "reportSubmittedBy" -> "PSP",
          "orgOrPartnershipDetails" -> Json.obj(
            "orgOrPartnershipName" -> "ABC Limited"
          )
        ),
        "psaDetails" -> Json.obj(
          "psaOrgOrPartnershipDetails" -> Json.obj(
            "orgOrPartnershipName" -> "XYZ Limited"
          )
        )
      )
    )

  def defaultGetEvent1832(pstr: String, eventType: String, version: String, startDate: String): JsValue = Json.parse(
    s"""
       |{
       |  "success": {
       |    "headerDetails": {
       |      "processingDate": "2023-12-15T12:30:46Z"
       |    },
       |    "schemeDetails": {
       |      "pSTR": "$pstr",
       |      "schemeName": "Abc Ltd"
       |    },
       |    "eventReportDetails": {
       |      "fbNumber": "123456789012",
       |      "reportStartDate": "$startDate",
       |      "reportEndDate": "2024-04-05",
       |      "reportStatus": "Compiled",
       |      "reportVersion": "$version",
       |      "reportSubmittedDateAndTime": "2023-12-13T12:12:12Z"
       |    },
       |    "eventDetails": [
       |      {
       |        "memberDetails": {
       |          "eventType": "$eventType",
       |          "amendedVersion": "001",
       |          "memberStatus": "New",
       |          "title": "0001",
       |          "firstName": "John",
       |          "middleName": "S",
       |          "lastName": "Smith",
       |          "ninoRef": "AS123456A"
       |        },
       |        "paymentDetails": {
       |          "amountPaidBenefitLumpsum": 100.34,
       |          "eventDateOrTaxYear": "2023-04-13"
       |        },
       |        "personReceivedThePayment": {
       |          "title": "0001",
       |          "firstName": "Andrew",
       |          "middleName": "D",
       |          "lastName": "Collin",
       |          "ninoRef": "AS123456A"
       |        }
       |      }
       |    ]
       |  }
       |}
       |
       |""".stripMargin)

  def defaultGetEvent1833(pstr: String, version: String, startDate: String): JsValue = Json.parse(
    s"""
       | {
       |   "processingDate": "2023-12-15T12:30:46Z",
       |   "schemeDetails": {
       |     "pSTR": "$pstr",
       |     "schemeName": "Abc Ltd"
       |   },
       |   "er1Details": {
       |     "reportStartDate": "$startDate",
       |     "reportEndDate": "2022-04-05",
       |     "reportVersionNumber": "$version",
       |     "reportSubmittedDateAndTime": "2023-12-13T12:12:12Z"
       |   },
       |   "schemeMasterTrustDetails": {
       |     "startDate": "2021-06-08"
       |   },
       |   "erDeclarationDetails": {
       |     "submittedBy": "PSP",
       |     "submittedID": "20000001",
       |     "submittedName": "ABCDEFGHIJKLMNOPQRSTUV",
       |     "pspDeclaration": {
       |     "authorisedPSAID": "A4045157",
       |     "pspDeclaration1": "Selected",
       |     "pspDeclaration2": "Selected"
       |   }
       |   }
       |
       |}""".stripMargin)

  def defaultGetEvent1831(pstr: String, version: String, startDate: String): JsValue = Json.parse(
    s"""
       |{
       |  "processingDate": "2021-08-19T23:00:00.0Z",
       |  "schemeDetails": {
       |    "pSTR": "$pstr",
       |    "schemeName": "Test Scheme"
       |  },
       |  "er20aDetails": {
       |    "periodStartDate": "$startDate",
       |    "periodEndDate": "2021-12-31",
       |    "reportVersionNumber": "$version",
       |    "reportSubmittedDateAndTime": "2021-10-05T22:10:09Z"
       |  },
       |  "schemeMasterTrustDetails": {
       |    "startDate": "2021-01-21"
       |  },
       |  "erDeclarationDetails": {
       |    "submittedBy": "PSA",
       |    "submittedID": "eP64hipm",
       |    "submittedName": "laborum deserunt ad al",
       |    "pspDeclaration": {
       |      "authorisedPSAID": "A9564957",
       |      "pspDeclaration1": "Not Selected",
       |      "pspDeclaration2": "Selected"
       |    },
       |    "psaDeclaration": {
       |      "psaDeclaration1": "Not Selected",
       |      "psaDeclaration2": "Selected"
       |    }
       |  }
       |}
       |
       |""".stripMargin)
}
