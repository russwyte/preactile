package todo

import conduit.Conduit
import preactile.{ConduitComponent, ClassSelector}
import todo.model.*
import conduit.Optics

abstract class TodoComponent[P, S] extends ConduitComponent[P, TodoList, S] with ClassSelector:
  val conduit  = TodosConduit
  val todoList = Optics[TodoList]
