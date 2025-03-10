package todo

import preactile.dsl.css.CssClass
import preactile.*
import todo.model.Todo

object Item extends Component[Todo]:

  object styles:
    import S.*

    object Base
        extends CssClass(
          position.relative,
          fontSize.px(24),
          borderBottom("1px solid rgba(237, 237, 237, 0.59)"),
          Selector(" label")(
            S("word-break")("break-all"),
            padding("15px 15px 15px 60px"),
            display.block,
            S.backgroundRepeat("no-repeat"),
            lineHeight.em(1.2),
            transition("color .4s"),
            fontSize.px(24),
          ),
          Selector(":last-child")(
            borderBottom("none")
          ),
        )

    object Editing
        extends CssClass(
          S.borderBottom("none"),
          S.padding.zero,
        )
  end styles

  override def render(todo: Todo): VNode =
    E.li(
      styles.Base,
      styles.Editing.when(todo.editing),
      if todo.editing then Editor(todo)
      else Viewer(todo),
    )
end Item
