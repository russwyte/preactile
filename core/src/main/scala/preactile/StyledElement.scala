package preactile

import preactile.dsl.ElementConstructor
import preactile.dsl.css.CssClass
import preactile.dsl.css.Styles.DeclarationOrSelector

abstract class StyledElement(elementConstructor: ElementConstructor)(ds: DeclarationOrSelector*) extends CssClass(ds*):
  def apply(as: Arg*): VNode = elementConstructor(args(as = this, as))
