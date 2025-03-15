package todo

import todo.model.*

import preactile.*
import preactile.dsl.css.*

object App:

  object css:
    import S.*

    object App
        extends CssClass(
          // spellchecker: disable-next-line
          font("14px 'Helvetica Neue', Helvetica, Arial, sans-serif"),
          lineHeight.em(1.4),
          background(" #e5e5e5)"),
          color("#4d4d4d"),
          minWidth.px(230),
          maxWidth.px(550),
          margin.apply("0", "auto"),
          fontWeight(300),
          textRendering.optimizeLegibility,
          S("-webkit-font-smoothing", "antialiased"),
        )

    object Info
        extends CssClass(
          margin("65px auto 0"),
          color.rgba(0, 0, 0, .6),
          fontSize.px(12),
          fontWeight(400),
          S("-webkit-font-smoothing", "antialiased"),
          textRendering.optimizeLegibility,
          textAlign.center,
          Selector(" p", S.lineHeight("1")),
        )

    object TodoApp
        extends CssClass(
          background.rgba(255, 255, 255, 0.67),
          margin("130px", "0", "40px", "0"),
          position.relative,
          boxShadow("0 3px 3px 0 rgba(0, 0, 0, 0.4), 0 25px 50px 0 rgba(0, 0, 0, 0.2)"),
          Selector(" :focus", outline("0")),
        )

    object Header
        extends CssClass(
          marginBlock.em(0.67),
          position.absolute,
          top.px(-155),
          width.pct(100),
          fontSize.px(100),
          fontWeight(300),
          textAlign.center,
          color.rgba(208, 50, 50, 0.75),
          textShadow("3px 3px 5px rgba(51, 29, 29, 0.4)"),
        )
  end css

  val component = TodosConduit.component: todoList =>
    E.body(
      css.App,
      E.section(
        css.TodoApp,
        E.div(
          E.header(
            E.h1(
              css.Header,
              "todos",
            ),
            Creator,
          ),
          when(todoList.todos.nonEmpty) {
            fragment(
              List.component,
              Footer.component,
            )
          },
        ),
      ),
      E.footer(
        css.Info,
        fragment(
          E.p("Double-click to edit a todo"),
          E.p("Rendered by ", E.strong("preactile"), " - a Scala.js UI library utilizing Preact"),
        ),
      ),
    )
end App
