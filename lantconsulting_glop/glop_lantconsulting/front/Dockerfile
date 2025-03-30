# Utiliser une image Node.js pour la construction
FROM node:16 AS build

# Répertoire de travail pour le frontend
WORKDIR /app

# Copier les fichiers package.json et package-lock.json
COPY package*.json ./

# Installer les dépendances
RUN npm install

# Copier tous les fichiers du frontend
COPY . .

# Construire l'application React pour la production
RUN npm run build

# Utiliser une image Nginx pour servir l'application React construite
FROM nginx:alpine

# Copier l'application construite dans le répertoire Nginx
COPY --from=build /app/build /usr/share/nginx/html

# Exposer le port de Nginx
EXPOSE 80

# Lancer Nginx pour servir l'application
CMD ["nginx", "-g", "daemon off;"]
