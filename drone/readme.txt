To run, execute the following from the project root: 
./gradlew clean bootRun -Pargs=--input=<path to input file>

To run tests:
./gradlew clean test jacocoTestReport
The Junit test report can be found here: $buildDir/reports/jacoco/test/html/com.walmart.drone.order/index.html
The Jacoco report can be found here: $buildDir/reports/jacoco/test

To run sonarqube plugin:
./gradlew clean test jacocoTestReport sonarqube
Assuming there's a local sonarcube server running, the report should be here: http://localhost:9000/dashboard?id=com.walmart%3Adrone




Assumptions:
* Drone can only carry one package at a time
* Weight has no impact on speed or battery charge
* Drone travels on a direct route, and does not need to worry about impediments
* Drone will always have enough charge to reach and return from destination
* There are no restricted flight paths
* It is assumed that input applies to a valid delivery day (i.e not a holiday)
* Both input and output apply to the current day
* Both input and output are on a 24h scale (i.e. 05:00:00 is at 5AM, 17:00:00 is 5PM)
* Orders that cannot be scheduled for the day automatically incur a 0 NPS, and are left off the output
* NPS is unique per order (multiple orders going to the same address result in unique NPS's)
* Coordinates are always represented by whole numbers (integers or longs)
* Longitudinal vs latitudinal coordinates are ordered with N/S first, then E/W


Questions:
* Time to charge the drone?
* Time to load/unload the customer's order?
* What happens to orders that cannot be completed during the current day?
* What happens to order that are placed in the middle of the day?
* Potential feature: using ML with actual runs to hone scheduling
* Assuming that more than one order is allowed on the drone, can orders be broken up by item to better route?
* Can the drone's schedule be changed throughout the day?.. does that cause any trouble with reporting to customers?