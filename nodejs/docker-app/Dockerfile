FROM node:18.16.0-alpine3.17
WORKDIR /opt/app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3333
CMD [ "npm", "start"]