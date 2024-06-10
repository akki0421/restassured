FROM markhobson/maven-chrome:jdk-8

ARG BUCKET_NAME
ARG BUILD_NUMBER
ARG FILE_NAME
ARG ENV

RUN apt-get update && apt-get install zip unzip -y

# download aws cli
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && unzip awscliv2.zip && ./aws/install

#Step 7: Copy our project
COPY . /app

#Making our working directory as /app
WORKDIR /app


#build mavan artifacts 
RUN mvn clean verify || true


#zip artifacts
RUN zip $FILE_NAME-$ENV-$BUILD_NUMBER.zip /app/target/*


# push artifacts to s3 bucket
RUN aws s3 cp $FILE_NAME-$ENV-$BUILD_NUMBER.zip s3://$BUCKET_NAME