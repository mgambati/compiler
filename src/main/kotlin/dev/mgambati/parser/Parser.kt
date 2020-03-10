package dev.mgambati.parser

import dev.mgambati.lexer.Token
import dev.mgambati.lexer.TokenType
import dev.mgambati.parser.exception.UndefinedVariableException


class Parser(tokens: List<Token>) {
    private val matcher = Matcher(tokens)
    private val symbolTable = SymbolTable()

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
            symbolTable.lookup(matched.value) as Variable? ?: throw UndefinedVariableException(matched)

            symbolTable.addToStaging(matched)
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
        symbolTable.assertStagingTypes()
    }

    private fun r() {
        val matched = matcher.matchOptionally(TokenType.OPERATOR_ADD)

        if (matched != null) {
            t()
            r()
        }
    }

    private fun t() {
        val matched = matcher.match(TokenType.IDENTIFIER)
        symbolTable.lookup(matched.value) ?: throw UndefinedVariableException(matched)
        symbolTable.addToStaging(matched)
    }

    private fun d() {
        l()
        matcher.match(TokenType.COLON)
        k()
        o()
    }

    private fun l() {
        val matched = matcher.match(TokenType.IDENTIFIER)
        symbolTable.addToStaging(matched)
        x()
    }

    private fun x() {
        val matched = matcher.matchOptionally(TokenType.COMMA)
        if (matched != null) l()
    }

    private fun k() {
        val matched = matcher.matchOneOf(TokenType.KEYWORD_INTEGER, TokenType.KEYWORD_REAL)
        val type = when (matched.type) {
            TokenType.KEYWORD_INTEGER -> VariableType.INTEGER
            TokenType.KEYWORD_REAL -> VariableType.REAL
            else -> throw Exception("lel")
        }
        symbolTable.addStagingToTable(type)
    }

    private fun o() {
        val matched = matcher.matchOptionally(TokenType.SEMICOLON)

        if (matched != null) d()
    }

}