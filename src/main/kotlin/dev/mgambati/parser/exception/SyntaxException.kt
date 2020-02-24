package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token
import dev.mgambati.lexer.TokenType


class SyntaxException(vararg val expected: TokenType, val found: Token) :
    Exception("Erro de sintaxe na linha ${found.line}, posição ${found.start}. \n Esperado: ${expected.map { it.name }} \n Entrado: ${found.type.name}")