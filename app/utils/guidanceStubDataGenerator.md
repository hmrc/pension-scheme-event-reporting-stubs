# Guidance

To test the summary pages for large Mongo documents please follow these steps.

1. Run the PODS_ALL profile from the service manager,
2. Drop your Event Reporting Data Mongo collection,
3. If required, re-create the event reporting toggle and switch on that toggle (http://localhost:8212/pods-admin/create-toggle),
4. Navigate to the event selection page by starting a new event report and following the journey
5. Select a member based event, for example, Event 22 on the event selection page: http://localhost:8216/manage-pension-scheme-event-report/event-selection,
6. Manually enter details for that event in a given tax year and follow the flow through to the summary section,
7. In pensions-scheme-event-reporting-stubs repo,
8. If you already have a large Mongo test document generated, skip to Step 13
9. Update the number of records to be generated in the numOfMembers val of app/utils/StubDataGenerator.scala,
10. Run the StubDataGenerator (this will generate a new file in conf/resources/data/api1832/...),
11. Copy and reformat this file with a json formatter,
12. Open the Mongo document generated and copy and paste this whole document to a text editor,
13. Store your original Mongo document with one entry to a text editor (should contain details from Step 6),
14. In your Mongo browser, replace the whole document with the generated document,
15. Replace the second line starting "_id" with the corresponding line from the original mongo document, saved in Step 13,
16. Replace the fourth line starting "pstr" with the corresponding line from the original mongo document, saved in Step 13,
17. Replace the penultimate line starting "expireAt" with the corresponding line from the original mongo document, saved in Step 13,
18. Replace the last line starting "lastUpdated" with the corresponding line from the original mongo document, saved in Step 13,
19. Save the new document in your Mongo browser,
20. Refresh your page and you should see the generated entries in the summary page.