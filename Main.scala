import akka.actor._
import akka.actor.SupervisorStrategy._

class Main extends Actor with ActorLogging
{
  log.info("starting")

  val f = context.actorOf(Foreman.props, "foreman")
  f ! Foreman.Go(0)

  override val supervisorStrategy = OneForOneStrategy()
  {
    case e => log.info("error! " + e); Stop
  }

  def receive =
  {
    case Foreman.Done => context.stop(self)
    case x => log.info("got msg " + x)
  }

  /* NB: When overriding these, they have default behaviour (killing their
   * children and calling each other. For this kind of info, just enable akka
   * logging */
  override def preStart = log.info("main pre-start")
  override def preRestart(reason: Throwable, msg: Option[Any]) = log.info("main pre-restart. Error was " + reason)
  override def postRestart(reason: Throwable) = log.info("main post-restart. Error was " + reason)
  override def postStop = log.info("main post-stop")
}
