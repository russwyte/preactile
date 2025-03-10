package todo

import org.scalajs.dom.document

import preactile.*
import todo.model.TodosConduit
import zio.*

object Main extends ZIOAppDefault:
  def run =
    for
      _ <- ZIO.attempt:
        val e = document.createElement("body")
        document.documentElement.replaceChild(e, document.body)
        preactile.preact.render(App, document.documentElement, e)
      c <- TodosConduit.run(false)
    yield c
  end run
end Main
