package base

import com.intellij.ide.starter.ci.CIServer
import com.intellij.ide.starter.ci.NoCIServer
import com.intellij.ide.starter.di.di
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.NoProject
import com.intellij.ide.starter.runner.Starter
import constants.IdeConstants
import org.junit.jupiter.api.fail
import org.kodein.di.DI
import org.kodein.di.bindSingleton

open class BaseTest {

    init {
        di = DI {
            extend(di)
            bindSingleton<CIServer>(overrides = true) {
                object : CIServer by NoCIServer {
                    override fun reportTestFailure(
                        testName: String,
                        message: String,
                        details: String,
                        linkToLogs: String?
                    ) {
                        fail { "$testName fails: $message. \n$details" }
                    }
                }
            }
        }
    }

    fun createContext(testName: String) = Starter.newContext(
        testName = testName,
        TestCase(
            IdeProductProvider.IU,
            projectInfo = NoProject
        ).withVersion(IdeConstants.IDE_VERSION)
    )
}