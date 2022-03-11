import java.io.InputStream;
import java.io.IOException;

class Calculator {
    private final InputStream in;

    private int lookahead;

    public Calculator(InputStream in) throws IOException {
        this.in = in;
        lookahead = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookahead == symbol)
            lookahead = in.read();
        else
            throw new ParseError();
    }

    private boolean isDigit(int c) {
        return '0' <= c && c <= '9';
    }

    private int evalDigit(int c) {
        return c - '0';
    }

    public int eval() throws IOException, ParseError {

        int value = EXP();

        if (lookahead != -1 && lookahead != '\n')
            throw new ParseError();

        if(lookahead==-1)
            System.out.printf("\n");
        return value;
    }

    private int EXP() throws IOException, ParseError {
        if (isDigit(lookahead)||lookahead=='(') {
            int cond=lookahead;
            consume(lookahead);
            return  TERM(cond)+EXP1();
        }
        throw new ParseError();
    }

    private int TERM(int condition) throws IOException, ParseError {
        if (isDigit(condition)||condition=='(') {
            return TERM1(FACTOR(condition)); 
        }
        else if(lookahead=='\n'||lookahead==-1)return -1;
        throw new ParseError(); 
    }
    private int EXP1() throws IOException, ParseError
    {
        if(lookahead=='+') {
            consume(lookahead);
            if((!(isDigit(lookahead))&&lookahead!='('))throw new ParseError();
            int cond=lookahead;
            consume(lookahead);
            return TERM(cond)+EXP1();
        }
        else if(lookahead=='-')
        {
            consume('-');
            if((!(isDigit(lookahead))&&lookahead!='('))throw new ParseError();
            int cond=lookahead;
            consume(lookahead);
            return  -(TERM(cond)-EXP1());
        }
        else if(lookahead==')'||lookahead=='\n'||lookahead==-1)return 0;
        throw new ParseError(); 
    }
    private int TERM1(int condition) throws IOException, ParseError
    {
        if(lookahead=='*') {
            consume('*');
            consume('*');
            if((!(isDigit(lookahead))&&lookahead!='('))throw new ParseError();
            int cond=lookahead;
            consume(lookahead);
            return pow(condition,TERM1(FACTOR(cond)));
        }
        else if(lookahead=='+'||lookahead=='-'||lookahead==')'||lookahead=='\n'||lookahead==-1)
        {
            return  condition;
        }

        throw new ParseError(); 
    }
    private int FACTOR(int condition) throws IOException, ParseError
    {
        if (isDigit(condition)) {
            String temp=NUM(condition);
            if(temp.charAt(0)=='0')throw new ParseError();
            int k=Integer.parseInt(temp);
            return k; 
        }
        else if(condition=='(')
        {
            int temp=EXP();
            consume(')');
            return  temp;
        }
        throw new ParseError(); 
    }
    private String NUM(int condition) throws IOException, ParseError
    {
        if (isDigit(condition)) {
            String d=Integer.toString(DIGIT(condition));
            String tmp=TEMP(lookahead);
            if(tmp!="-1")d=d+tmp;
            return d;
        }else if(lookahead=='\n'||lookahead==-1) return " ";

        throw new ParseError(); 
    }
    private String TEMP(int condition) throws IOException, ParseError
    {
        if (isDigit(condition)) {
            consume(lookahead);
            return NUM(condition); 
        }
        else if(condition==')'||condition=='-'||condition=='+'||condition==-1||condition=='*'||condition=='\n')return "-1";
        
        throw new ParseError(); 
    }
    private int DIGIT(int condition) throws IOException, ParseError
    {
        if (isDigit(condition)) {
            return evalDigit(condition); 
        }

        throw new ParseError(); 
    }
    private static int pow(int base, int exponent) {
        if (exponent < 0)
            return 0;
        if (exponent == 0)
            return 1;
        if (exponent == 1)
            return base;    
    
        if (exponent % 2 == 0) //even exp -> b ^ exp = (b^2)^(exp/2)
            return pow(base * base, exponent/2);
        else                   //odd exp -> b ^ exp = b * (b^2)^(exp/2)
            return base * pow(base * base, exponent/2);
    }
}
