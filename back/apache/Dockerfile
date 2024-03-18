FROM httpd:2.4

# Mettre à jour et installer openssl pour la génération de certificats SSL, si nécessaire
RUN apt-get update && \
    apt-get install -y openssl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Activer les modules SSL et le cache de session dans Apache
RUN sed -i \
    -e 's/#LoadModule proxy_module modules\/mod_proxy.so/LoadModule proxy_module modules\/mod_proxy.so/' \
    -e 's/#LoadModule proxy_http_module modules\/mod_proxy_http.so/LoadModule proxy_http_module modules\/mod_proxy_http.so/' \
    -e 's/#LoadModule ssl_module modules\/mod_ssl.so/LoadModule ssl_module modules\/mod_ssl.so/' \
    -e 's/#LoadModule socache_shmcb_module modules\/mod_socache_shmcb.so/LoadModule socache_shmcb_module modules\/mod_socache_shmcb.so/' \
    # Remplacer le listen 80 par 443
    -e 's/Listen 80/Listen 443/' \
    /usr/local/apache2/conf/httpd.conf

# Copier les certificats SSL dans le conteneur
# Assurez-vous que ces fichiers existent dans votre contexte de build
COPY ./server.crt /usr/local/apache2/conf/server.crt
COPY ./server.key /usr/local/apache2/conf/server.key

# Ajouter la configuration de reverse proxy si nécessaire
COPY ./my-proxy.conf /usr/local/apache2/conf/extra/my-proxy.conf
RUN echo 'Include conf/extra/my-proxy.conf' >> /usr/local/apache2/conf/httpd.conf

RUN echo 'ServerName localhost' >> /usr/local/apache2/conf/httpd.conf

EXPOSE 80 443
