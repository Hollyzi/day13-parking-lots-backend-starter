1.parkinglotManager

Implement ParkinglotManager,
Holding 3 parkingLots:The Plaza Park (9 parking capacity)、 City Mall Garage (12 parking capacity)、Office Tower Parking (
9 parking capacity);3 types of parkingBoys(each utilizing a specific parking strategy Standard parking strategy,Smart
parking strategy,Super Smart parking strategy),parkinhBoy can manage the parking-lots;
has Methods parkCar,getCars,fetchCar

2.parkinglotManagerTest
generate the test cases for parkinglotManager,
content format as "Given [condition],When [event],Then [result]".
name format as "should[Event]When[Condition]Then[Result]"

3.Apidesginer
Implement ParkinglotManagerController, Holding ParkingManager, has Methods parkCar,getCars,fetchCar, parkCar strategy
should according to the incoming strategy parameter Standard parking strategy correspond to SequentiallyStrategy; Smart
parking strategy correspond to MaxAvailableStrategy; Super Smart parking strategy correspond to AvailableRateStrategy
generate unit test correspond to ParkinglotManagerController
content format as "Given [condition],When [event],Then [result]".
name format as "should[Event]When[Condition]Then[Result]"

4.plateNumber validate
add verify of incoming parameter:car ,since it should be format as "%c%c-%04d",when input car is unvalid or
null,should return refuse message

5.validate repeat plateNumber
when park car ,should validate if the plateNumber of car exist in Tickets,if so,refuse to park car

6.parkedCar existed exception
park the car with this plate number already exists,should throw an existException
use @ControllerAdvice to catch the ExistException

7.calculate and Display Parking Time
refactor demo to achieve function of calculate and Display Parking Time. When fetch car, need to show the entry time,
exit time, and the total parking duration on the ticket accurate to the minute. For example: Entry Time: 2024-12-24 9:
15Exit Time: 2024-12-24 10:30 ● Parking Duration: 1 hour 15 minutes If the duration exceeds one day, display the format
as: X days + Parking Duration

8.calculate and Display Parking Fee
generate demo achieve calculate and return Parking Fees When fetch car, need return the parking fees calculated based on
the parking duration. The current fee calculation formula is 4 RMB per 15 minutes, with any fraction rounded up to the
next 15 minutes. Example: Parking for 1 minute = 4 RMB; parking for 16 minutes = 8 RMB.