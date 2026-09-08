## Current Features

- Product management
- Inventory management
- Stock reservation and release
- Order management
- Order state machine
- Payment processing
- Payment idempotency
- Transaction management
- Pessimistic locking for concurrent inventory reservations

## Inventory Concurrency

SmartStock uses pessimistic database locking when reserving inventory.

This prevents multiple concurrent requests from reserving the same available stock and causing overselling.

Example:

Stock = 10

Request A → 7 units
Request B → 5 units

Only one reservation can proceed when the total demand exceeds available stock.

The inventory row is locked during the reservation transaction using
`PESSIMISTIC_WRITE`.