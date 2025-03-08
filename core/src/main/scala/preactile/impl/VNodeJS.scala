package preactile.impl

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

import org.scalajs.dom

import preactile.VNode

abstract class VNodeJS extends js.Object:

  @JSName("type")
  def `type`: String | js.Dynamic

  def props: js.Dynamic

  def key: js.UndefOr[String]

  def ref: js.UndefOr[js.Function1[dom.Element, Unit]]
end VNodeJS

object VNodeJS:
  given Conversion[VNodeJS, VNode] = vn =>
    new VNode:
      def vNode: VNodeJS = vn
