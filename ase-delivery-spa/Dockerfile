FROM node:14-alpine

ARG deployDir=/app

ENV API_URL=http://localhost:10789/api

RUN mkdir -p $deployDir

WORKDIR $deployDir

COPY build build

EXPOSE 3000

RUN npm install -g serve

CMD ["serve", "-s", "build"]