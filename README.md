# Equipment Checkout System

A Java Swing desktop application that allows employees and supervisors to manage equipment checkouts, returns, inventory, certifications, and usage reporting for GB Manufacturing.

## Features

### Authentication & User Roles

* Secure login using username and password
* Role-based access control
* Employee and Supervisor user roles
* Logout and switch users

### Equipment Management

* View available and checked-out equipment
* Check out equipment
* Return equipment
* Add equipment (Supervisor only)
* Delete equipment (Supervisor only)
* Track current checkout status and assigned user

### Certification Requirements

* Equipment may require a specific certification before checkout
* Users without the required certification are prevented from checking out restricted equipment
* Certification records are maintained in the database and are intended to be managed by HR, Training Administrators, or Database Administrators
* Examples include Forklift Certification, Welding Certification, HVAC Certification, and other skill-based qualifications

### Reporting

* Equipment checkout history tracking
* User checkout activity tracking
* Timestamped checkout and return transaction records
* Equipment usage statistics and reporting

### User Management

* Add users (Supervisor only)
* Delete users (Supervisor only)
* Assign employee or supervisor roles

---

## Prerequisites

* Java 11 or newer
* Maven 3.8 or newer

---

## Running the Application

From the project root directory:

```bash
mvn clean compile exec:java -Dexec.mainClass="com.gbmanufacturing.ecs.Main"
```

---

## Building the Application

```bash
mvn clean package
```

Run the generated JAR:

```bash
java -jar target/ecs-1.0-SNAPSHOT.jar
```

---

## Database

The application uses SQLite for persistent storage.

Database location:

```text
database/ecs.db
```

The database is automatically created and seeded on first launch with sample users, equipment, certifications, and transaction history.

---

## Default Seeded Accounts

### Supervisors

| Username | Password |
| -------- | -------- |
| sroberts | admin123 |
| tbrown   | admin123 |

### Employees

| Username  | Password    |
| --------- | ----------- |
| jsmith    | password123 |
| agarcia   | password123 |
| mjohnson  | password123 |
| kwilliams | password123 |
| dlee      | password123 |

---

## Seeded Certifications

The application includes sample certification records to demonstrate certification-restricted equipment access.

Examples:

* Forklift Certification
* Welding Certification

Additional certifications can be added directly to the database as organizational requirements evolve.

---

## Project Use Cases Implemented

### Employee

* Login
* Logout
* View equipment inventory
* Check out equipment
* Return equipment
* View equipment status

### Supervisor

* Login
* Logout
* View equipment inventory
* Check out equipment
* Return equipment
* Add equipment
* Delete equipment
* Manage users
* Generate usage reports

---

## Technology Stack

* Java 11
* Java Swing
* SQLite
* JDBC
* Maven

---

## Source Repository

Project source code is maintained in the team's GitHub repository.

Repository URL:

https://github.com/densterwilson-png/Tool-Checkout

Team members used GitHub for:

* Version control
* Pull requests
* Code reviews
* Collaborative development
* Issue tracking and project updates

---

## Notes

* Equipment checkout restrictions are enforced through certification validation.
* All checkout and return transactions are recorded with timestamps.
* Usage reports are generated from historical transaction data.
* Certification assignments are intended to be maintained by authorized personnel such as HR, Training Coordinators, or Database Administrators.
* Passwords are stored in plaintext for demonstration purposes and should be replaced with hashed passwords in a production environment.
