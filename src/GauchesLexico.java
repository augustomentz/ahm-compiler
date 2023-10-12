import java.io.*;

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
 <LISTA_FUNCOES> ::= <FUNCAO> ; <LISTA_FUNCOES>
 <LISTA_FUNCOES> ::= <FUNCAO>
 <FUNCAO> ::= <FUNCAO_RECEBER>
 <FUNCAO> ::= <FUNCAO_ESCREVER>
 <FUNCAO> ::= <FUNCAO_LER>
 <FUNCAO> ::= <FUNCAO_IF>
 <FUNCAO> ::= <FUNCAO_ATE>
 <FUNCAO> ::= <FUNCAO_ESCOLHA>
 <FUNCAO> ::= <FUNCAO_EXECUTA_FUNCAO>
 <FUNCAO_IF> ::= 'INICIO_QUEBRA' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'FIM_QUEBRA'
 <FUNCAO_IF> ::= 'INICIO_QUEBRA' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'MOLAS' <LISTA_FUNCAO> 'FIM_QUEBRA'
 <FUNCAO_ATE> ::= 'TRAMPA_ATE' '(' <CONDICAO> ')' <LISTA_FUNCAO> 'FIM_TRAMPA_ATE'
 <FUNCAO_RECEBER> ::= <VAR> 'APROCHEGA' <EXP> ';'
 <FUNCAO_ESCREVER> ::= 'BAH' '[' '"' <EXP> '"' ']'
 <FUNCAO_LER> ::= 'TCHE' '(' <VAR> ')'
 <FUNCAO_EXECUTA_FUNCAO> ::= 'BAITA' <ID> '(' <PARAMETROS> ')'
 <FUNCAO_ESCOLHA> ::= 'INICIO_TROVAR' '[' <EXP> ']' <CASOS> 'FIM_TROVAR'
 <CASOS> ::= <CASOS> ';' <CASOS>
 <CASOS> ::= <CASO>
 <CASO> ::= 'BAGUAL' <EXP> ':' <METHOD_LIST>
 <CONDICAO> ::= '[' <EXP> ']' 'EM_CIMA_DO_LACO' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'EM_CIMA_DO_LACO_OU_IGUAL' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'BEM_CAPAZ' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'EM_BAIXO_DO_LACO_OU_IGUAL' '[' <EXP> ']'
 <CONDICAO> ::= '[' <EXP> ']' 'EM_BAIXO_LACO' '[' <EXP> ']'
 <CONDICAO> ::= <'[' <EXP> ']' 'BUCHA' '[' <EXP> ']'
 <PARAMETROS> ::= <E>
 <PARAMETROS> ::= <E> ',' <E>
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
 <ID> ::= [A-Z]+([A-Z]_[0-9]*)
 */

public class GauchesLexico {

    // Lista de tokens
    static final int T_ME_CAIU              = 1;
    static final int T_OS_BUTIA_DO_BOLSO    = 2;
    static final int T_BAGULHETES           = 3;
    static final int T_VIRGULA              = 4;
    static final int T_PONTO_VIRGULA        = 5;
    static final int T_INICIO_QUEBRA        = 6;
    static final int T_MOLAS                = 7;
    static final int T_FIM_QUEBRA           = 8;

    static final int T_APROCHEGA            = 9;

    static final int T_BAH                  = 10;

    static final int T_BAITA                = 11;

    static final int T_INICIO_TROVAR        = 12;

    static final int T_FIM_TROVAR           = 13;

    static final int T_BAGUAL               = 14;

    static final int T_ECL                  = 15;

    static final int T_ECL_OU_IGUAL         = 16;

    static final int T_BEM_CAPAZ            = 17;

    static final int T_EBL_OU_IGUAL         = 18;

    static final int T_EBL                  = 19;

    static final int T_BUCHA                = 20;

    static final int T_TRAMPA_ATE           = 21;
    static final int T_FIM_TRAMPA_ATE       = 22;

    static final int T_TCHE                 = 23;

    static final int T_ABRE_PAR             = 24;
    static final int T_FECHA_PAR            = 25;

    static final int T_ABRE_COL             = 26;

    static final int T_FECHA_COL            = 27;

