// package preactile.dsl.css

// import scala.annotation.unused
// import scala.concurrent.Future
// import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
// import scala.scalajs.js
// import scala.scalajs.js.Promise
// import scala.scalajs.js.UndefOr
// import scala.scalajs.js.annotation.JSImport

// object AutoPrefixed:

//   object Options extends js.Object:
//     val from: UndefOr[Nothing] = js.undefined
//     // spellchecker: disable-next-line
//     val overrideBrowserslist: js.Array[String] = js.Array("last 2 versions", "> 5%")

//   @js.native @JSImport("postcss", JSImport.Namespace)
//   class PostCSS(@unused arg: js.Array[js.Any]) extends js.Object:
//     def process(s: String, options: js.Any): Promise[Result] = js.native

//   @js.native @JSImport("autoprefixer", JSImport.Namespace)
//   object AutoPrefixer extends js.Object:
//     def apply(options: js.Any): js.Any = js.native

//   trait Result extends js.Object:
//     def css: String

//   def apply(css: String): Future[String] =
//     try Processor.process(css, Options).toFuture.map(_.css)
//     catch
//       case e: js.JavaScriptException =>
//         js.Dynamic.global.console.error("Error auto-prefixing:\n", css, e.asInstanceOf[js.Any])
//         Future.successful(css)
//   val AP        = AutoPrefixer(Options)
//   val Processor = new PostCSS(js.Array(AP))

// end AutoPrefixed
