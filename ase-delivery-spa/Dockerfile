# FROM node:alpine

# ARG deployDir=/app

# ENV BACKEND_URL=http://localhost:8080
# # ENV BACKEND_URL=http://backend-service:8080

# RUN mkdir -p $deployDir

# WORKDIR $deployDir

# COPY ["package.json", "package-lock.json", "./"]

# RUN npm install --production

# COPY . .

# EXPOSE 3000

# RUN npm install -g serve

# CMD ["npm", "start"]


FROM node:alpine

ARG deployDir=/app

ENV API_URL=http://localhost:10789/api
# ENV BACKEND_URL=http://backend-service:8080

RUN mkdir -p $deployDir

WORKDIR $deployDir

COPY build build

EXPOSE 3000

RUN npm install -g serve

CMD ["serve", "-s", "build"]

# COPY ["package.json", "package-lock.json", "./"]

# RUN npm install --production

# COPY . .

# EXPOSE 3000

# RUN npm install -g serve

# CMD ["npm", "start"]


# FROM openjdk:8-jdk-alpine
# ARG JAR_FILE=target/*.jar
# #copy into the image
# COPY ${JAR_FILE} app.jar
# #The array form of the Dockerfile ENTRYPOINT is used so that there is no shell wrapping the java process
# ENTRYPOINT ["java","-jar","/app.jar"]