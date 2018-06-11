cd\
cd "C:\R4\Project\apache-jmeter-4.0\bin\"
call jmeter -n -t C:\BitbucketRepo\jmeter\ApiTesting\DAMsl\TestScripts\CRUD\CRUD_GET_Files_TC.jmx
call jmeter -n -t C:\BitbucketRepo\jmeter\ApiTesting\DAMsl\TestScripts\CRUD\CRUD_GET_CommitHistory_TC.jmx
call jmeter.bat -n -t C:\BitbucketRepo\jmeter\ApiTesting\DAMsl\TestScripts\CRUD\CRUD_GET_FileContents_TC.jmx
call jmeter.bat -n -t C:\BitbucketRepo\jmeter\ApiTesting\DAMsl\TestScripts\CRUD\CRUD_GET_ZipFiles_TC.jmx
call jmeter.bat -n -t C:\BitbucketRepo\jmeter\ApiTesting\DAMsl\TestScripts\CRUD\CRUD_POST_WriteContentsInFile_TC.jmx
call jmeter.bat -n -t C:\BitbucketRepo\jmeter\ApiTesting\DAMsl\TestScripts\CRUD\CRUD_PUT_WriteContentsInFile_TC.jmx
call jmeter.bat -n -t C:\BitbucketRepo\jmeter\ApiTesting\DAMsl\TestScripts\CRUD\CRUD_PATCH_RenameFile_TC.jmx
call jmeter.bat -n -t C:\BitbucketRepo\jmeter\ApiTesting\DAMsl\TestScripts\CRUD\CRUD_DELETE_DeleteFile_TC.jmx

