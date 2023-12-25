# Event explorer
## 1. Brief description of the project
The goal of the project is to create a service for buying tickets to various events. The user can create their own events and buy tickets to other people's events.
## 2. System description
The system consists of the following basic parts:

1. User registration and authorization
2. Event search
3. Buy tickets
4. Creating events
### 2.1 User registration and authorization
---
Before using the service you need to create or log in to your account. After successful registration/authorization access to the service is opened.
#### 2.1.1 Registration
---
To register you need to give yourself a nickname and password, specify your age and email.
#### 2.1.2 Authorization
---
When authorizing, you need to provide your email and password.
### 2.2 Event Search
---
The main page will show a feed of all possible events. The user can filter the events by certain criteria: event topic, date, location, price. 

When selecting an event, you can view the full details of the event by clicking on it.

If people like the event, they can give it a like, thus promoting it.
### 2.3 Buying tickets
---
Buying a ticket is allowed under certain conditions:

- there are free seats left for the event
- If the age limit is specified, the user must meet it.

If all conditions are met, the user can buy a ticket. It will be possible to choose the number of tickets, as well as the seat.

After buying a ticket, it will be displayed in a separate tab.
### 2.4 Creating Events
---
When creating an event, you need to specify the name, date and time of the event, ticket price and location. You can add optional parameters, such as: number of seats, description, theme of the event. 

Own events are displayed in a separate tab. You can see who bought tickets, how many seats are left and how much money was collected.

## 3. Conceptual model of the database
![data model](images/DataModel.png)
## 4. Complex request on the server side
For any organizer it is very important to understand the success of his event and marketing, for this purpose you can create a query that analyzes the audience that came to the event and gives the percentage of new visitors who have never been to the events of this user, in relation to the total number of visitors.
## 5. Client-side business operation: Ticket purchase with improved availability control

### 5.1 Description
---
This optimized process is designed for convenient purchase of tickets for various events. When a customer clicks the "Buy Ticket" button, the system automatically checks the availability of seats. If seats are available, the ticket purchase process continues successfully; otherwise, the customer is informed that tickets are not available.

### 5.2 Steps
---
1. The customer selects an event, enters the required information and clicks the "Buy Ticket" button.
2. After this action, the system instantly checks the current availability of the selected event.
3. If tickets are available, the ticket purchase is successfully completed.
4. If all tickets are already sold out, the system informs the customer.
