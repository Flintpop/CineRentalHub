
# Générer la Clé Privée de la CA
openssl genrsa -out ca.key 2048

# Créer le Certificat Racine de la CA
openssl req -x509 -new -nodes -key ca.key -sha256 -days 1024 -out ca.pem

# Générer la Clé Privée du Serveur
openssl genrsa -out server.key 2048

# Générer une Demande de Signature de Certificat (CSR) pour le Serveur
openssl req -new -key server.key -out server.csr -config san.cnf

# Signer le CSR avec votre CA
openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.crt -days 365 -sha256 -extfile san.cnf -extensions req_ext


# Convertir le Certificat d'autorité en format CRT
openssl x509 -outform der -in ca.pem -out ca.crt
