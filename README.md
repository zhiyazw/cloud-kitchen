# Cloud Kitchen

## Setup and Build
This project is built with IntelliJ IDEA 2020.1.1, Maven 3.6.3, Spring Boot 1.5.9.RELEASE, JDK 1.8.
- JDK 1.8
- Spring Boot 1.5.9.RELEASE
- Maven 3.6.3

It can be built by mvn or an IDE like IDEA
- Build with Maven
```
cd <ProjectFolder>
mvn package
```
- Build with IntelliJ IDEA, import project, run Maven Build with package lifecycle. Note that lombok used, please install and enable lombok plugin in Settings\Plugins.

## Configuration
In order to provide flexibility, I create several config items as below. We will see the usage of some in Test section.
````
# thread pool size
thread.pool.size=132

# Used in CloudKitchenApplicationTests, send an order every this milliseconds
order.speed=500

# courier will arrive at most in this seconds
courier.arrive.slow=6
#courier will arrive at least in this seconds
courier.arrive.fast=2

# check all shelves for decayed food every this milliseconds
shelf.decayCheck.rate=1000

# maximum foods that hot shelf can hold
shelf.hot.capacity=10
# decay modifier of hot shelf
shelf.hot.decayModifier=1.0

# maximum foods that cold shelf can hold
shelf.cold.capacity=10
# decay modifier of cold shelf
shelf.cold.decayModifier=1.0

# maximum foods that frozen shelf can hold
shelf.frozen.capacity=10
# decay modifier of frozen shelf
shelf.frozen.decayModifier=1.0

# maximum foods that overflow shelf can hold
shelf.overflow.capacity=15
# decay modifier of overflow shelf
shelf.overflow.decayModifier=2.0
````

## Test
- Test with postman

The application provides a RESTful api /v1/orders/, you can use postman or any tools else to post Order with json format to this api. For example this is the api url from localhost
```
http://127.0.0.1:8080/v1/orders/
```
And here is an example of Order json
```
  {
    "id": "a8cfcb76-7f24-4420-a5ba-d46dd77bdffd",
    "name": "Banana Split",
    "temp": "frozen",
    "shelfLife": 20,
    "decayRate": 0.63
  }
```
- Run test case in IDEA, with JUnit (Recommend)

`CloudKitchenApplicationTests.runAllOrders` is the only but powerful test case, it works with configuration items, uses test\resources\order.json as data source. Next section will show some.
### Test normal situation
Set `order.speed=500`, test with `order.json`, run test case `CloudKitchenApplicationTests.runAllOrders` , we can see normal processing of orders, no use of overflow shelf.
### Test using overflow shelf
Set `order.speed=150`, test with `order.json`, run test case `CloudKitchenApplicationTests.runAllOrders`, we can see overflow shelf is used, but it is sufficient to handle all orders, no back move.(We saw `full` in log, but didn't see `move Food *** to backShelf`)
### Test that overflow shelf is full, back moving happens
Set `order.speed=80`, test with `order.json`, run test case `CloudKitchenApplicationTests.runAllOrders`, we can see overflow shelf is full, most time there is a free room in other temperature shelf, to which we can move [food](#food) back.(We saw `move Food *** to backShelf` in log)
### Test that overflow shelf is full, no backShelf available, [food](#food) discarded
Set `order.speed=50`, test with `order.json`, run test case `CloudKitchenApplicationTests.runAllOrders`, we can see overflow shelf is full and no backShelf available, many food discard.(We saw `discard` in log)
### Test that food deteriorates over time
Set `order.speed=500`, test with `order_short_life.json`(rename test\resources\order_short_life.json to order.json), run test case `CloudKitchenApplicationTests.runAllOrders` , we can see food deteriorates over time and is wasted, with value<=0.(We saw `wasted` in log)
### Other approach to test
We can also try to change configurations `courier.arrive.slow`, `courier.arrive.fast`, `shelf.*.capacity`, to simulate different courier speed, different shelf capacity, to over different process.


## Key Procedure

### <a name="food">Introduce Food entity</a>
After cooking, an Order becomes a Food. Food encapsulates the correspending order, adds produceTime, shelfDecayModifier properties, and responds to calculate age and value of the Food.

### Shelf choosing
Usually, a Food is placed into the Shelf which has the same temperature properties as the Order of the Food.

In case that the Shelf talked above is full, the Food is placed into the Overflow Shelf if it has free room.

Otherwise, we determine the behavior in two cases.

First, find all kinds of temperatures with existing Food in Overflow Shelf, then determine which Shelf among these temperatures has more that zero free room and has the maximun free room. If such one exists, move a Food with allowable temperature from Overlow Shelf to that Shelf, then place the new Food into Overflow Shelf.

Second, if no shelf talked above exists, find the food with lowest value in Overflow Shelf, discard it, then place the new Food into Overflow Shelf.

### Decay modifier
Although all shelves except Overflow Shelf have the same decayModifier in current stage, we still design individual decayModifier for each shelf to gain the flexibility.

When a Food is placed into a Shelf (include transfered from Overflow Shelf), we give it the decayModifier of that Shelf, so that the Food is able to calculate its value.

In current version, we didn't implement to calculate respective value when a Food ever lived in different Shelf, but record this in the TODO list, we can implement it in later version.
