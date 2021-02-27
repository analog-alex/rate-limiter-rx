import { config, DotenvConfigOutput } from "dotenv";

const configuration: DotenvConfigOutput = config();

import fastify from "fastify";
import router from "./router";
import { DynamoDBConnector } from "./db";

const server = fastify({ logger: true });

server.log.info(`Configurations loaded: ${configuration.parsed != null}`);
server.register(router);

const db = new DynamoDBConnector(server);
db.decorate();
db.createCallsTable();

server.listen(3000, '0.0.0.0', (_, __) => server.log.info(`Server up at ${3000}`));
