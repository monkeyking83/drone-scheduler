Assumptions:
* Drone can only carry one package at a time
* Weight has no impact on speed or battery charge
* Drone can only travel North, South, East, and West, but not diagonally
* Drone will always have enough charge to reach and return from destination
* There are no restricted flight paths
* It is assumed that input applies to a valid delivery day (i.e not a holiday)
* Both input and output apply to the current day
* Both input and output are on a 24h scale (i.e. 05:00:00 is at 5AM, 17:00:00 is 5PM)
* Items that cannot be scheduled for the day automatically incur a 0 NPS, and are left off the output
* NPS is unqie per order (mulitple orders going to the same address result in unique NPS's)


Questions:
* Time to charge the drone?
* Time to load/unload the customer's order?
* What happens to orders that cannot be completed during the current day?
* What happens to order that are placed in the middle of the day?
* Potential feature: using ML with actual runs to hone scheduling