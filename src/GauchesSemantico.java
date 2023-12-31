import com.sun.org.apache.regexp.internal.RE;
import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.*;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

/**
 * Sintático -> Augusto Hansen Mentz

 Gramatica:

 <G> ::= 'ME_CAIU' <LISTA_VARIAVEIS> <LISTA_METODOS> 'OS_BUTIA_DO_BOLSO'
 <LISTA_VARIAVEIS> ::= 'BAGULHETES' , <VARS> ';'
 <VARS> ::= <VAR> , <VARS>
 <VARS> ::= <VAR>
 <VAR>  ::= <ID>
 <VARIAVEL>  ::= <ID>
 <LISTA_FUNCOES> ::= <FUNCAO> ';' <LISTA_FUNCOES>
 <LISTA_FUNCOES> ::= <FUNCAO>
 <FUNCAO> ::= <FUNCAO_RECEBER>
 <FUNCAO> ::= <FUNCAO_ESCREVER>
 <FUNCAO> ::= <FUNCAO_LER>
 <FUNCAO> ::= <FUNCAO_IF>
 <FUNCAO> ::= <FUNCAO_ATE>
 <FUNCAO> ::= <FUNCAO_EXECUTA_FUNCAO>
 <FUNCAO> ::= <FUNCAO_PARA>
 <FUNCAO_IF> ::= 'INICIO_QUEBRA' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'FIM_QUEBRA'
 <FUNCAO_IF> ::= 'INICIO_QUEBRA' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'MOLAS' <LISTA_FUNCAO> 'FIM_QUEBRA'
 <FUNCAO_ATE> ::= 'TRAMPA_ATE' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'FIM_TRAMPA_ATE'
 <FUNCAO_RECEBER> ::= <VARIAVEL> 'APROCHEGA' <EXP>
 <FUNCAO_ESCREVER> ::= 'BAH' '[' '"' <EXP> '"' ']'
 <FUNCAO_LER> ::= 'TCHE' '(' <VARIAVEL> ')'
 <FUNCAO_ESCOLHA> ::= 'INICIO_TROVAR' '[' <EXP> ']' <CASOS> 'FIM_TROVAR'
 <FUNCAO_PARA> ::= <FUNCAO_PARA> ::= 'INICIO_PELEIA' <VARIAVEL> 'APROCHEGA' <EXP> 'ATE_LEVAR' <EXP> 'TUNDA' <LISTA_FUNCOES> 'FIM_PELEIA'
 <CASOS> ::= <CASOS> '.' <CASOS>
 <CASOS> ::= <CASO>
 <CASO> ::= 'BAGUAL' <EXP> ':' <LISTA_FUNCOES>
 <CONDICAO> ::= '[' <EXP> ']' 'EM_CIMA_DO_LACO' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'EM_CIMA_DO_LACO_OU_IGUAL' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'BEM_CAPAZ' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'EM_BAIXO_DO_LACO_OU_IGUAL' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'EM_BAIXO_LACO' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'BUCHA' '[' <EXP> ']'
 <EXP> ::= <EXP> + <T>
 <EXP> ::= <EXP> - <T>
 <EXP> ::= <T>
 <T> ::= <T> * <F>
 <T> ::= <T> / <F>
 <T> ::= <T> % <F>
 <T> ::= <F>
 <F> ::= -<X>
 <F> ::= <X> ** <F>
 <F> ::= <X>
 <X> ::= '(' '[' <EXP> ']' ')'
 <X> ::= [0-9]+('.'[0-9]+)
 <X> ::= <VAR>
 <ID> ::= [A-Z]+([A-Z]_[0-9])*
 */

public class GauchesSemantico {

    // Lista de tokens
    static final int T_ME_CAIU = 1;
    static final int T_OS_BUTIA_DO_BOLSO = 2;
    static final int T_BAGULHETES = 3;
    static final int T_VIRGULA = 4;
    static final int T_PONTO_VIRGULA = 5;
    static final int T_INICIO_QUEBRA = 6;
    static final int T_MOLAS = 7;
    static final int T_FIM_QUEBRA = 8;

    static final int T_APROCHEGA = 9;

    static final int T_BAH = 10;

    static final int T_INICIO_TROVAR = 12;

    static final int T_FIM_TROVAR = 13;

    static final int T_BAGUAL = 14;

    static final int T_ECL = 15;

    static final int T_ECL_OU_IGUAL = 16;

    static final int T_BEM_CAPAZ = 17;

    static final int T_EBL_OU_IGUAL = 18;

    static final int T_EBL = 19;

    static final int T_BUCHA = 20;

    static final int T_TRAMPA_ATE = 21;
    static final int T_FIM_TRAMPA_ATE = 22;

    static final int T_TCHE = 23;

    static final int T_ABRE_PAR = 24;
    static final int T_FECHA_PAR = 25;

    static final int T_ABRE_COL = 26;

    static final int T_FECHA_COL = 27;

    static final int T_MAIS = 28;
    static final int T_MENOS = 29;
    static final int T_VEZES = 30;
    static final int T_DIVIDIDO = 31;
    static final int T_RESTO = 32;
    static final int T_ELEVADO = 33;
    static final int T_NUMERO = 34;
    static final int T_ID = 35;
    static final int T_DOIS_PONTOS = 36;
    static final int T_ASPAS_DUPLAS = 37;

    static final int T_PONTO = 38;

    static final int T_INICIO_PELEIA = 39;

    static final int T_ATE_LEVAR = 41;

    static final int T_TUNDA = 40;

    static final int T_FIM_PELEIA = 43;

    static final int T_FIM_FONTE = 90;
    static final int T_ERRO_LEX = 98;
    static final int T_NULO = 99;

    static final int FIM_ARQUIVO = 226;

    static final int E_SEM_ERROS = 0;

    static final int E_ERRO_LEXICO = 1;
    static final int E_ERRO_SINTATICO = 2;

    static final int E_ERRO_SEMANTICO    =   3;

    static File arqFonte;
    static BufferedReader rdFonte;
    static File arqDestino;

    static char lookAhead;
    static int token;
    static String lexema;
    static int ponteiro;
    static String linhaFonte;
    static int linhaAtual;
    static int colunaAtual;
    static String mensagemDeErro;
    static StringBuffer tokensIdentificados = new StringBuffer();

    // Vars sintatico
    static StringBuffer regrasReconhecidas = new StringBuffer();
    static int estadoCompilacao;

    // Vars semantico
    static String ultimoLexema;
    static StringBuffer codigoGauchesco = new StringBuffer();
    static int nivelIdentacao = 0; // para saber quantos espaços eu dou
    static String exp_0;
    static String exp_1;
    static String exp_2;
    static String exp_alvo;
    static NodoPilhaSemantica nodo;
    static NodoPilhaSemantica nodo_0;
    static NodoPilhaSemantica nodo_1;
    static NodoPilhaSemantica nodo_2;
    static PilhaSemantica     pilhaSemantica = new PilhaSemantica();
    static HashMap<String,Integer> tabelaSimbolos = new HashMap<String,Integer>();

