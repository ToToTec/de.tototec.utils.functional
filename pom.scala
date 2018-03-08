import org.sonatype.maven.polyglot.scala.model._
import scala.collection.immutable.Seq

val gav = "de.tototec" % "de.tototec.utils.functional" % "1.1.0-SNAPSHOT"
val url = "https://github.com/ToToTec/de.tototec.utils.functional"

object Deps {
  val bndLib = "biz.aQute.bnd" % "biz.aQute.bndlib" % "3.2.0"
  val asciiDoclet = "org.asciidoctor" % "asciidoclet" % "1.5.4"
  val testng = "org.testng" % "testng" % "6.9.10"
  val lambdatest = "de.tototec" % "de.tobiasroeser.lambdatest" % "0.4.0"
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.25"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
}

object Plugins {
  val animalSniffer = "org.codehaus.mojo" % "animal-sniffer-maven-plugin" % "1.15"
  val bundle = "org.apache.felix" % "maven-bundle-plugin" % "3.2.0"
  val javadoc = "org.apache.maven.plugins" % "maven-javadoc-plugin" % "2.10.3"
}

Model(
  gav = gav,
  packaging = "bundle",
  modelVersion = "4.0.0",
  name = "Functional Utils",
  description = "Functional Utility Classes for working with Java 5+",
  url = url,
  developers = Seq(
    Developer(
      email = "le.petit.fou@web.de",
      name = "Tobias Roeser"
    )
  ),
  licenses = Seq(
    License(
      name = "Apache License, Version 2.0",
      url = "http://www.apache.org/licenses/LICENSE-2.0.txt",
      distribution = "repo"
    )
  ),
  scm = Scm(
    url = url
  ),
  dependencies = Seq(
    Deps.testng % "test",
    Deps.lambdatest % "test",
    Deps.slf4j % "test",
    Deps.logbackClassic % "test"
  ),
  properties = Map(
    "bundle.namespace" -> gav.artifactId,
    "project.build.sourceEncoding" -> "UTF-8",
    "maven.compiler.source" -> "1.5",
    "maven.compiler.target" -> "1.5",
    "maven.compiler.testTarget" -> "1.8",
    "maven.compiler.testSource" -> "1.8"
  ),
  build = Build(
    plugins = Seq(
      // Check that no Java6+ API is used
      Plugin(
        Plugins.animalSniffer,
        executions = Seq(
          Execution(
            id = "signature-check",
            phase = "test",
            goals = Seq("check")
          )
        ),
        configuration = Config(
          signature = Config(
            groupId = "org.codehaus.mojo.signature",
            artifactId = "java15",
            version = "1.0"
          )
        )
      ),
      // Package OSGi-compatible JAR and check version against previous release version
      Plugin(
        Plugins.bundle,
        extensions = true,
        executions = Seq(
          Execution(
            phase = "verify",
            goals = Seq("baseline"),
            configuration = Config(
              failOnError = "false"
            )
          )
        ),
        dependencies = Seq(
          Deps.bndLib
        ),
        configuration = Config(
          unpackBundle = "true",
          instructions = Config(
            _include = "osgi.bnd"
          )
        )
      ),
      // Use Asciidoclet processor instead of standard Javadoc
      Plugin(
        Plugins.javadoc,
        configuration = Config(
          source = "${maven.compiler.source}",
          doclet = "org.asciidoctor.Asciidoclet",
          docletArtifact = Config(
            groupId = Deps.asciiDoclet.groupId.get,
            artifactId = Deps.asciiDoclet.artifactId,
            version = Deps.asciiDoclet.version.get
          ),
          overview = "README.adoc",
          additionalparam = s"""--base-dir "$${project.basedir}"
            | --attribute "name=${gav.artifactId}"
						| --attribute "version=${gav.version.get}"
						| --attribute "functionalversion=${gav.version.get}"
						| --attribute "title-link=${url}[${gav.artifactId} ${gav.version.get}]"
						| --attribute "env-asciidoclet=true"""".stripMargin
        )
      )
    )
  )
)
