package br.com.bhavantis.jinko.di.processor.core

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName

abstract class AbstractJinkoVisitor: KSVisitorVoid() {

    protected fun getQualifier(declaration: KSDeclaration): String {
        val qualifiers = declaration.annotations
            .filter { it.annotationType.toString() == "Qualifier" }

        if(qualifiers.toList().isEmpty()) return ""

        return "\"${qualifiers.first().arguments[0].value}\""
    }

    protected fun isPriority(declaration: KSDeclaration) =
        declaration
            .annotations
            .filter { it.annotationType.toString()  == "Priority" }
            .toList()
            .isNotEmpty()

    protected fun isSingle(declaration: KSDeclaration) =
        declaration
            .annotations
            .filter { it.annotationType.toString() == "Single" }
            .toList()
            .isNotEmpty()

    protected abstract fun getFactoryMethod(declaration: KSDeclaration): String
    protected abstract fun getMappedType(declaration: KSDeclaration): ClassName
    protected abstract fun getParams(declaration: KSDeclaration): String
}