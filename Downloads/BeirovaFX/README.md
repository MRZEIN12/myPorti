# BeirovaFX (JavaFX + Maven)

A professional, minimal e-commerce desktop application for the **BEÏROVA** brand using **JavaFX 21** and **Java 17**.

## Features
- Login & customer registration
- Role-based navigation: Customer, Admin, SuperAdmin
- Customer store: products list, cart, checkout (orders saved)
- Admin dashboard: manage products, view orders
- Super Admin dashboard: manage users & roles
- JSON storage (Jackson) in a `data/` folder next to the app
- Uses your logo at `src/main/resources/assets/logo.png`

## Quick Start
1. Install **JDK 17+** and **Maven**.
2. From the project root, run:

   ```bash
   mvn clean javafx:run
   ```

3. Seeded logins:
   - SuperAdmin — `super@beirova.local` / `Password123!`
   - Admin — `admin@beirova.local` / `Admin123!`
   - Customer — `customer@beirova.local` / `Customer123!`

## Build
```bash
mvn clean package
```
The runnable jar will be in `target/` (you may need to add `--module-path` for JavaFX if running without the Maven plugin).

## Notes
- Data is stored in `./data` relative to the working directory. Delete the `data/*.json` files to reseed.
- You can change product images by setting `imagePath` to another classpath resource (e.g., `assets/your-image.png`).
