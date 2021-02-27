import aws from "aws-sdk";
import { ServiceConfigurationOptions } from "aws-sdk/lib/service";
import { exception } from "console";
import { FastifyInstance, FastifyLoggerInstance } from "fastify";

class DynamoDBConnector {
  private connectionParams: ServiceConfigurationOptions = {};
  private dynbamoDB: aws.DynamoDB;
  public isConnected: boolean = false;

  constructor(private fastify: FastifyInstance) {}

  private loadParamsFromEnviroment() {
    this.connectionParams = {
      region: process.env.AWS_DYNAMO_REGION,
      endpoint: process.env.AWS_DYNAMO_URL,
    };
  }

  private connect(): aws.DynamoDB.DocumentClient {
    this.loadParamsFromEnviroment();
    this.dynbamoDB = new aws.DynamoDB(this.connectionParams);
    this.isConnected = true;

    return new aws.DynamoDB.DocumentClient({
      ...this.connectionParams,
      convertEmptyValues: true,
    });
  }

  decorate() {
    this.fastify.decorate("dynamo", this.connect());
    this.fastify.log.info("Connected to dynamoDB instance!");
  }

  createCallsTable() {
    if (!this.isConnected) {
      throw exception("No connection to DB is established. Cannot load table!");
    }

    // create 'Calls' table, fail if already exists
    var params = {
      TableName: "Calls",
      KeySchema: [
        { AttributeName: "id", KeyType: "HASH" }, //Partition key
        { AttributeName: "at", KeyType: "RANGE" }, //Sort key
      ],
      AttributeDefinitions: [
        { AttributeName: "id", AttributeType: "S" },
        { AttributeName: "at", AttributeType: "N" }, // timestamp
      ],
      ProvisionedThroughput: {
        ReadCapacityUnits: 10,
        WriteCapacityUnits: 10,
      },
    };

    this.dynbamoDB.createTable(params, function (err, data) {
      if (err) {
        console.error(
          "Unable to create table. Error JSON:",
          JSON.stringify(err, null, 2)
        );
      } else {
        console.log(
          "Created table. Table description JSON:",
          JSON.stringify(data, null, 2)
        );
      }
    });
  }
}

export { DynamoDBConnector };
