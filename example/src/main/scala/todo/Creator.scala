package todo

import org.scalajs.dom.HTMLInputElement
import todo.model.*
import todo.model.TodoList.actions.Add

import preactile.*
import preactile.S.*

object Creator extends Component[Unit]:

  object Input
      extends StyledElement(E.input)(
        position.relative,
        margin.zero,
        width.pct(100),
        fontSize.px(24),
        fontFamily.inherit,
        fontWeight.inherit,
        lineHeight.em(1.4),
        color.inherit,
        boxSizing.borderBox,
        borderBottom("1px solid #ededed"),
        padding("16px 16px 16px 60px"),
        border.none,
        background.rgba(0, 0, 0, 0.05),
        Selector("::placeholder", fontStyle.italic, fontWeight(300), color.rgba(75, 25, 25, .5)),
      )

  override def render(props: Unit): VNode =
    Input(
      A.placeholder("What needs to be done?"),
      A.onKeyDown { x =>
        val e = x.target.asInstanceOf[HTMLInputElement]
        if x.keyCode == ENTER && e.value.nonEmpty then
          Todos(Add(Todo(e.value)))
          e.value = ""
      },
    )
end Creator
