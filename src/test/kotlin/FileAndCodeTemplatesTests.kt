import base.BaseTest
import com.intellij.driver.sdk.ui.components.settingsDialog
import com.intellij.driver.sdk.ui.components.showSettings
import com.intellij.driver.sdk.ui.components.welcomeScreen
import com.intellij.ide.starter.driver.engine.BackgroundRun
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import constants.FileTemplate
import constants.TextConstants.CODE_TAB_TEMPLATE
import constants.TextConstants.FILES_TAB_TEMPLATE
import constants.TextConstants.INCLUDES_TAB_TEMPLATE
import constants.TextConstants.NEW_TEMPLATE_CONTENT
import constants.TextConstants.NEW_TEMPLATE_EXTENSION
import constants.TextConstants.NEW_TEMPLATE_NAME
import constants.TextConstants.OTHER_TAB_TEMPLATE
import constants.TextConstants.TAB_CODE
import constants.TextConstants.TAB_FILES
import constants.TextConstants.TAB_INCLUDES
import constants.TextConstants.TAB_OTHER
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pages.FileAndCodeTemplatesPanel

class FileAndCodeTemplatesTest : BaseTest() {

    private lateinit var bgRun: BackgroundRun

    @BeforeEach
    fun startIde() {
        bgRun = createContext("FileAndCodeTemplatesTest")
            .runIdeWithDriver()
    }

    @AfterEach
    fun closeIde() {
        bgRun.closeIdeAndWait()
    }

    @Test
    @DisplayName("Verify correct content is displayed for selected file template")
    fun verifyTemplateContentOnSelection() {
        bgRun.useDriverAndCloseIde {
            welcomeScreen {
                showSettings()
                settingsDialog {
                    val fileAndCodeTemplatesPanel = FileAndCodeTemplatesPanel(this)
                    //Open panel and verify its loaded correctly
                    fileAndCodeTemplatesPanel.open()
                    fileAndCodeTemplatesPanel.assertIsLoaded()
                    //Select and verify editor content for provided File Templates
                    FileTemplate.entries.forEach { template ->
                        fileAndCodeTemplatesPanel.selectTemplate(template.templateName)
                        val actualContent = fileAndCodeTemplatesPanel.getEditorText()
                        check(actualContent.contains(template.expectedContentSegment)) {
                            "Template content for ${template.templateName} doesn't match.\nExpected: ${template.expectedContentSegment}\nActual: $actualContent"
                        }
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Verify custom template can be created and persists all the entered data after saving")
    fun verifyCustomTemplateCreationAndPersistence() {
        bgRun.useDriverAndCloseIde {
            var templateCountBefore = 0

            welcomeScreen {
                showSettings()
                settingsDialog {
                    val fileAndCodeTemplatesPanel = FileAndCodeTemplatesPanel(this)
                    fileAndCodeTemplatesPanel.open()
                    fileAndCodeTemplatesPanel.assertIsLoaded()
                    // Create new template
                    templateCountBefore = fileAndCodeTemplatesPanel.getTemplateNamesInTab(TAB_FILES).size

                    fileAndCodeTemplatesPanel.clickCreateTemplate()
                    fileAndCodeTemplatesPanel.enterTemplateName(NEW_TEMPLATE_NAME)
                    fileAndCodeTemplatesPanel.enterTemplateExtension(NEW_TEMPLATE_EXTENSION)
                    fileAndCodeTemplatesPanel.enterTemplateContent(NEW_TEMPLATE_CONTENT)
                    fileAndCodeTemplatesPanel.clickOk()
                }
            }

            // Reopen settings to verify existence and persistence
            welcomeScreen {
                showSettings()
                settingsDialog {
                    val fileAndCodeTemplatesPanel = FileAndCodeTemplatesPanel(this)
                    //Verify template is present in the list
                    val templateCountAfter = fileAndCodeTemplatesPanel.getTemplateNamesInTab(TAB_FILES).size
                    val templateNames = fileAndCodeTemplatesPanel.getTemplateNamesInTab(TAB_FILES)

                    check(templateCountAfter == templateCountBefore + 1) {
                        "Template count should increase by 1 after creation. Before: $templateCountBefore, After: $templateCountAfter"
                    }
                    check(templateNames.contains(NEW_TEMPLATE_NAME)) {
                        "Created template '${NEW_TEMPLATE_NAME}' should persist after saving"
                    }

                    //Select the template and verify the data persistence
                    fileAndCodeTemplatesPanel.selectTemplate(NEW_TEMPLATE_NAME)

                    val actualName = fileAndCodeTemplatesPanel.getNameValue()
                    val actualExtension = fileAndCodeTemplatesPanel.getExtensionValue()
                    val actualContent = fileAndCodeTemplatesPanel.getEditorText()

                    check(actualName == NEW_TEMPLATE_NAME) {
                        "Template name should persist. Expected: ${NEW_TEMPLATE_NAME}, Actual: $actualName"
                    }
                    check(actualContent == NEW_TEMPLATE_CONTENT) {
                        "Template content should persist after saving. Expected to contain: ${NEW_TEMPLATE_CONTENT}, Actual: $actualContent"
                    }
                    check(actualExtension == NEW_TEMPLATE_EXTENSION) {
                        "Template extension should persist. Expected: ${NEW_TEMPLATE_EXTENSION}, Actual: $actualExtension"
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Verify each tab displays its correct template category")
    fun verifyTabSwitchingShowsCorrectTemplates() {
        bgRun.useDriverAndCloseIde {
            welcomeScreen {
                showSettings()
                settingsDialog {
                    val fileAndCodeTemplatesPanel = FileAndCodeTemplatesPanel(this)
                    fileAndCodeTemplatesPanel.open()
                    fileAndCodeTemplatesPanel.assertIsLoaded()

                    val tabsWithExpectedTemplates = mapOf(
                        TAB_FILES to FILES_TAB_TEMPLATE,
                        TAB_INCLUDES to INCLUDES_TAB_TEMPLATE,
                        TAB_CODE to CODE_TAB_TEMPLATE,
                        TAB_OTHER to OTHER_TAB_TEMPLATE
                    )

                    tabsWithExpectedTemplates.forEach { (tab, expectedTemplate) ->
                        fileAndCodeTemplatesPanel.selectTab(tab)
                        val templates = fileAndCodeTemplatesPanel.getTemplateNamesInTab(tab)
                        check(templates.isNotEmpty()) {
                            "Template list should not be empty on '$tab' tab"
                        }
                        check(templates.contains(expectedTemplate)) {
                            "'$tab' tab should contain '$expectedTemplate' but got: $templates"
                        }
                    }
                }
            }
        }
    }
}