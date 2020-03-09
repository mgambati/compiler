package dev.mgambati.parser.exception

import dev.mgambati.lexer.TokenType

class EarlyExitSyntaxException(vararg expected: TokenType) :
    ParserException(
        level = ExceptionLevel.SYNTAX,
        description = "Final do arquivo encontrado, era esperado ${buildExpectedTypeMessage(*expected)}"
    )