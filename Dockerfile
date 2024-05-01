FROM node:20.11.1-alpine3.19

WORKDIR /app

COPY package.json .

RUN npm install

COPY . .

CMD [ "npm", "run", "dev" ]
