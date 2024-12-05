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