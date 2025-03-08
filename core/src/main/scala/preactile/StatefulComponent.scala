package preactile

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.JSName

import preactile.impl.VNodeJS

trait StatefulComponent[Props, State] extends PreactileComponent[Props, State]:
  theComponent =>
  def initialState(props: Props): State

  def deriveState(props: Props, oldState: State) = oldState

  def shouldUpdate(nextProps: Props, nextState: State, previous: Instance): Boolean =
    previous.state != nextState || previous.props != nextProps

  def render(props: Props, state: State, instance: Instance): VNode

  override lazy val instanceConstructor: js.Dynamic = js.constructorOf[StatefulInstance]

  final private class StatefulInstance extends InstanceFacade[Props, State]:

    override def componentDidMount(): Unit = didMount(this)

    override def componentWillUnmount(): Unit = willMount(this)

    @JSName("render")
    override def renderJS(props: js.Dynamic, state: js.Dynamic): VNodeJS =
      addSelectors(render(lookupProps(props), lookupState(state), instance = this), this)

    override def shouldComponentUpdate(nextProps: js.Dynamic, nextState: js.Dynamic, nextContext: js.Dynamic): Boolean =
      theComponent.shouldUpdate(lookupProps(nextProps), lookupState(nextState), previous = this)

    override def componentDidUpdate(oldProps: js.Dynamic, oldState: js.Dynamic, snapshot: js.Dynamic): Unit =
      theComponent.didUpdate(
        lookupProps(oldProps),
        lookupState(oldState),
        instance = this,
        snapshot.asInstanceOf[UndefOr[StatefulComponent[Props, State]#Instance]],
      )

    override def componentWillReceiveProps(nextProps: js.Dynamic, nextContext: js.Dynamic): Unit =
      val res = theComponent.deriveState(lookupProps(nextProps), lookupState())
      setState(res)

    override def componentWillMount(): Unit =
      setState(theComponent.initialState(props))
      willMount(this)

    override def componentDidCatch(e: js.Error): Unit =
//      log("caught", e)
      didCatch(e, this)
  end StatefulInstance
end StatefulComponent

object StatefulComponent:
  given selfComp[Comp <: StatefulComponent[Comp, ?], T <: Arg]: Conversion[Comp, T] = c => c.apply(c).asInstanceOf[T]
