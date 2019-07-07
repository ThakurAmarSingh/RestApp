package app.service
import akka.http.scaladsl.server.directives.Credentials
trait AuthenticationService {
    def myUserAuthenticator(credentials: Credentials): Option[String] =
    credentials match {
      case p @ Credentials.Provided(id) if p.verify("Welcome@123") => Some(id)
      case _ => None
    }
}
