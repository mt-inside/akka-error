import akka.actor._
import akka.actor.SupervisorStrategy._

object Foreman
{
  def props = Props[Foreman]

  /* input */
  case class Go(x: Int)

  /* output */
  case object Done
}

class Foreman extends Actor with ActorLogging
{
  override val supervisorStrategy = OneForOneStrategy()
  {
    case e => log.info("error! " + e); Escalate
  }

  def receive =
  {
    case Foreman.Go(x) => {
      val w = context.actorOf(Worker.props, "worker1")
      w ! Worker.DoWork(x)
      w ! "random message down"
    }
    case Worker.Result(d) => log.info("answer is... " + d); context.parent ! Foreman.Done; context.stop(self)
    case x => log.info("got msg " + x)
  }

  override def preStart = log.info("supervisor pre-start")
  override def preRestart(reason: Throwable, msg: Option[Any]) = log.info("supervisor pre-restart. Error was " + reason)
  override def postRestart(reason: Throwable) = log.info("supervisor post-restart. Error was " + reason)
  override def postStop = log.info("supervisor post-stop")
}
