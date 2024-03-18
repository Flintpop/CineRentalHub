FROM httpd:2.4

# Installer les outils nécessaires (mod_ssl pour SSL, mod_proxy et mod_proxy_http pour le proxy)
RUN apt-get update && \
    apt-get install -y openssl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Activer les modules nécessaires dans Apache
RUN sed -i \
    -e 's/#LoadModule proxy_module modules\/mod_proxy.so/LoadModule proxy_module modules\/mod_proxy.so/' \
    -e 's/#LoadModule proxy_http_module modules\/mod_proxy_http.so/LoadModule proxy_http_module modules\/mod_proxy_http.so/' \
    -e 's/#LoadModule ssl_module modules\/mod_ssl.so/LoadModule ssl_module modules\/mod_ssl.so/' \
    -e 's/#LoadModule socache_shmcb_module modules\/mod_socache_shmcb.so/LoadModule socache_shmcb_module modules\/mod_socache_shmcb.so/' \
    /usr/local/apache2/conf/httpd.conf

# Ajouter votre configuration de reverse proxy
COPY ./my-proxy.conf /usr/local/apache2/conf/extra/my-proxy.conf
RUN echo 'Include conf/extra/my-proxy.conf' >> /usr/local/apache2/conf/httpd.conf
RUN echo 'ServerName localhost' >> /usr/local/apache2/conf/httpd.conf

# Copiez les certificats SSL générés dans le conteneur
COPY ./mysite.crt /usr/local/apache2/ssl/mysite.crt
COPY ./mysite.key /usr/local/apache2/ssl/mysite.key

EXPOSE 80 443