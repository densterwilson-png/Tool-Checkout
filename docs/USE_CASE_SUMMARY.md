Equipment Checkout System - Use Case Summary

This file maps the provided use case diagram to the implemented features in the application.

Use cases from the diagram:

- Login: implemented as `LoginPanel` in Swing with users stored in SQLite (`users` table).
- Equipment Check-out: implemented in `MainPanel` via `onCheckout()` and `ToolDAO.checkout()`.
- Equipment Check-in: implemented in `MainPanel` via `onReturn()` and `ToolDAO.checkin()`.
- Add/Delete Equipment Inventory: implemented as `onAddTool()` / `onDeleteTool()` (supervisor-only controls).
- Generate Usage Reports: implemented via `onReport()` in `MainPanel` which shows a simple aggregate report.
- Manage Employee Access: implemented as a modal dialog `ManageUsersPanel` for add/delete users (supervisor-only).

Default accounts for testing:

- supervisor / admin  (supervisor)
- employee / password (employee)

Implementation details:

- **Framework:** Pure Java Swing (no JavaFX, no FXML)
- **Database:** SQLite with JDBC
- **Build:** Maven with `maven-shade-plugin` for creating a runnable JAR
- **Java version:** 11+

Notes:
- Passwords are stored in plaintext for the demo; replace with hashed passwords (bcrypt) for production.
- Reports are basic; extend `ToolDAO.getUsageReport()` for richer queries (date ranges, per-user history, CSV export).

