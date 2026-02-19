# Spring Boot Google Forms Automation Service

This project is a Spring Boot application designed to automate the processing of Google Form submissions. It acts as a webhook listener that receives form data, generates a unique coupon code for each submission, saves the data to a MongoDB database, and sends a confirmation email to the user.

## Features

*   **Webhook Listener**: Exposes a REST endpoint (`/webhook/submit`) to receive JSON payloads from Google Forms (via Google Apps Script).
*   **Coupon Generation**: Automatically generates a unique coupon code for each user (e.g., `COUPON-NAM-A1B2C`).
*   **Data Persistence**: Stores submission details, including timestamp and generated coupon code, in a **MongoDB** database.
*   **Email Automation**: Sends a confirmation email to the user with their submission details and coupon code using SMTP (configured for Hostinger/Gmail/etc).
*   **WhatsApp Integration**: (Optional/Currently Disabled) Codebase supports sending WhatsApp notifications via Meta Cloud API.

## Tech Stack

*   **Java**: 21
*   **Framework**: Spring Boot 3.4.2
*   **Build Tool**: Maven
*   **Database**: MongoDB
*   **Email**: JavaMailSender (SMTP)
*   **Architecture**: MVC (Model-View-Controller) & Service Layer

## Prerequisites

Before running the application, ensure you have the following installed:

1.  **Java JDK 21**: Verify with `java -version`.
2.  **MongoDB**: Ensure MongoDB is installed and running locally on port `27017`, or have a remote connection string ready.
3.  **Maven**: verify with `mvn -version` (or use the included `mvnw` wrapper).
4.  **Google Account**: To create the Google Form.
5.  **SMTP Server Access**: Credentials for sending emails (e.g., Hostinger, Gmail App Password).

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd whatsapp
```

### 2. Database Configuration
Ensure your MongoDB instance is running. The application defaults to:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/whatsapp-forms
```
If using a remote database (like MongoDB Atlas), update this in `src/main/resources/application.properties`.

### 3. Email Configuration
Open `src/main/resources/application.properties` and configure your SMTP settings.

**Example for Hostinger:**
```properties
spring.mail.host=smtp.hostinger.com
spring.mail.port=587
spring.mail.username=your-email@yourdomain.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Example for Gmail:**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-gmail@gmail.com
spring.mail.password=your-app-password  # Generate from Google Account Security
```

### 4. WhatsApp Configuration (Optional)
To enable WhatsApp messages, uncomment the code in `FormProcessorServiceImpl.java` and configure:
```properties
whatsapp.api.url=https://graph.facebook.com/v17.0/YOUR_PHONE_NUMBER_ID/messages
whatsapp.api.token=YOUR_ACCESS_TOKEN
```

### 5. Google Form & Apps Script Setup
1.  Create a Google Form with fields: Name, Phone, Email, Pump, Address, Mandal, District, State, Pincode, Facing problems.
2.  Open the **Script Editor** (3 dots > Script Editor).
3.  Copy the code from the `GoogleAppsScript.js` file in this project root.
4.  Paste it into the Script Editor.
5.  **Update the URL**: Change the `url` variable in the script to your public server URL.
    *   *If testing locally*: Use [Ngrok](https://ngrok.com/) (`ngrok http 8080`) to get a public URL (e.g., `https://xxxx.ngrok-free.app/webhook/submit`).
6.  Save trigger:
    *   **Triggers** > **Add Trigger** > `onFormSubmit` > `From form` > `On form submit`.

## Running the Application

Use the Maven wrapper to run the application:

**Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080`.

## Testing

### Manual Testing with HTTP Client
You can use the included `test-requests.http` file (requires REST Client extension in VS Code) or `curl` to send a test request:

```bash
curl -X POST http://localhost:8080/webhook/submit \
-H "Content-Type: application/json" \
-d '{"name": "Test User", "email": "test@example.com", "phone": "1234567890", "facingProblems": "Testing"}'
```

### Unit Tests
Run the JUnit tests to verify the controller and service logic:
```bash
.\mvnw.cmd test
```

## Project Structure

```
src/main/java/com/thejatech/whatsapp/
├── config/             # Configuration classes
├── controller/         # REST Controllers (API Endpoints)
├── model/              # Data Models (MongoDB Entities, DTOs)
├── repository/         # Database Repositories
├── service/            # Business Logic Interfaces
│   └── impl/           # Service Implementations
└── WhatsappApplication.java  # Main Entry Point
```

## Troubleshooting

*   **Email Authentication Failed**: Check your `application.properties` credentials. If using Gmail, ensure you used an *App Password*, not your login password.
*   **Connection Refused (MongoDB)**: Ensure MongoDB service is running (`net start MongoDB` on Windows).
*   **Google Script Error**: Check the execution logs in the Apps Script dashboard. Ensure the URL is accessible from the internet (use Ngrok for localhost).
