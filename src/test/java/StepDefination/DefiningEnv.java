package StepDefination;

import io.cucumber.java.en.Given;

public class DefiningEnv {
	public static String client;
	
	@Given("client is {string}")
	public static void defineEnvVariable(String client_header)
	{
		 			
		
		 if (client_header.equals("crm"))
		 
		{
			 client_header = "crm";
			
		}else if (client_header.equals("hub"))
		{
			client_header = "hub";

		}else if (client_header.equals(""))

		{
			client_header = "";

		}

		else 
			client_header = "dcf";
		client = client_header;
		System.out.println(client);
		
	}
	
}