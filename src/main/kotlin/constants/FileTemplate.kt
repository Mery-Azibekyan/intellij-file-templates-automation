package constants

enum class FileTemplate(val templateName: String, val expectedContentSegment: String) {
    HTML("HTML File", "<!DOCTYPE html>"),
    KOTLIN("Kotlin File", "parse(\"File Header.java\")"),
    CLASS("Class", "public class \${NAME}"),
    INTERFACE("Interface", "public interface \${NAME}"),
    ENUM("Enum", "public enum \${NAME}"),
    RECORD("Record", "public record \${NAME}"),
}