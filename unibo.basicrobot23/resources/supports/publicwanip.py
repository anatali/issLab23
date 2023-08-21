import urllib.request,json
req = urllib.request.Request(url="https://api.ipify.org/?format=json")
response = urllib.request.urlopen(req)
data = response.read()
print("data after request:", data)
parsed_data = json.loads(data.decode())
print(parsed_data)
parsed_data["ip"]

##python3 publicwanip.py