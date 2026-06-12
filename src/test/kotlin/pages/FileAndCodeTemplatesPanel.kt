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
import java.awt.event.KeyEvent

class FileAndCodeTemplatesPanel(private val scope: SettingsUiComponent) {


    fun open() {
        scope.settingsTree.clickPath(
            IdeConstants.EDITOR_SECTION,
            IdeConstants.FILE_AND_CODE_TEMPLATES
        )
    }

    fun getBreadcrumb() = scope.x(
        xQuery { byType("com.intellij.ui.components.breadcrumbs.Breadcrumbs") }
    )

    private fun getFileTemplateList() = scope.jBlist(
        xQuery { contains(byVisibleText(TextConstants.FILES_TAB_TEMPLATE)) }
    )

    private fun getEditorContent() = scope.x(
        "//div[@accessiblename='Editor' and @class='EditorComponentImpl']"
    )

    private fun getExtensionField() = scope.x(
        "//div[@class='JTextField' and @accessiblename='Extension:']"
    )

    private fun getNameField() = scope.x(
        "//div[@class='JTextField' and @accessiblename='Name:']"
    )

    fun getEditorText(): String {
        return getEditorContent().getAllTexts().allText()
    }

    private fun verifyTabsExist() {
        listOf(TAB_FILES, TAB_INCLUDES, TAB_CODE, TAB_OTHER).forEach { tab ->
            scope.x(xQuery { byVisibleText(tab) })
                .shouldBe("'$tab' tab should be present", present)
        }
    }

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

    fun selectTemplate(name: String) {
        scope.jBlist().clickItem(name)
    }

    fun clickCreateTemplate() {
        scope.x(xQuery { byAccessibleName("Create Template") }).click()
    }

    fun enterTemplateName(name: String) {
        getNameField().click()
        scope.keyboard {
            hotKey(if (SystemInfo.isMac) KeyEvent.VK_META else KeyEvent.VK_CONTROL, KeyEvent.VK_A)
            enterText(name)
        }
    }

    fun enterTemplateExtension(extension: String) {
        getExtensionField().click()
        scope.keyboard {
            hotKey(if (SystemInfo.isMac) KeyEvent.VK_META else KeyEvent.VK_CONTROL, KeyEvent.VK_A)
            enterText(extension)
        }
    }

    fun enterTemplateContent(content: String) {
        getEditorContent().click()
        scope.keyboard { enterText(content) }
    }

    fun clickOk() {
        scope.x(xQuery { byAccessibleName("OK") }).click()
    }

    private fun getTemplatesTreeList() = scope.x(
        "//div[@class='Tree' and not(contains(@classhierarchy, 'SettingsTreeView'))]"
    )

    fun getTemplateNamesInTab(tab: String): List<String> {
        return if (tab == TAB_OTHER) {
            getTemplatesTreeList().getAllTexts().map { it.text }
        } else {
            scope.jBlist().items
        }
    }

    fun getExtensionValue(): String {
        return getExtensionField().getAllTexts().allText()
    }

    fun getNameValue(): String {
        return getNameField().getAllTexts().allText()
    }

    fun selectTab(tabName: String) {
        scope.x(xQuery { byVisibleText(tabName) }).click()
    }
}