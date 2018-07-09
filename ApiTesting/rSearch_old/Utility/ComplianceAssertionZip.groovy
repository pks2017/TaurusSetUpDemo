import groovy.json.JsonSlurper;

//variable with Empty error message
def failureMessage = "";

def successMessage = "";

// json variable
def jsonResponse = null;

//boolean set_assertion = true;

JsonSlurper JSON = new JsonSlurper ();

//Failure if ResponseCode!=200
if (!prev.getResponseCode().equals("200"))
{
	failureMessage += "Expected <response code> [200] but we got instead [" + prev.getResponseCode() + "]\n\n" ;
}

else if(SampleResult.getResponseHeaders().contains("Content-Disposition"))
{
				//log.info("\n\n"+ SampleResult.getResponseHeaders().toString()+"\n\n")
				successMessage += "The Response data looks as per compliance.";
}

//further validation if ResponseCode=200
else
{
	try 
	{
		jsonResponse = JSON.parseText(prev.getResponseDataAsString());		
	}		
    catch (Exception e)
    {
    	failureMessage += "Invalid JSON.\n";
    	throw(e);
    }
	try 
	{		
		if (!jsonResponse.keySet().equals(null))
		{			
			//validate Response format if Diagnostics exist
			if (jsonResponse.keySet().contains("diagnostics"))
			{
				if (!jsonResponse.diagnostics.equals(null))
				{
					if (!(jsonResponse.diagnostics.containsKey("error") && jsonResponse.diagnostics.containsKey("message") && jsonResponse.diagnostics.containsKey("warnings") && jsonResponse.diagnostics.containsKey("processingTimeMs")))
					{
						failureMessage += "Any or all Diagnostic fields are missing.\n\n";
					}
					else 
					{
						if (!(jsonResponse.keySet().contains("data")) && !(jsonResponse.keySet().contains("success")))
						{
							failureMessage += "The json response structure is not as per compliance, Data or Success field is missing.\n\n";
						}
						else 
						{
							if (jsonResponse.keySet().contains("data") && jsonResponse.keySet().contains("success"))
							{
								failureMessage += "The json response structure is not as per compliance, Data and Success both field are present.\n\n";
							}
							else 
							{
								successMessage += "The Response data looks as per compliance.";
							}
						}
					}
				}
				else
				{
					failureMessage += "Diagnostic data fields(i.e. error, message warnings, etc) are missing or null.\n\n";
				}				
			}			
			else 
			{
				failureMessage += "Diagnostics key not found in json response.\n\n";
			}			
		}
		else
		{
			failureMessage += "The json response is null or not as per compliance.\n\n";
		}					
	}
	catch (Exception ex)
	{
		failureMessage += ex.getMessage();
		throw(ex);
	}
}

//Custom failure message and assertion result

if (failureMessage?.trim()) 
{
	failureMessage += "URL: " + SampleResult.getURL() + "\n\n";
	failureMessage += "JSON RESPONSE: " + jsonResponse + "\n\n";
	failureMessage += "REQUEST HEADERS: " + SampleResult.getRequestHeaders() + "\n\n";
	AssertionResult.setFailureMessage(failureMessage);
	AssertionResult.setFailure(true);
}

//Custom Response message if Passed

else 
{
	prev.setResponseMessage('Endpoint is working and returned a result.' + successMessage);
}