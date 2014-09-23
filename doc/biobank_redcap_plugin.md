# A REDCap pluing for Biobank

Biobank is an application that manages the collection, processing and storage of biological
specimens. REDCap (Research Electronic Data Capture) is a browser-based, metadata-driven EDC
software solution and workflow methodology for designing clinical and translational research
databases. This plugin allows REDCap to query the inventory at specimen storage facilitiees for a
patient in a a REDCap study.

The plugin is accessible by adding a bookmark to a study in REDCap. Once on the plugin page, the
user choose to display:

* The count of specimens categorized by storage facility and specimen type. *[add screenshot here]*

* The specimen inventory. *[add screenshot here]* The information included here is:

    * The specimen's inventory ID,
    * The date drawn (the date it was collected from the patient),
    * The specimen type, and
    * The quantity

The information displayed by this plugin is provided by a Biobank server running the Biobank REDCap
plugin API. This API is currently defined here:

* [Biobank REDCap Plugin API](biobank_redcap_rest_api.md).

Since the information provided by the *Plugin API* is anonymized, the plugin does not require any
security settings. All that is required is to have the proper authentication done on the REDCap
server.

## Configuration

Before the plugin can be used, configuration information is required by the plugin. The following
sections provide details on what is required. Most of this information can be provided by the Biobank
support staff.

### Biobank server definition

The URL for the Biobank server running the *Plugin API*.

### Study Name

The name of the study used on REDCap may be different than the name used by Biobank. The name of the
Biobank study must specified here.

###Patient number mapping

The patient identifiers used in REDCap may be different than the ones used in
Biobank. The

###Specimen type mapping

The names of the specimen types used on REDCap may also be different than the ones used in
Biobank. This mapping can be entered here.

