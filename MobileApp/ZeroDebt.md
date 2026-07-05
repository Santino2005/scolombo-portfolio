# ZeroDebt

ZeroDebt is an Android application designed to help users manage and split shared expenses in a simple and transparent way. Whether it's a trip, a shared apartment, or a group event, ZeroDebt keeps track of who paid for what and calculates how much each participant owes, making debt management effortless.

## Features

- **Expense Tracking**
  - Record shared expenses with descriptions, amounts, dates, and the person who paid.
  - Categorize expenses for better organization.

- **Group Management**
  - Create and manage groups for trips, roommates, events, or any shared activity.
  - Add or remove participants from groups.

- **Debt Calculation**
  - Automatically calculate balances between participants.
  - Display who owes money, who should receive money, and the exact amount.

- **Settlement Tracking**
  - Mark debts as settled.
  - Keep a history of completed payments.

- **User-Friendly Interface**
  - Clean and intuitive interface built with modern Android development practices.
  - Responsive layouts following Material Design principles.

## Technologies

The application is being developed using modern Android technologies:

- **Kotlin**
- **Jetpack Compose**
- **Material Design 3**
- **MVVM Architecture**
- **Navigation Compose**
- **ViewModel**
- **State Management**
- **Firebase Authentication** *(planned)*
- **Cloud Firestore** *(planned)*

## Architecture

ZeroDebt follows the **MVVM (Model–View–ViewModel)** architecture to ensure a clean separation of concerns, making the application scalable, maintainable, and easy to test.

```
Presentation
    │
    ▼
ViewModel
    │
    ▼
Repository
    │
    ▼
Data Sources
(Local / Remote)
```

## Current Status

**Work in Progress**

The project is currently under active development. Core features such as expense management, debt calculation, and user authentication are being implemented incrementally.

## Goals

The objective of ZeroDebt is to provide a simple yet powerful solution for people who frequently share expenses, eliminating manual calculations and reducing misunderstandings about payments.

Future improvements include:

- User authentication
- Cloud synchronization
- Multiple currencies
- Expense statistics and charts
- Notifications and reminders
- Offline support
- Dark mode customization

## Learning Objectives

This project also serves as a way to deepen my knowledge of:

- Android development with Kotlin
- Jetpack Compose
- Clean Architecture
- MVVM
- Firebase services
- Mobile application architecture
- Software engineering best practices

## Author

**Santino Colombo**

Computer Engineering Student (4th Year)  
Full-Stack Developer
