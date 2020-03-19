# RemoteLightOperator

## Firebase

Firebase project connected with app: https://console.firebase.google.com/u/0/project/smartlampa-ce48c/

To obtain privilages contact with: iot.ti.gr5@gmail.com

### Database Objects
In this section each object type stored in firestore will be described 
#### PlantTemplate

URL: https://smartlampa-ce48c.firebaseio.com/PlantTemplate/.json
Fields:

- name: String -> name of this template
- desctiption: String -> short description of plant
- irradiationTime: Number(Int32) -> Time for which plant should be exposed to the light during one day. Unit: minutes.
- creatorID: String -> Id of user who has added plant to DB. For preadded plants value is "sys"
- rate: Number(Float)verage rate of this template it should be in range from 0 to 5
- rateCount: Number(Int32) number of rates of this template
