# Guidance

To test the pagination for the Event 22 summary please follow these steps.

1. Run the PODS_ER profile from the service manager,
2. Drop your Event Reporting Mongo collection,
3. Select Event 22 on this page: http://localhost:8216/manage-pension-scheme-event-report/event-selection ,
4. Manually enter details for the latest tax year and follow the flow through to the summary section,
5. In pensions-scheme-event-reporting-stubs repo,
6. Update the number of records to be generated in the numOfMembers val of app/utils/StubDataGenerator.scala,
7. Run the StubDataGenerator (this will generate a new file in conf/resources/data/api1832/event22PaginationTestPayloads),
8. Copy and reformat this file with a json formatter,
9. Open the Mongo document generated from step 3 for apiTypes 1830 and edit the document,
10. Copy and paste this whole document to a text editor,
11. In the Mongo document, replace the whole document with the formatted document (step 7),
12. Replace the second line starting "_id" with the corresponding line from the original mongo document, saved in step 9,
13. Replace the penultimate line starting "expireAt" with the corresponding line from the original mongo document, saved in step 9,
14. Replace the last line starting "lastUpdated" with the corresponding line from the original mongo document, saved in step 9,
15. Save the new document in your Mongo browser,
16. Refresh your page and you should see many entries in the summary page.