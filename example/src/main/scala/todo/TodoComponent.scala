package todo

import conduit.Conduit
import preactile.{ConduitComponent, ClassSelector}
import todo.model.*
import conduit.Optics

abstract class TodoComponent[P, S] extends ConduitComponent[P, TodoList, S] with ClassSelector:
  val conduit  = TodosCircuit
  val todoList = Optics[TodoList]
