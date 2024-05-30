import com.github.lolgab.mill.mima.Mima
import de.tobiasroeser.mill.jacoco.JacocoTestModule
import de.tobiasroeser.mill.osgi.{OsgiBundleModule, OsgiHeaders}
import de.tobiasroeser.mill.vcs.version.VcsVersion
import mill._
import mill.scalalib._
import mill.scalalib.publish.{Developer, License, PomSettings, VersionControl}

// Enable the Mill meta-build under `mill-build`
import $meta.`mill-build`

trait PublishSettings extends PublishModule {
  override def pomSettings: T[PomSettings] = PomSettings(
    description = "Functional Utility Classes for working with Java 8+",
    organization = "de.tototec",
    url = "https://github.com/ToToTec/de.tototec.utils.functional",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github("ToToTec", "de.tototec.utils.functional"),
    developers = Seq(Developer(id = "lefou", name = "Tobias Roeser", url = "https://github.com/lefou")),
    packaging = "jar"
  )
  override def publishVersion: T[String] = VcsVersion.vcsState().format()
}

trait MimaSettings extends Mima {
  override def mimaPreviousVersions = T{ Seq("2.3.0", "2.2.0", "2.1.0", "2.0.0") }
  // TODO: Cleanup below, once https://github.com/lolgab/mill-mima/pull/155 is released
  override def scalaVersion = "2.13.14"
  override def artifactId: T[String] = T{ artifactName()}
}

object root extends RootModule
    with MavenModule
    with PublishSettings
    with OsgiBundleModule
    with MimaSettings {

  override def artifactName: T[String] = "de.tototec.utils.functional"
  override def javacOptions = Seq(
    "-source", "1.8", "-target", "8", "-deprecation", "-encoding", "UTF-8"
  )
  override def javadocOptions: T[Seq[String]] = Seq(
    "-Xdoclint:none"
  )
  override def osgiHeaders: T[OsgiHeaders] = super.osgiHeaders().copy(
  `Export-Package` = Seq("de.tototec.utils.functional")
  )

  object test extends MavenModuleTests with TestModule.TestNg with JacocoTestModule {
    override def ivyDeps = Agg(
      ivy"de.tototec:de.tobiasroeser.lambdatest:0.7.1",
      ivy"ch.qos.logback:logback-classic:1.2.3",
      ivy"org.testng:testng:6.14.3",
      ivy"com.lihaoyi:mill-contrib-testng:${mill.main.BuildInfo.millVersion}"
    )
  }

}

