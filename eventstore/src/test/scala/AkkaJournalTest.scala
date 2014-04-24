
import com.typesafe.config.ConfigFactory
import akka.persistence.journal.JournalSpec

class ReactiveCouchbaseJournal extends JournalSpec {
  lazy val config = ConfigFactory.parseString("...")

  override def beforeAll() {
    // before plugin initialization
    // ...

    // plugin initialization
    super.beforeAll()
  }

  override def afterAll() {
    // plugin shutdown
    super.afterAll()

    // after plugin shutdown
    // ...
  }
}