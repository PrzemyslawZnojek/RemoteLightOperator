## Installation ##

### Type following commands form console in server folder: ###

Venv:
```
>python3 -m venv venv  #  python 3.7.5
>source venv/Scripts/activate #  windows
>source venv/bin/activate #  linux
```

```
>pip install -r requirements.txt
```
```
>export FLASK_APP=server.py #  linux
>set FLASK_APP=server.py #  windows
```

## In order to run server: ##
```
>flask run
```
in web browser: http://127.0.0.1:5000/

* in order to work with firebase download firebase json key, name it "firebase_key.json" and place it in server/ folder