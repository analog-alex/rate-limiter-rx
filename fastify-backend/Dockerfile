# base image (at leasty nodeJS 13)
FROM node:13-alpine

# Create app directory
WORKDIR /usr/src/app

# install dependencies
COPY package*.json ./
RUN apk add g++
RUN apk add make
RUN apk add python
RUN npm install
RUN npm install typescript -g

# Bundle app source
COPY src ./src
COPY tsconfig.json ./

RUN tsc
EXPOSE 3000
CMD [ "node", "dist/index.js" ]