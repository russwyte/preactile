package todo

import org.scalajs.dom.HTMLInputElement
import todo.model.*
import todo.model.TodoList.actions.SetAll

import preactile.*
import preactile.dsl.css.CssClass

object List extends TodoComponent[Unit, TodoList] with ClassSelector:
  override def lens = model

  object css:
    import S.*

    object Main
        extends CssClass(
          position.relative,
          zIndex(2),
          borderTop("1px solid #e6e6e6"),
        )

    object ToggleAll
        extends CssClass(
          textAlign.center,
          border.none,
          opacity(0),
          position.absolute,
          Selector(
            " + label",
            cursor.pointer,
            width.px(60),
            height.px(34),
            fontSize.zero,
            position.absolute,
            top.px(-52),
            left.px(-13),
            transform("rotate(90deg)"),
          ),
          Selector(
            " + label:before",
            content("'❯'"),
            fontSize.px(22),
            padding("10px 27px 10px 27px"),
          ),
        )

    object List
        extends CssClass(
          listStyle.none,
          Selector(" li:last-child", borderBottom("none")),
        )
  end css

  override def render(props: Unit, todos: TodoList): VNode =
    E.section(
      css.Main,
      E.input(
        css.ToggleAll,
        A.id("toggle-all"),
        A.`type`("checkbox"),
        A.checked(todos.filtered.forall(_.complete)),
        A.onChange { e =>
          Todos(SetAll(e.target.asInstanceOf[HTMLInputElement].checked))
        },
      ),
      E.label(
        A.`for`("toggle-all"),
        if todos.filtered.nonEmpty && todos.filtered.forall(_.complete) then S.color("#000000") else S.color("#e6e6e6"),
      ),
      E.ul(
        css.List,
        S.margin.zero,
        S.padding.zero,
        todos.filtered.map(x => Item(x).withKey(x.key)),
      ),
    )
end List
