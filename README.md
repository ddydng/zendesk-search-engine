# Zendesk SearchEngine
An interactive command line search engine to search for entries in organization, ticket and user table.

## Running the app
### if `java` is installed
On Linux/UNIX
```shell
java -cp "target/dependency/*:target/classes" cli.Console
```

On windoes
```shell
java -cp "target/dependency/*;target/classes" cli.Console
```

### if `docker` is installed 
```shell
./run-docker.sh
```

## Compiling the app from source
```shell
mvn dependency:copy-dependencies package 
```

## Search behavior
Can return multiple results.

Can search on empty values.

Search result of a table will attempt to join `Entry` of other tables. 
#### Search results of `user` will join
- `organizations` on their `organization_id`
- `tickets` on their `assignee_id`
- `tickets` on their `submitter_id`
#### Search results of `tickets` will join
- `users` on their `assignee_id`
- `users` on their `submitter_id`
- `organizations` on their `organization_id`
#### Search results of `organizations` will join
- `users` on their `organization_id`
- `tickets` on their `organization_id`

## Sample usage
```shell
$ java -cp "target/dependency/*:target/classes" cli.Console
Welcome to Zendesk Search
Type '/quit' or ctrl-c to exit at any time.

Choices are: 
0) Search Zendesk 1) View a list of searchable fields. 
1
All fields are:
Fields of tickets
------------------
_id
url
external_id
created_at
type
subject
description
priority
status
submitter_id
assignee_id
organization_id
tags
has_incidents
due_at
via

Fields of organizations
------------------
_id
url
external_id
name
domain_names
created_at
details
shared_tickets
tags

Fields of users
------------------
_id
url
external_id
name
alias
created_at
active
verified
shared
locale
timezone
last_login_at
email
phone
signature
organization_id
tags
suspended
role


Choices are: 
0) Search Zendesk 1) View a list of searchable fields. 
0
Start searching.

Select a table to search from
To see searchable tables, type '/list'
Choices are: 
0) organizations 1) tickets 2) users 
2

Enter a field to search in table users
To see searchable fields, type '/list'
/list
Choices are: 
_id, url, external_id, name, alias, created_at, active, verified, shared, locale, timezone, last_login_at, email, phone, signature, organization_id, tags, suspended, role
_id

Enter the value to search for field _id in table users
1

Searching table users for _id with a value of 1
_id                                                                             1                                                                               
url                                                                             http://initech.zendesk.com/api/v2/users/1.json                                  
external_id                                                                     74341f74-9c79-49d5-9611-87ef9b6eb75f                                            
name                                                                            Francisca Rasmussen                                                             
alias                                                                           Miss Coffey                                                                     
created_at                                                                      2016-04-15T05:19:46 -10:00                                                      
active                                                                          true                                                                            
verified                                                                        true                                                                            
shared                                                                          false                                                                           
locale                                                                          en-AU                                                                           
timezone                                                                        Sri Lanka                                                                       
last_login_at                                                                   2013-08-04T01:03:27 -10:00                                                      
email                                                                           coffeyrasmussen@flotonic.com                                                    
phone                                                                           8335-422-718                                                                    
signature                                                                       Don't Worry Be Happy!                                                           
organization_id                                                                 119                                                                             
tags                                                                            ["Springville","Sutton","Hartsville\/Hartley","Diaperville"]                    
suspended                                                                       true                                                                            
role                                                                            admin                                                                           
Submitted Ticket Id: fc5a8a70-3814-4b17-a6e9-583936fca909                       A Nuisance in Kiribati                                                          
Submitted Ticket Id: cb304286-7064-4509-813e-edc36d57623d                       A Nuisance in Saint Lucia                                                       
Assigned Ticket Id: 1fafaa2a-a1e9-4158-aeb4-f17e64615300                        A Problem in Russian Federation                                                 
Assigned Ticket Id: 13aafde0-81db-47fd-b1a2-94b0015803df                        A Problem in Malawi                                                             
Organisation Id: 119                                                            Multron                                                                         

Returned 1 result(s)

Another search?
Choices are: 
0) Yes, perform another search. 1) No 
0
Select a table to search from
To see searchable fields, type '/list'
Choices are: 
0) organizations 1) tickets 2) users 
0

Enter a field to search in table organizations
To see searchable fields, type '/list'
_id

Enter the value to search for field _id in table organizations
101

Searching table organizations for _id with a value of 101
_id                                                                             101                                                                             
url                                                                             http://initech.zendesk.com/api/v2/organizations/101.json                        
external_id                                                                     9270ed79-35eb-4a38-a46f-35725197ea8d                                            
name                                                                            Enthaze                                                                         
domain_names                                                                    ["kage.com","ecratic.com","endipin.com","zentix.com"]                           
created_at                                                                      2016-05-21T11:10:28 -10:00                                                      
details                                                                         MegaCorp                                                                        
shared_tickets                                                                  false                                                                           
tags                                                                            ["Fulton","West","Rodriguez","Farley"]                                          
Having Ticket Id: b07a8c20-2ee5-493b-9ebf-f6321b95966e                          A Drama in Portugal                                                             
Having Ticket Id: c22aaced-7faa-4b5c-99e5-1a209500ff16                          A Problem in Ethiopia                                                           
Having Ticket Id: 89255552-e9a2-433b-970a-af194b3a39dd                          A Problem in Turks and Caicos Islands                                           
Having Ticket Id: 27c447d9-cfda-4415-9a72-d5aa12942cf1                          A Problem in Guyana                                                             
Having User Id: 5                                                               Loraine Pittman                                                                 
Having User Id: 23                                                              Francis Bailey                                                                  
Having User Id: 27                                                              Haley Farmer                                                                    
Having User Id: 29                                                              Herrera Norman                                                                  

Returned 1 result(s)

Another search?
Choices are: 
0) Yes, perform another search. 1) No 
1

```

## Design
### Assumptions
Using string to store and match values is OKAY. There is no such value that can be queried with different string representations (for example, using `1.0` to search for a value of `1`). Sample dataset does not contain such value.

### Architecture
Layered architecture
- User interface: `cli` package which handles interative flow. Talks to service layer.
- Service: `service` package which handles search query and the join logic.
- Model: `model` package which manages the data structure and provide search capabilities

### Data model
```shell
Table ----------> Index --------> Entry
```
Each `JSONObject` in raw JSON file is modeled as an `Entry`.

Each `Index` contains the inverted indexes of a field(e.g. `status`). The inverted index is a mapping of values and the list of `Entry` sharing that value in that field.

Each JSON file is modeled as a `Table`, containing `Index` of all the fields.

### Index
Index enables O(1) searching performance. It is a map between a certain searchable value and all the `Entry` sharing that value.

### Tradeoff
Using string to store and match is quick to implement, but complex to extend to more granular data types. 