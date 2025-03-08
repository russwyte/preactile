package preactile

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

import preactile.impl.VNodeJS

import conduit.*

trait ConduitComponent[Props, Model <: Product: Optics as optics, State] extends PreactileComponent[Props, State]:
  theComponent =>
  val model = optics
  def conduit: Conduit[Model]
  def render(props: Props, state: State): VNode
  def lens: Lens[Model, State]

  def shouldUpdate(nextProps: Props, nextState: State, previous: Instance): Boolean =
    previous.state != nextState || previous.props != nextProps
  override lazy val instanceConstructor: js.Dynamic = js.constructorOf[CircuitInstance]

  final private class CircuitInstance extends InstanceFacade[Props, State]:
    private var unsubscribe: Listener[Model, State] = Listener.unit(lens)

    override def componentDidUpdate(oldProps: js.Dynamic, oldState: js.Dynamic, snapshot: js.Dynamic): Unit =
      didUpdate(
        oldProps = lookupProps(oldProps),
        oldState = lookupState(oldState),
        instance = this,
        oldInstance = snapshot.asInstanceOf[js.UndefOr[ConduitComponent[Props, Model, State]#Instance]],
      )
    override def componentDidMount(): Unit = didMount(this)

    @JSName("render")
    override def renderJS(p: js.Dynamic, s: js.Dynamic): VNodeJS =
      addSelectors(render(lookupProps(p), lookupState(s)), this)

    override def componentWillMount(): Unit =
      willMount(this)
      setState(conduit.unsafe.zoom(lens))
      unsubscribe = conduit.unsafe.subscribe(lens)(x => setState(x))
    override def componentWillUnmount(): Unit = conduit.unsafe.unsubscribe(unsubscribe)

    override def shouldComponentUpdate(nextProps: js.Dynamic, nextState: js.Dynamic, context: js.Dynamic): Boolean =
      theComponent.shouldUpdate(lookupProps(nextProps), lookupState(nextState), this)

    override def componentDidCatch(e: js.Error): Unit = didCatch(e, this)
  end CircuitInstance
end ConduitComponent
