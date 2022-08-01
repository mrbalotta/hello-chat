package br.com.bhavantis.jinko.di.processor

import br.com.bhavantis.jinko.di.core.Component
import br.com.bhavantis.jinko.di.core.Provider
import br.com.bhavantis.jinko.di.processor.core.AbstractDependencyInjectionProcessor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

class JinkoDependencyInjectionProcessor(
    codeGenerator: CodeGenerator,
    logger: KSPLogger
): AbstractDependencyInjectionProcessor(codeGenerator, logger) {

    private val mappingTrack = mutableMapOf<String,String>()

    override fun processComponents(func: FunSpec.Builder, resolver: Resolver) {
        val symbols = resolver
            .getSymbolsWithAnnotation(Component::class.qualifiedName.toString())
            .filter { it.validate() }
            .filterIsInstance<KSClassDeclaration>()

        symbols.forEach { it.accept(ComponentVisitor(func), Unit) }
        val invalids = symbols.filterNot { it.validate() }.toList()
        if(invalids.isNotEmpty()) invalidSymbols.addAll(invalids)
    }

    override fun startGeneratedFunSpec(): FunSpec.Builder {
        return FunSpec.builder("load").apply {
            beginControlFlow("%M", MemberName("br.com.bhavantis.jinko.di", "jinkoContext"))
            beginControlFlow("provider")
        }
    }

    override fun endGeneratedFunSpec(func: FunSpec.Builder) {
        func.apply {
            endControlFlow()
            endControlFlow()
        }
    }

    override fun processProviders(func: FunSpec.Builder, resolver: Resolver) {
        val symbols = resolver
            .getSymbolsWithAnnotation(Provider::class.qualifiedName.toString())
            .filter { it.validate() }
            .filterIsInstance<KSClassDeclaration>()

        symbols.forEach { it.accept(ProviderVisitor(func), Unit) }
        val invalids = symbols.filterNot { it.validate() }.toList()
        if(invalids.isNotEmpty()) invalidSymbols.addAll(invalids)
    }

    inner class ProviderVisitor(private val func: FunSpec.Builder) : JinkoDependencyInjectionVisitor() {

        override fun getParams(declaration: KSDeclaration): String {
            val params = (declaration as KSFunctionDeclaration).parameters
            val paramList = mutableListOf<String>()
            for(param in params) {
                val qualifiedBy = param.annotations.filter { it.annotationType.toString() == "QualifiedBy" }.toList()
                if(qualifiedBy.isNotEmpty()) {
                    val qualifier = qualifiedBy.first().arguments[0].value
                    paramList.add("get(\"$qualifier\")")
                } else {
                    paramList.add("get()")
                }
            }
            return paramList.joinToString()
        }

        @OptIn(KotlinPoetKspPreview::class)
        override fun getMappedType(declaration: KSDeclaration): ClassName {
            val method = declaration as KSFunctionDeclaration
            return method.returnType!!.resolve().toClassName()
        }

        @OptIn(KotlinPoetKspPreview::class)
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            with(func) {
                val provider = classDeclaration.toClassName()
                for(method in classDeclaration.getAllFunctions()) {
                    val isBean = method.annotations
                        .filter { it.annotationType.toString() == "Bean" }
                        .toList()
                        .isNotEmpty()
                    if(isBean) {
                        val qualifier =  getQualifier(method)
                        val mapped = getMappedType(method)
                        val params = getParams(method)
                        val factoryMethod = getFactoryMethod(method)
                        beginControlFlow("$factoryMethod<%T>($qualifier)", mapped)
                            addStatement("%T().${method}($params)", provider)
                        endControlFlow()
                    }
                }
            }
        }
    }

    inner class ComponentVisitor(private val func: FunSpec.Builder) : JinkoDependencyInjectionVisitor() {

        @OptIn(KotlinPoetKspPreview::class)
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            with(func) {
                val qualifier = getQualifier(classDeclaration)
                val mapper = getMappedType(classDeclaration)

              /*  if(mappingTrack.containsKey(mapper.toString()) && !isPriority(classDeclaration))
                    throw AmbiguousMappingException("$mapper mapped to ${mappingTrack[mapper.toString()]}")
                mappingTrack[mapper.toString()] = classDeclaration.toClassName().toString()
                */

                val factoryMethod = getFactoryMethod(classDeclaration)
                beginControlFlow("$factoryMethod<%T>($qualifier)", mapper)
                    if(classDeclaration.classKind == ClassKind.OBJECT)
                        addStatement("%T", classDeclaration.toClassName())
                    else {
                        val constructorParams = getParams(classDeclaration)
                        addStatement("%T($constructorParams)", classDeclaration.toClassName())
                    }
                endControlFlow()
            }
        }

        override fun getParams(declaration: KSDeclaration): String {
            val classDeclaration = (declaration as KSClassDeclaration)

            val params = classDeclaration.primaryConstructor?.parameters ?: emptyList()
            val paramList = mutableListOf<String>()
            for(param in params) {
                val qualifiedBy = param.annotations
                    .filter { it.annotationType.toString() == "QualifiedBy" }
                    .toList()
                if(qualifiedBy.isNotEmpty()) {
                    val qualifier = qualifiedBy.first().arguments[0].value
                    paramList.add("get(\"$qualifier\")")
                } else {
                    paramList.add("get()")
                }
            }

            return paramList.joinToString()
        }

        @OptIn(KotlinPoetKspPreview::class)
        override fun getMappedType(declaration: KSDeclaration): ClassName {
            val classDeclaration = declaration as KSClassDeclaration

            val enforcedMapping = getEnforcedMapping(declaration)
            if(enforcedMapping == null) {
                classDeclaration.superTypes.forEach {
                    val resolved = it.resolve()
                    val superTypeDeclaration: KSClassDeclaration = resolved.declaration as KSClassDeclaration
                    if(superTypeDeclaration.classKind == ClassKind.INTERFACE || superTypeDeclaration.modifiers.contains(Modifier.ABSTRACT))
                        return resolved.toClassName()
                }
                return classDeclaration.toClassName()
            }
            return enforcedMapping
        }

        @OptIn(KotlinPoetKspPreview::class)
        private fun getEnforcedMapping(declaration: KSDeclaration): ClassName? {
            val value = declaration.annotations
                .filter { it.annotationType.toString() == "ComponentMapping" }
                .firstOrNull()
                ?.arguments
                ?.first()
                ?.value

            if(value != null) {
                (value as KSType).toClassName()
            }
            return null
        }
    }
}