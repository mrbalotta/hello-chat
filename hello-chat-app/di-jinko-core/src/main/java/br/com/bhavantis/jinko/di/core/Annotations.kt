package br.com.bhavantis.jinko.di.core

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FIELD, VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class Inject

@Target(CLASS)
@Retention(RUNTIME)
annotation class Provider

@Target(CLASS)
@Retention(RUNTIME)
annotation class ModuleEntryPoint(val value: String)

@Target(FUNCTION)
@Retention(RUNTIME)
annotation class Bean

@Target(CLASS)
@Retention(RUNTIME)
annotation class Component

@Target(CLASS, FUNCTION)
@Retention(RUNTIME)
annotation class Single

@Target(CLASS, FUNCTION)
@Retention(RUNTIME)
annotation class Qualifier(val value: String)

@Target(VALUE_PARAMETER, FIELD)
annotation class  QualifiedBy(val value: String)

@Target(FIELD, VALUE_PARAMETER, FUNCTION)
@Retention(RUNTIME)
annotation class Scope(val value: String)

@Target(CLASS)
@Retention(RUNTIME)
annotation class ComponentMapping(val value: KClass<*>)

@Target(CLASS)
@Retention(RUNTIME)
annotation class Priority
