package todo

import org.scalajs.dom.document

import preactile.*
import todo.model.TodosCircuit
import zio.*

object Main extends ZIOAppDefault:
  def run =
    for
      _ <- ZIO.attempt:
        val e = document.createElement("body")
        document.documentElement.replaceChild(e, document.body)
        preactile.preact.render(App, document.documentElement, e)
      c <- TodosCircuit.run(false)
    yield c
  end run
end Main
