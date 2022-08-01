package br.com.bhavantis.jinko.di

fun activityContext(fn: JinkoContext.()->Unit) = jinkoContext("activity", fn)
fun fragmentContext(fn: JinkoContext.()->Unit) = jinkoContext("fragment", fn)