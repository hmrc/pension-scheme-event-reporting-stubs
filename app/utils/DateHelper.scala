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

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.{IsoFields, TemporalAdjusters}
import scala.util.Random

object DateHelper {

  private def random(from: LocalDate, to: LocalDate): LocalDate = {
    val diff = DAYS.between(from, to)
    val random = new Random(System.nanoTime)
    from.plusDays(random.nextInt(diff.toInt))
  }

  val pastDate: LocalDate = random(LocalDate.of(2020, 4, 1), LocalDate.now())

  val futureDate: LocalDate = random(LocalDate.now(), LocalDate.now().plusYears(1))

  val currentYear: Int = LocalDate.now().getYear

  val taxYearStart: LocalDate = LocalDate.of(currentYear, 4, 6)

  //noinspection ScalaStyle
  private[utils] def dateReplacement(x: String, currentDate: LocalDate): String = {
    x match {
      case "<StartOfCurrentQ>" =>
        firstDayOfQuarter(currentDate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<EndOfCurrentQ>" =>
        lastDayOfQuarter(currentDate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<StartOfPreviousQ>" =>
        firstDayOfPreviousQuarter(currentDate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<EndOfPreviousQ>" =>
        lastDayOfPreviousQuarter(currentDate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<StartOfCurrentQLastYear>" =>
        firstDayOfQuarter(currentDate).minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<EndOfCurrentQLastYear>" =>
        lastDayOfQuarter(currentDate).minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<StartOfCurrentQNextYear>" =>
        firstDayOfQuarter(currentDate).plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<EndOfCurrentQNextYear>" =>
        lastDayOfQuarter(currentDate).plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<StartOfQ1NextYear>" =>
        firstDayOfQuarter(currentDate.minusMonths(currentDate.getMonthValue - 1)).plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<EndOfQ1NextYear>" =>
        lastDayOfQuarter(currentDate.minusMonths(currentDate.getMonthValue - 1)).plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<StartOfQ1LastYear>" =>
        firstDayOfQuarter(currentDate.minusMonths(currentDate.getMonthValue - 1)).minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<EndOfQ1LastYear>" =>
        lastDayOfQuarter(currentDate.minusMonths(currentDate.getMonthValue - 1)).minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

      case "<StartLastTaxYear>" =>
        if (LocalDate.now().isAfter(taxYearStart)) {
          taxYearStart.minusYears(1) format (DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
          taxYearStart.minusYears(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
      case "<EndLastTaxYear>" =>
        if (LocalDate.now().isAfter(taxYearStart)) {
          taxYearStart.minusDays(1) format (DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
          taxYearStart.minusDays(1).minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

      // Due Dates
      case "<Q1NextYearPlus45Days>" =>
        lastDayOfQuarter(currentDate.minusMonths(currentDate.getMonthValue - 1)).plusYears(1).plusDays(45).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<Q1LastYearPlus45Days>" =>
        lastDayOfQuarter(currentDate.minusMonths(currentDate.getMonthValue - 1)).minusYears(1).plusDays(45).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<CurrentQPlus45Days>" =>
        lastDayOfQuarter(currentDate).plusDays(45).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      case "<CurrentQLastYearPlus45Days>" =>
        lastDayOfQuarter(currentDate).minusYears(1).plusDays(45).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

      case "<LastTaxYearPlus45Days>" =>
        if (LocalDate.now().isAfter(taxYearStart)) {
          taxYearStart.plusDays(45).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
          taxYearStart.plusDays(45).minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
  }

  private def firstDayOfQuarter(date: LocalDate): LocalDate = {
    date.withMonth(date.get(IsoFields.QUARTER_OF_YEAR) * 3 - 2).`with`(TemporalAdjusters.firstDayOfMonth)
  }

  private def lastDayOfQuarter(date: LocalDate): LocalDate = {
    date.withMonth(date.get(IsoFields.QUARTER_OF_YEAR) * 3).`with`(TemporalAdjusters.lastDayOfMonth)
  }

  private def lastDayOfPreviousQuarter(date: LocalDate): LocalDate = {
    val previousDayDate = firstDayOfQuarter(date).minusDays(1)
    lastDayOfQuarter(previousDayDate)
  }

  private def firstDayOfPreviousQuarter(date: LocalDate): LocalDate = {
    val previousDayDate = firstDayOfQuarter(date).minusDays(1)
    firstDayOfQuarter(previousDayDate)
  }

  def replacePlaceholderJson(json: String, currentDate: LocalDate = LocalDate.now()): String = {
    val startOfCurrentQ = dateReplacement("<StartOfCurrentQ>", currentDate)
    val endOfCurrentQ = dateReplacement("<EndOfCurrentQ>", currentDate)
    val endOfPreviousQ = dateReplacement("<EndOfPreviousQ>", currentDate)
    val startOfPreviousQ = dateReplacement("<StartOfPreviousQ>", currentDate)
    val startOfCurrentQLastYear = dateReplacement("<StartOfCurrentQLastYear>", currentDate)
    val endOfCurrentQLastYear = dateReplacement("<EndOfCurrentQLastYear>", currentDate)
    val startOfCurrentQNextYear = dateReplacement("<StartOfCurrentQNextYear>", currentDate)
    val endOfCurrentQNextYear = dateReplacement("<EndOfCurrentQNextYear>", currentDate)
    val startOfQ1NextYear = dateReplacement("<StartOfQ1NextYear>", currentDate)
    val endOfQ1NextYear = dateReplacement("<EndOfQ1NextYear>", currentDate)
    val q1NextYearPlus45Days = dateReplacement("<Q1NextYearPlus45Days>", currentDate)
    val startOfQ1LastYear = dateReplacement("<StartOfQ1LastYear>", currentDate)
    val endOfQ1LastYear = dateReplacement("<EndOfQ1LastYear>", currentDate)
    val startLastTaxYear = dateReplacement("<StartLastTaxYear>", currentDate)
    val endLastTaxYear = dateReplacement("<EndLastTaxYear>", currentDate)
    val q1LastYearPlus45Days = dateReplacement("<Q1LastYearPlus45Days>", currentDate)
    val currentQPlus45Days = dateReplacement("<CurrentQPlus45Days>", currentDate)
    val currentQLastYearPlus45Days = dateReplacement("<CurrentQLastYearPlus45Days>", currentDate)
    val lastTaxYearPlus45Days = dateReplacement("<LastTaxYearPlus45Days>", currentDate)
    json.replace(
      "<StartOfCurrentQ>", startOfCurrentQ).replace(
      "<EndOfCurrentQ>", endOfCurrentQ).replace(
      "<EndOfPreviousQ>", endOfPreviousQ).replace(
      "<StartOfPreviousQ>", startOfPreviousQ).replace(
      "<StartOfCurrentQLastYear>", startOfCurrentQLastYear).replace(
      "<EndOfCurrentQLastYear>", endOfCurrentQLastYear).replace(
      "<StartOfCurrentQNextYear>", startOfCurrentQNextYear).replace(
      "<EndOfCurrentQNextYear>", endOfCurrentQNextYear).replace(
      "<StartOfQ1NextYear>", startOfQ1NextYear).replace(
      "<EndOfQ1NextYear>", endOfQ1NextYear).replace(
      "<StartOfQ1LastYear>", startOfQ1LastYear).replace(
      "<EndOfQ1LastYear>", endOfQ1LastYear).replace(
      "<StartLastTaxYear>", startLastTaxYear).replace(
      "<EndLastTaxYear>", endLastTaxYear).replace(
      "<Q1NextYearPlus45Days>", q1NextYearPlus45Days).replace(
      "<Q1LastYearPlus45Days>", q1LastYearPlus45Days).replace(
      "<CurrentQPlus45Days>", currentQPlus45Days).replace(
      "<CurrentQLastYearPlus45Days>", currentQLastYearPlus45Days).replace(
      "<LastTaxYearPlus45Days>", lastTaxYearPlus45Days)
  }
}
