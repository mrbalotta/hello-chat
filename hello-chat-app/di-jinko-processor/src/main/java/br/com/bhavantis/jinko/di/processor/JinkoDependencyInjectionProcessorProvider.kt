package br.com.bhavantis.jinko.di.processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
class JinkoDependencyInjectionProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return JinkoDependencyInjectionProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}