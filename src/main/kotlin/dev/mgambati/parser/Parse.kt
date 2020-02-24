package dev.mgambati.parser

import dev.mgambati.lexer.Token
import dev.mgambati.lexer.TokenType


class Parser(private val tokens: List<Token>) {
    private val matcher = Matcher(tokens)
    fun parse() {
        z()
    }

    private fun z() {
        i()
        s()
    }

    private fun i() {
        matcher.match(TokenType.KEYWORD_VAR)
        d()
    }

    private fun s() {
        val matched = matcher.matchOneOf(TokenType.IDENTIFIER, TokenType.KEYWORD_IF)

        if (matched of TokenType.IDENTIFIER) {
            matcher.match(TokenType.OPERATOR_ASSIGNMENT)
            e()
        } else {
            e()
            matcher.match(TokenType.KEYWORD_THEN)
            s()
        }
    }

    private fun e() {
        t()
        r()
    }

    private fun r() {
        val matched = matcher.matchOptionally(TokenType.OPERATOR_ADD)

        if (matched != null) {
            t()
            r()
        }
    }

    private fun t() {
        matcher.match(TokenType.IDENTIFIER)
    }

    private fun d() {
        l()
        matcher.match(TokenType.COLON)
        k()
        o()
    }

    private fun l() {
        val matched = matcher.match(TokenType.IDENTIFIER)
        x()
    }

    private fun x() {
        val matched = matcher.matchOptionally(TokenType.COMMA)
        if (matched != null) l()
    }

    private fun k() {
        matcher.matchOneOf(TokenType.KEYWORD_INTEGER, TokenType.KEYWORD_REAL)
    }

    private fun o() {
        val matched = matcher.matchOptionally(TokenType.SEMICOLON)

        if (matched != null) d()
    }

}