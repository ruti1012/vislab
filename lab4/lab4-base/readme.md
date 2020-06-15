### What happens if the test client is run 11 times in a row?

The hotel service returns a `409 Conflict`, because it only allows 10 bookings per day.

### Which parts of ACID are fulfilled?

Atomicity: Yes, if a service that went successfully into a reserved state is always capable of confirming the operation.
Consistent: Yes, if confirming the operation cannot fail.
Isolation: Yes, if one serive reserves a resource, then no other service can use this resource until the reservation ends.
Durability: Yes
