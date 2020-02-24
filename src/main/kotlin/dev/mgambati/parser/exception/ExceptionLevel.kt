package dev.mgambati.parser.exception

enum class ExceptionLevel {
    SYNTAX {
        override val readableName = "Syntax Error"
    },
    SEMANTIC {
        override val readableName = "Semantic Error"
    };
    abstract val readableName: String
}