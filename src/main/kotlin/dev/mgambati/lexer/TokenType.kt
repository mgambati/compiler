package dev.mgambati.lexer

import dev.mgambati.lexer.exceptions.NotFoundTokenDescription
import java.util.regex.Pattern

enum class TokenType {
    /** Comments */

    COMMENT_IN_BACKSLASHES {
        override val pattern = "\\/\\*.+\\*\\/"
    },

    COMMENT_IN_BRACKETS {
        override val pattern = "\\{.+}"
    },

    /** Main Keywords */
    KEYWORD_PROGRAM {
        override val pattern = "program(?!\\w)"
    },

    KEYWORD_BEGIN {
        override val pattern = "begin(?!\\w)"
    },
    KEYWORD_END {
        override val pattern = "end(?!\\w)"
    },
    KEYWORD_PROCEDURE {
      override val pattern = "procedure(?!\\w)"
    },

    KEYWORD_VAR {
        override val pattern = "var(?!\\w)"
    },
    KEYWORD_INTEGER {
        override val pattern = "integer(?!\\w)"
    },
    KEYWORD_REAL {
        override val pattern = "real(?!\\w)"
    },

    KEYWORD_READ {
        override val pattern = "read(?!\\w)"
    },
    KEYWORD_WRITE {
        override val pattern = "write(?!\\w)"
    },

    /** Control Flow */
    KEYWORD_IF {
        override val pattern = "if(?!\\w)"
    },
    KEYWORD_THEN {
        override val pattern = "then(?!\\w)"
    },
    KEYWORD_ELSE {
        override val pattern = "else(?!\\w)"
    },
    KEYWORD_WHILE {
        override val pattern = "while(?!\\w)"
    },
    KEYWORD_DO {
        override val pattern = "do(?!\\w)"
    },
    KEYWORD_END_FLOW {
        override val pattern = "\\$"
    },

    /** Operators */
    OPERATOR_ASSIGNMENT {
        override val pattern = ":="
    },
    OPERATOR_DIVISION {
      override val pattern = "/"
    },
    OPERATOR_MULTIPLY {
        override val pattern = "\\*"
    },
    OPERATOR_ADD {
        override val pattern = "\\+"
    },
    OPERATOR_MINUS {
        override val pattern = "-"
    },

    /** Relational Operators */

    RELATIONAL_NOT_EQUAL {
      override val pattern = "<>"
    },
    RELATIONAL_EQUAL {
        override val pattern = "="
    },
    RELATIONAL_GREATER_OR_EQUALS {
        override val pattern = ">="
    },
    RELATIONAL_LESSER_OR_EQUALS {
        override val pattern = "<="
    },
    RELATIONAL_GREATER {
        override val pattern = ">"
    },
    RELATIONAL_LESSER {
        override val pattern = "<"
    },

    /** Numbers */

    NUMBER_REAL {
        override val pattern = "[0-9]+\\.[0-9]+"

    },
    NUMBER_INTEGER {
        override val pattern = "[0-9]+"
    },
    /** Symbols */
    COMMA {
        override val pattern = ","
    },
    DOT {
        override val pattern = "\\."
    },
    COLON {
        override val pattern = ":"
    },
    SEMICOLON {
        override val pattern = ";"
    },
    PARENTHESIS_LEFT {
        override val pattern = "\\("
    },
    PARENTHESIS_RIGHT {
        override val pattern = "\\)"
    },
    IDENTIFIER {
        override val pattern = "[a-zA-Z](?:[a-zA-Z]|\\d)*"
    },
    // Para representar qualquer token que não seja válida.
    INVALID_TOKEN {
        override val pattern = "\\S"
    };

    abstract val pattern: String
    infix fun matches(value: String) = pattern.toRegex().matches(value)
    infix fun isA(type: TokenType) = this === type
    fun isAComment() = this === COMMENT_IN_BACKSLASHES || this === COMMENT_IN_BRACKETS


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