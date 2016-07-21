import akka.actor._

object Worker
{
  def props = Props[Worker]

  /* Input */
  case class DoWork(x: Int)

  /* Output */
  case class Result(x: Int)
}

class Worker extends Actor with ActorLogging
{
  import Worker._

  def receive =
  {
    case DoWork(x) => sender ! "random message up"; sender ! Result(10 / x); context.stop(self)
    case x => log.info("got msg " + x)
  }

  override def preStart = log.info("worker pre-start")
  override def preRestart(reason: Throwable, msg: Option[Any]) = log.info("worker pre-restart. Error was " + reason)
  override def postRestart(reason: Throwable) = log.info("worker post-restart. Error was " + reason)
  override def postStop = log.info("worker post-stop")
}
