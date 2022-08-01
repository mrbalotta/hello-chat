package br.com.bhavantis.jinko.di.processor.core

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.kspDependencies
import com.squareup.kotlinpoet.ksp.writeTo

abstract class AbstractDependencyInjectionProcessor(
    private val codeGenerator: CodeGenerator,
    protected val logger: KSPLogger
): SymbolProcessor {
    private var counter = 0
    protected val invalidSymbols = mutableListOf<KSClassDeclaration>()

    @OptIn(KotlinPoetKspPreview::class)
    final override fun process(resolver: Resolver): List<KSAnnotated> {
        counter++
        if(counter > 1) return emptyList()
        try {
            val func = startGeneratedFunSpec()
            processComponents(func, resolver)
            processProviders(func, resolver)
            endGeneratedFunSpec(func)
            finishAndFlush(func)
        }catch (e: Exception) {
            logger.exception(e)
        }

        return invalidSymbols
    }

    protected abstract fun processProviders(func: FunSpec.Builder, resolver: Resolver)
    protected abstract fun processComponents(func: FunSpec.Builder, resolver: Resolver)
    protected abstract fun startGeneratedFunSpec(): FunSpec.Builder
    protected abstract fun endGeneratedFunSpec(func: FunSpec.Builder)

    @OptIn(KotlinPoetKspPreview::class)
    protected fun finishAndFlush(func: FunSpec.Builder) {
        val typeSpec = getGeneratedClassSpec(func)
        val fileKotlinPoet = getFileSpec(typeSpec)
        val dependencies = fileKotlinPoet.kspDependencies(true)
        fileKotlinPoet.writeTo(codeGenerator, dependencies)
    }

    private fun getFileSpec(typeSpec: TypeSpec.Builder) =
        FileSpec.builder("br.com.bhavantis.jinko.di", "GeneratedProviders")
            .addType(typeSpec.build())
            .build()

    private fun getGeneratedClassSpec(func: FunSpec.Builder): TypeSpec.Builder {
        return TypeSpec
            .classBuilder("GeneratedProviders").apply {
                addFunction(func.build())
            }
    }
}