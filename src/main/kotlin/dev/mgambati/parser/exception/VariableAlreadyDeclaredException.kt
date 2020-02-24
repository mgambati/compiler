package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token


class VariableAlreadyDeclaredException(token: Token) :
    ParserException(
        level = ExceptionLevel.SEMANTIC,
        description = "Variável ${token.value} já foi previamente declarada.",
        token = token
    )