Welcome to the best URL Shortener around.
=========================================

How to run:

1.  Download the .jar file from releases from [the GitHub repo.](https://github.com/petrusova/URLshortener)
2.  Run using a command: `java -jar URLshortener-1.0.1-SNAPSHOT.jar`
3.  You can access page localhost:8080/help

Available endpoints without registration:

*   `GET /help` - displays this helpful page
*   `POST /account` - create an account

### Creating an account:

`POST /account`  
Request body:

    {
       "accountId" : "<account_id_you_want>"
    }


Account id is your username, it is mandatory and has to be unique.  
Response (in case you are successful):

    {
     "success": true,
     "description": "Your account is opened.",
     "password": "<your_password>>"
    }

**Don't forget to save the password!**

After you create your account you will be able to use your credentials to access these endpoints:

*   `POST /register` - register a URL
*   `GET /statistic` - display usage statistics

### Registering a URL

`POST /register`  
Request body:

    {
      "URL":"https://google.com",
      "redirectType": <301 or 302>
    }


You can also choose to not include the redirectType property, in that case the value 301 is the default.  
Response (in case you are successful):

    {
      "shortUrl": "<your_shortened_url>"
    }



Now you are ready to call the shortened URL you registered to get redirected to your website.  
Do this by going to `localhost:8080/<your_shortened_url>`

### Checking your statistics

`GET /statistic`  
Response:

    {
      "https://google.com": 1
      "https://stackoverflow.com/" : 0
    }