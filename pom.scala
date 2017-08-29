import org.sonatype.maven.polyglot.scala.model._
import scala.collection.immutable.Seq

val projectName = "de.tototec.utils.functional"
val projectVersion = "0.8-SNAPSHOT"

object Deps {
  val bndLib = "biz.aQute.bnd" % "biz.aQute.bndlib" % "3.2.0"
  val asciiDoclet = "org.asciidoctor" % "asciidoclet" % "1.5.4"
}

Model(
  gav = "de.tototec" % projectName % projectVersion,
  packaging = "bundle",
  modelVersion = "4.0.0",
  name = "Functional Utils",
  description = "Functional Utility Classes for working with Java 5+",
  url = "https://github.com/ToToTec/de.tototec.utils.functional",
  developers = Seq(
    Developer(
      email = "le.petit.fou@web.de",
      name = "Tobias Roeser")),
  licenses = Seq(
    License(
      name = "Apache License, Version 2.0",
      url = "http://www.apache.org/licenses/LICENSE-2.0.txt",
      distribution = "repo")),
  scm = Scm(
    url = "https://github.com/ToToTec/de.tototec.utils.functional"),
  dependencies = Seq(
    "org.testng" % "testng" % "6.9.10" % "test",
    "de.tototec" % "de.tobiasroeser.lambdatest" % "0.2.3" % "test"),
  properties = Map(
    "bundle.namespace" -> projectName,
    "project.build.sourceEncoding" -> "UTF-8",
    "maven.compiler.source" -> "1.5",
    "maven.compiler.target" -> "1.5",
    "maven.compiler.testTarget" -> "1.8",
    "maven.compiler.testSource" -> "1.8"),
  build = Build(
    plugins = Seq(
      // Check that no Java6+ API is used
      Plugin(
        "org.codehaus.mojo" % "animal-sniffer-maven-plugin" % "1.15",
        executions = Seq(
          Execution(
            id = "signature-check",
            phase = "test",
            goals = Seq("check"))),
        configuration = Config(
          signature = Config(
            groupId = "org.codehaus.mojo.signature",
            artifactId = "java15",
            version = "1.0"))),
      // Package OSGi-compatible JAR and check version against previous release version
      Plugin(
        "org.apache.felix" % "maven-bundle-plugin" % "3.2.0",
        extensions = true,
        executions = Seq(
          Execution(
            phase = "verify",
            goals = Seq("baseline"),
            configuration = Config(
              failOnError = "false"))),
        dependencies = Seq(
          Deps.bndLib),
        configuration = Config(
          unpackBundle = "true",
          instructions = Config(
            _include = "osgi.bnd"))),
      // Use Asciidoclet processor instead of standard Javadoc
      Plugin(
        "org.apache.maven.plugins" % "maven-javadoc-plugin" % "2.10.3",
        configuration = Config(
          source = "${maven.compiler.source}",
          doclet = "org.asciidoctor.Asciidoclet",
          docletArtifact = Config(
            groupId = Deps.asciiDoclet.groupId.get,
            artifactId = Deps.asciiDoclet.artifactId,
            version = Deps.asciiDoclet.version.get),
          overview = "README.adoc",
          additionalparam = s"""--base-dir "$${project.basedir}"
            | --attribute "name=${projectName}"
						| --attribute "version=${projectVersion}"
						| --attribute "functionalversion=${projectVersion}"
						| --attribute "title-link=https://github.com/ToToTec/de.tototec.utils.functional[${projectName} ${projectVersion}]"
						| --attribute "env-asciidoclet=true"""".stripMargin)))))
