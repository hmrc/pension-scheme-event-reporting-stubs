# Guidance

To test the pagination for the Event 22 summary please follow these steps.

1. Drop your Event Reporting Mongo collection,
2. Select Event 22 on this page: http://localhost:8216/manage-pension-scheme-event-report/event-selection ,
3. Manually enter details and follow the flow through to the summary section,
4. Open the Mongo document for apiTypes 1830 and edit the document,
5. Replace all the fields except the id field with your new data and save,
6. Refresh your page and you should see many entries in the summary page.