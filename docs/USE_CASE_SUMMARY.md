# Equipment Checkout System - Use Case Summary

This document maps the project use cases to the implemented functionality within the Equipment Checkout System application.

## Implemented Use Cases

### Login

Implemented through `LoginPanel` using Swing and SQLite authentication.

* Users authenticate with username and password.
* User roles are loaded from the database.
* Role-based permissions are enforced throughout the application.

### Equipment Check-Out

Implemented through `MainPanel.onCheckout()` and `EquipmentDAO.checkout()`.

* Employees and Supervisors may check out equipment.
* Equipment availability is validated before checkout.
* Certification requirements are validated before restricted equipment may be checked out.
* Checkout transactions are recorded in the database with timestamps.

### Equipment Check-In

Implemented through `MainPanel.onReturn()` and `EquipmentDAO.checkin()`.

* Employees may return equipment they currently have checked out.
* Supervisors may return equipment when necessary.
* Return transactions are recorded in the database with timestamps.

### Manage Equipment Inventory

Implemented through supervisor-only controls in `MainPanel`.

Features include:

* Add Equipment
* Delete Equipment
* Equipment status tracking
* Certification requirement assignment during equipment creation

### Generate Usage Reports

Implemented through `MainPanel.onReport()` and `EquipmentDAO.getUsageReport()`.

Reports include:

* Equipment checkout history
* User checkout activity
* Transaction timestamps
* Equipment usage statistics

### Manage Employee Access

Implemented through `ManageUsersPanel`.

Supervisor capabilities:

* Add users
* Delete users
* Assign employee or supervisor roles

## Default Test Accounts

### Supervisors

* sroberts / admin123
* tbrown / admin123

### Employees

* jsmith / password123
* agarcia / password123
* mjohnson / password123
* kwilliams / password123
* dlee / password123

## Technology Stack

* Java 11+
* Java Swing
* SQLite
* JDBC
* Maven

## Notes

* Certification records are maintained within the database and are intended to be managed by HR, Training Coordinators, or Database Administrators.
* Checkout and return transactions are stored with timestamps to provide historical reporting.
* Passwords are stored in plaintext for demonstration purposes and should be replaced with secure password hashing in a production environment.
