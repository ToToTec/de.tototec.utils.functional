import $ivy.`de.tototec::de.tobiasroeser.mill.vcs.version::0.4.0`

import de.tobiasroeser.mill.vcs.version.VcsVersion
import mill._
import mill.scalalib._
import mill.scalalib.publish.{Developer, License, PomSettings, VersionControl}

trait PublishSettings extends PublishModule {
  override def pomSettings: T[PomSettings] = PomSettings(
    description = "Functional Utility Classes for working with Java 5+",
    organization = "de.tototec.utils.functional",
    url = "https://github.com/ToToTec/de.tototec.utils.functional",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github("ToToTec", "de.tototec.utils.functional"),
    developers = Seq(Developer(id = "lefou", name = "Tobias Roeser", url = "https://github.com/lefou")),
    packaging = "jar"
  )
  override def publishVersion: T[String] = VcsVersion.vcsState().format()
}

object root extends RootModule with MavenModule with PublishSettings {
  override def artifactName: T[String] = "de.tototec.utils.functional"
  override def javacOptions = Seq(
    "-source", "1.8", "-target", "8", "-deprecation", "-encoding", "UTF-8"
  )
  object test extends MavenModuleTests with TestModule.TestNg {
    override def ivyDeps = Agg(
      ivy"de.tototec:de.tobiasroeser.lambdatest:0.7.1",
      ivy"ch.qos.logback:logback-classic:1.2.3",
      ivy"org.testng:testng:6.14.3",
      ivy"com.lihaoyi:mill-contrib-testng:${mill.main.BuildInfo.millVersion}"
    )
  }
}