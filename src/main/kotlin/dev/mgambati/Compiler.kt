package dev.mgambati

import picocli.CommandLine


fun main(args: Array<String>) {
    CommandLine(CompileCommand()).execute(*args)
}