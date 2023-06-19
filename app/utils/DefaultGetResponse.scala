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

import play.api.libs.json.{JsValue, Json}

object DefaultGetResponse {

  def defaultVersions(startDate: String): JsValue = Json.parse("{}")

  def defaultGetEvent1832(): JsValue = Json.parse("{}")

  def defaultGetEvent1833(): JsValue = Json.parse("{}")

  def defaultGetEvent1834(pstr: String, version: String, startDate: String): JsValue = {
    Json.parse(s"""
                  |  {
                  |  "schemeDetails": {
                  |    "pSTR": "$pstr",
                  |    "schemeName": "Abc Ltd"
                  |  },
                  |  "eventReportDetails": {
                  |    "reportFormBundleNumber": "123456789012",
                  |    "reportStartDate": "$startDate",
                  |    "reportEndDate": "2022-04-05",
                  |    "reportStatus": "Compiled",
                  |    "reportVersionNumber": "$version",
                  |    "reportSubmittedDateAndTime": "2023-12-13T12:12:12Z"
                  |  },
                  |  "eventDetails": {
                  |    "event10": [
                  |      {
                  |        "recordVersion": "001",
                  |        "invRegScheme": {
                  |          "startDateDetails": {
                  |            "startDateOfInvReg": "2022-01-31",
                  |            "contractsOrPolicies": "Yes"
                  |          }
                  |        }
                  |      }
                  |    ],
                  |    "event11": {
                  |      "recordVersion": "001",
                  |      "unauthorisedPmtsDate": "2022-01-31",
                  |      "contractsOrPoliciesDate": "2022-01-10"
                  |    },
                  |    "event12": {
                  |      "recordVersion": "001",
                  |      "twoOrMoreSchemesDate": "2022-01-02"
                  |    },
                  |    "event13": [
                  |      {
                  |        "recordVersion": "001",
                  |        "schemeStructure": "A single trust under which all of the assets are held for the benefit of all members of the scheme",
                  |        "dateOfChange": "2022-01-02"
                  |      }
                  |    ],
                  |    "event14": {
                  |      "recordVersion": "001",
                  |      "schemeMembers": "12 to 50"
                  |    },
                  |    "event18": {
                  |      "recordVersion": "001",
                  |      "chargeablePmt": "Yes"
                  |    },
                  |    "event19": [
                  |      {
                  |        "recordVersion": "001",
                  |        "countryCode": "GB",
                  |        "dateOfChange": "2022-01-14"
                  |      }
                  |    ],
                  |    "event20": [
                  |      {
                  |        "recordVersion": "001",
                  |        "occSchemeDetails": {
                  |          "startDateOfOccScheme": "2022-01-27"
                  |        }
                  |      }
                  |    ],
                  |    "eventWindUp": {
                  |      "recordVersion": "001",
                  |      "dateOfWindUp": "2022-01-28"
                  |    }
                  |  },
                  |  "memberEventsSummary": {
                  |    "event2": {
                  |      "recordVersion": "001",
                  |      "numberOfMembers": 150000
                  |    },
                  |    "event3": {
                  |      "recordVersion": "002",
                  |      "numberOfMembers": 1000
                  |    },
                  |    "event4": {
                  |      "recordVersion": "001",
                  |      "numberOfMembers": 1000
                  |    },
                  |    "event5": {
                  |      "recordVersion": "004",
                  |      "numberOfMembers": 50000
                  |    },
                  |    "event6": {
                  |      "recordVersion": "007",
                  |      "numberOfMembers": 10000
                  |    },
                  |    "event7": {
                  |      "recordVersion": "002",
                  |      "numberOfMembers": 150000
                  |    },
                  |    "event8": {
                  |      "recordVersion": "004",
                  |      "numberOfMembers": 1500
                  |    },
                  |    "event8A": {
                  |      "recordVersion": "003",
                  |      "numberOfMembers": 150000
                  |    },
                  |    "event22": {
                  |      "recordVersion": "004",
                  |      "numberOfMembers": 10000
                  |    },
                  |    "event23": {
                  |      "recordVersion": "003",
                  |      "numberOfMembers": 150000
                  |    },
                  |    "event24": {
                  |      "recordVersion": "001",
                  |      "numberOfMembers": 150000
                  |    }
                  |  },
                  |  "event1ChargeDetails": {
                  |    "recordVersion": "002",
                  |    "numberOfMembers": 1000,
                  |    "sscCharge": {
                  |      "totalCharge": 10000.66,
                  |      "previousPostedCharge": 67000,
                  |      "deltaCharge": 8099.78,
                  |      "chargeReference": "0123456789012345"
                  |    },
                  |    "fcmtCharge": {
                  |      "totalCharge": 107889.66,
                  |      "previousPostedCharge": 123456,
                  |      "deltaCharge": 1299.78,
                  |      "chargeReference": "0123456789012677"
                  |    }
                  |  }
                  |}""".stripMargin)
  }

  def defaultGetEvent1831(): JsValue = Json.parse("{}")
}
