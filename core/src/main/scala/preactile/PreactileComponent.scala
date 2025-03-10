package preactile

import scala.scalajs.js
import scala.scalajs.js.Dictionary
import scala.scalajs.js.UndefOr

import preactile.impl.Preactile

//noinspection ScalaUnusedSymbol
trait PreactileComponent[Props, State]:

  type P = Props

  import dictionaryNames.*

  def instanceConstructor: js.Dynamic

  lazy val classForClass = ClassSelector.makeCssClass(this.getClass.getName)

  type Instance = preactile.Instance[Props, State]

  def didMount(instance: Instance): Unit = ()

  def willMount(instance: Instance): Unit = ()

  def willUnMount(instance: Instance): Unit = ()

  def didCatch(e: js.Error, instance: Instance): Unit = ()

  def didUpdate(
      oldProps: Props,
      oldState: State,
      instance: Instance,
      oldInstance: UndefOr[Instance],
  ): Unit = ()

  def baseDictionary(props: Props): Dictionary[js.Any] =
    js.Dictionary(
      Seq[(String, js.Any)](
        (PropsFieldName, props.asInstanceOf[js.Any]),
        ("key", classForClass), // this is a precaution - I may want to make this optional
      )*
    )

  def apply(props: Props): VNode = Preactile
    .h(instanceConstructor, baseDictionary(props))

  def addSelectors(n: VNode, facade: InstanceFacade[Props, State]): VNode =
    type CI = ClassSelector & InstanceDataSelector
    this match
      case selectors: CI =>
        n.withRef { e =>
          selectors.addDataAttribute(e, facade)
          selectors.addClass(e)
        }
      case classSelector: ClassSelector => n.withRef(e => classSelector.addClass(e))
      case instanceDataSelector: InstanceDataSelector =>
        n.withRef(e => instanceDataSelector.addDataAttribute(e, facade))
      case _ => n
    end match
  end addSelectors
end PreactileComponent

object PreactileComponent:
  given Conversion[PreactileComponent[Unit, ?], VNode] = _.apply(())
