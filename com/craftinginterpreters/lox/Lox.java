package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        }else if (args.length == 1) {
            runFile(args[0]);
        }else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
    }

    //interactive REPL(read evaluate print loop) mode. Doable for 
    //scripting languages like lox is
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.println("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);

            //if the user makes an error in this interactive session
            //it shldnt kill the session, so to indicate that
            //set hadError to false, altough setting it in an of itself
            //has no real effect, and for that, System.exit() must be called
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        //print scanned tokens
        // for (Token token : tokens) {
        //     System.out.println(token);
        // }

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        //stop rest of the process if syntax error 
        if (hadError) return;

        System.out.println(new AstPrinterVisitor().print(expression));
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        }else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    private static void report(int line, String where, String message) {
        System.err.println(
        "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
