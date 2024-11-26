package teste;

public interface Constants extends ScannerConstants, ParserConstants
{
    int EPSILON  = 0;
    int DOLLAR   = 1;

    int t_palavra_reservada = 2;
    int t_pr_main = 3;
    int t_pr_end = 4;
    int t_pr_if = 5;
    int t_pr_elif = 6;
    int t_pr_else = 7;
    int t_pr_false = 8;
    int t_pr_true = 9;
    int t_pr_read = 10;
    int t_pr_write = 11;
    int t_pr_writeln = 12;
    int t_pr_repeat = 13;
    int t_pr_until = 14;
    int t_pr_while = 15;
    int t_TOKEN_16 = 16; //"&&"
    int t_TOKEN_17 = 17; //"||"
    int t_TOKEN_18 = 18; //"!"
    int t_TOKEN_19 = 19; //"=="
    int t_TOKEN_20 = 20; //"!="
    int t_TOKEN_21 = 21; //"<"
    int t_TOKEN_22 = 22; //">"
    int t_TOKEN_23 = 23; //"+"
    int t_TOKEN_24 = 24; //"-"
    int t_TOKEN_25 = 25; //"*"
    int t_TOKEN_26 = 26; //"/"
    int t_TOKEN_27 = 27; //","
    int t_TOKEN_28 = 28; //";"
    int t_TOKEN_29 = 29; //"="
    int t_TOKEN_30 = 30; //"("
    int t_TOKEN_31 = 31; //")"
    int t_id = 32;
    int t_constante_int = 33;
    int t_constante_float = 34;
    int t_constante_string = 35;
    int t_comentario = 36;
    int t_caractere_de_formatação = 37;

}
