package com.craftinginterpreters.lox;

import java.util.List;

/*
A note on the Visitor pattern: 

This design pattern enables the replication of the style of pattern match
on types in OO paradigm. So if we have Pastry.java which is the interface
and two classes which implement it: Beignet.java and Cruller.java, then to
add a new operation/method called bake() to these, we define:
    * abstract void bake(): in Pastry.java interface
    * void bake(): in Beignet.java
    * void bake(): in Cruller.java

So for n types of pastries, we have to define the function bake() n+1 times.

So, we define a Visitor interface called PastryVisitor.java:
    interface PastryVisitor {
        void visitBeignet(Beignet beignet);
        void visitCruller(Cruller cruller);
    }

Then to define a new operator/method, we instead define a new class for the 
same, so BakeVisitor.java class is defined, which implements the PastryVisitor interface:
    public class BakeVisitor implements PastryVisitor {
        @Override
        public void visitBeignet(Beignet beignet) {
            //logic for beignet here
        }

        @Override
        public void visitCruller(Cruller cruller) {
            //logic for cruller here
        }
    }

And thats it, no need to introduce the logic for bake() across all the types/classes.
Then say for Beignet class:
    public class Beignet extends Pastry {
        @Override
        public void accept(PastryVisitor visitor) {
            visitor.visitBeignet(this);
        }
    }

So when we pass in an instance of BakeVisitor to the accept method of Beignet instance,
it calls the visitBeignet() method of BakeVisitor class instead. Likewise for some other
operator that could be defined say decorate(), so define DecorateVisitor.java instead and do
the same.

In functional languages, this can be done by simply defining the operator
which is a function, and pattern matching on the types that could call that
function.
*/

abstract class Expr {
    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
        R visitUnaryExpr(Unary expr);
    }

    abstract <R> R accept(Visitor<R> visitor);

    static class Binary extends Expr {

        final Expr left;
        final Token operator;
        final Expr right;
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        };

    }

    static class Grouping extends Expr {

        final Expr expression;
        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        };

    }

    static class Literal extends Expr {

        final Object value;
        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        };

    }

    static class Unary extends Expr {

        final Token operator;
        final Expr right;
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        };

    }

}

