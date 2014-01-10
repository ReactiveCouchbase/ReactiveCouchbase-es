ReactiveCouchbase Event Store
===================================

The Couchbase Event store provide a very simple way to store application events. 

It uses <a href="http://reactivecouchbase.org">ReactiveCoucbase</a> to work and can be nicely used from within a Play 2/Akka application.

You can find more informations about <a href="https://github.com/ReactiveCouchbase/ReactiveCouchbase-core">ReactiveCouchbase-core</a> and <a href="https://github.com/ReactiveCouchbase/ReactiveCouchbase-play">ReactiveCouchbase-play</a> on their github pages.

You can use ReactiveCouchbase-es that way :

```scala

case class CartCreated(customerId: Long, message: String)

object CartCreated {
  val cartCreatedFormat = Json.format[CartCreated]
}

object EventSourcingBoostrap {

  lazy val driver = ReactiveCouchbaseDriver()

  lazy val couchbaseES = CouchbaseEventSourcing( ActorSystem("couchbase-es-1"), driver.bucket("es") )
    .registerFormatter(CartCreated.cartCreatedFormat)

  def bootstrap() = {
    couchbaseES.replayAll()
  }
}

object Cart {
  val cartProcessor = EventSourcingBoostrap.couchbaseES
                        .processorOf(Props(new CartProcessor with EventStored))
  def createCartForUser(user: User) {
    cartProcessor ! Message.create( CartCreated(user.id, "Useful message") )
  }
}

class CartProcessor extends Actor {
  // Application state is here !!!
  var numberOfCreatedCart = 0
  def receive = {
    case msg: CartCreated => {
      numberOfCreatedCart = numberOfCreatedCart + 1
      println( s"[CartProcessor] live carts ${counter} - Last message (${msg.message})" )
    }
    case _ =>
  }
}

val user1 = User( ... )
val user2 = User( ... )
val user3 = User( ... )

EventSourcingBoostrap.bootstrap()
// prints nothing if bucket is empty

Cart.createCartForUser( user1 )
// prints : [CartProcessor] live carts 1 - Last message (Useful message)
Cart.createCartForUser( user2 )
// prints : [CartProcessor] live carts 2 - Last message (Useful message)
Cart.createCartForUser( user3 )
// prints : [CartProcessor] live carts 3 - Last message (Useful message)

EventSourcingBoostrap.bootstrap()
// prints : [CartProcessor] live carts 4 - Last message (Useful message)
// prints : [CartProcessor] live carts 5 - Last message (Useful message)
// prints : [CartProcessor] live carts 6 - Last message (Useful message)

Cart.createCartForUser( user1 )
// prints : [CartProcessor] live carts 7 - Last message (Useful message)
Cart.createCartForUser( user2 )
// prints : [CartProcessor] live carts 8 - Last message (Useful message)
Cart.createCartForUser( user3 )
// prints : [CartProcessor] live carts 9 - Last message (Useful message)

```