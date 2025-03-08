package preactile.dsl.css
import scala.scalajs.js

import org.scalajs.dom

import preactile.Attribute
import preactile.ClassSelector
import preactile.dsl.css.Styles.DeclarationOrSelector
import preactile.dsl.css.Styles.KeyFrames
import preactile.dsl.css.Styles.MediaQuery
import preactile.dsl.css.Styles.Selector

abstract class CssClass(ds: DeclarationOrSelector*) extends Attribute:
  self =>
  override val name                       = "class"
  lazy val className: String              = ClassSelector.makeCssClass(self.getClass.getName)
  def members: Seq[DeclarationOrSelector] = ds
  lazy val selector                       = s".$className"
  override val value: js.Any              = className
  lazy val sel: Selector                  = Selector(selector, members*)

  def mkString: String =
    val keyframes    = js.Array[KeyFrames]()
    val mediaQueries = js.Array[MediaQuery]()
    val mainCss      = sel.mkString(className, keyframes, mediaQueries)
    val keyFramesCss = keyframes
      .map { k =>
        Selector(s"@keyframes $className-${k.name}", k.selectors*)
      }
      .mkString("\n")
    val mediaQueriesCss = mediaQueries.map(_.render).mkString("\n")
    mainCss + keyFramesCss + mediaQueriesCss
  end mkString

  private def appendStyle(): Unit =
    try
      val d                  = org.scalajs.dom.document
      val style: dom.Element = d.createElement("style")
      style.setAttribute("data-style-for", className)
      style.appendChild(d.createTextNode(mkString))
      Option(d.head.querySelector(s"[data-style-for='$className']")).fold(d.head.appendChild(style))(e =>
        d.head.replaceChild(style, e)
      )
      // AutoPrefixed(mkString).map { css =>
      //   style.appendChild(d.createTextNode(css))
      //   Option(d.head.querySelector(s"[data-style-for='$className']")).fold(d.head.appendChild(style))(e =>
      //     d.head.replaceChild(style, e)
      //   )
      // }
    catch
      // this should only happen when there is no DOM - like in certain test runners.
      case e: Throwable => ()

  self.appendStyle()
end CssClass
