FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

# Este ARG invalida la caché a partir de aquí en cada build
ARG CACHEBUST=1

COPY . /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]