public class Program {
	public static void main(String[] args) throws Exception
    {
        //  if(args.length == 0)
        //  args = new String[]
        //  {
        //      "D:\\svn\\amazonaws-PSU\\CMPSC 470-2018bFall\\public_html\\2018bFall.cmpsc470\\working\\proj3-minc-byaccj\\sample\\"
        //      +"test_04_fail4.minc",
        //  };

        java.io.Reader r;
        if(args.length <= 0)
        {
            r = new java.io.StringReader
                    ("int main()\n"
                    +"{\n"
                    +"    b = true and (a == func());\n" // assume that local variable a:int and b:bool, and function "int func()" are already in symbol table
                    +"    return 0;\n"
                    +"}\n"
                    );
            //return;
        }
        else
        {
            String foopath = args[0];
            r = new java.io.FileReader(foopath);
        }

        Parser yyparser = new Parser(r);

        try {
            //Parser._debug = true;
            if (yyparser.yyparse() == 0)
            {
                System.out.println("Success: no error found.");
            }
            else
            {
                System.out.println("Error: there exists error(s).");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Error: there exists error(s).");
        }
	}
}
