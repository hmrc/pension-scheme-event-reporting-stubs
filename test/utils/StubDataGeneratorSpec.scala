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


import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StubDataGeneratorSpec extends AnyWordSpec with Matchers {

  "generateEvent22Or23SummaryJson" must {
    "generate a payload of length 2 for event 22" in {
      (StubDataGenerator.generateEvent22Or23SummaryJson("event22", 2, 2022) \\ "membersDetails").length mustBe  2
    }
  }

  "generateEvent6SummaryJson" must {
    "generate a payload of length 5 for event 6" in {
      (StubDataGenerator.generateEvent6SummaryJson("event6", 5, 2022) \\  "AmountCrystallisedAndDate").length mustBe 5

    }
  }
  "generateEvent6SummaryCsv" must {
    "generate headers for event 6 csv" in {
      val event6Payload: String = StubDataGenerator.generateEvent6SummaryCsv(2, 2022)
      event6Payload must startWith("First name," +
        "Last name," +
        "National Insurance number," +
        "Type of protection held for the crystallisation (see LTA certificate)," +
        "Protection type reference,Total amount crystallised (£)," +
        "Date the benefit was crystallised (XX/XX/XXXX)")

    }
  }
  "generateEvent22SummaryCsv" must {
    "generate headers for event 22 csv" in {
      val event22Payload: String = StubDataGenerator.generateEvent22SummaryCsv(5, 2022)
      event22Payload must startWith("First name," +
        "Last name," +
        "National Insurance number," +
        "In which tax year was the annual allowance exceeded? (XXXX to XXXX)," +
        "What is the total of the member's pension input amounts for all arrangements under the scheme in the tax year that the annual allowance was exceeded? (£)\n")
    }
  }

  "generateEvent23SummaryCsv" must {
    "generate headers for event 23 csv" in {
      val event23Payload: String = StubDataGenerator.generateEvent23SummaryCsv(5, 2022)
      event23Payload must startWith("First name," +
        "Last name," +
        "National Insurance number," +
        "In which tax year was the money purchase pension savings statement issued? (XXXX to XXXX)," +
        "What is the total of the member’s pension input amounts for money purchase arrangements under the scheme for the tax year that the purchase pension savings statement was issued? (£)\n")

    }
  }

}
