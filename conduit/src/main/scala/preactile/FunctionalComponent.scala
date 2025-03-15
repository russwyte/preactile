package preactile

import conduit.*

private class FunctionalComponent[Props, Model <: Product: Optics as m, State](
    conduit: Conduit[Model],
    lensF: Optics[Model] => Lens[Model, State] = identity,
    renderF: (Props, State) => VNode,
) extends ConduitComponent[Props, Model, State](conduit, lensF):
  override def render(props: Props, state: State): VNode = renderF(props, state)

extension [Model <: Product: Optics as model](conduit: Conduit[Model])
  def component[Props, State](
      lens: Optics[Model] => Lens[Model, State]
  )(render: (Props, State) => VNode): ConduitComponent[Props, Model, State] =
    FunctionalComponent(conduit, lens, render)

  def component[Props](
      render: (Props, Model) => VNode
  ): ConduitComponent[Props, Model, Model] =
    FunctionalComponent(conduit, identity, render)

  def component(
      render: Model => VNode
  ): ConduitComponent[Unit, Model, Model] =
    FunctionalComponent(conduit, identity, (_, m) => render(m))

  inline def component[Props, State](
      inline path: Model => State
  )(render: (Props, State) => VNode): FunctionalComponent[Props, Model, State] =
    FunctionalComponent(
      conduit,
      (m: Optics[Model]) => m(path),
      render,
    )
  inline def component[State](
      inline path: Model => State
  )(render: State => VNode): FunctionalComponent[Any, Model, State] =
    FunctionalComponent(
      conduit,
      (m: Optics[Model]) => m(path),
      (_, m) => render(m),
    )

end extension
