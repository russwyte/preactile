package todo

import todo.model.*

import preactile.ClassSelector
import preactile.ConduitComponent

import conduit.Conduit
import conduit.Optics

abstract class TodoComponent[P, S] extends ConduitComponent[P, TodoList, S] with ClassSelector:
  val conduit  = TodosConduit
  val todoList = Optics[TodoList]