    static final int T_MAIS                 =  28;
    static final int T_MENOS                =  29;
    static final int T_VEZES                =  30;
    static final int T_DIVIDIDO             =  31;
    static final int T_RESTO                =  32;
    static final int T_ELEVADO              =  33;
    static final int T_NUMERO               =  34;
    static final int T_ID                   =  35;
    static final int T_DOIS_PONTOS           =  36;
    static final int T_ASPAS_DUPLAS         =  37;

    static final int T_FIM_FONTE       =  90;
    static final int T_ERRO_LEX        =  98;
    static final int T_NULO            =  99;

    static final int FIM_ARQUIVO       =  226;

    static File arqFonte;
    static BufferedReader rdFonte;
    static File arqDestino;

    static char   lookAhead;
    static int    token;
    static String lexema;
    static int    ponteiro;
    static String linhaFonte;
    static int    linhaAtual;
    static int    colunaAtual;
    static String mensagemDeErro;
    static StringBuffer tokensIdentificados = new StringBuffer();

    public static void main( String s[] ) throws java.io.IOException
    {
        try {
            abreArquivo();
            abreDestino();
            linhaAtual     = 0;
            colunaAtual    = 0;
            ponteiro       = 0;
            linhaFonte     = "";
            token          = T_NULO;
            mensagemDeErro = "";

            movelookAhead();

            while ( ( token != T_FIM_FONTE ) && ( token != T_ERRO_LEX ) ) {
                buscaProximoToken();
                mostraToken();
            }
            if ( token == T_ERRO_LEX ) {
                JOptionPane.showMessageDialog( null, mensagemDeErro, "Erro L�xico!", JOptionPane.ERROR_MESSAGE );
            } else {
                JOptionPane.showMessageDialog( null, "An�lise L�xica terminada sem erros l�xicos", "An�lise L�xica terminada!", JOptionPane.INFORMATION_MESSAGE );
            }
            exibeTokens();
            gravaSaida( arqDestino );
            fechaFonte();
        } catch( FileNotFoundException fnfe ) {
            JOptionPane.showMessageDialog( null, "Arquivo nao existe!", "FileNotFoundException!", JOptionPane.ERROR_MESSAGE );
        } catch( UnsupportedEncodingException uee ) {
            JOptionPane.showMessageDialog( null, "Erro desconhecido", "UnsupportedEncodingException!", JOptionPane.ERROR_MESSAGE );
        } catch( IOException ioe ) {
            JOptionPane.showMessageDialog( null, "Erro de io: " + ioe.getMessage(), "IOException!", JOptionPane.ERROR_MESSAGE );
        } finally {
            System.out.println( "Execucao terminada!" );
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
            else if ( lexema.equals( "BAITA" ) )
                token = T_BAITA;
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
            case T_BAITA            : tokenLexema.append( "T_BAITA" ); break;
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
            default                : tokenLexema.append( "N/A" ); break;
        }
        System.out.println( tokenLexema.toString() + " ( " + lexema + " )" );
        acumulaToken( tokenLexema.toString() + " ( " + lexema + " )" );
        tokenLexema.append( lexema );
    }

    private static void abreArquivo() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

        FiltroJoz3 filtro = new FiltroJoz3();

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

        FiltroJoz3 filtro = new FiltroJoz3();

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

}

/**
 * Classe Interna para cria��o de filtro de sele��o
 */
class FiltroJoz3 extends FileFilter {

    public boolean accept(File arg0) {
        if(arg0 != null) {
            if(arg0.isDirectory()) {
                return true;
            }
            if( getExtensao(arg0) != null) {
                if ( getExtensao(arg0).equalsIgnoreCase( "grm" ) ) {
                    return true;
                }
            };
        }
        return false;
    }

    /**
     * Retorna quais extens�es poder�o ser escolhidas
     */
    public String getDescription() {
        return "*.grm";
    }

    /**
     * Retorna a parte com a extens�o de um arquivo
     */
    public String getExtensao(File arq) {
        if(arq != null) {
            String filename = arq.getName();
            int i = filename.lastIndexOf('.');
            if(i>0 && i<filename.length()-1) {
                return filename.substring(i+1).toLowerCase();
            };
        }
        return null;
    }
}
