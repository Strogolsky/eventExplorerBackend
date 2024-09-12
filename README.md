# **Tusa**

## 1. **Project Overview**
The goal of the project is to create a service for purchasing tickets to events. Users can create their own events and buy tickets to other people's events. The service is called "Tusa."

## 2. **Key Features**
- **Registration and Authorization**
- **Ticket Purchase**
- **Event Creation**
- **User and Event Deletion**
- **Comments and Ratings**
- **Private Meetings**
- **Friends and Contacts**
- **Recommendations and Filtering**
- **User Chats**

### 2.1 **Registration and Authorization**
To use the service, users must register or log in.

**Registration**:
- Username: 3-30 characters.
- Password: minimum 8 characters, including letters, numbers, and special characters.
- Age: If the user is underage, they will not receive recommendations for 18+ events.
- Email: Requires verification.

Users can receive a **verification badge** after verification.

**Authorization** is done using email and password.

### 2.2 **Ticket Purchase**
- Tickets are available if there are free seats and age restrictions are met.
- **Ticket Statuses**: 
  - Active: Until the event takes place.
  - Inactive: After the event has ended.
  - Destroyed: If the event or author's account is deleted.
  
Funds from ticket purchases are transferred to the author **3 days after the event**. If the author's account is deleted before the event, the funds are returned to the buyers, and tickets are marked as **"Destroyed"**.

### 2.3 **Event Creation**
When creating an event, the following parameters must be specified:
- Title: 3-100 characters.
- Description: up to 100 characters.
- Date, time, ticket price, location.
- Number of seats, theme, and event images.

**Private meetings** can be created for friends only.

### 2.4 **User and Event Deletion**
Users can delete their accounts or their created events. If an event or author's account is deleted, all funds are returned to buyers, and tickets are marked as **"Destroyed"**.

### 2.5 **Comments and Ratings**
Users can leave **comments** and rate events. The rating affects the author's reputation.

### 2.6 **Friends and Contacts**
Users can **add friends** and communicate with them. They can create a profile description with contact details.

### 2.7 **Recommendations and Filtering**
The recommendation system is based on:
- User interests and those of their friends.
- Ratings of authors and events.

Filtering is available by tags, price, dates, and age restrictions.

### 2.8 **User Chats**
Users can communicate with each other through built-in chat features, discussing events and asking questions.

## 3. **Search and Filtering**
Events can be searched by:
- Title.
- Description.
- Tags.
- Date.
- Price.
- Age restrictions.

## 4. **Notifications**
The notification system will inform users about upcoming events, ticket status changes, and other important information.
