package dev.mgambati.parser

import dev.mgambati.lexer.Token
import dev.mgambati.lexer.TokenType


class Parser(tokens: List<Token>) {
    private val matcher = Matcher(tokens)
    private val symbolTable = SymbolTable()

    fun printSymbolsTable() {
        symbolTable.print()
    }
    fun parse() {
        programa()
    }

    private fun programa() {
        matcher.match(TokenType.KEYWORD_PROGRAM)
        val matched = matcher.match(TokenType.IDENTIFIER)

        symbolTable.setRootScope(matched.value)
        symbolTable.addProgramToTable(matched)

        corpo()

        matcher.match(TokenType.DOT)
    }


    private fun corpo() {
        dc()
        matcher.match(TokenType.KEYWORD_BEGIN)
        comandos()
        matcher.match(TokenType.KEYWORD_END)
    }

    private fun dc() {
        val matched = matcher.previewOneOfOptionally(TokenType.KEYWORD_VAR, TokenType.KEYWORD_PROCEDURE) ?: return

        when (matched.type) {
            TokenType.KEYWORD_VAR -> dcV()
            TokenType.KEYWORD_PROCEDURE -> dcP()
            else -> throw Exception("Invalid Token")
        }

        maisDc()
    }

    private fun dcV() {
        matcher.match(TokenType.KEYWORD_VAR)
        variaveis()
        matcher.match(TokenType.COLON)
        tipoVar(true)
    }

    private fun maisDc() {
        matcher.matchOptionally(TokenType.SEMICOLON) ?: return
        dc()
    }

    private fun variaveis() {
        val matched = matcher.match(TokenType.IDENTIFIER)
        symbolTable.addToStaging(matched)
        maisVar()
    }

    private fun maisVar() {
        matcher.matchOptionally(TokenType.COMMA) ?: return
        variaveis()
    }

    private fun tipoVar(isMatchingAgainstVariable: Boolean) {
        val matched = matcher.matchOneOf(TokenType.KEYWORD_INTEGER, TokenType.KEYWORD_REAL)

        val type = when (matched.type) {
            TokenType.KEYWORD_INTEGER -> VariableType.INTEGER
            TokenType.KEYWORD_REAL -> VariableType.REAL
            else -> throw Exception("lel")
        }

        if (isMatchingAgainstVariable)
            symbolTable.addStagingToTableAsVariable(type)
        else
            symbolTable.addStagingToTableAsParameter(type)
    }

    private fun dcP() {
        matcher.match(TokenType.KEYWORD_PROCEDURE)
        val matched = matcher.match(TokenType.IDENTIFIER)

        symbolTable.addProcedureToTable(matched)
        symbolTable.setScope(matched.value)
        parametros()
        corpoP()
        symbolTable.resetToRootScope()
    }

    private fun parametros() {
        matcher.matchOptionally(TokenType.PARENTHESIS_LEFT) ?: return
        listaPar()
        matcher.match(TokenType.PARENTHESIS_RIGHT)
    }


    private fun listaPar() {
        variaveis()
        matcher.match(TokenType.COLON)
        tipoVar(false)
        maisPar()
    }

    private fun maisPar() {
        matcher.matchOptionally(TokenType.SEMICOLON) ?: return
        listaPar()
    }

    private fun corpoP() {
        dcLoc()
        matcher.match(TokenType.KEYWORD_BEGIN)
        comandos()
        matcher.match(TokenType.KEYWORD_END)
    }

    private fun dcLoc() {
        matcher.previewOptionally(TokenType.KEYWORD_VAR) ?: return
        dcV()
        maisDcloc()
    }

    private fun maisDcloc() {
        matcher.matchOptionally(TokenType.SEMICOLON) ?: return
        dcLoc()
    }

    private fun comandos() {
        comando()
        maisComandos()
    }

    private fun maisComandos() {
        matcher.matchOptionally(TokenType.SEMICOLON) ?: return
        comandos()
    }

