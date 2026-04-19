package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Unary;

/*
yet another operation, this would be print_RPN()
*/

public class AstPrinterRPNVisitor implements Expr.Visitor<String>{

    @Override
    public String visitBinaryExpr(Binary expr) {
        return expr.left.accept(this) + " " + 
               expr.right.accept(this) + " " + 
               expr.operator.lexeme;
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        //the order is implicitely present in the tree resolution
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return expr.right.accept(this) + " " + expr.operator.lexeme;
    }
    
}
