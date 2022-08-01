package br.com.bhavantis.jinko.di.processor

import br.com.bhavantis.jinko.di.processor.core.AbstractJinkoVisitor
import com.google.devtools.ksp.symbol.KSDeclaration

abstract class JinkoDependencyInjectionVisitor: AbstractJinkoVisitor() {

    override fun getFactoryMethod(declaration: KSDeclaration) =
        if(isSingle(declaration)) "single" else "factory"
}