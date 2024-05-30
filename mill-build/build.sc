import mill._
import mill.runner.MillBuildRootModule
import mill.scalalib._

object root extends MillBuildRootModule {
  override def ivyDeps = Agg(
    // derive the publish version from latest git tags
    ivy"de.tototec::de.tobiasroeser.mill.vcs.version::0.4.0",
    // generate OSGi metadata
    ivy"de.tototec::de.tobiasroeser.mill.osgi::0.5.0-11-bf16ad",
    // check bincompat against previous releases
    ivy"com.github.lolgab::mill-mima::0.1.1",
    // generate test coverage data
    ivy"de.tototec::de.tobiasroeser.mill.jacoco::0.0.4"
  )
}
