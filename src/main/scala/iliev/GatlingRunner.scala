package iliev

import io.gatling.app.Gatling
import io.gatling.core.scenario.Simulation

/**
 * Created by sasho on 30/12/13.
 */
object GatlingRunner extends App
{
  sys.exit(
    Gatling.runGatling(Array("--simulation", classOf[BasicSimulation].getName),
      Some(classOf[BasicSimulation].asInstanceOf[Class[Simulation]]))
  )
}
