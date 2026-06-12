package constants

enum class FileTemplate(val templateName: String, val expectedContent: String) {
    HTML("HTML File", "<!DOCTYPE html><html lang=\"en\"><head>  <meta charset=\"UTF-8\">  <title>#[[\$Title\$]]#</title></head><body>#[[\$END\$]]#</body></html>"),
    KOTLIN("Kotlin File", "#if (\${PACKAGE_NAME} && \${PACKAGE_NAME} != \"\")package \${PACKAGE_NAME#end#parse(\"File Header.java\")"),
    CLASS("Class", "#if (\${PACKAGE_NAME} && \${PACKAGE_NAME} != \"\")package \${PACKAGE_NAME#end#parse(\"File Header.java\")public class \${NAME} {}"),
    INTERFACE("Interface", "#if (\${PACKAGE_NAME} && \${PACKAGE_NAME} != \"\")package \${PACKAGE_NAME#end#parse(\"File Header.java\")public interface \${NAME} {}"),
    ENUM("Enum", "#if (\${PACKAGE_NAME} && \${PACKAGE_NAME} != \"\")package \${PACKAGE_NAME#end#parse(\"File Header.java\")public enum \${NAME} {}"),
    RECORD("Record", "#if (\${PACKAGE_NAME} && \${PACKAGE_NAME} != \"\")package \${PACKAGE_NAME#end#parse(\"File Header.java\")public record \${NAME}() {}"),
}