    public static void main(String s[])  throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        try {
            abreArquivo();
            abreDestino();
            linhaAtual = 0;
            colunaAtual = 0;
            ponteiro = 0;
            linhaFonte = "";
            token = T_NULO;
            mensagemDeErro = "";
            tokensIdentificados.append( "Tokens reconhecidos: \n\n" );
            regrasReconhecidas.append( "\n\nRegras reconhecidas: \n\n" );
            estadoCompilacao 	= E_SEM_ERROS;

            movelookAhead();
            buscaProximoToken();

            analiseSintatica();
            exibeSaida();
            gravaSaida(arqDestino);
            fechaFonte();
        } catch( FileNotFoundException fnfe ) {
            JOptionPane.showMessageDialog( null, "Arquivo nao existe!", "FileNotFoundException!", JOptionPane.ERROR_MESSAGE );
        } catch( UnsupportedEncodingException uee ) {
            JOptionPane.showMessageDialog( null, "Erro desconhecido", "UnsupportedEncodingException!", JOptionPane.ERROR_MESSAGE );
        } catch( IOException ioe ) {
            JOptionPane.showMessageDialog( null, "Erro de io: " + ioe.getMessage(), "IOException!", JOptionPane.ERROR_MESSAGE );
        } catch( ErroLexicoException ele ) {
            JOptionPane.showMessageDialog( null, ele.getMessage(), "Erro Lexico Exception!", JOptionPane.ERROR_MESSAGE );
        } catch( ErroSintaticoException ese ) {
            JOptionPane.showMessageDialog( null, ese.getMessage(), "Erro Sint�tico Exception!", JOptionPane.ERROR_MESSAGE );
        } catch( ErroSemanticoException esme ) {
            JOptionPane.showMessageDialog( null, esme.getMessage(), "Erro Semantico Exception!", JOptionPane.ERROR_MESSAGE );
        } finally {
            System.out.println( "Execucao terminada!" );
        }
    }

    static void analiseSintatica() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {

        g();

        if (estadoCompilacao == E_ERRO_LEXICO) {
            JOptionPane.showMessageDialog(null, mensagemDeErro, "Erro Lexico!", JOptionPane.ERROR_MESSAGE);
        } else if (estadoCompilacao == E_ERRO_SINTATICO) {
            JOptionPane.showMessageDialog(null, mensagemDeErro, "Erro Sintatico!", JOptionPane.ERROR_MESSAGE);
        }  else if (estadoCompilacao == E_ERRO_SEMANTICO) {
            JOptionPane.showMessageDialog( null, mensagemDeErro, "Erro Semantico!", JOptionPane.ERROR_MESSAGE );
        } else {
            JOptionPane.showMessageDialog(null, "Sintatico analisado e sem erros", "Analise Sintatica terminada!", JOptionPane.INFORMATION_MESSAGE);
            acumulaRegraSintaticaReconhecida("<G>");
        }
    }

    // <G> ::= 'ME_CAIU' <LISTA_VARIAVEIS> <LISTA_METODOS> 'OS_BUTIA_DO_BOLSO'
    private static void g() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if (token == T_ME_CAIU) {
            buscaProximoToken();
            regraSemantica(0);
            listaVariaveis();
            listaFuncoes();
            if (token == T_OS_BUTIA_DO_BOLSO) {
                buscaProximoToken();
                regraSemantica(1);
                acumulaRegraSintaticaReconhecida("<G> ::= 'ME_CAIU' <LISTA_VARIAVEIS> <LISTA_METODOS> 'OS_BUTIA_DO_BOLSO'");
            } else {
                registraErroSintatico("Erro Sintatico. Linha: " + linhaAtual + "\nColuna: " + colunaAtual + "\nErro: <" + linhaFonte + ">\n('os_butia_do_bolso') esperado, mas encontrei: " + lexema);
            }
        } else {
            registraErroSintatico("Erro Sintatico. Linha: " + linhaAtual + "\nColuna: " + colunaAtual + "\nErro: <" + linhaFonte + ">\n('me_caiu') esperado, mas encontrei: " + lexema);
        }
    }

    // <LISTA_VARIAVEIS> ::= 'BAGULHETES' , <VARS> ';'
    private static void listaVariaveis() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if (token == T_BAGULHETES) {
            buscaProximoToken();
            vars();
            if (token == T_PONTO_VIRGULA) {
                buscaProximoToken();
                acumulaRegraSintaticaReconhecida("<LISTA_VARIAVEIS> ::= 'BAGULHETES' , <VARS> ';'");
            } else {
                registraErroSintatico("Erro Sintatico. Linha: " + linhaAtual + "\nColuna: " + colunaAtual + "\nErro: <" + linhaFonte + ">\n';' esperado, mas encontrei: " + lexema);
            }
        } else {
            registraErroSintatico("Erro Sintatico. Linha: " + linhaAtual + "\nColuna: " + colunaAtual + "\nErro: <" + linhaFonte + ">\n('bagulhetes') esperado, mas encontrei: " + lexema);
        }
    }

    // <VARS> ::= <VAR> , <VARS>
    // <VARS> ::= <VAR>
    private static void vars() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        var();
        while (token == T_VIRGULA) {
            buscaProximoToken();
            var();
        }
        acumulaRegraSintaticaReconhecida("<VARS> ::= <VAR> , <VARS> | <VAR>");
    }

    // <VAR> ::= <ID>
    private static void var() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        id();
        regraSemantica(2);
        acumulaRegraSintaticaReconhecida("<VAR> ::= <ID>");
    }

    //<variavel> ::= <ID>
    private static void variavel() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        id();
        regraSemantica( 4 );
        acumulaRegraSintaticaReconhecida( "<VARIAVEL> ::= <ID>" );
    }

    // <ID> ::= [A-Z]+([A-Z]_[0-9])*
    private static void id() throws IOException, ErroLexicoException, ErroSintaticoException {
        if (token == T_ID) {
            buscaProximoToken();
            acumulaRegraSintaticaReconhecida("<id> ::= [A-Z]+([A-Z]_[0-9])*");
        } else {
            registraErroSintatico("Erro Sintatico. Linha: " + linhaAtual + "\nColuna: " + colunaAtual + "\nErro: <" + linhaFonte + ">\nEsperava um identificador. Encontrei: " + lexema);
        }
    }

    // <LISTA_FUNCOES> ::= <LISTA_FUNCOES> ; <FUNCAO>
    // <LISTA_FUNCOES> ::= <FUNCAO>
    private static void listaFuncoes() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        funcao();
        while (token == T_PONTO_VIRGULA) {
            buscaProximoToken();
            funcao();
        }
        acumulaRegraSintaticaReconhecida("<LISTA_FUNCOES> ::= <FUNCAO> ; <LISTA_FUNCOES> | <FUNCAO>");
    }

    // <FUNCAO> ::= <FUNCAO_RECEBER>
    // <FUNCAO> ::= <FUNCAO_ESCREVER>
    // <FUNCAO> ::= <FUNCAO_LER>
    // <FUNCAO> ::= <FUNCAO_IF>
    // <FUNCAO> ::= <FUNCAO_ATE>
    // <FUNCAO> ::= <FUNCAO_ESCOLHA>
    private static void funcao() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        switch (token) {
            case T_INICIO_QUEBRA:
                funcaoIf();
                break;
            case T_TRAMPA_ATE:
                funcaoAte();
                break;
            case T_ID:
                funcaoReceber();
                break;
            case T_BAH:
                funcaoEscrever();
                break;
            case T_TCHE:
                funcaoLer();
                break;
            case T_INICIO_TROVAR:
                funcaoEscolha();
                break;
            case T_INICIO_PELEIA:
                funcaoPara();
                break;
            default:
                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\nMetodo nao identificado va aprender a programar pois encontrei: " + lexema);
        }
        acumulaRegraSintaticaReconhecida(
            "<FUNCAO> ::= <FUNCAO_RECEBER> | <FUNCAO_ESCREVER> | <FUNCAO_LER> | <FUNCAO_IF> | <FUNCAO_ATE> | <FUNCAO_EXECUTA_FUNCAO>"
        );
    }

    // <FUNCAO_LER> ::= 'TCHE' '(' <VAR> ')'
    private static void funcaoLer() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if (token == T_TCHE)  {
            buscaProximoToken();
            if (token == T_ABRE_PAR) {
                buscaProximoToken();
                variavel();
                if (token == T_FECHA_PAR) {
                    buscaProximoToken();
                    regraSemantica(27);
                    acumulaRegraSintaticaReconhecida("<FUNCAO_LER> ::= 'TCHE' '(' <VAR> ')'");
                }
            } else {
                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'(' esperado mas encontrei: " + lexema);
            }
        } else {
            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'tche' esperado mas encontrei: " + lexema);
        }
    }

    // <FUNCAO_ATE> ::= 'TRAMPA_ATE' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'FIM_TRAMPA_ATE'
    private static void funcaoAte() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if (token == T_TRAMPA_ATE) {
            buscaProximoToken();
            if (token == T_ABRE_PAR) {
                buscaProximoToken();
                condicao();
                regraSemantica(25);
                if (token == T_FECHA_PAR) {
                    buscaProximoToken();
                    listaFuncoes();
                    regraSemantica(14);
                    if (token == T_FIM_TRAMPA_ATE) {
                        buscaProximoToken();
                        acumulaRegraSintaticaReconhecida("<FUNCAO_ATE> ::= 'TRAMPA_ATE' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'FIM_TRAMPA_ATE'");
                    }
                } else {
                    registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n')' esperado mas encontrei: " + lexema);
                }
            } else {
                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'(' esperado mas encontrei: " + lexema);
            }
        } else {
            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'trampa_ate' esperado mas encontrei: " + lexema);
        }
    }

    // <FUNCAO_IF> ::= 'INICIO_QUEBRA' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'FIM_QUEBRA'
    // <FUNCAO_IF> ::= 'INICIO_QUEBRA' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'MOLAS' <LISTA_FUNCAO> 'FIM_QUEBRA'
    private static void funcaoIf() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if (token == T_INICIO_QUEBRA) {
            buscaProximoToken();
            if (token == T_ABRE_PAR) {
                buscaProximoToken();
                condicao();
                regraSemantica(17);
                if (token == T_FECHA_PAR) {
                    buscaProximoToken();
                    listaFuncoes();
                    regraSemantica(14);
                    if (token == T_MOLAS) {
                        regraSemantica(18);
                        buscaProximoToken();
                        listaFuncoes();
                        regraSemantica(16);
                    }
                    if (token == T_FIM_QUEBRA) {
                        buscaProximoToken();
                        acumulaRegraSintaticaReconhecida("<FUNCAO_IF> ::= 'INICIO_QUEBRA' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'FIM_QUEBRA'");
                    } else {
                        registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'fim_quebra ou molas' esperado mas encontrei: " + lexema);
                    }
                } else {
                    registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n')' esperado mas encontrei: " + lexema);
                }
            } else {
                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'(' esperado mas encontrei: " + lexema);
            }
        } else {
            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'inicio_quebra' esperado mas encontrei: " + lexema);
        }
    }

    // <CONDICAO> ::= '[' <EXP> ']' 'EM_CIMA_DO_LACO' '[' <EXP> ']'
    // <CONDICAO> ::= '[' <EXP> ']' 'EM_CIMA_DO_LACO_OU_IGUAL' '[' <EXP> ']'
    // <CONDICAO> ::= '[' <EXP> ']' 'BEM_CAPAZ' '[' <EXP> ']'
    // <CONDICAO> ::= '[' <EXP> ']' 'EM_BAIXO_DO_LACO_OU_IGUAL' '[' <EXP> ']'
    // <CONDICAO> ::= '[' <EXP> ']' 'EM_BAIXO_LACO' '[' <EXP> ']'
    // <CONDICAO> ::= '[' <EXP> ']' 'BUCHA' '[' <EXP> ']'
    private static void condicao() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if (token == T_ABRE_COL) {
            buscaProximoToken();
            exp();
            if (token == T_FECHA_COL) {
                buscaProximoToken();
                switch (token) {
                    case T_ECL: {
                        buscaProximoToken();
                        if (token == T_ABRE_COL) {
                            buscaProximoToken();
                            exp();
                            regraSemantica(19);
                            if (token == T_FECHA_COL) {
                                buscaProximoToken();
                                acumulaRegraSintaticaReconhecida("<CONDICAO> ::= '[' <EXP> ']' 'EM_CIMA_DO_LACO' '[' <EXP> ']'");
                            } else {
                                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema);
                            }
                        } else {
                            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema);
                        }
                        break;
                    }
                    case T_ECL_OU_IGUAL: {
                        buscaProximoToken();
                        if (token == T_ABRE_COL) {
                            buscaProximoToken();
                            exp();
                            regraSemantica(21);
                            if (token == T_FECHA_COL) {
                                buscaProximoToken();
                                acumulaRegraSintaticaReconhecida("<CONDICAO> ::= '[' <EXP> ']' 'EM_CIMA_DO_LACO_OU_IGUAL' '[' <EXP> ']'");
                            } else {
                                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema);
                            }
                        } else {
                            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema);
                        }
                        break;
                    }
                    case T_BEM_CAPAZ: {
                        buscaProximoToken();
                        if (token == T_ABRE_COL) {
                            buscaProximoToken();
                            exp();
                            regraSemantica(24);
                            if (token == T_FECHA_COL) {
                                buscaProximoToken();
                                acumulaRegraSintaticaReconhecida("<CONDICAO> ::= '[' <EXP> ']' 'BEM_CAPAZ' '[' <EXP> ']'");
                            } else {
                                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema);
                            }
                        } else {
                            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema);
                        }
                        break;
                    }
                    case T_EBL: {
                        buscaProximoToken();
                        if (token == T_ABRE_COL) {
                            buscaProximoToken();
                            exp();
                            regraSemantica(20);
                            if (token == T_FECHA_COL) {
                                buscaProximoToken();
                                acumulaRegraSintaticaReconhecida("<CONDICAO> ::= '[' <EXP> ']' 'EM_BAIXO_LACO' '[' <EXP> ']'");
                            } else {
                                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema);
                            }
                        } else {
                            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema);
                        }
                        break;
                    }
                    case T_EBL_OU_IGUAL: {
                        buscaProximoToken();
                        if (token == T_ABRE_COL) {
                            buscaProximoToken();
                            exp();
                            regraSemantica(22);
                            if (token == T_FECHA_COL) {
                                buscaProximoToken();
                                acumulaRegraSintaticaReconhecida("<CONDICAO> ::= '[' <EXP> ']' 'EM_BAIXO_DO_LACO_OU_IGUAL' '[' <EXP> ']'");
                            } else {
                                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema);
                            }
                        } else {
                            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema);
                        }
                        break;
                    }
                    case T_BUCHA: {
                        buscaProximoToken();
                        if (token == T_ABRE_COL) {
                            buscaProximoToken();
                            exp();
                            regraSemantica(23);
                            if (token == T_FECHA_COL) {
                                buscaProximoToken();
                                acumulaRegraSintaticaReconhecida("<CONDICAO> ::= '[' <EXP> ']' 'BUCHA' '[' <EXP> ']'");
                            } else {
                                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema);
                            }
                        } else {
                            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema);
                        }
                        break;
                    }
                    default:
                        registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'Comando não encontrado'  mas encontrei: " + lexema);
                }
            } else {
                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema);
            }
        } else {
            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema);
        }
    }

    // <FUNCAO_RECEBER> ::= <VAR> 'APROCHEGA' <EXP>
    private static void funcaoReceber() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        variavel();
        if (token == T_APROCHEGA) {
            buscaProximoToken();
            exp();
            regraSemantica(3);
            acumulaRegraSintaticaReconhecida("<FUNCAO_RECEBER> ::= <VAR> 'APROCHEGA' <EXP>");
        } else {
            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'>>' esperado mas encontrei: " + lexema);
        }
    }

    // <FUNCAO_ESCREVER> ::= 'BAH' '[' '"' <EXP> '"' ']'
    private static void funcaoEscrever() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if ( token == T_BAH ) {
            buscaProximoToken();
            if ( token == T_ABRE_COL ) {
                buscaProximoToken();
                if (token == T_ASPAS_DUPLAS) {
                    buscaProximoToken();
                    exp();
                    if (token == T_ASPAS_DUPLAS) {
                        buscaProximoToken();
                        if (token == T_FECHA_COL) {
                            buscaProximoToken();
                            regraSemantica(26);
                            acumulaRegraSintaticaReconhecida("<FUNCAO_ESCREVER> ::= 'BAH' '[' '" + "' <EXP> '' ']'");
                        }
                    } else {
                        registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'ASPAS_DUPLAS' esperado mas encontrei: " + lexema );
                    }
                } else {
                    registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'ASPAS_DUPLAS' esperado mas encontrei: " + lexema );
                }
            } else {
                registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema );
            }
        } else {
            registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'bah' esperado mas encontrei: " + lexema );
        }
    }

    // <FUNCAO_PARA> ::= 'INICIO_PELEIA' <VAR> 'APROCHEGA' <EXP> 'ATE_LEVAR' <EXP> 'UMA_TUNDA' <LISTA_FUNCOES> 'FIM_PELEIA'
    private static void funcaoPara() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if (token == T_INICIO_PELEIA) {
            buscaProximoToken();
            variavel();
            if (token == T_APROCHEGA) {
                buscaProximoToken();
                exp();
                if (token == T_ATE_LEVAR) {
                    buscaProximoToken();
                    exp();
                    regraSemantica(28);
                    if (token == T_TUNDA) {
                        buscaProximoToken();
                        listaFuncoes();
                        if (token == T_FIM_PELEIA) {
                            buscaProximoToken();
                            regraSemantica(16);
                            acumulaRegraSintaticaReconhecida("<FUNCAO_PARA> ::= 'INICIO_PELEIA' <VAR> 'APROCHEGA' <EXP> 'ATE_LEVAR' <EXP> 'TUNDA' <LISTA_FUNCOES> 'FIM_PELEIA'");
                        } else {
                            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'fim_peleia' esperado mas encontrei: " + lexema);
                        }
                    } else {
                        registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'tunda' esperado mas encontrei: " + lexema);
                    }
                } else {
                    registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'ate_levar' esperado mas encontrei: " + lexema);
                }
            } else {
                registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'aprochega' esperado mas encontrei: " + lexema);
            }
        } else {
            registraErroSintatico("Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'inicio_peleia' esperado mas encontrei: " + lexema);
        }
    }

     // <FUNCAO_ESCOLHA> ::= 'INICIO_TROVAR' '[' <EXP> ']' <CASOS> 'FIM_TROVAR'
     private static void funcaoEscolha() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
         if (token == T_INICIO_TROVAR) {
             buscaProximoToken();
             if (token == T_ABRE_COL) {
                 buscaProximoToken();
                 exp();
                 regraSemantica(30);
                 if (token == T_FECHA_COL) {
                     buscaProximoToken();
                     casos();
                     if (token == T_FIM_TROVAR) {
                         regraSemantica(32);
                         regraSemantica(16);
                         buscaProximoToken();
                         acumulaRegraSintaticaReconhecida("<FUNCAO_ESCOLHA> ::= 'INICIO_TROVAR' '[' <EXP> ']' <CASOS> 'FIM_TROVAR'");
                     } else {
                         registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'fim_trovar' esperado mas encontrei: " + lexema );
                     }
                 } else {
                     registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema );
                 }
             } else {
                 registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema );
             }
         } else {
             registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'inicio_trovar' esperado mas encontrei: " + lexema );
         }
     }

     // <CASOS> ::= <CASOS> '.' <CASOS>
     // <CASOS> ::= <CASO>
     private static void casos() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        caso();
        while(token == T_PONTO) {
            buscaProximoToken();
            caso();
        }
        acumulaRegraSintaticaReconhecida( "<CASOS> ::= <CASOS> '.' <CASOS> | <CASO>" );
     }


     // <CASO> ::= 'BAGUAL' <EXP> ':' <LISTA_FUNCOES>
    private static void caso() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if (token == T_BAGUAL) {
            buscaProximoToken();
            exp();
            regraSemantica(31);
            if (token == T_DOIS_PONTOS) {
                buscaProximoToken();
                listaFuncoes();
                acumulaRegraSintaticaReconhecida("<CASO> ::= 'BAGUAL' <EXP> ':' <LISTA_FUNCOES>");
            } else {
                registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n':' esperado mas encontrei: " + lexema );
            }
        } else {
            registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'bagual' esperado mas encontrei: " + lexema );
        }
    }

    // <EXP> ::= <EXP> + <T>
    // <EXP> ::= <EXP> - <T>
    // <EXP> ::= <T>
    private static void exp() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        t();
        while ((token == T_MAIS) || (token == T_MENOS)) {
            switch (token) {
                case T_MAIS: {
                    buscaProximoToken();
                    t();
                    regraSemantica(5);
                }
                break;
                case T_MENOS: {
                    buscaProximoToken();
                    t();
                    regraSemantica(6);
                }
                break;
            }
        }
        acumulaRegraSintaticaReconhecida(
            "<EXP> ::= <EXP> + <T> | <EXP> - <T> | <T>"
        );
    }

    // <T> ::= <T> * <F>
    // <T> ::= <T> / <F>
    // <T> ::= <T> % <F>
    // <T> ::= <F>
    private static void t() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        f();
        while ( (token == T_VEZES) || (token == T_DIVIDIDO) || (token == T_RESTO) ) {
            switch (token) {
                case T_VEZES: {
                    buscaProximoToken();
                    f();
                    regraSemantica(7);
                }
                break;
                case T_DIVIDIDO: {
                    buscaProximoToken();
                    f();
                    regraSemantica(8);
                }
                break;
                case T_RESTO: {
                    buscaProximoToken();
                    f();
                    regraSemantica(9);
                }
                break;
            }
        }
        acumulaRegraSintaticaReconhecida(
            "<T> ::= <T> * <F> | <T> / <F> | <T> % <F> | <F>"
        );
    }

    // <F> ::= -<F>
    // <F> ::= <X> ** <F>
    // <F> ::= <X>
    private static void f() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        if ( token == T_MENOS ) {
            buscaProximoToken();
            f();
        } else {
            x();
            while ( token == T_ELEVADO ) {
                buscaProximoToken();
                x();
                regraSemantica(10);
            }
        }
        acumulaRegraSintaticaReconhecida(
            "<F> ::= -<X> | <X> ** <F> | <X>"
        );

    }

    // <X> ::= '(' '[' <EXP> ']' ')'
    // <X> ::= [0-9]+('.'[0-9]+)
    // <X> ::= <VAR>
    private static void x() throws IOException, ErroLexicoException, ErroSintaticoException, ErroSemanticoException {
        switch ( token ) {
            case T_ID: buscaProximoToken(); acumulaRegraSintaticaReconhecida( "<X> ::= <VAR>" ); regraSemantica(11); break;
            case T_NUMERO: buscaProximoToken(); acumulaRegraSintaticaReconhecida( "<X> ::= [0-9]+('.'[0-9]+)" ); regraSemantica(12); break;
            case T_ABRE_PAR: {
                buscaProximoToken();
                if (token == T_ABRE_COL) {
                    buscaProximoToken();
                    exp();
                    if (token == T_FECHA_COL) {
                        buscaProximoToken();
                        if (token == T_FECHA_PAR) {
                            buscaProximoToken();

                            acumulaRegraSintaticaReconhecida("<X> ::= '(' '[' <EXP> ']' ')'");
                        }
                        regraSemantica(13);
                    } else {
                        registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n']' esperado mas encontrei: " + lexema );
                    }
                } else {
                    registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\n'[' esperado mas encontrei: " + lexema );
                }
            } break;
            default: registraErroSintatico( "Erro Sintatico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\nFator invalido: encontrei: " + lexema );
        }
    }

    static void fechaFonte() throws IOException
    {
        rdFonte.close();
    }

    static void movelookAhead() throws IOException
    {
        if ( ( ponteiro + 1 ) > linhaFonte.length() ) {
            linhaAtual++;
            ponteiro = 0;
            if ( ( linhaFonte = rdFonte.readLine() ) == null ) {
                lookAhead = FIM_ARQUIVO;
            } else {
                StringBuffer sbLinhaFonte = new StringBuffer( linhaFonte );
                sbLinhaFonte.append( '\13' ).append( '\10' );
                linhaFonte = sbLinhaFonte.toString();
                lookAhead = linhaFonte.charAt( ponteiro );
            }
        } else {
            lookAhead = linhaFonte.charAt( ponteiro );
        }
        if ( ( lookAhead >= 'a' ) &&
                ( lookAhead <= 'z' ) ) {
            lookAhead = (char) ( lookAhead - 'a' + 'A' );
        }
        ponteiro++;
        colunaAtual = ponteiro + 1;
    }

    static void buscaProximoToken() throws IOException
    {
        //int i, j;
        if ( lexema != null ) {
            ultimoLexema = new String( lexema );
        }

        StringBuffer sbLexema = new StringBuffer( "" );

        // Salto espa�oes enters e tabs at� o inicio do proximo token
        while ( ( lookAhead == 9 ) ||
                ( lookAhead == '\n' ) ||
                ( lookAhead == 8 ) ||
                ( lookAhead == 11 ) ||
                ( lookAhead == 12 ) ||
                ( lookAhead == '\r' ) ||
                ( lookAhead == 32 ) )
        {
            movelookAhead();
        }

        /*--------------------------------------------------------------*
         * Caso o primeiro caracter seja alfabetico, procuro capturar a *
         * sequencia de caracteres que se segue a ele e classifica-la   *
         *--------------------------------------------------------------*/
        if ( ( lookAhead >= 'A' ) && ( lookAhead <= 'Z' ) ) {
            sbLexema.append( lookAhead );
            movelookAhead();

            while ( ( ( lookAhead >= 'A' ) && ( lookAhead <= 'Z' ) ) ||
                    ( ( lookAhead >= '0' ) && ( lookAhead <= '9' ) ) || ( lookAhead == '_' ) )
            {
                sbLexema.append( lookAhead );
                movelookAhead();
            }

            lexema = sbLexema.toString();

            /* Classifico o meu token como palavra reservada ou id */
            if ( lexema.equals( "ME_CAIU" ) )
                token = T_ME_CAIU;
            else if ( lexema.equals( "OS_BUTIA_DO_BOLSO" ) )
                token = T_OS_BUTIA_DO_BOLSO;
            else if ( lexema.equals( "BAGULHETES" ) )
                token = T_BAGULHETES;
            else if ( lexema.equals( "INICIO_QUEBRA" ) )
                token = T_INICIO_QUEBRA;
            else if ( lexema.equals( "MOLAS" ) )
                token = T_MOLAS;
            else if ( lexema.equals( "FIM_QUEBRA" ) )
                token = T_FIM_QUEBRA;
            else if ( lexema.equals( "TRAMPA_ATE" ) )
                token = T_TRAMPA_ATE;
            else if ( lexema.equals( "FIM_TRAMPA_ATE" ) )
                token = T_FIM_TRAMPA_ATE;
            else if ( lexema.equals( "APROCHEGA" ) )
                token = T_APROCHEGA;
            else if ( lexema.equals( "BAH" ) )
                token = T_BAH;
            else if ( lexema.equals( "TCHE" ) )
                token = T_TCHE;
            else if ( lexema.equals( "INICIO_TROVAR" ) )
                token = T_INICIO_TROVAR;
            else if ( lexema.equals( "FIM_TROVAR" ) )
                token = T_FIM_TROVAR;
            else if ( lexema.equals( "BAGUAL" ) )
                token = T_BAGUAL;
            else if ( lexema.equals( "EM_CIMA_DO_LACO" ) )
                token = T_ECL;
            else if ( lexema.equals( "EM_CIMA_DO_LACO_OU_IGUAL" ) )
                token = T_ECL_OU_IGUAL;
            else if ( lexema.equals( "BEM_CAPAZ" ) )
                token = T_BEM_CAPAZ;
            else if ( lexema.equals( "EM_BAIXO_DO_LACO_OU_IGUAL" ) )
                token = T_EBL_OU_IGUAL;
            else if ( lexema.equals( "EM_BAIXO_DO_LACO" ) )
                token = T_EBL;
            else if ( lexema.equals( "BUCHA" ) )
                token = T_BUCHA;
            else if ( lexema.equals( "INICIO_PELEIA" ) )
                token = T_INICIO_PELEIA;
            else if ( lexema.equals( "ATE_LEVAR" ) )
                token = T_ATE_LEVAR;
            else if ( lexema.equals( "TUNDA" ) )
                token = T_TUNDA;
            else if ( lexema.equals( "FIM_PELEIA" ) )
                token = T_FIM_PELEIA;
            else {
                token = T_ID;
            }
        } else if ( ( lookAhead >= '0' ) && ( lookAhead <= '9' ) ) {
            sbLexema.append( lookAhead );
            movelookAhead();
            while ( ( lookAhead >= '0' ) && ( lookAhead <= '9' ) )
            {
                sbLexema.append( lookAhead );
                movelookAhead();
            }
            token = T_NUMERO;
        } else if ( lookAhead == '(' ){
            sbLexema.append( lookAhead );
            token = T_ABRE_PAR;
            movelookAhead();
        } else if ( lookAhead == ')' ){
            sbLexema.append( lookAhead );
            token = T_FECHA_PAR;
            movelookAhead();
        } else if ( lookAhead == '[' ){
            sbLexema.append( lookAhead );
            token = T_ABRE_COL;
            movelookAhead();
        } else if ( lookAhead == ']' ){
            sbLexema.append( lookAhead );
            token = T_FECHA_COL;
            movelookAhead();
        } else if ( lookAhead == ';' ){
            sbLexema.append( lookAhead );
            token = T_PONTO_VIRGULA;
            movelookAhead();
        } else if ( lookAhead == ':' ){
            sbLexema.append( lookAhead );
            token = T_DOIS_PONTOS;
            movelookAhead();
        }
        else if ( lookAhead == '.' ){
            sbLexema.append( lookAhead );
            token = T_PONTO;
            movelookAhead();
        } else if ( lookAhead == '"' ){
            sbLexema.append( lookAhead );
            token = T_ASPAS_DUPLAS;
            movelookAhead();
        } else if ( lookAhead == ',' ){
            sbLexema.append( lookAhead );
            token = T_VIRGULA;
            movelookAhead();
        } else if ( lookAhead == '+' ){
            sbLexema.append( lookAhead );
            token = T_MAIS;
            movelookAhead();
        } else if ( lookAhead == '-' ){
            sbLexema.append( lookAhead );
            token = T_MENOS;
            movelookAhead();
        } else if ( lookAhead == '*' ){
            sbLexema.append( lookAhead );
            movelookAhead();
            if ( lookAhead == '*' ) {
                sbLexema.append( lookAhead );
                movelookAhead();
                token = T_ELEVADO;
            } else {
                token = T_VEZES;
            }
        } else if ( lookAhead == '/' ){
            sbLexema.append( lookAhead );
            token = T_DIVIDIDO;
            movelookAhead();
        } else if ( lookAhead == '%' ){
            sbLexema.append( lookAhead );
            token = T_RESTO;
            movelookAhead();
        } else if ( lookAhead == FIM_ARQUIVO ){
            token = T_FIM_FONTE;
        } else {
            token = T_ERRO_LEX;
            mensagemDeErro = "Erro L�xico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual + "\nLinha do Erro: <" + linhaFonte + ">\nToken desconhecido: " + lookAhead;
            sbLexema.append( lookAhead );
        }

        lexema = sbLexema.toString();

        mostraToken();
    }

    static void mostraToken()
    {

        StringBuffer tokenLexema = new StringBuffer( "" );
        switch ( token ) {
            case T_ME_CAIU    : tokenLexema.append( "T_ME_CAIU" ); break;
            case T_OS_BUTIA_DO_BOLSO:     tokenLexema.append( "T_OS_BUTIA_DO_BOLSO" ); break;
            case T_BAGULHETES    : tokenLexema.append( "T_BAGULHETES" ); break;
            case T_VIRGULA    : tokenLexema.append( "T_VIRGULA" ); break;
            case T_PONTO_VIRGULA    : tokenLexema.append( "T_PONTO_VIRGULA" ); break;
            case T_INICIO_QUEBRA    : tokenLexema.append( "T_INICIO_QUEBRA" ); break;
            case T_MOLAS    : tokenLexema.append( "T_MOLAS" ); break;
            case T_FIM_QUEBRA    : tokenLexema.append( "T_FIM_QUEBRA" ); break;
            case T_APROCHEGA    : tokenLexema.append( "T_APROCHEGA" ); break;
            case T_BAH    : tokenLexema.append( "T_BAH" ); break;
            case T_INICIO_TROVAR            : tokenLexema.append( "T_INICIO_TROVAR" ); break;
            case T_FIM_TROVAR             : tokenLexema.append( "T_FIM_TROVAR" ); break;
            case T_BAGUAL        : tokenLexema.append( "T_BAGUAL" ); break;
            case T_ECL             : tokenLexema.append( "T_ECL" ); break;
            case T_ECL_OU_IGUAL        : tokenLexema.append( "T_ECL_OU_IGUAL" ); break;
            case T_BEM_CAPAZ       : tokenLexema.append( "T_BEM_CAPAZ" ); break;
            case T_EBL_OU_IGUAL        : tokenLexema.append( "T_EBL_OU_IGUAL" ); break;
            case T_EBL           : tokenLexema.append( "T_EBL" ); break;
            case T_BUCHA           : tokenLexema.append( "T_BUCHA" ); break;
            case T_TRAMPA_ATE     : tokenLexema.append( "T_TRAMPA_ATE" ); break;
            case T_FIM_TRAMPA_ATE     : tokenLexema.append( "T_FIM_TRAMPA_ATE" ); break;
            case T_INICIO_PELEIA    : tokenLexema.append("T_INICIO_PELEIA"); break;
            case T_ATE_LEVAR    : tokenLexema.append("T_ATE_LEVAR"); break;
            case T_TUNDA    : tokenLexema.append("T_TUNDA"); break;
            case T_FIM_PELEIA    : tokenLexema.append("T_FIM_PELEIA"); break;
            case T_TCHE           : tokenLexema.append( "T_TCHE" ); break;
            case T_ABRE_PAR       : tokenLexema.append( "T_ABRE_PAR" ); break;
            case T_FECHA_PAR       : tokenLexema.append( "T_FECHA_PAR" ); break;
            case T_ABRE_COL       : tokenLexema.append( "T_ABRE_COL" ); break;
            case T_FECHA_COL       : tokenLexema.append( "T_FECHA_COL" ); break;
            case T_MAIS            : tokenLexema.append( "T_MAIS" ); break;
            case T_MENOS           : tokenLexema.append( "T_MENOS" ); break;
            case T_VEZES           : tokenLexema.append( "T_VEZES" ); break;
            case T_DIVIDIDO        : tokenLexema.append( "T_DIVIDIDO" ); break;
            case T_RESTO           : tokenLexema.append( "T_RESTO" ); break;
            case T_ELEVADO         : tokenLexema.append( "T_ELEVADO" ); break;
            case T_NUMERO          : tokenLexema.append( "T_NUMERO" ); break;
            case T_ID              : tokenLexema.append( "T_ID" ); break;
            case T_FIM_FONTE       : tokenLexema.append( "T_FIM_FONTE" ); break;
            case T_DOIS_PONTOS        : tokenLexema.append( "T_DOIS_PONTOS" ); break;
            case T_ASPAS_DUPLAS        : tokenLexema.append( "T_ASPAS_DUPLAS" ); break;
            case T_NULO            : tokenLexema.append( "T_NULO" ); break;
            case T_ERRO_LEX        : tokenLexema.append( "T_ERRO_LEX" ); break;
            case T_PONTO           : tokenLexema.append( "T_PONTO" ); break;
            default                : tokenLexema.append( "N/A" ); break;
        }
        System.out.println( tokenLexema.toString() + " ( " + lexema + " )" );
        acumulaToken( tokenLexema.toString() + " ( " + lexema + " )" );
        tokenLexema.append( lexema );
    }

    private static void abreArquivo() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

        FiltroSab filtro = new FiltroSab();

        fileChooser.addChoosableFileFilter( filtro );
        int result = fileChooser.showOpenDialog( null );

        if( result == JFileChooser.CANCEL_OPTION ) {
            return;
        }

        arqFonte = fileChooser.getSelectedFile();
        abreFonte( arqFonte );

    }


    private static boolean abreFonte( File fileName ) {

        if( arqFonte == null || fileName.getName().trim().equals( "" ) ) {
            JOptionPane.showMessageDialog( null, "Nome de Arquivo Inv�lido", "Nome de Arquivo Inv�lido", JOptionPane.ERROR_MESSAGE );
            return false;
        } else {
            linhaAtual = 1;
            try {
                FileReader fr = new FileReader( arqFonte );
                rdFonte = new BufferedReader( fr );
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
    }

    private static void abreDestino() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

        FiltroSab filtro = new FiltroSab();

        fileChooser.addChoosableFileFilter( filtro );
        int result = fileChooser.showSaveDialog( null );

        if( result == JFileChooser.CANCEL_OPTION ) {
            return;
        }

        arqDestino = fileChooser.getSelectedFile();
    }


    private static boolean gravaSaida( File fileName ) {

        if( arqDestino == null || fileName.getName().trim().equals( "" ) ) {
            JOptionPane.showMessageDialog( null, "Nome de Arquivo Inv�lido", "Nome de Arquivo Inv�lido", JOptionPane.ERROR_MESSAGE );
            return false;
        } else {
            FileWriter fw;
            try {
                System.out.println( arqDestino.toString() );
                System.out.println( tokensIdentificados.toString() );
                fw = new FileWriter( arqDestino );
                BufferedWriter bfw = new BufferedWriter( fw );
                bfw.write( tokensIdentificados.toString() );
                bfw.write(regrasReconhecidas.toString());
                bfw.write( "\n" );
                bfw.write(codigoGauchesco.toString());
                bfw.close();
                JOptionPane.showMessageDialog( null, "Arquivo Salvo: " + arqDestino, "Salvando Arquivo", JOptionPane.INFORMATION_MESSAGE );
            } catch (IOException e) {
                JOptionPane.showMessageDialog( null, e.getMessage(), "Erro de Entrada/Sa�da", JOptionPane.ERROR_MESSAGE );
            }
            return true;
        }
    }

    public static void exibeTokens() {

        JTextArea texto = new JTextArea();
        texto.append( tokensIdentificados.toString() );
        JOptionPane.showMessageDialog(null, texto, "Tokens Identificados (token/lexema)", JOptionPane.INFORMATION_MESSAGE );
    }

    public static void acumulaToken( String tokenIdentificado ) {
        tokensIdentificados.append( tokenIdentificado );
        tokensIdentificados.append( "\n" );
    }

    public static void acumulaRegraSintaticaReconhecida( String regra ) {
        regrasReconhecidas.append( regra );
        regrasReconhecidas.append( "\n" );
    }

    public static void exibeSaida() {

        JTextArea texto = new JTextArea();
        texto.append( tokensIdentificados.toString() );
        JOptionPane.showMessageDialog(null, texto, "Analise Lexica", JOptionPane.INFORMATION_MESSAGE );

        texto.setText( regrasReconhecidas.toString() );
        texto.append( "\n\nStatus da Compilacao:\n\n" );
        texto.append( mensagemDeErro );

        JOptionPane.showMessageDialog(null, texto, "Resumo da Compilacao", JOptionPane.INFORMATION_MESSAGE );
    }

    static void registraErroSintatico( String msg ) throws ErroSintaticoException {
        if ( estadoCompilacao == E_SEM_ERROS ) {
            estadoCompilacao = E_ERRO_SINTATICO;
            mensagemDeErro = msg;
        }
        throw new ErroSintaticoException( msg );
    }

    static void registraErroSemantico( String msg ) {

        if ( estadoCompilacao == E_SEM_ERROS ) {
            estadoCompilacao = E_ERRO_SEMANTICO;
            mensagemDeErro = msg;
        }
    }

    static void regraSemantica( int numeroRegra ) throws ErroSemanticoException {
        System.out.println( "Regra Semantica " + numeroRegra );
        switch ( numeroRegra ) {
            case 0:    // inicio
                codigoGauchesco.append("meCaiu {\n");
                nivelIdentacao++;
                break;
            case 1: // fim
                codigoGauchesco.append(tabulacao(nivelIdentacao));
                codigoGauchesco.append("\n} os butia do bolso ()\n\n");
                break;
            case 2: // var
                insereNaTabelaSimbolos(ultimoLexema);
                break;
            case 3: // atribuir
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                codigoGauchesco.append(tabulacao(nivelIdentacao));
                codigoGauchesco.append(nodo_1.getCodigoMinusculo() + " aprochega " + nodo_2.getCodigoMinusculo() + ";\n");
                break;
            case  4: // variavel
                if ( VeSeExisteNaTabelaSimbolos( ultimoLexema ) ) {
                    pilhaSemantica.push( ultimoLexema, 4 );
                }
                break;
            case  5: // MAIS
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( nodo_1.getCodigoMinusculo() + " + " + nodo_2.getCodigoMinusculo(), 5 );
                break;
            case  6:  // MENOS
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( nodo_1.getCodigoMinusculo() + " - " + nodo_2.getCodigoMinusculo(), 6 );
                break;
            case  7:    // VEZES
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( nodo_1.getCodigoMinusculo() + " * " + nodo_2.getCodigoMinusculo(), 7 );
                break;
            case  8:    // DIVIDO
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( nodo_1.getCodigoMinusculo() + " / " + nodo_2.getCodigoMinusculo(), 8 );
                break;
            case  9:    // RESTO
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( nodo_1.getCodigoMinusculo() + " % " + nodo_2.getCodigoMinusculo(), 9 );
                break;
            case 10:    // ELEVADO
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( nodo_1.getCodigoMinusculo() + " ** " + nodo_2.getCodigoMinusculo(), 10 );
                break;
            case 11:	// UNIQUE_IDENTIFIER
                if ( VeSeExisteNaTabelaSimbolos( ultimoLexema ) ) {
                    pilhaSemantica.push( ultimoLexema, 11 );
                }
                break;
            case 12:	// NUMERO
                pilhaSemantica.push( ultimoLexema, 12 );
                break;
            case 13:	// ABRE E FECHA PAR
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( "(" + nodo_1.getCodigoMinusculo() + ")" , 13 );
                break;
            case 14:    // FECHA PAR OU COL E CHAVE
                nivelIdentacao--;
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "}" );
                break;
            case 15:	// ABRE E FECHA COLCHETE
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( "[" + nodo_1.getCodigoMinusculo() + "]" , 15 );
                break;
            case 16:    // FECHA PAR E CHAVE + QUEBRA LINHA
                nivelIdentacao--;
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "}\n" );
                break;
            case 17:    // if
                nodo_1 = pilhaSemantica.pop();
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "quebra(" + nodo_1.getCodigoMinusculo() + ") {\n" );
                nivelIdentacao++;
                break;
            case 18:    // else
                codigoGauchesco.append( " molas {\n" );
                nivelIdentacao++;
                break;
            case 19:    // MAIOR
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( "["+ nodo_1.getCodigoMinusculo() + "] em cima do laço [" + nodo_2.getCodigoMinusculo() + "]", 19 );
                break;
            case 20:    // MENOR
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push("[" + nodo_1.getCodigoMinusculo() + "] embaixo do laço [" + nodo_2.getCodigoMinusculo() + "]", 20 );
                break;
            case 21:    // MAIOR_IGUAL
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( "[" +nodo_1.getCodigoMinusculo() + "] em cima do laço ou igual [" + nodo_2.getCodigoMinusculo() + "]", 21 );
                break;
            case 22:    // MENOR_IGUAL
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( "[" + nodo_1.getCodigoMinusculo() + "] embaixo do laço ou igual [" + nodo_2.getCodigoMinusculo() + "]", 22 );
                break;
            case 23:    // IGUAL
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( "[" + nodo_1.getCodigoMinusculo() + "] bucha [" + nodo_2.getCodigoMinusculo() + "]", 23 );
                break;
            case 24:    // DIFERENTE
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                pilhaSemantica.push( "[" + nodo_1.getCodigoMinusculo() + "] bem capaz [" + nodo_2.getCodigoMinusculo() + "]", 24 );
                break;
            case 25:    // TRAMPA
                nodo_1 = pilhaSemantica.pop();
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "trampa ate(" + nodo_1.getCodigoMinusculo() + ") {\n" );
                nivelIdentacao++;
                break;
            case 26:    // escrever
                nodo_1 = pilhaSemantica.pop();
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "bah['" + nodo_1.getCodigoMinusculo() + "'];\n" );
                break;
            case 27:    // ler
                nodo_1 = pilhaSemantica.pop();
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "tche(" + nodo_1.getCodigoMinusculo() + ");\n" );
                break;
            case 28:
                nodo_2 = pilhaSemantica.pop();
                nodo_1 = pilhaSemantica.pop();
                nodo_0 = pilhaSemantica.pop();
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "peleia(" + nodo_0.getCodigoMinusculo() + " aprochega " + nodo_1.getCodigoMinusculo() + " ate levar " + nodo_2.getCodigoMinusculo() + " tunda) {\n" );
                nivelIdentacao++;
                break;
            case 30:    // escolha
                nodo_1 = pilhaSemantica.pop();
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "trovar[" + nodo_1.getCodigoMinusculo() + "] {\n");
                nivelIdentacao++;
                nivelIdentacao++;
                break;
            case 31:    // case
                nodo_1 = pilhaSemantica.pop();
                nivelIdentacao--;
                codigoGauchesco.append( tabulacao( nivelIdentacao ) );
                codigoGauchesco.append( "bagual " + nodo_1.getCodigoMinusculo() + ":\n" );
                nivelIdentacao++;
                break;
            case 32:
                nivelIdentacao--;
                break;
        }
    }

    private static int buscaTipoNaTabelaSimbolos(String ultimoLexema ) throws ErroSemanticoException {
        return tabelaSimbolos.get( ultimoLexema );
    }

    private static boolean VeSeExisteNaTabelaSimbolos(String ultimoLexema ) throws ErroSemanticoException {
        if ( !tabelaSimbolos.containsKey( ultimoLexema ) ) {
            throw new ErroSemanticoException( "Variavel " + ultimoLexema + " nao esta declarada! linha: " + linhaAtual );
        } else {
            return true;
        }
    }

    private static void insereNaTabelaSimbolos(String ultimoLexema) throws ErroSemanticoException {
        if ( tabelaSimbolos.containsKey( ultimoLexema ) ) {
            throw new ErroSemanticoException( "Variavel " + ultimoLexema + " ja declarada! linha: " + linhaAtual );
        } else {
            tabelaSimbolos.put( ultimoLexema, 0 );
        }
    }

    static String tabulacao( int qtd ) {
        StringBuffer sb = new StringBuffer();
        for ( int i=0; i<qtd; i++ ) {
            sb.append( "    " );
        }
        return sb.toString();
    }
}


/**
 * Classe Interna para cria��o de filtro de sele��o
 */
/**
 * Classe Interna para criacao de filtro de selecao
 */
class FiltroSab extends FileFilter {

    public boolean accept(File arg0) {
        if (arg0 != null) {
            if (arg0.isDirectory()) {
                return true;
            }
            if (getExtensao(arg0) != null) {
                if (getExtensao(arg0).equalsIgnoreCase("grm")) {
                    return true;
                }
            }
            ;
        }
        return false;
    }

    /**
     * Retorna quais extensoes poderao ser escolhidas
     */
    public String getDescription() {
        return "*.grm";
    }

    /**
     * Retorna a parte com a extensao de um arquivo
     */
    public String getExtensao(File arq) {
        if (arq != null) {
            String filename = arq.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
            ;
        }
        return null;
    }
}
