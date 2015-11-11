import de.tototec.sbuild._

@version("0.7.6")
class SBuild(implicit _project: Project) {

  Target("phony:testCp") dependsOn
    "mvn:org.testng:testng:6.8" ~
    "mvn:de.tototec:de.tobiasroeser.lambdatest:0.1.0"

}
