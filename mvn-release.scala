// You should include this file from your pom.scala
// In your pom.scala (or somewhere else) you must define
// - a Deps object with the required dependencies
// - a Plugins object with the required plugin dependencies

import org.sonatype.maven.polyglot.scala.model._
import scala.collection.immutable._

object ReleaseProfile {

  val mvnDeploySettingsFile = "mvn-deploy-settings.xml"

  val mvnDeploySettings =
    """|<settings>
       |  <servers>
       |    <server>
       |      <id>ossrh</id>
       |      <!-- Enter your credentials here -->
       |      <username>your-username</username>
       |      <password>your-password</password>
       |    </server>
       |  </servers>
       |</settings>
       |""".stripMargin

  def echoMvnDeploySettings: Config = new Config(
    mvnDeploySettings.split("[\n]").toList.map { line =>
      "echo" -> Some(Config(
        `@file` = "${project.basedir}/" + mvnDeploySettingsFile,
        `@append` = "true",
        `@message` = line + "${line.separator}"
      ))
    }
  )

  def apply(
      skipDeploy: Boolean = false,
      autoReleaseAfterClose: Boolean = false
  ): Profile = Profile(
    id = "release",
    build = BuildBase(
      plugins = Seq(
        Plugin(
          Plugins.nexusStaging,
          executions = Seq(
            Execution(
              id = "deploy-to-ossrh",
              goals = Seq("deploy"),
              configuration = Config(
                skipNexusStagingDeployMojo = skipDeploy,
                serverId = "ossrh",
                nexusUrl = "https://oss.sonatype.org",
                autoReleaseAfterClose = if(autoReleaseAfterClose) "true" else "false"
              )
            )
          )
        ),
        Plugin(
          Plugins.antrun,
          dependencies = Seq(
            Dependency(Deps.antContrib, exclusions = Seq("*" % "*"))
          ),
          executions = Seq(
            Execution(
              id = "prepare-nexus-staging",
              phase = "initialize",
              goals = Seq("run"),
              configuration = Config(
                target = Config(
                  taskdef = Config(
                    `@resource` = "net/sf/antcontrib/antlib.xml",
                    `@classpathref` = "maven.plugin.classpath"
                  ),
                  `if` = Config(
                    available = Config(
                      `@file` = "${project.basedir}/" + mvnDeploySettingsFile
                    ),
                    `else` = new Config(
                      echoMvnDeploySettings.elements ++ Config(
                        fail = Config(
                          `@unless` = "deploy-settings",
                          `@message` = "Created the file '" + mvnDeploySettingsFile + "'." +
                            "${line.separator}For deployment edit and add your credentials and then run:" +
                            "${line.separator}  'mvn -s " + mvnDeploySettingsFile + " -Prelease clean deploy'"
                        )).elements
                    )
                  )
                )
              )
            ),
            // must be defined after the deploy task, to execeted after it
            Execution(
              id = "post-clean-info",
              phase = "deploy",
              goals = Seq("run"),
              configuration = Config(
                target = Config(
                  echo = Config(
                    `@message` = "Don't forget to clean/delete '" + mvnDeploySettingsFile + "'"
                  )
                )
              )
            )
          )
        ),
        Plugin(
          Plugins.source,
          executions = Seq(
            Execution(
              id = "attach-sources",
              goals = Seq("jar")
            )
          )
        ),
        Plugin(
          Plugins.javadoc,
          executions = Seq(
            Execution(
              id = "attach-javadocs",
              goals = Seq("jar")
            )
          )
        ),
        Plugin(
          Plugins.gpg,
          executions = Seq(
            Execution(
              id = "sign-artifacts",
              phase = "verify",
              goals = Seq("sign")
            )
          )
        )

      )
    )
  )
}
