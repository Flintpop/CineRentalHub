FROM httpd:2.4

# Copier la configuration du proxy
COPY my-proxy.conf /usr/local/apache2/conf/extra/my-proxy.conf

# Activer les modules de proxy
RUN sed -i \
    -e 's/#LoadModule proxy_module modules\/mod_proxy.so/LoadModule proxy_module modules\/mod_proxy.so/' \
    -e 's/#LoadModule proxy_http_module modules\/mod_proxy_http.so/LoadModule proxy_http_module modules\/mod_proxy_http.so/' \
    /usr/local/apache2/conf/httpd.conf && \
    echo 'Include conf/extra/my-proxy.conf' >> /usr/local/apache2/conf/httpd.conf

EXPOSE 80
