package dev.mgambati.lexer

import dev.mgambati.lexer.exceptions.NotFoundTokenDescription
import java.util.regex.Pattern

enum class TokenType {
    KEYWORD_VAR {
        override val pattern = "var"
    },
    KEYWORD_IF {
        override val pattern = "if"
    },
    KEYWORD_THEN {
        override val pattern = "then"
    },
    KEYWORD_INTEGER {
        override val pattern = "integer"
    },
    KEYWORD_REAL {
        override val pattern = "real"
    },
    OPERATOR_ASSIGNMENT {
        override val pattern = ":="
    },
    OPERATOR_ADD {
        override val pattern = "\\+"
    },
    COMMA {
        override val pattern = ","
    },
    COLON {
        override val pattern = ":"
    },
    SEMICOLON {
        override val pattern = ";"

    },
    IDENTIFIER {
        override val pattern = "(?:[a-zA-Z]|\\d)+"

    },
    // Para representar qualquer token que não seja válida.
    INVALID_TOKEN {
        override val pattern = "\\S"
    };

    abstract val pattern: String
    infix fun matches(value: String) = pattern.toRegex().matches(value)
    infix fun isA(type: TokenType) = this === type


    companion object {
        fun findByValue(value: String): TokenType =
            TokenType.values().find { item -> item matches value } ?: throw NotFoundTokenDescription()

        fun createPattern(): Pattern {
            val patterns = TokenType.values().map { item -> item.pattern }
            val groupedPatterns = patterns.joinToString(separator = ")|(", prefix = "(", postfix = ")")

            return Pattern.compile(groupedPatterns) ?: throw Exception("Failed to create pattern")
        }
    }

}