package dev.gigaherz.jsonthings.codegen.codetree;

import org.objectweb.asm.MethodVisitor;

public interface ValueExpression<F>
{
    void compile(MethodVisitor mv);
}