package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token


class ParameterAlreadyDeclaredException(token: Token) :
    ParserException(
        level = ExceptionLevel.SEMANTIC,
        description = "Parâmetro ${token.value} já foi previamente declarado.",
        token = token
    )