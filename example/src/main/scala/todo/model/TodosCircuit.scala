package todo.model

import zio.*

import conduit.*
type Todos = Conduit[TodoList]
val echo = handle[TodoList]:
  case a => m => Console.printLine(s"$a: $m").as(ActionResult.terminal(m))
val Todos: UIO[Todos] = Conduit(TodoList(Seq.empty, Filter.All, true))(TodoList.handler ++ echo)

val TodosCircuit = get(Todos)

def get[A](zio: UIO[A]): A = Unsafe.unsafe: u =>
  given Unsafe = u
  Runtime.default.unsafe.run(zio).getOrThrow()
