# Biobank REST API for REDCap

This REST API allows for the [Biobank REDCap Plugin]() to extract information from a
[Biobank](https://github.com/cbsrbiobank/biobank) database. Biobank is an application that manages
the collection, processing and storage of biological
specimens. [REDCap](http://www.project-redcap.org/) (Research Electronic Data Capture) is a
browser-based, metadata-driven EDC software solution and workflow methodology for designing clinical
and translational research databases.

By using the *Biobank REDCap Plugin*, REDCap users are able to query the inventory at a specimen
storage facility for a patient in a a REDCap study.

## REST API

### /patients/{pnumber}/specimens/counts

Parameter | value | description
--- | --- | ---
pnumber | string | The Biobank patient number.

#### GET

Returns the specimen type counts for a given patient. The counts are for the processed specimens
present in the inventory at a given storage site.

available response representations:

* 200 - application/json (component)

    ##### Example
    ```json
    {
      "pnumber": "EDM001",
      "studyName": "PROBE",
      "storageCenters": [
        {
          "storageCenter": "CBSR",
          "specimenTypeCounts": [
           { "specimenType": "SerumG500", "count": 8 },
           { "specimenType": "Urine", "count": 50 }
          ]
        },
        {
          "storageCenter": "Calgary-F",
          "specimenTypes": [
           { "specimenType": "SerumG500", "count": 3 },
           { "specimenType": "Urine", "count": 15 }
          ]
        }
      ]
    }
    ```

* 404

    Returned if there is no patient with the given patient number.

### /patients/{pnumber}/study

Parameter | value | description
--- | --- | ---
pnumber | string | The Biobank patient number.

#### GET

Returns the specimen types stored for the study the patient belongs to. It is possible that a
patient does not have all of these specimen types stored for him / her.

available response representations:

* 200 - application/json (component)

    ##### Example
    ```json
    {
      "pnumber": "EDM001",
      "study": {
        "name": "PROBE",
        "description" : "PROBE",
        "specimenTypes": ["SerumG500", "Urine"]
      }
    }
    ```

* 404

    Returned if there is no patient with the given patient number.

### /patients/{pnumber}/specimens/aliquots

#### GET

Returns the specimen stored for a patient. Specimens may be stored at multiple storage sites.

Parameter | value | description
--- | --- | ---
pnumber | string | The Biobank patient number.

available response representations:

* 200 - application/json (component)

    ##### Example
    ```json
    {
      "pnumber": "EDM001",
      "storageCenters": [{
        "storageCenter": "CBSR",
        "specimens": [{
          "inventoryId": "S0062-3",
          "dateDrawn": "2013-07-16T21:45:21-0600",
          "specimenType": "SerumG500",
          "quantity": 0.5000000000
        }, {
          "inventoryId": "S0062-1",
          "dateDrawn": "2013-07-16T21:45:21-0600",
          "specimenType": "SerumG500",
          "quantity": 0.5000000000
        }, {
          "inventoryId": "U0301-4",
          "dateDrawn": "2013-08-01T13:38:36-0600",
          "specimenType": "Urine",
          "quantity": 1.8000000000
        }, {
         "inventoryId": "U0312-3",
         "dateDrawn": "2014-08-28T06:00:41-0600",
         "specimenType": "Urine"
        }]
      }]
    }
    ```

    Note that the **quantity** attribute is optional and may not be present.

* 404

    Returned if there is no patient with the given patient number.
