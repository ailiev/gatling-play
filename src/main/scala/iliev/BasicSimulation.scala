package iliev

import io.gatling.core.Predef._ // 2
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation { // 3

  val feeder = Array(Map("foo" -> "foo1", "bar" -> "bar1"),
    Map("foo" -> "foo2", "bar" -> "bar2"),
    Map("foo" -> "foo3", "bar" -> "bar3")).random

  object Search {

    val feeder = csv("search.csv").random // 1, 2

    val search = exec(http("Home")
      .get("/"))
      .pause(1)
      .feed(feeder) // 3
      .exec(http("Search")
      .get("/computers")
      .queryParam("f", "${searchCriterion}") // 4
      .check(regex("""<a href="([^"]+)">${searchComputerName}</a>""").saveAs("computerURL"))) // 5
      .pause(1)
      .exec(http("Select")
      .get("${computerURL}")) // 6
      .pause(1)
  }

  val httpConf = http // 4
    .baseURL("http://computer-database.herokuapp.com") // 5
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn0 = scenario("BasicSimulation") // 7
    .exec(http("request_1")  // 8
      .get("/")) // 9
    .pause(5) // 10

  val scn = scenario("Scenario Name").exec(Search.search)

  setUp( // 11
    scn.inject(
      rampUsers(10) over(5 seconds)
        , constantUsersPerSec(20) during(5 seconds)
        //      atOnceUsers(1)
    ) // 12
  )
    .protocols(httpConf)
}
