# Tool Checkout System

Small Java Swing + SQLite GUI application for managing equipment/tool checkouts.

Prerequisites:

- Java 11 or newer installed
- Maven installed (or optional: use the built JAR directly if you build it first)

Run with Maven from the project root:

```
mvn clean compile exec:java -Dexec.mainClass="com.gbmanufacturing.ecs.Main"
```

Or build and run the shaded JAR:

```
mvn clean package
java -jar target/ecs-1.0-SNAPSHOT.jar
```

Data is stored at `database/ecs.db` (created automatically).

Default seeded accounts:

**Supervisors:**
- sroberts / admin123
- tbrown / admin123

**Employees:**
- jsmith / password123
- agarcia / password123
- mjohnson / password123
- kwilliams / password123
- dlee / password123

Features implemented to match project use cases:

- Login (users with roles)
- Equipment Check-out
- Equipment Check-in
- Add/Delete equipment (supervisor only)
- Generate a simple usage report (supervisor only)
- Manage employee access (add/delete users, supervisor only)

