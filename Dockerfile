
FROM maven:3.5-jdk-11 as BUILD
COPY src /usr/loghmeh/src
COPY pom.xml /usr/loghmeh
RUN mvn -f /usr/loghmeh/pom.xml clean package

FROM tomcat:9.0.20-jre11
COPY --from=BUILD ???? /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]









## Stage 1
#FROM node:8 as react-build
#
#RUN mkdir -p /usr/src/app
#WORKDIR /usr/src/app
#ENV PATH /usr/src/app/node_modules/.bin:$PATH
#COPY package.json /usr/src/app/package.json
#RUN npm install -g
#RUN npm install  -g react-scripts@1.1.1 -g --silent
#COPY . /usr/src/app
#RUN npm run build
#
#
## production environment
#FROM nginx:1.13.9-alpine
#COPY ./build /usr/share/nginx/html
#EXPOSE 80
#CMD ["nginx", "-g", "daemon off;"]

# Stage 0, "build-stage", based on Node.js, to build and compile the frontend


#
#FROM tiangolo/node-frontend:10 as build-stage
#WORKDIR /app
#COPY package*.json /app/
#RUN npm install
#COPY ./ /app/
#RUN npm run build
# Stage 1, based on Nginx, to have only the compiled app, ready for production with Nginx
#FROM nginx:1.15
#COPY --from=build-stage /app/build/ /usr/share/nginx/html
## Copy the default nginx.conf provided by tiangolo/node-frontend
#COPY --from=build-stage /nginx.conf /etc/nginx/conf.d/default.conf