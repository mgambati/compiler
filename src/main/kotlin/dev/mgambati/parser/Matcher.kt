package dev.mgambati.parser

import dev.mgambati.lexer.Token
import dev.mgambati.lexer.TokenType
import dev.mgambati.parser.exception.EarlyExitSyntaxException
import dev.mgambati.parser.exception.SyntaxException

class Matcher(private val tokens: List<Token>) {
    private val iterator = tokens.listIterator()

    fun match(type: TokenType): Token {
        val token = next() ?: throw EarlyExitSyntaxException(type)
        if (token notOf type) throw SyntaxException(type, found = token)

        return token
    }


    fun previewOptionally(type: TokenType): Token? {
        val matched = matchOptionally(type)

        if (matched != null) previous()

        return matched
    }

    fun previewOneOfOptionally(vararg type: TokenType): Token? {
        val matched = matchOneOfOptionally(*type)
        if (matched != null) previous()

        return matched
    }

    fun matchOptionally(type: TokenType): Token? {
        return try {
            match(type)
        } catch (e: SyntaxException) {
            previous()
            null
        } catch (e: EarlyExitSyntaxException) {
            previous()
            null
        }
    }

    fun matchOneOf(vararg types: TokenType): Token {
        val token = next() ?: throw EarlyExitSyntaxException(*types)

        types.find { token of it } ?: throw SyntaxException(
            *types,
            found = token
        )

        return token
    }

    fun matchOneOfOptionally(vararg types: TokenType): Token? {
        return try {
            matchOneOf(*types)
        } catch (e: SyntaxException) {
            previous()
            null
        } catch (e: EarlyExitSyntaxException) {
            previous()
            null
        }
    }

    private fun next(): Token? = if (iterator.hasNext()) iterator.next() else null

    private fun previous(): Token? = if (iterator.hasPrevious()) iterator.previous() else null
}