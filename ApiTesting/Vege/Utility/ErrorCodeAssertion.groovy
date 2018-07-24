import groovy.json.JsonSlurper;
//import static groovy.json.JsonParserType.LAX as RELAX

//variable with Empty error message
def failureMessage = "";

def successMessage = "";

// json variable
def jsonResponse = null;

//def Code ="${code}";
//boolean set_assertion = true;

//JsonSlurper JSON = new JsonSlurper();

//Failure if ResponseCode!=200
log.info("\n\n"+"${code}"+"\n\n");

if (!prev.getResponseCode().equals("200"))
{
	failureMessage += "Expected <response code> [200] but we got instead [" + prev.getResponseCode() + "]\n\n" ;
}

//further validation if ResponseCode=200
else
{
	try 
	{
		//jsonResponse = JSON.parseText(prev.getResponseDataAsString());		
		jsonResponse = new JsonSlurper().parseText(prev.getResponseDataAsString());		
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
								successMessage += "The Response data looks as per compliance, validate the response data if needed.";
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
		if (failureMessage?.trim()) 
		{
			failureMessage += "URL: " + SampleResult.getURL() + "\n\n";
			failureMessage += "JSON RESPONSE: " + jsonResponse + "\n\n";
			failureMessage += "REQUEST HEADERS: " + prev.getRequestHeaders() + "\n\n";
			AssertionResult.setFailureMessage(failureMessage);
			AssertionResult.setFailure(true);
		}	
	//Custom Response message if Passed	
		else 
		{
			prev.setResponseMessage('Endpoint is working and returned a result.' + successMessage);
		}					
	}
	catch (Exception ex)
	{
		failureMessage += ex.getMessage();
		throw(ex);
	}
}

//Custom failure message and assertion result

def validateErrorCode (String expectedCode)
{
	def message ="";
	def jResponse = new JsonSlurper().parseText(prev.getResponseDataAsString());
	def actualCode  = jResponse.diagnostics.error.toString();

//def actualCode = com.jayway.jsonpath.JsonPath.read(prev.getResponseDataAsString(), '$..error').get(0).toString()

	if (actualCode.equals(expectedCode))
	{
		message = "Response data is as per expectation, and Actual code is matching with expected one."
		SampleResult.setResponseMessage(message);
	}
	else  
	{
		message += "Failed due to error code mismatch. Expected code is " +expectedCode+ " but Actual code is " + actualCode + "\n";
		AssertionResult.setFailureMessage(message);
	     AssertionResult.setFailure(true)
	}
}

def errorValidationResult = this.&validateErrorCode;
errorValidationResult("${code}");
//validateErrorCode("${code}");