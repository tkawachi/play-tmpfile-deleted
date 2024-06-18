package controllers

import play.api.mvc._

import java.nio.file.Files
import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def fileSize() =
    Action.async(parse.multipartFormData) { implicit request =>
      request.body.file("file") match {
        case Some(image) =>
          for {
            path <- Future(image.ref.path)
            _ <- Future(System.gc())
            _ <- Future(Thread.sleep(100))
          } yield Ok(Files.size(path).toString)
        case None =>
          Future.successful(BadRequest("No image supplied"))
      }
    }

}
