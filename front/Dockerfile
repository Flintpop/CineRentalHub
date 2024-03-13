# Étape de construction
FROM node:lts as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Étape de production
FROM node:lts
WORKDIR /app
RUN npm install -g serve
COPY --from=build-stage /app/dist /app
CMD ["serve", "-s", "/app", "-l", "8080"]
EXPOSE 8080
