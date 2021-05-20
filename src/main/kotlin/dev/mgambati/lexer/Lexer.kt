package dev.mgambati.lexer

import dev.mgambati.lexer.exceptions.LexicalException
import java.io.FileInputStream
import java.util.*

class Lexer {
    private val matchedTokens = mutableListOf<Token>()
    private val pattern = TokenType.createPattern()
    private var currentLine = 1

    private fun createTokenForMatches(value: String, line: Int, start: Int, end: Int): Token {
        val type = TokenType.findByValue(value)
        return Token(value, type, line, start, end)
    }

    private fun matchTokensForLine(line: String) {
        val matcher = pattern.matcher(line)

        while (matcher.find()) {
            val token = createTokenForMatches(matcher.group(), currentLine, matcher.start(), matcher.end())

            if (token.type isA TokenType.INVALID_TOKEN)
                throw LexicalException(
                    matcher.group(),
                    currentLine,
                    matcher.start()
                )

            if (!token.type.isAComment())
                matchedTokens.add(token)
        }
    }

    fun tokenize(source: FileInputStream): List<Token> {
        val scanner = Scanner(source)
        currentLine = 1

        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            matchTokensForLine(line)
            currentLine += 1
        }

        return matchedTokens.toList()
    }
}
