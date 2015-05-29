# Sphinx API Pool

Sphinx Search Java API wrapper for pooling sphinx search connections.


## Basic Usage

Connection pooling is handled by the `PooledSphinxDataSource` class. Once configured, you can fetch an instance of
the sphinx search client from the data source by calling the `getSphinxClient()` method. Note that pooled sphinx
clients must be returned to the pool by calling `Close()` when you are finished with them.

```java
PooledSphinxDataSource dataSource = new PooledSphinxDataSource();
dataSource.setHost("190.10.14.168");
dataSource.setPort(9312);
dataSource.setTestOnBorrow(true);
dataSource.setTestOnReturn(true);
dataSource.setMinIdle(0);
dataSource.setMaxIdle(8);
dataSource.setMaxTotal(8);

ISphinxClient client = dataSource.getSphinxClient();

// do stuff
SphinxResult result = client.Query(...);

// close and return to pool
client.Close();
```


## The Client Manager

Simple applications may use the `SphinxClientManager` to provide basic management of configured data sources. The
manager allows you to configure and reuse data sources with very little effort. The first time the client manager
is called, it will create a new data source and store it for future reference. All subsequent calls made with
the same arguments will use the original data source.

**Configure data source with sphinx host & port**
```java
ISphinxClient client = new SphinxClientManager("localhost", 9312).getSphinxClient();
```

**Configure data source from properties file**
```java
ISphinxClient client = new SphinxClientManager("sphinx-api-pool.properties").getSphinxClient();
```


## Configuration

The Sphinx Connection Pool can be configured both programmatically and via external properties files. To get the
most out of the connection pool, you should configure your pool size to ensure that there will not be contention
over connections to the sphinx server. Ideally, the `maxTotal` value of your all connection pools should not exeed the
`max_children` config value of your sphinx server.

Setting | Default | Description
------------- | -------------
host          | localhost | The sphinx server IP address or host name
port          | 9312      | The sphinx server connection port
testOnBorrow  | false     | Test the sphinx client for connection errors when borrowing from the pool
testOnReturn  | false     | Test the sphinx client for connection errors when returning it it to the pool (via `Close()`)
minIdle       | 0         | The target for the minimum number of idle instances to maintain in the pool.
maxIdle       | 8         | The maximum cap of idle instances in the pool.
maxTotal      | 8         | Maximum number of instances, both idle & in use, that can be allocated by the pool.


### Configuration values

#### testOnBorrow

_Default: false_

If true, instances borrowed from the pool will be validated to ensure a valid socket connection before being returned. If
the instance fails validation, it will be removed from the pool, destroyed, and a new attempt will be made to borrow
from the pool.

This setting is recommended if you have contention issues on your sphinx server and you cannot lower your `maxTotal`
 connections to ensure that new instances will be valid.

#### testOnReturn

 _Default: false_

If true, instances will be validated before being returned to the pool. Instances that have incurred a connection
error or other issues during use will be destroyed rather than being returned to the pool.


#### minIdle

_Default: 0_

The target for the minimum number of idle instances to maintain in the pool. This setting only has an effect
if it is positive and `timeBetweenEvictionRuns` is greater than zero. If this is the case, an attempt is made to
ensure that the pool has the required minimum number of instances during idle object eviction runs. New instances
will be created to fill the deficit.

#### maxIdle

_Default: 8_

The number of "idle" instances allowed in the pool. If `maxIdle` is set too low on heavily loaded systems it is
possible you will see objects being destroyed and almost immediately new objects being created. This is a
result of the active threads momentarily returning objects faster than they are requesting them them, causing
the number of idle objects to rise above maxIdle. The best value for maxIdle for heavily loaded system will
vary but the default is a good starting point.

#### maxTotal

_Default: 8_

The maximum number of objects that can be allocated by the pool (checked out to clients, or idle awaiting checkout)
at a given time. When negative, there is no limit to the number of objects that can be managed by the pool at one time.

It is recommended that you set your maximum below the sphinx `max_children` setting to prevent contention.


### Properties files

Data sources can be configured using external properties files by using the provided `ConfigurationReader`. The reader
will load the file and return a configuration object that can be used to initialize a new data source.

As a convenience, this functionality is also provided by the `SphinxClientManager` as a one stop shop for configuration
and management of data sources.

```java
BasicPooledDataSourceConfig config = new ConfigurationReader("sphinx-api-pool.properties").getConfigObject();
PooledSphinxDataSource dataSource = new PooledSphinxDataSource(config);
```

```java
PooledSphinxDataSource dataSource = new SphinxClientManager("sphinx-api-pool.properties").getDataSource();
```

```properties
# sphinx server connection details
sphinx.dataSource.host=localhost
sphinx.dataSource.port=9312

# pooling
sphinx.dataSource.testOnBorrow=false
sphinx.dataSource.testOnReturn=false
sphinx.dataSource.minIdle=0
sphinx.dataSource.maxIdle=8
sphinx.dataSource.maxTotal=8
```


## Building

This project is built using Maven. Sources can be compiled and packaged using the standard `mvn compile` and `mvn package`
commands.

```
mvn package
```

### Generating documentation

Project documentation can be generated using the `mvn site` command. API docs will be generated in the build output
`\target\site\apidocs` directory.

```
mvn site
```

## License
Copyright (c) 2012 Brian Cowdery  
Licensed under the MIT, GPL licenses.