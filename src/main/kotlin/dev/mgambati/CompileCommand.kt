package dev.mgambati

import dev.mgambati.lexer.Lexer
import dev.mgambati.parser.Parser
import dev.mgambati.utils.printTokensTable
import picocli.CommandLine
import picocli.CommandLine.Parameters
import java.io.File


@CommandLine.Command(name = "compile")
class CompileCommand : Runnable {
    @Parameters(paramLabel = "Source", description = ["the source code file"])
    lateinit var archive: File

    override fun run() {
        try {
            val sourceStream = archive.inputStream()
            val lexer = Lexer()
            val tokens = lexer.tokenize(sourceStream)

            printTokensTable(tokens)

            val parser = Parser(tokens)
            parser.parse()

            println("Nenhum erro sintático detectado.")

        } catch (e: Exception) {
            val red = "\u001B[31m"
            val reset = "\u001B[0m"
            println(red + e.message + reset)
        }
    }


}