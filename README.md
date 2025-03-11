# Cinema Tickets Exercise

## Objective

This is a coding exercise which will allow you to demonstrate how you code and your approach to a given problem.

### Assessment Criteria

You will be assessed on:
- Your ability to write clean, well-tested and reusable code
- How you have ensured the following business rules are correctly met

## Business Rules

1. There are 3 types of tickets i.e. Infant, Child, and Adult
2. The ticket prices are based on the type of ticket (see table below)
3. The ticket purchaser declares how many and what type of tickets they want to buy
4. Multiple tickets can be purchased at any given time
5. Only a maximum of 25 tickets that can be purchased at a time
6. Infants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap
7. Child and Infant tickets cannot be purchased without purchasing an Adult ticket

### Ticket Pricing

| Ticket Type | Price |
|-------------|-------|
| INFANT      | £0    |
| CHILD       | £15   |
| ADULT       | £25   |

## Services

- There is an existing `TicketPaymentService` responsible for taking payments
- There is an existing `SeatReservationService` responsible for reserving seats

## Constraints

1. The TicketService interface CANNOT be modified
2. The code in the thirdparty.* packages CANNOT be modified
3. The `TicketTypeRequest` SHOULD be an immutable object

## Assumptions

You can assume:

- All accounts with an id greater than zero are valid. They also have sufficient funds to pay for any no of tickets
- The `TicketPaymentService` implementation is an external provider with no defects
- The payment will always go through once a payment request has been made
- The `SeatReservationService` implementation is an external provider with no defects
- The seat will always be reserved once a reservation request has been made

## Your Task

Provide a working implementation of a `TicketService` that:

1. Considers the above objective, business rules, constraints & assumptions
2. Calculates the correct amount for the requested tickets and makes a payment request to the `TicketPaymentService`
3. Calculates the correct no of seats to reserve and makes a seat reservation request to the `SeatReservationService`
4. Rejects any invalid ticket purchase requests