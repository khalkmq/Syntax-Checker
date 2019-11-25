import java.util.*;
import java.util.HashMap;

public class ParserImpl
{
    Env env = new Env(null);            // For Variable type returns
    Env func_env = new Env(null);       // For Function Parameter Storage
    Env recr_env = new Env(null);       // For Record Parameter Storage

    int line_Count = 0;
    public static Boolean _debug = false;
    void Debug(String message)
    {
        if(_debug)
            System.out.println(line_Count + ":  " + message);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////     Printing Functions     ///////////////////////////////////////////////
    
    public void setLine(int passIn)     { line_Count = passIn; }                                                                ////DONE////
    public String eline()               { return ("Error at line " + line_Count + " : " ); }                                    ////DONE////
    public String fline()               { return ("Error at line " + func_line + " : " ); }                                     ////DONE////
    public String sline()               { return ("Error at line " + stmt_line + " : " ); }                                     ////DONE////
    public String notfound(String x)    { return ("undefine variable " + x + " used."); }                                       ////DONE////
    public void printEnv(Env currentEnv){
        System.out.println("==============================");
        for (String key: currentEnv.map.keySet()){      
                Object value = currentEnv.Get(key);  
                System.out.println(key + " " + value);
        }
        if(currentEnv.prev_env != null) printEnv(currentEnv.prev_env);
        else return;
        System.out.println("==============================");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////          program           ///////////////////////////////////////////////

    public Object program____decllist () throws Exception {                                                                     ////DONE////
		Object p1 = "main";	
		Object id = env.Get((String)p1);
        if(id == null)              throw new Exception ("Error in program: The program must have one main function.");
        String id_type = (String)id;
        if(!id_type.equals("int")) 	throw new Exception ("Error in program: The return type of main function must be int.");
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////         decl_list          ///////////////////////////////////////////////

    public Object decllist_decllist_decl () throws Exception {
        return null;
    }

    public Object decllist_eps () throws Exception {
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////            decl            ///////////////////////////////////////////////

    public Object decl____vardecl () throws Exception {
        return null;
    }

    public Object decl____fundecl () throws Exception {
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////          var_decl          ///////////////////////////////////////////////

    public Object vardecl____typespec_IDENT_SEMI (Object p1, Object p2) throws Exception {
        
        Object id = env.Get_Scope((String)p2);
        if(id != null)   throw new Exception(eline() + "the variable " + (String)p2 + " is already defined in this scope.");
        
	    env.Put((String)p2, p1);
	    if(record_param) record_params.add((String)p2);
        if(rdy_to_store) {
            ArrayList<String> copy_record = new ArrayList<String>(record_param_types);
            record_param_types.clear(); 
            recr_env.Put((String)p2, copy_record);
            rdy_to_store = false;
        }
	    
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////         type_spec          ///////////////////////////////////////////////

    public Object typespec____BOOL () throws Exception      {return "bool";}                                                    ////DONE////
    public Object typespec____INT () throws Exception       {return "int";}                                                     ////DONE////
    public Object typespec____FLOAT () throws Exception     {return "float";}                                                   ////DONE////
    public Object typespec____typespec_LBRACKET_RBRACKET (Object p1) throws Exception { return ((String)p1+" []"); }            ////DONE////
    
    ArrayList<String> record_params = new ArrayList<String>();
    ArrayList<String> record_param_types = new ArrayList<String>();
    public static Boolean record_param = false;
    public static Boolean rdy_to_store = false;
    
    public Object typespec____RECORD_LBRACE     () throws Exception { 
        env = new Env(env);             // Create new local env
        record_param = true;
        
        return null;
    }
    
    public Object typespec____localdecls_RBRACE () throws Exception { 
        
        String params_declared = "record {";
        Set<String> hs = new HashSet<>();
        hs.addAll(record_params);
        record_params.clear();
        record_params.addAll(hs);
        Collections.sort(record_params);
        for(int i = 0; i < record_params.size(); i++){
            Object id = env.Get_Scope(record_params.get(i));
            record_param_types.add((String)id);
            record_param_types.add(record_params.get(i));
            if (i != 0)     params_declared = params_declared + ", ";
            params_declared = params_declared + (String)id + " " + record_params.get(i);
        }
        params_declared = params_declared + "}";
        
        record_params.clear(); 
        record_param = false;
        rdy_to_store = true;
        env = env.prev_env;              // Pop local env     
        
        return params_declared; 
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////          fun_decl          ///////////////////////////////////////////////

    Object current_func;
    String return_exists = "";
    int func_line;
    
    public Object fundecl____typespec_IDENT     	            (Object p1, Object p2) throws Exception{
        Object id = env.Get_Scope((String)p2);
        if(id != null)   throw new Exception(eline() + "the function " + (String)p2 + "() is already defined in this scope.");
                        
	    env.Put((String)p2, p1);        // Add to global Variables
	    if(record_param) record_params.add((String)p2);    
        if(rdy_to_store) {
            ArrayList<String> copy_record = new ArrayList<String>(record_param_types);
            record_param_types.clear(); 
            recr_env.Put((String)p2, copy_record);
            rdy_to_store = false;
        }
	    
	    current_func = p2;              // Keep track of which function were in
	    func_line = line_Count;         // Save line we're at in case of a error
	    env = new Env(env);             // Create new local env
	    recr_env = new Env(recr_env);        // Create new local env for records
	    params.clear();                 // Clean up any params from previous functions
        return null;
    }
    
    public Object fundecl____LPAREN_params_RPAREN	            (Object p2            ) throws Exception{
        ArrayList<String> these_params = new ArrayList<String>(params); 
        func_env.Put((String)current_func, these_params);
        params.clear();                
        return null;
    }
    
    public Object fundecl____LBRACE 		                    (		     ) throws Exception{  
        return_exists = "none";
        return null;
    }
    
    public Object fundecl____localdecls_stmtlist_RBRACE 		(		     ) throws Exception{  
        String id = (String)current_func;
        String id_type = (String)env.Get(id);
        if(return_exists.equals("none")) 
	        throw new Exception("Error at function " + (String)current_func + "() defined in line " + func_line + " : at least one return statement must be used to return \"" + id_type + "\" value.");
	    current_func = null;
	    return_exists = "none";
	    env = env.prev_env;              // Pop local env
	    recr_env = recr_env.prev_env;    // Pop local record env
        return null;
    }
    

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////           params           ///////////////////////////////////////////////

    ArrayList<String> params = new ArrayList<String>();       
        
    public Object params____paramlist 	() throws Exception {
        return params;
    }

    public Object params____eps 		() throws Exception {
        return params;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////         param_list         ///////////////////////////////////////////////

    public Object paramlist____paramlist_COMMA_param 	() throws Exception {
        return null;
    }

    public Object paramlist____param 					() throws Exception {
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////           param            ///////////////////////////////////////////////

    public Object param____typespec_IDENT 		(Object p1, Object p2) throws Exception {
        Object id = env.Get_Scope((String)p2);
        if(id != null)   throw new Exception(eline() + "the variable " + (String)p2 + " is already defined in this scope.");
    
	    env.Put((String)p2, p1);                // Add parameter to local variable table
	    if(record_param) record_params.add((String)p2);            
        if(rdy_to_store) {
            ArrayList<String> copy_record = new ArrayList<String>(record_param_types);
            record_param_types.clear(); 
            recr_env.Put((String)p2, copy_record);
            rdy_to_store = false;
        }
	    
	    params.add((String)p1);
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////         stmt_list          ///////////////////////////////////////////////

    public Object stmtlist____stmtlist_stmt 	() throws Exception {        
        return null;
    }
    
    public Object stmtlist____eps 				() throws Exception {
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////            stmt            ///////////////////////////////////////////////

    int stmt_line;
    
    public Object stmt____exprstmt 		() throws Exception {
        return null;
    }

    public Object stmt____compoundstmt 	() throws Exception {       
        return null;
    }

    public Object stmt____ifstmt 		() throws Exception {
        return null;
    }

    public Object stmt____whilestmt 	(Object p1) throws Exception {
        return null;
    }

    public Object stmt____returnstmt 	(Object p1) throws Exception {
        return p1;
    }

    public Object stmt____breakstmt 	() throws Exception {
        return null;
    }

    public Object stmt____continuestmt  () throws Exception {
        return null;
    }

    public Object stmt____printstmt 	() throws Exception {
        return null;
    }

    public Object stmt____SEMI 			() throws Exception {
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////         expr_stmt          ///////////////////////////////////////////////

    public Object exprstmt____IDENT_ASSIGN_exprstmt 				(Object p1, Object p3) throws Exception {	                ////DONE////
        Object id = env.Get((String)p1);
        if(id == null) throw new Exception(eline() + notfound((String)p1));
        String id_type       = (String)id;
        String exprstmt_type = (String)p3;
        if(id_type.equals(exprstmt_type)) return exprstmt_type;	
        
       // CATCH IF RECORD IS BEING TESTED
       String delims = "[ {},]+";
       String[] splited = ((String)p3).split(delims);
       ArrayList<String> rec1 = (ArrayList<String>)recr_env.Get((String)p1);
       
       if (rec1 == null) throw new Exception(eline() + "\"" + exprstmt_type + "\" value is tried to assign to \"" + id_type + "\" variable " + (String)p1 + ".");
       
       if ((splited.length - 1) == rec1.size()) {
       
            for(int i = 0; i < rec1.size(); i = i + 2)
                    if(!(((String)splited[i+1]).equals((String)rec1.get(i))))     
                        throw new Exception(eline() + "\"" + exprstmt_type + "\" value is tried to assign to \"" + id_type + "\" variable " + (String)p1 + ".");
            
           return exprstmt_type;
        }
       
	    throw new Exception(eline() + "\"" + exprstmt_type + "\" value is tried to assign to \"" + id_type + "\" variable " + (String)p1 + ".");
    }

    public Object exprstmt____expr_SEMI 					(Object p1) throws Exception {
        String expr_type = (String)p1;
        return expr_type;
    }

    public Object exprstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_exprstmt 	(Object p1, Object p3, Object p6) throws Exception {
        String id1 = (String)env.Get((String)p1);
        if(!id1.equals("int []") && !id1.equals("float []") && !id1.equals("bool []"))
            throw new Exception(eline() + (String)p1 + " must be an array type to use operator []");
    
        // if index is not int, throw error
        if(!p3.equals("int"))   throw new Exception(eline() + "index of " + (String)p1 + "[] must be an int value.");
        
        String id2 = "";
        if(id1.equals("int []"))   id2 = "int";
        if(id1.equals("bool []"))  id2 = "bool";
        if(id1.equals("float []")) id2 = "float";
        if(!p6.equals(id2))
            throw new Exception(eline() + "\"" + p6 + "\" value is tried to assign to \"" + id2 + "\" variable " + (String)p1 + "[].");
        
        return (String)id2;
    }

    public Object exprstmt____IDENT_DOT_IDENT_ASSIGN_exprstmt 			(Object p1, Object p3, Object p5) throws Exception {
    
        
        ArrayList<String> rec0 = (ArrayList<String>)recr_env.Get((String)p1);
        ArrayList<String> rec1 = (ArrayList<String>)recr_env.Get((String)p3);
        if(rec0 == null)                 throw new Exception(eline() + (String)p1 + " must be a record type to use " + (String)p3 +" field.");
        if(!(rec0.contains((String)p3))) throw new Exception(eline() + "the record value " + (String)p1 + " does not have " + (String)p3 + " field.");     
        
        
        // CATCH IF RECORD IS BEING TESTED
        String delims = "[ {},]+";
        String[] splited = ((String)p5).split(delims);
        
        ////////////////////////////////////      This code is an attempt at solving Ext2, but didnt work       ////////////////////////////////////   
        if(rec1 != null){
            String params_declared = "record {";
            for(int i = 0; i < rec1.size(); i = i + 2){
                if (i != 0)     params_declared = params_declared + ", ";
                    params_declared = params_declared + rec1.get(i) + " " + rec1.get(i+1);
            }
            params_declared = params_declared + "}";

            if ((splited.length - 1) == rec1.size()) {
                for(int i = 0; i < rec1.size(); i = i + 2)
                    if(!(((String)splited[i+1]).equals((String)rec1.get(i))))     
                        throw new Exception(eline() + "\"" + (String)p5 + "\" value is tried to assign to \"" + params_declared + "\" variable " + (String)p1 + "." + (String)p3 + ".");
                return (String)p5;
            }
            throw new Exception(eline() + "\"" + (String)p5 + "\" value is tried to assign to \"" + params_declared + "\" variable " + (String)p1 + "." + (String)p3 + ".");
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        return (String)p5;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////        while_stmt          ///////////////////////////////////////////////

    public Object whilestmt____WHILE_LPAREN_expr_RPAREN_stmt (Object p3) throws Exception {
        String id_type = (String)p3;
        if(id_type.equals("bool")) return p3;
        throw new Exception(sline() + "while does not use \"" + id_type + "\" value to check condition. Use bool value in while loop.");
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////       compound_stmt        ///////////////////////////////////////////////

    public Object compoundstmt____LBRACE () throws Exception {
        env = new Env(env);              // Create new local env
        recr_env = new Env(recr_env);
        stmt_line = line_Count;  
        return null;
    }

    public Object compoundstmt____localdecls_stmtlist_RBRACE () throws Exception {
        env = env.prev_env;              // Pop local env 
        recr_env = recr_env.prev_env;    // Pop local record env
        return null;
    }
    

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////        local_decls         ///////////////////////////////////////////////

    public Object localdecls____localdecls_localdecl 	() throws Exception {
        return null;
    }

    public Object localdecls____eps 			() throws Exception {
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////         local_decl         ///////////////////////////////////////////////

    public Object localdecl____typespec_IDENT_SEMI (Object p1, Object p2) throws Exception {
        Object id = env.Get_Scope((String)p2);
        if(id != null)   throw new Exception(eline() + "the variable " + (String)p2 + " is already defined in this scope.");
        
	    env.Put((String)p2, p1);
	    if(record_param) record_params.add((String)p2);
	    if(rdy_to_store) {
            ArrayList<String> copy_record = new ArrayList<String>(record_param_types);
            record_param_types.clear(); 
            recr_env.Put((String)p2, copy_record);
            rdy_to_store = false;
        }
	    
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////          if_stmt           ///////////////////////////////////////////////

    public Object ifstmt____IF_LPAREN_expr_RPAREN_stmt      (Object p3) throws Exception { 
        String id_type       = (String)p3;
        if(id_type.equals("bool")) return id_type;
        throw new Exception(sline() + "if does not use \"" + id_type + "\" value to check condition. Use bool value in if statement.");
    }
    
    public Object ifstmt____ELSE_stmt 	                    () throws Exception { 
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////        return_stmt         ///////////////////////////////////////////////

    public Object returnstmt____RETURN_expr_SEMI  (Object p2       ) throws Exception {
        
        Object id = env.Get((String)current_func);
        String cf_type = (String)id;
	    String p2_type = (String)p2;
	    
	    if(!p2_type.equals(cf_type)) 
	       throw new Exception(eline() + "return type of " + (String)current_func + "() is \"" + cf_type + "\", not \"" + p2_type + "\".");
	    
	    return_exists = (String)p2;
	       
        return p2;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////         break_stmt         ///////////////////////////////////////////////

    public Object breakstmt____BREAK_SEMI         (	               ) throws Exception { return null; }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////       continue_stmt        ///////////////////////////////////////////////

    public Object continuestmt____CONTINUE_SEMI   (                    ) throws Exception { return null; }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////         print_stmt         ///////////////////////////////////////////////

    public Object printstmt____PRINT_expr_SEMI    (                    ) throws Exception { return null; }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////          arg_list          ///////////////////////////////////////////////
    
    ArrayList<String> args = new ArrayList<String>(); 
    
    public Object arglist____arglist_COMMA_expr   (Object p1, Object p3) throws Exception { 
        args.add((String)p3);
        return null; 
    }
    public Object arglist____expr                 (Object p1           ) throws Exception { 
        args.add((String)p1);
        return null; 
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////            args            ///////////////////////////////////////////////

    public Object args____arglist                 (Object p1           ) throws Exception { 
        ArrayList<String> these_args = new ArrayList<String>(args);
        args.clear(); 
        return these_args; 
    }
    public Object args____eps                     (                    ) throws Exception { 
        return args;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////            expr            ///////////////////////////////////////////////

    public Object expr____expr_OR_expr              		(Object p1, Object p3) throws Exception { 
        if(p1 instanceof String == false) throw new Exception("error1");
        if(p3 instanceof String == false) throw new Exception("error2");
        if(((String)p1).equals("bool" ) && ((String)p3).equals("bool" )) return "bool";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" or \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_AND_expr             		(Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error3");
        if(p3 instanceof String == false) throw new Exception("error4");
        if(((String)p1).equals("bool" ) && ((String)p3).equals("bool" )) return "bool";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" and \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____NOT_expr                  		(Object p2           ) throws Exception { 
        if(p2 instanceof String == false) throw new Exception("error5");
        if(((String)p2).equals("bool" )) return "bool";
        throw new Exception(eline() + "unary operation of not \"" + (String)p2 + "\" is not allowed."); 
    }
    public Object expr____expr_EQ_expr              		(Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error6");
        if(p3 instanceof String == false) throw new Exception("error7");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "bool";
        if(((String)p1).equals("bool" ) && ((String)p3).equals("bool" )) return "bool";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "bool";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" == \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_NE_expr             		    (Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error8");
        if(p3 instanceof String == false) throw new Exception("error9");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "bool";
        if(((String)p1).equals("bool" ) && ((String)p3).equals("bool" )) return "bool";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "bool";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" != \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_LE_expr             		    (Object p1, Object p3) throws Exception { 
        if(p1 instanceof String == false) throw new Exception("error9");
        if(p3 instanceof String == false) throw new Exception("error10");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "bool";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "bool";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" <= \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_LT_expr             		    (Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error11");
        if(p3 instanceof String == false) throw new Exception("error12");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "bool";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "bool";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" < \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_GE_expr              		    (Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error13");
        if(p3 instanceof String == false) throw new Exception("error14");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "bool";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "bool";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" >= \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_GT_expr              		    (Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error15");
        if(p3 instanceof String == false) throw new Exception("error16");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "bool";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "bool";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" > \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_PLUS_expr            		    (Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error17");
        if(p3 instanceof String == false) throw new Exception("error18");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "int";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "float";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" + \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_MINUS_expr            		    (Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error19");
        if(p3 instanceof String == false) throw new Exception("error20");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "int";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "float";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" - \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_MUL_expr              		    (Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error21");
        if(p3 instanceof String == false) throw new Exception("error22");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "int";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "float";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" * \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____expr_DIV_expr              		    (Object p1, Object p3) throws Exception {
        if(p1 instanceof String == false) throw new Exception("error23");
        if(p3 instanceof String == false) throw new Exception("error24");
        if(((String)p1).equals("int"  ) && ((String)p3).equals("int"  )) return "int";
        if(((String)p1).equals("float") && ((String)p3).equals("float")) return "float";
        throw new Exception(eline() + "operation of \"" + (String)p1 + "\" / \"" + (String)p3 + "\" is not allowed."); 
    }
    public Object expr____LPAREN_expr_RPAREN        		    (Object p2           ) throws Exception {
        return p2;
    }
    public Object expr____IDENT                     		    (Object p1           ) throws Exception {
    
        Object id = env.Get((String)p1);
        // If variable declared as the same name as a function, let is pass
        if(((String)current_func).equals((String)p1))   return (String)id;
        
        Object id3 = func_env.Get((String)p1);
        
        if(id3 != null) throw new Exception(eline() + "a function " + (String)p1 + "() cannot be used as a variable."); 
        
        if(id == null) throw new Exception(eline() + notfound((String)p1));
        return (String)id;
    }
    public Object expr____BOOLLIT                   		    (Object p1           ) throws Exception { return "bool";}
    public Object expr____INTLIT                    		    (Object p1           ) throws Exception { return "int";}
    public Object expr____FLOATLIT                  		    (Object p1           ) throws Exception { return "float";}
    // HANDLING FUNCTION CALLS
    public Object expr____IDENT_LPAREN_args_RPAREN  		    (Object p1, Object p3) throws Exception {
        
        Object id = func_env.Get((String)p1);
        
        // Detect if a function was not defined
        if(id == null) throw new Exception(eline() + "a function "+ (String)p1 + "() is not defined.");
        
        // Make a List for variables being passed in
        ArrayList<String> passed_params = (ArrayList<String>)func_env.Get((String)p1);  
        ArrayList<String> passed_args = (ArrayList<String>)p3;
        
        // Check Number of arguments
        if(passed_params.size() != passed_args.size()) throw new Exception(eline() + "only " + passed_params.size() + " arguments must be passed to " + (String)p1 + "().");
        
        // Check Param Types
        for(int i = 0; i < passed_params.size(); i++){
            if(!passed_params.get(i).equals(passed_args.get(i))) 
                throw new Exception(eline() + (i+1) +"th parameter of function " + (String)p1 + "() must be " + passed_params.get(i) + " type.");
        }
        
        // Catch if local variable is being used as function
        Object id2 = env.Get((String)p1);
        Object id3 = env.prev_env.Get((String)p1);
        if(id2 != id3 && !(id2 == null || id3 == null)) {
            throw new Exception(eline() + (String)p1 + " is a \"" + (String)id2 + "\" variable, not a function.");}
        
        return (String)id2;
    }
    public Object expr____IDENT_LBRACKET_expr_RBRACKET 		    (Object p1, Object p3) throws Exception { 
    
        String id1 = (String)env.Get((String)p1);
        if(!id1.equals("int []") && !id1.equals("float []") && !id1.equals("bool []"))
            throw new Exception(eline() + (String)p1 + " must be an array type to use operator []");
    
        if(!p3.equals("int"))    throw new Exception(eline() + "index of " + (String)p1 + "[] must be an int value.");
        
        if(id1.equals("int []"))   return "int";
        if(id1.equals("bool []"))  return "bool";
        if(id1.equals("float []")) return "float";
       
        return null; 
    }
    public Object expr____IDENT_DOT_SIZE			            (Object p1           ) throws Exception { 
        String id1 = (String)env.Get((String)p1);
        if(!id1.equals("int []") && !id1.equals("float []") && !id1.equals("bool []"))
            throw new Exception(eline() + (String)p1 + " must be an array type to use \".size\"."); 
        
        return "int"; 
    }
    public Object expr____NEW_typespec_LBRACKET_expr_RBRACKET  	(Object p2, Object p4) throws Exception { 
        if(!p4.equals("int"))   throw new Exception(eline() + "size of an array must be an int value, to create an array using new operator.");
        return ((String)p2+" []"); 
    }
    public Object expr____IDENT_DOT_IDENT               	    (Object p1, Object p3) throws Exception { 
        ArrayList<String> rec1 = (ArrayList<String>)recr_env.Get((String)p1);
        
        if(rec1 == null)                throw new Exception(eline() + (String)p1 + " must be a record type to use " + (String)p3 +" field.");
        if(!(rec1.contains((String)p3))) throw new Exception(eline() + "the record value " + (String)p1 + " does not have " + (String)p3 + " field.");
        
        int get_int = rec1.indexOf((String)p3);
        
        return rec1.get(get_int - 1); 
    }

}
