### What happens if the test client is run 11 times in a row?

The hotel service is returning a 409 conflict, because it only allows 10 bookings per day.

### 

Atomicity: Yes, if a service that went successfully into a reserved state is always capable of confirming the operation.
Consistent: Yes
Isolation: Depends on setup: If multiple services write into the same db, its possible that one serice goes into reserved state,
but while waiting for conformation another service alters the db to a state where the first service would not be allowed to
go into reserved state (e.g. booking count is 9, another service B quickly adds the 10th, now service A cant confirm)
Durability: Yes