    private fun comando() {
        val matched = matcher.matchOneOf(
            TokenType.KEYWORD_READ,
            TokenType.KEYWORD_WRITE,
            TokenType.KEYWORD_WHILE,
            TokenType.KEYWORD_IF,
            TokenType.IDENTIFIER
        )
        when (matched.type) {
            TokenType.KEYWORD_READ, TokenType.KEYWORD_WRITE -> {
                matcher.match(TokenType.PARENTHESIS_LEFT)
                variaveis()
                matcher.match(TokenType.PARENTHESIS_RIGHT)
                symbolTable.lookupStaging()
                symbolTable.clearStaging()
            }
            TokenType.KEYWORD_WHILE -> {
                condicao()
                matcher.match(TokenType.KEYWORD_DO)
                comandos()
                matcher.match(TokenType.KEYWORD_END_FLOW)
            }
            TokenType.KEYWORD_IF -> {
                condicao()
                matcher.match(TokenType.KEYWORD_THEN)
                comandos()
                pfalsa()
                matcher.match(TokenType.KEYWORD_END_FLOW)
            }
            TokenType.IDENTIFIER -> {
                symbolTable.lookupOnCurrentScopeOrRoot(matched)
                restoident(matched)
            }
            else -> throw Exception("Invalid command")
        }

    }

    private fun pfalsa() {
        matcher.matchOptionally(TokenType.KEYWORD_ELSE) ?: return
        comandos()
    }

    private fun condicao() {
        expressao()
        relacao()
        expressao()
    }

    private fun restoident(identifierMatchedPreviously:  Token) {
        val matched = matcher.previewOneOfOptionally(TokenType.OPERATOR_ASSIGNMENT, TokenType.PARENTHESIS_LEFT)

        when (matched?.type) {
            TokenType.OPERATOR_ASSIGNMENT -> {
                matcher.match(TokenType.OPERATOR_ASSIGNMENT)
                expressao()
            }
            else -> {
                listaArg()
                symbolTable.assertStagingTypesForProcedureCall(identifierMatchedPreviously)
            }
        }
    }

    private fun listaArg() {
        matcher.matchOptionally(TokenType.PARENTHESIS_LEFT) ?: return
        argumentos()
        matcher.match(TokenType.PARENTHESIS_RIGHT)
    }

    private fun argumentos() {
        val matched = matcher.match(TokenType.IDENTIFIER)
        symbolTable.lookupOnCurrentScopeOrRoot(matched)
        symbolTable.addToStaging(matched)
        maisIdent()
    }

    private fun maisIdent() {
        matcher.matchOptionally(TokenType.SEMICOLON) ?: return
        argumentos()
    }

    private fun expressao() {
        termo()
        outrosTermos()
    }

    private fun termo() {
        opUn()
        fator()
        maisFatores()
    }

    private fun outrosTermos() {
        matcher.previewOneOfOptionally(TokenType.OPERATOR_ADD, TokenType.OPERATOR_MINUS) ?: return
        opAd()
        termo()
        outrosTermos()
    }

    private fun opAd() {
        matcher.matchOneOf(TokenType.OPERATOR_ADD, TokenType.OPERATOR_MINUS)
    }

    private fun opUn() {
        matcher.matchOneOfOptionally(TokenType.OPERATOR_ADD, TokenType.OPERATOR_MINUS)
    }

    private fun fator() {
        val matched = matcher.matchOneOf(
            TokenType.IDENTIFIER,
            TokenType.NUMBER_INTEGER,
            TokenType.NUMBER_REAL,
            TokenType.PARENTHESIS_LEFT
        )

        when (matched.type) {
            TokenType.PARENTHESIS_LEFT -> {
                expressao()
                matcher.match(TokenType.PARENTHESIS_RIGHT)
            }
            TokenType.IDENTIFIER -> {
                symbolTable.lookupOnCurrentScopeOrRoot(matched)
                return
            }
            else -> return
        }
    }


    private fun maisFatores() {
        matcher.previewOneOfOptionally(TokenType.OPERATOR_DIVISION, TokenType.OPERATOR_MULTIPLY) ?: return
        opMul()
        fator()
        maisFatores()
    }

    private fun opMul() {
        matcher.matchOneOf(TokenType.OPERATOR_MULTIPLY, TokenType.OPERATOR_DIVISION)
    }

    private fun relacao() {
        matcher.matchOneOf(
            TokenType.RELATIONAL_EQUAL,
            TokenType.RELATIONAL_NOT_EQUAL,
            TokenType.RELATIONAL_GREATER_OR_EQUALS,
            TokenType.RELATIONAL_LESSER_OR_EQUALS,
            TokenType.RELATIONAL_GREATER,
            TokenType.RELATIONAL_LESSER
        )
    }


}