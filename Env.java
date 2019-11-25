import java.util.ArrayList;
import java.util.HashMap;

public class Env
{
	// Class Variables    
	public Env prev_env;
    public HashMap<String, Object> map = new HashMap<>();
    
   	// Constructor 
   	public Env(Env prev)		       { prev_env = prev; }

	// Setter
    public void Put(String s, Object sym)  { map.put(s,sym); }

	// Getter
   	public Object Get(String s)
    	{
		// Explore the table by traversing to next environment
		Env prev = prev_env;

		// If current map contains our key, return it
		if(map.containsKey(s))
			return map.get(s);	
		
		// Otherwise, traverse to previous environment 
		else
			while(prev != null) {
				if (prev.map.containsKey(s))    return prev.map.get(s);
				else 		    		        prev = prev.prev_env;			
			}
		
		// If only one environment and string is not in it, return null
		return null;
    	}
    	
    // Getter for scope : Searches for a string within the current env
   	public Object Get_Scope(String s){
   	    if(map.containsKey(s))
			return map.get(s);	
		else
		    return null;
   	}
}
