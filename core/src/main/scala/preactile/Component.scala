package preactile

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.JSName

import preactile.impl.VNodeJS

trait Component[Props] extends PreactileComponent[Props, Nothing]:
  theComponent =>

  def render(props: Props): VNode

  // noinspection ScalaUnusedSymbol
  def didUpdate(oldProps: Props, instance: Instance, oldInstance: UndefOr[Instance]): Unit = ()

  def shouldUpdate(nextProps: Props, previous: Instance): Boolean = nextProps != previous.props

  override lazy val instanceConstructor: js.Dynamic = js.constructorOf[StatelessInstance]

  final private class StatelessInstance extends InstanceFacade[Props, Nothing]:

    override def componentDidMount(): Unit = didMount(this)

    override def componentWillMount(): Unit = willMount(this)

    override def componentWillUnmount(): Unit = willUnMount(this)

    @JSName("render")
    override def renderJS(props: js.Dynamic, state: js.Dynamic): VNodeJS =
      addSelectors(render(lookupProps(props)), this)

    override def shouldComponentUpdate(nextProps: js.Dynamic, nextState: js.Dynamic, nextContext: js.Dynamic): Boolean =
      shouldUpdate(lookupProps(nextProps), this)

    override def componentDidUpdate(oldProps: js.Dynamic, oldState: js.Dynamic, snapshot: js.Dynamic): Unit =
      didUpdate(
        oldProps = lookupProps(oldProps),
        oldState = lookupState(oldState),
        instance = this,
        oldInstance = snapshot.asInstanceOf[UndefOr[PreactileComponent[Props, Nothing]#Instance]],
      )

    override def componentDidCatch(e: js.Error): Unit = didCatch(e, this)
  end StatelessInstance
end Component

object Component:

  given selfComp[Comp <: Component[Comp], T <: Arg]: Conversion[Comp, T] = c => c.apply(c).asInstanceOf[T]
  def apply[Props](renderF: ComponentFunction[Props]): Component[Props] =
    new FunctionalComponent(renderF)

type ComponentFunction[T] = T => VNode

class FunctionalComponent[Props](renderF: ComponentFunction[Props]) extends Component[Props]:
  override def render(props: Props): VNode = renderF(props)
given Conversion[ComponentFunction[?], Component[?]] = (f: ComponentFunction[?]) => Component(f)
