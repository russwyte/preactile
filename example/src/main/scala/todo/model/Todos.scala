package todo.model

import zio.*
import conduit.*

val echo = handle[TodoList]:
  case a => m => Console.printLine(s"$a: $m").as(ActionResult.terminal(m))

val TodosConduit = Conduit.make(
  TodoList(Seq.empty, Filter.All, true)
)(
  TodoList.handler ++ echo
)

val Todos = TodosConduit.unsafe
