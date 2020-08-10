# RingCentral Engage Voice SDK

[![Download](https://api.bintray.com/packages/tylerlong/maven/ringcentral-engage-voice/images/download.svg)](https://bintray.com/tylerlong/maven/ringcentral-engage-voice/_latestVersion)


## Installation

This SDK assumes that you have a RingCentral token. You can use [RingCentral Java SDK](https://github.com/ringcentral/ringcentral-java) to get a RingCentral token. Please install it first, here is its [installation instructions](https://github.com/ringcentral/ringcentral-java#installation).


### Gradle

```groovy
repositories {
  jcenter()
}

dependencies {
  compile 'com.ringcentral:ringcentral-engage-voice:[version]'
}
```

Don't forget to replace `[version]` with expected version.


### Maven

```xml
<repositories>
  <repository>
    <id>jcenter</id>
    <url>https://jcenter.bintray.com/</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.ringcentral</groupId>
  <artifactId>ringcentral-engage-voice</artifactId>
  <version>[version]</version>
</dependency>
```

Don't forget to replace `[version]` with expected version.


### Manually

[Download jar here](https://bintray.com/tylerlong/maven/ringcentral-engage-voice/_latestVersion) and save it into your java classpath.


## Usage

```java
RestClient rc = new RestClient(clientId, clientSecret, rcServer);
rc.authorize(username, extension, password);

EngageVoice engageVoice = new EngageVoice(evServer); // evServer: "https://engage.ringcentral.com"
engageVoice.authorize(rc.token.access_token);

ResponseBody responseBody = engageVoice.get("/voice/api/v1/admin/accounts");
```


## For maintainers

### Build

```
./gradlew build
```

### Test

```
./gradlew test
```
