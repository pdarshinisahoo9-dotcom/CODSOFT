# CODSOFT Java Programming Internship

> A set of five Java projects built during the CODSOFT internship program, covering console applications, GUI desktop apps, and live API integration.

---

## Table of Contents

- [Projects Overview](#projects-overview)
- [Task 1 - Number Guessing Game](#task-1---number-guessing-game)
- [Task 2 - Student Grade Calculator](#task-2---student-grade-calculator)
- [Task 3 - ATM Interface](#task-3---atm-interface)
- [Task 4 - Currency Converter](#task-4---currency-converter)
- [Task 5 - Student Management System](#task-5---student-management-system)
- [Requirements](#requirements)
- [How to Run](#how-to-run)
- [Author](#author)

---

## Projects Overview

| # | Project | Type | Key Concepts |
|---|---------|------|--------------|
| 1 | Number Guessing Game | Console | Random, loops, scoring |
| 2 | Student Grade Calculator | Console | Arrays, input validation, formatting |
| 3 | ATM Interface | Console | OOP, classes, encapsulation |
| 4 | Currency Converter | Console | HTTP client, API, JSON parsing |
| 5 | Student Management System | Desktop GUI | Swing, file I/O, CRUD |

---

## Task 1 - Number Guessing Game

**File:** `NumberGuessingGame.java`

A console game where the player guesses a randomly generated number within a configurable range, with a limited number of attempts per round.

### Features
- Configurable number range, max attempts per round, and total rounds
- Instant feedback after every guess: Too HIGH, Too LOW, or Correct
- Visual attempt progress bar `[XXX----]`
- Score system: fewer guesses used = more points per round
- Running score and performance grade across all rounds
- Full input validation with re-prompting on invalid entries

### How to Run
```bash
javac NumberGuessingGame.java
java NumberGuessingGame
```

### Sample Output
```
+======================================+
|      NUMBER  GUESSING  GAME          |
+======================================+

--- Game Setup ---
Range minimum        : 1
Range maximum        : 100
Max attempts / round : 7
Number of rounds     : 3

[Attempt 1/7 | 7 left] Your guess: 50
Too HIGH! Try a lower number.
Progress: [X------]

[Attempt 2/7 | 6 left] Your guess: 25
Correct! The number was 25.
Points earned: +60  (total: 60)
```

---

## Task 2 - Student Grade Calculator

**File:** `GradeCalculator.java`

A console application that computes a student's overall grade from marks entered across any number of subjects.

### Features
- Accepts a student name and any number of subjects
- Validates that each mark falls between 0 and 100
- Calculates total marks, average percentage, and letter grade
- Grade scale: A+ (90+), A (80+), B (70+), C (60+), D (50+), E (40+), F (below 40)
- Displays a formatted result card with pass/fail status

### How to Run
```bash
javac GradeCalculator.java
java GradeCalculator
```

### Sample Output
```
=========================================
       STUDENT GRADE CALCULATOR
=========================================
Enter student name       : Priya
Enter number of subjects : 3

Subject 1 name  : Maths
Marks obtained  : 88
Subject 2 name  : Science
Marks obtained  : 76
Subject 3 name  : English
Marks obtained  : 91

=========================================
             RESULT CARD
=========================================
Student Name       : Priya
-----------------------------------------
Subject              Marks
-----------------------------------------
Maths                88.0
Science              76.0
English              91.0
-----------------------------------------
Total Marks          255.0
Average Percentage   85.00%
Grade             A  (Excellent)
=========================================
  Result: PASS - Congratulations, Priya!
```

---

## Task 3 - ATM Interface

**File:** `ATM.java`

A console-based ATM simulation using object-oriented design. The `BankAccount` class manages the balance, and the `ATMMachine` class handles user interaction.

### Features
- Check current account balance
- Deposit money with amount validation
- Withdraw money with insufficient balance check
- Clean menu-driven interface
- OOP design: separate `BankAccount` and `ATMMachine` classes
- Default starting balance of Rs. 1000

### How to Run
```bash
javac ATM.java
java ATM
```

### Sample Output
```
========== ATM MENU ==========
1. Check Balance
2. Deposit
3. Withdraw
4. Exit
==============================
Enter your choice: 2
Enter amount to deposit: Rs. 500
Deposit Successful!
Updated Balance: Rs. 1500.0

Enter your choice: 3
Enter amount to withdraw: Rs. 2000
Insufficient Balance!
```

---

## Task 4 - Currency Converter

**File:** `CurrencyConverter.java`

A console application that converts between 12 major world currencies using live exchange rates fetched from a public REST API. No external libraries are used; HTTP requests and JSON parsing are handled with built-in Java APIs.

### Features
- Supports 12 currencies: USD, EUR, GBP, INR, JPY, AUD, CAD, CHF, CNY, SGD, AED, SAR
- Fetches real-time rates from [ExchangeRate-API](https://www.exchangerate-api.com/)
- Built-in lightweight JSON parser (no third-party dependencies)
- Displays exchange rate, converted amount, and rate date
- Handles network errors gracefully
- Convert multiple currencies in one session

### How to Run
```bash
javac CurrencyConverter.java
java CurrencyConverter
```

> **Note:** Requires an active internet connection. Uses `java.net.http.HttpClient` — needs JDK 11 or later.

### Sample Output
```
==========================================
        LIVE CURRENCY CONVERTER
      Real-time Exchange Rates
==========================================

  SELECT BASE CURRENCY (FROM)
  1.  USD  -  US Dollar
  2.  EUR  -  Euro
  ...
  Choose (1-12): 1
  >> USD - US Dollar selected.

  Enter amount in US Dollar (USD): 100

  Fetching live exchange rate...

==========================================
          CONVERSION RESULT
------------------------------------------
  From   : USD  (US Dollar)
  To     : INR  (Indian Rupee)
------------------------------------------
  Amount         : $ 100.00
  Exchange Rate  : 1 USD = Rs. 83.412000
------------------------------------------
  CONVERTED AMT  : Rs. 8341.20
  Rate Date      : 2025-06-01
==========================================
```

---

## Task 5 - Student Management System

**File:** `StudentManagement.java`

A full desktop GUI application built with Java Swing for managing student records, with automatic persistence to a CSV file.

### Features
- Add, update, delete, and search student records
- Student fields: Roll Number, Name, Grade, Course, Email, Phone
- Search by roll number or partial name match
- Form validation: required fields, email format, phone number length
- Sortable table with alternating row colors and live record count
- Data auto-saved to `students.csv` on every operation
- Modern UI with custom color theme, hover effects on buttons, and split-pane layout

### How to Run
```bash
javac StudentManagement.java
java StudentManagement
```

> A `students.csv` file is created automatically in the same directory and persists data between sessions.

### UI Overview
```
+-----------------------------------------------+
|  Student Management System          Total: 12  |
+---------------+-------------------------------+
|  [ Form ]     |  Roll | Name | Grade | Course |
|  Roll No:     |  ----------------------------  |
|  Name:        |  001  | Priya| A+   | CS      |
|  Grade:       |  002  | Ravi | B    | ECE     |
|  Course:      |  ...                           |
|  Email:       |                                |
|  Phone:       |  [ Search bar ]                |
|               |                                |
| [Add][Update] |                                |
| [Delete][Clr] |                                |
+---------------+--------------------------------+
|  Ready  |  12 student(s) in database.          |
+------------------------------------------------+
```

---

## Requirements

| Requirement | Detail |
|-------------|--------|
| JDK Version | 11 or later (17+ recommended) |
| External Libraries | None — standard Java library only |
| Internet | Required for `CurrencyConverter.java` only |
| OS | Windows, macOS, Linux |

---

## How to Run

### Step 1 — Clone the repository
```bash
git clone https://github.com/<your-username>/CODSOFT.git
cd CODSOFT
```

### Step 2 — Compile
```bash
javac <FileName>.java
```

### Step 3 — Run
```bash
java <ClassName>
```

| File | Compile | Run |
|------|---------|-----|
| `NumberGuessingGame.java` | `javac NumberGuessingGame.java` | `java NumberGuessingGame` |
| `GradeCalculator.java` | `javac GradeCalculator.java` | `java GradeCalculator` |
| `ATM.java` | `javac ATM.java` | `java ATM` |
| `CurrencyConverter.java` | `javac CurrencyConverter.java` | `java CurrencyConverter` |
| `StudentManagement.java` | `javac StudentManagement.java` | `java StudentManagement` |

### Windows encoding note
If you see `unmappable character` errors on Windows, compile with:
```bash
javac -encoding UTF-8 <FileName>.java
```

---

## Author

**Priyadarshini**
CODSOFT Java Programming Intern

---

*Built as part of the CODSOFT Internship Program*
