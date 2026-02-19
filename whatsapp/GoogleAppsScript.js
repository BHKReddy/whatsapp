function onFormSubmit(e) {
  try {
    // 1. Get the form response
    var formResponse = e.response;
    var itemResponses = formResponse.getItemResponses();
    
    // Log raw data for debugging
    Logger.log("Form submitted. Processing " + itemResponses.length + " items.");

    // 2. Prepare the payload matching the Java model
    var payload = {
      "name": "",
      "phone": "",
      "email": "",
      "address": "",
      "mandal": "",
      "district": "",
      "state": "",
      "pincode": "",
      "facingProblems": ""
    };

    // 3. Map form items to payload fields
    for (var i = 0; i < itemResponses.length; i++) {
      var itemResponse = itemResponses[i];
      var title = itemResponse.getItem().getTitle().toLowerCase().trim();
      var responseRaw = itemResponse.getResponse();
      var response = "";
      if (Array.isArray(responseRaw)) {
        response = responseRaw.join(", "); // Handle Checkbox arrays: "Issue A, Issue B"
      } else if (responseRaw) {
        response = responseRaw.toString();
      }

      Logger.log("Processing item: " + title + " -> " + response);

      if (title.includes("name")) {
        payload.name = response;
      } else if (title.includes("phone") || title.includes("number")) {
        // Basic cleaning of phone number
        payload.phone = response.toString().replace(/\D/g, ''); 
      } else if (title.includes("mail")) {
        payload.email = response;
      } else if (title.includes("address")) {
        payload.address = response;
      } else if (title.includes("mandal")) {
        payload.mandal = response;
      } else if (title.includes("district")) {
        payload.district = response;
      } else if (title.includes("state")) {
        payload.state = response;
      } else if (title.includes("pincode")) {
        payload.pincode = response;
      } else if (title.includes("problem") || title.includes("facing")) {
        payload.facingProblems = response;
      }
    }


    // ---------------------------------------------------------
    // NEW: Auto-fill Mandal, District, State from Pincode (Post-Submit)
    // ---------------------------------------------------------
    if (payload.pincode && payload.pincode.length === 6) {
      try {
        var geoUrl = "https://api.postalpincode.in/pincode/" + payload.pincode;
        var geoResponse = UrlFetchApp.fetch(geoUrl);
        var geoData = JSON.parse(geoResponse.getContentText());

        if (geoData && geoData[0].Status === "Success" && geoData[0].PostOffice && geoData[0].PostOffice.length > 0) {
          var details = geoData[0].PostOffice[0];
          
          // Auto-populate if fields are empty
          if (!payload.district) payload.district = details.District;
          if (!payload.state) payload.state = details.State;
          if (!payload.mandal) payload.mandal = details.Block; // 'Block' often corresponds to Mandal/Taluk in this API
          
          Logger.log("Auto-filled details for Pincode " + payload.pincode + ": " + payload.mandal + ", " + payload.district);
        }
      } catch (geoError) {
        Logger.log("Failed to fetch pincode details: " + geoError);
      }
    }
    // ---------------------------------------------------------

    Logger.log("Final Payload: " + JSON.stringify(payload));

    // 4. Send the payload to your Spring Boot application
    // IMPORTANT: Make sure there are NO LEADING SPACES in the URL string
    var url = "https://unvitrifiable-unstanding-carlton.ngrok-free.dev/webhook/submit"; 
    
    var options = {
      "method": "post",
      "contentType": "application/json",
      "payload": JSON.stringify(payload),
      "muteHttpExceptions": true // This prevents the script from crashing on 4xx/5xx errors so we can log them
    };

    var response = UrlFetchApp.fetch(url, options);
    var responseCode = response.getResponseCode();
    var responseBody = response.getContentText();

    Logger.log("Response Code: " + responseCode);
    Logger.log("Response Body: " + responseBody);

    if (responseCode !== 200) {
       // Send an email to YOURSELF to alert that the webhook failed
       MailApp.sendEmail("your-email@gmail.com", "Webhook Failed", "Code: " + responseCode + "\nBody: " + responseBody);
    }

  } catch (error) {
    Logger.log("CRITICAL ERROR: " + error);
    // Send an email to YOURSELF to alert that the script crashed
    if (Session.getActiveUser().getEmail()) {
         MailApp.sendEmail(Session.getActiveUser().getEmail(), "Script Error", error.toString());
    }
  }
}
