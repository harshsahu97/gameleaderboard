# Game LeaderBoard Service
This web app is a project for a game-service with consumes players scores and when invoked returns top 5 scores.


#### PreRequisites
* Java, SpringBoot
* MySql


###  Endpoints
The following are open to the public:
1. Get top 5 scores -  curl --location --request GET 'localhost:8080/get_top_scores'
2. Read From a static file - curl --location --request GET 'localhost:8080/filereader'
3. Save a score with an API - curl --location --request POST 'localhost:8080/save' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "id": 33111111,
   "score": 325141111,
   "player_name" : "venom"
   }'


### If you're running into issues:
contact me on [twitter](https://www.twitter.com/harshsahu97/)
