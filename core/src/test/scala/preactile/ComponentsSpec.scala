package preactile
import scala.scalajs.js

import zio.*
import zio.test.*

object ComponentsSpec extends PreactileSpec:
  val spec = suite("Components")(test("A didCatch() error boundary should render an error condition"):
    object Catcher extends StatefulComponent[String, Option[js.Error]]:
      override def initialState(name: String): Option[js.Error] = None

      val SuperBorkedChildComponent: Component[String] = (name: String) =>
        parent.removeChild(parent) // this will throw an Exception
        E.span(name)

      override def render(name: String, error: Option[js.Error], instance: Catcher.Instance): VNode =
        error.fold(SuperBorkedChildComponent(name))(e => E.div(e.message))

      override def didCatch(e: js.Error, instance: Catcher.Instance): Unit = instance.setState(Some(e))
    end Catcher
    render(Catcher("foo"))
    checkAfter(10)(
      "<div>The node to be removed is not a child of this node.</div>"
    ) // check async so we can get the final re-render
  ) @@ TestAspect.sequential @@ TestAspect.withLiveClock
end ComponentsSpec
