import mill._
import mill.runner.MillBuildRootModule
import mill.scalalib._

object root extends MillBuildRootModule {
  override def ivyDeps = Agg(
    ivy"de.tototec::de.tobiasroeser.mill.vcs.version::0.4.0",
    ivy"de.tototec::de.tobiasroeser.mill.osgi::0.5.0-11-bf16ad",
    ivy"com.github.lolgab::mill-mima::0.1.0"
  )
}
