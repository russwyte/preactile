package todo

import preactile.S.*
import preactile.*
import todo.model.TodoList.actions.ApplyFilter
import todo.model.*

object FilterButton
    extends StyledElement(E.li)(
      display.inline,
      color.inherit,
      margin.px(3),
      padding("3px 7px"),
      textDecoration.none,
      border("1px solid transparent"),
      borderRadius.px(3),
      cursor.pointer,
      Selector(":hover", borderColor.rgba(175, 47, 47, 0.1)),
    ):

  def apply(todoList: TodoList, filter: Filter): VNode =
    FilterButton(
      filter.getClass.getSimpleName.init,
      S.borderColor.rgba(175, 47, 47, 0.2).when(todoList.filter == filter),
      A.onClick(_ => TodosCircuit.unsafe(ApplyFilter(filter))),
    )
end FilterButton
