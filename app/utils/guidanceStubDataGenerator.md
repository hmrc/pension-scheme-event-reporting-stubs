# Guidance

To test the pagination for the Event 22 summary please follow these steps.

1. Run the PODS_ALL profile from the service manager,
2. Drop your Event Reporting Mongo collection,
3. Re-create the event reporting toggle and switch on that toggle (http://localhost:8212/pods-admin/create-toggle),
4. Navigate to the event selection page by starting a new event report and following the journey
5. Select Event 22 on the event selection page: http://localhost:8216/manage-pension-scheme-event-report/event-selection,
6. Manually enter details for the latest tax year and follow the flow through to the summary section,
7. In pensions-scheme-event-reporting-stubs repo,
8. Update the number of records to be generated in the numOfMembers val of app/utils/StubDataGenerator.scala,
9. Run the StubDataGenerator (this will generate a new file in conf/resources/data/api1832/event22PaginationTestPayloads),
10. Copy and reformat this file with a json formatter,
11. Open the Mongo document generated from step 3 for apiTypes 1830 and edit the document,
12. Copy and paste this whole document to a text editor,
13. In the Mongo document, replace the whole document with the formatted document (step 7),
14. Replace the second line starting "_id" with the corresponding line from the original mongo document, saved in step 9,
15. Replace the fourth line starting "pstr" with the corresponding line from the original mongo document, saved in step 9,
16. Replace the penultimate line starting "expireAt" with the corresponding line from the original mongo document, saved in step 9,
17. Replace the last line starting "lastUpdated" with the corresponding line from the original mongo document, saved in step 9,
18. Save the new document in your Mongo browser,
19. Refresh your page and you should see many entries in the summary page.