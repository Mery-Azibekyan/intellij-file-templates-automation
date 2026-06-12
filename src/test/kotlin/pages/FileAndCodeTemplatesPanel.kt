package pages

import com.intellij.driver.sdk.ui.*
import com.intellij.driver.sdk.ui.UiText.Companion.allText
import com.intellij.driver.sdk.ui.components.SettingsUiComponent
import com.intellij.driver.sdk.ui.components.jBlist
import com.intellij.openapi.util.SystemInfo
import constants.IdeConstants
import constants.TextConstants
import constants.TextConstants.HTML_DOCTYPE
import constants.TextConstants.TAB_CODE
import constants.TextConstants.TAB_FILES
import constants.TextConstants.TAB_INCLUDES
import constants.TextConstants.TAB_OTHER
import io.qameta.allure.Step
import java.awt.event.KeyEvent

class FileAndCodeTemplatesPanel(private val scope: SettingsUiComponent) {

    @Step("Open File and Code Templates settings")
    fun open() {
        scope.settingsTree.clickPath(
            IdeConstants.EDITOR_SECTION,
            IdeConstants.FILE_AND_CODE_TEMPLATES
        )
    }

    @Step("Get breadcrumb navigation")
    fun getBreadcrumb() = scope.x(
        xQuery { byType("com.intellij.ui.components.breadcrumbs.Breadcrumbs") }
    )

    @Step("Get file templates list")
    private fun getFileTemplateList() = scope.jBlist(
        xQuery { contains(byVisibleText(TextConstants.FILES_TAB_TEMPLATE)) }
    )

    @Step("Get editor component")
    private fun getEditorContent() = scope.x(
        "//div[@accessiblename='Editor' and @class='EditorComponentImpl']"
    )

    @Step("Get extension field")
    private fun getExtensionField() = scope.x(
        "//div[@class='JTextField' and @accessiblename='Extension:']"
    )

    @Step("Get name field")
    private fun getNameField() = scope.x(
        "//div[@class='JTextField' and @accessiblename='Name:']"
    )

    @Step("Get editor text content")
    fun getEditorText(): String {
        return getEditorContent().getAllTexts().allText()
    }

    @Step("Verify all tabs are present")
    private fun verifyTabsExist() {
        listOf(TAB_FILES, TAB_INCLUDES, TAB_CODE, TAB_OTHER).forEach { tab ->
            scope.x(xQuery { byVisibleText(tab) })
                .shouldBe("'$tab' tab should be present", present)
        }
    }

    @Step("Verify File and Code Templates panel is loaded correctly")
    fun assertIsLoaded() {
        getBreadcrumb().shouldHave(
            "Breadcrumb should show 'File and Code Templates' in path",
            haveText(IdeConstants.FILE_AND_CODE_TEMPLATES)
        )
        verifyTabsExist()
        getFileTemplateList().shouldBe("Template list should be present", present)
        getEditorContent()
            .shouldBe("Template editor should be present", present)
            .shouldHave("Text area should contain HTML template code", haveText(HTML_DOCTYPE))
    }

    @Step("Select template: {name}")
    fun selectTemplate(name: String) {
        scope.jBlist().clickItem(name)
    }

    @Step("Click Create Template button")
    fun clickCreateTemplate() {
        scope.x(xQuery { byAccessibleName("Create Template") }).click()
    }

    @Step("Enter template name: {name}")
    fun enterTemplateName(name: String) {
        getNameField().click()
        scope.keyboard {
            hotKey(if (SystemInfo.isMac) KeyEvent.VK_META else KeyEvent.VK_CONTROL, KeyEvent.VK_A)
            enterText(name)
        }
    }

    @Step("Enter template extension: {extension}")
    fun enterTemplateExtension(extension: String) {
        getExtensionField().click()
        scope.keyboard {
            hotKey(if (SystemInfo.isMac) KeyEvent.VK_META else KeyEvent.VK_CONTROL, KeyEvent.VK_A)
            enterText(extension)
        }
    }

    @Step("Enter template content: {content}")
    fun enterTemplateContent(content: String) {
        getEditorContent().click()
        scope.keyboard { enterText(content) }
    }

    @Step("Save settings by clicking OK button")
    fun clickOk() {
        scope.x(xQuery { byAccessibleName("OK") }).click()
    }

    @Step("Get templates tree for Other tab")
    private fun getTemplatesTreeList() = scope.x(
        "//div[@class='Tree' and not(contains(@classhierarchy, 'SettingsTreeView'))]"
    )

    @Step("Get template names for tab: {tab}")
    fun getTemplateNamesInTab(tab: String): List<String> {
        return if (tab == TAB_OTHER) {
            getTemplatesTreeList().getAllTexts().map { it.text }
        } else {
            scope.jBlist().items
        }
    }

    @Step("Get extension field value")
    fun getExtensionValue(): String {
        return getExtensionField().getAllTexts().allText()
    }

    @Step("Get name field value")
    fun getNameValue(): String {
        return getNameField().getAllTexts().allText()
    }

    @Step("Select tab: {tabName}")
    fun selectTab(tabName: String) {
        scope.x(xQuery { byVisibleText(tabName) }).click()
    }
}