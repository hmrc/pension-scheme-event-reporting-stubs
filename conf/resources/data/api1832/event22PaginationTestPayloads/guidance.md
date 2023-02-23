# Guidance

To test the pagination for the Event 22 summary please follow these steps.

1. Drop your Event Reporting Mongo collection,
2. Select Event 22 on this page: http://localhost:8216/manage-pension-scheme-event-report/event-selection ,
3. Manually enter details for the current year and follow the flow through to the summary section,
4. In pensions-scheme-event-reporting-stubs repo,
5. Update the number of records to be generated on line 62 of app/utils/StubDataGenerator.scala,
6. Run the StubDataGenerator (this will generate a new file in conf/resources/data/api1832/event22PaginationTestPayloads),
7. Copy and reformat this file with a json formatter, such as https://jsonformatter.curiousconcept.com/
8. Open the Mongo document generated from step 3 for apiTypes 1830 and edit the document,
9. Copy and paste this whole document to a text editor,
10. In the mongo document, replace the whole document with the whole document from the json formatter (step 7)
11. Replace the second line starting "_id" with the corresponding line from the original mongo document, saved in step 9,
12. Replace the penultimate line starting "expireAt" with the corresponding line from the original mongo document, saved in step 9,
13. Replace the last line starting "lastUpdated" with the corresponding line from the original mongo document, saved in step 9,
14. Save,
15. Refresh your page and you should see many entries in the summary page.